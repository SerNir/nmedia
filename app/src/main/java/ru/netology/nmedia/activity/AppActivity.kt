package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg


class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootFragmentName = "root"
        val fragmentToRemove = "tag"
        supportFragmentManager.commit {
            add(findViewById(R.id.nav_host_fragment), fragmentToRemove)
            addToBackStack(rootFragmentName)
        }

        intent?.let {
            if (it.action != Intent.ACTION_SEND){
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank()==true){
                intent.removeExtra(Intent.EXTRA_TEXT)
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
            }
        }
    }
    override fun onBackPressed() {

        val rootFragmentName = "root"
        supportFragmentManager.popBackStack(
            rootFragmentName,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        super.onBackPressed()
    }
}