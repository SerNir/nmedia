package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
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
    fun toDto() =
        Post(id, author,authorAvatar, content, published, likes, shares, views, likedByMe, sharedByMe)

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.author,
            dto.authorAvatar,
            dto.content,
            dto.published,
            dto.likes,
            dto.shares,
            dto.views,
            dto.likedByMe,
            dto.sharedByMe,

        )

    }

}