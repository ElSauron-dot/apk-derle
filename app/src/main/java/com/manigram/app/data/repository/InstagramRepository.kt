package com.manigram.app.data.repository

import com.manigram.app.data.model.InstagramPost
import com.manigram.app.data.model.InstagramProfile
import com.manigram.app.data.model.WebProfileResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface InstagramApi {
    @GET("api/v1/users/web_profile_info/")
    suspend fun profile(
        @Query("username") username: String,
        @Header("User-Agent") userAgent: String = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36",
        @Header("X-IG-App-ID") appId: String = "936619743392459"
    ): WebProfileResponse
}

class InstagramRepository(private val api: InstagramApi = createApi()) {
    suspend fun getProfile(username: String): InstagramProfile {
        require(USERNAME.matches(Regex("[A-Za-z0-9._]{1,30}"))) { "Geçerli bir kullanıcı adı girin." }
        val response = api.profile(username)
        val user = response.data?.user ?: response.legacyUser
            ?: error("Bu herkese açık profil bulunamadı veya Instagram yanıt vermedi.")
        val avatar = user.profilePictureHd ?: user.profilePicture.orEmpty()
        val posts = user.timeline?.edges.orEmpty().map { edge ->
            val media = edge.node
            InstagramPost(
                id = media.id, username = user.username, profilePicture = avatar,
                imageUrl = media.imageUrl ?: media.thumbnailUrl.orEmpty(), videoUrl = media.videoUrl,
                caption = media.caption?.edges?.firstOrNull()?.node?.text.orEmpty(),
                likes = media.likes?.count ?: 0, isVideo = media.isVideo
            )
        }.filter { it.imageUrl.isNotBlank() }
        return InstagramProfile(user.username, user.fullName, user.biography, avatar,
            user.followers?.count ?: 0, user.following?.count ?: 0, posts)
    }

    suspend fun getFeed(usernames: List<String>): List<InstagramPost> = usernames.flatMap { getProfile(it).posts }

    companion object {
        private val USERNAME = Regex("[A-Za-z0-9._]{1,30}")
        private fun createApi(): InstagramApi {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder().addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build()
            return Retrofit.Builder().baseUrl("https://www.instagram.com/").client(client)
                .addConverterFactory(GsonConverterFactory.create()).build().create(InstagramApi::class.java)
        }
    }
}
