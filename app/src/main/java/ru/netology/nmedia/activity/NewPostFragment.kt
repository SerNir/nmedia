package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg
            ?.let(binding.contentEditText::setText)


        binding.ok.setOnClickListener {

            viewModel.changeContent(binding.contentEditText.text.toString())
            viewModel.save()

            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}

object StringArg : ReadWriteProperty<Bundle, String?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
        thisRef.putString(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
        thisRef.getString(property.name)
}
