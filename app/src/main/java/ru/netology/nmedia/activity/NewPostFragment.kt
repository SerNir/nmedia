package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NewPostFragment : Fragment() {

    companion object{
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



        var text = arguments?.getString("TEXT_KEY")
        binding.contentEditText.setText(text)
//        arguments?.textArg?.let(binding.contentEditText::setText)

        binding.ok.setOnClickListener {
            val text1 = binding.contentEditText.text.toString()
            if (text1.isNotBlank()){
                viewModel.changeContentAndSave(text1)
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }else{
                Toast.makeText(activity,
                    getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT).show()
            }

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
