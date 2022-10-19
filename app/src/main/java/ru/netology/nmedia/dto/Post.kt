package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    var likes: Long = 0,
    var shares: Long = 0,
    var views: Long = 0,
    var likedByMe: Boolean = false,
    var sharedByMe: Boolean = false,

) {
    fun reducingNumber(value: Long): String {
        val second = value.toString().toCharArray()
        return if (value in 1000..9_999) {
            if (second[1] == '0') "${value / 1000}K" else String.format(
                "%1.1fK",
                value.toDouble() / 1000
            )
        } else if (value in 10_000..999_999) {
            "${value / 1000}K"
        } else if (value >= 1_000_000) {
            if (second[1] == '0') "${value / 1_000_000}M" else String.format(
                "%1.1fM",
                value.toDouble() / 1_000_000
            )
        } else "$value"
    }
}
