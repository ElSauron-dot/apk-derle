package com.manigram.app.data.model

import com.google.gson.annotations.SerializedName

data class WebProfileResponse(
    @SerializedName("data") val data: ProfileData? = null,
    @SerializedName("user") val legacyUser: InstagramUser? = null
)

data class ProfileData(@SerializedName("user") val user: InstagramUser? = null)

data class InstagramUser(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("biography") val biography: String = "",
    @SerializedName("profile_pic_url_hd") val profilePictureHd: String? = null,
    @SerializedName("profile_pic_url") val profilePicture: String? = null,
    @SerializedName("edge_followed_by") val followers: CountEdge? = null,
    @SerializedName("edge_follow") val following: CountEdge? = null,
    @SerializedName("edge_owner_to_timeline_media") val timeline: MediaConnection? = null,
    @SerializedName("edge_felix_video_timeline") val reels: MediaConnection? = null
)

data class CountEdge(@SerializedName("count") val count: Long = 0)
data class MediaConnection(@SerializedName("edges") val edges: List<MediaEdge> = emptyList())
data class MediaEdge(@SerializedName("node") val node: MediaNode)
data class MediaNode(
    @SerializedName("id") val id: String = "",
    @SerializedName("display_url") val imageUrl: String? = null,
    @SerializedName("thumbnail_src") val thumbnailUrl: String? = null,
    @SerializedName("video_url") val videoUrl: String? = null,
    @SerializedName("is_video") val isVideo: Boolean = false,
    @SerializedName("edge_media_to_caption") val caption: CaptionConnection? = null,
    @SerializedName("edge_liked_by") val likes: CountEdge? = null,
    @SerializedName("shortcode") val shortcode: String = ""
)
data class CaptionConnection(@SerializedName("edges") val edges: List<CaptionEdge> = emptyList())
data class CaptionEdge(@SerializedName("node") val node: CaptionNode)
data class CaptionNode(@SerializedName("text") val text: String = "")

data class InstagramProfile(
    val username: String, val fullName: String, val biography: String,
    val profilePicture: String, val followers: Long, val following: Long,
    val posts: List<InstagramPost>
)
data class InstagramPost(
    val id: String, val username: String, val profilePicture: String,
    val imageUrl: String, val videoUrl: String?, val caption: String,
    val likes: Long, val isVideo: Boolean
)
