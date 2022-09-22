package ru.netology.nmedia.dao

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import ru.netology.nmedia.dto.Post


class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumn.TABLE} (
            ${PostColumn.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumn.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumn.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumn.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumn.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumn.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumn.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumn.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumn.COLUMN_SHARED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumn.COLUMN_VIDEO} TEXT NOT NULL DEFAULT ""
           
        );
        """.trimIndent()
    }

    object PostColumn {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARES = "shares"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_SHARED_BY_ME = "sharedByMe"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_VIEWS,
            COLUMN_LIKED_BY_ME,
            COLUMN_SHARED_BY_ME,
            COLUMN_VIDEO

        )

    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumn.TABLE,
            PostColumn.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumn.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumn.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumn.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumn.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumn.COLUMN_PUBLISHED)),
                likes = getLong(getColumnIndexOrThrow(PostColumn.COLUMN_LIKES)),
                shares = getLong(getColumnIndexOrThrow(PostColumn.COLUMN_SHARES)),
                views = getLong(getColumnIndexOrThrow(PostColumn.COLUMN_VIEWS)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumn.COLUMN_LIKED_BY_ME)) != 0,
                sharedByMe = getInt(getColumnIndexOrThrow(PostColumn.COLUMN_SHARED_BY_ME)) != 0,
                video = getString(getColumnIndexOrThrow(PostColumn.COLUMN_VIDEO))

            )
        }

    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
               UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
               WHERE id = ?;
           """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                shares = shares + CASE WHEN sharedByMe THEN -1 ELSE 1 END
                WHERE id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumn.TABLE,
            "${PostColumn.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {

            put(PostColumn.COLUMN_AUTHOR, post.author)
            put(PostColumn.COLUMN_CONTENT, post.content)
            put(PostColumn.COLUMN_PUBLISHED, post.published)
            Log.d("post.content", post.content)
            Log.d("post.all", post.toString())
        }
        val id =
            if (post.id != 0L) {
                db.update(
                    PostColumn.TABLE,
                    values,
                    "${PostColumn.COLUMN_ID} = ?",
                    arrayOf(post.id.toString()),
                )
                post.id
            } else {
                db.insert(PostColumn.TABLE, null, values)
            }

        db.query(
            PostColumn.TABLE,
            PostColumn.ALL_COLUMNS,
            "${PostColumn.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }


    override fun getPostById(id: Long): Post? {

         db.query(PostColumn.TABLE,
             PostColumn.ALL_COLUMNS,
             "${PostColumn.COLUMN_ID} = $id",
             arrayOf(id.toString()),
             null,
             null,
             null,
        ).use {
            it.moveToNext()
             return map(it)
         }

    }
}