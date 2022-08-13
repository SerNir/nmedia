package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будующего",
            content = "Привет это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичка до уверенных профессианалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начить цепочку перемен - http://netolo.gy/fyb",
            published = "19 июля 2022",
            likedByMe = false,
            shares = 997,
            likes = 992
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будующего",
            content = "В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется",
            published = "21 июля 2022",
            likedByMe = false,
            shares = 997,
            likes = 997,
            video = "https://www.youtube.com/watch?v=hBuqbUjKvTs"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будующего",
            content = "Над нашими курсами работает большая команда: авторы, методисты, продюсеры, преподаватели, маркетологи, редакторы. Каждый следит за трендами на рынке, чтобы запустить качественную программу. Мы создаём продукт, которым пользуемся сами. Мы поддерживаем в течение всего обучения. Наши кураторы, эксперты и аспиранты не дают студентам сойти с дистанции. Кроме того, мы помогаем с трудоустройством: собрать портфолио, оформить резюме и пройти собеседование. Лучшие студенты стажируются у наших партнёров.",
            published = "23 июля 2022",
            likedByMe = false,
            shares = 997,
            likes = 997
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будующего",
            content = "В каждом есть сила и талант, чтобы добиваться больших целей. Мы помогаем найти путь развития и реализовать себя через профессию — так, как вам этого хочется",
            published = "25 июля 2022",
            likedByMe = false,
            shares = 997,
            likes = 997
        )
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.likedByMe)
                    it.copy(likedByMe = true, likes = it.likes + 1)
                else
                    it.copy(likedByMe = false, likes = it.likes - 1)
            }

        }

        data.value = posts
    }

    override fun share(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1, sharedByMe = true)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Netology", published = "Now")) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }

        data.value = posts
    }
}
