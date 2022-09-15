package ru.netology.nmedia_ind.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia_ind.databinding.FragmentNewPostBinding
import ru.netology.nmedia_ind.util.AndroidUtils
import ru.netology.nmedia_ind.util.StringArg
import ru.netology.nmedia_ind.viewmodel.PostViewModel

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
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(binding.content::setText)

        binding.content.requestFocus()
        binding.save.setOnClickListener {
            if (!binding.content.text.isNullOrBlank()) {
                viewModel.editContent(binding.content.text.toString())
                viewModel.save()
            }

            viewModel.postCreated.observe(viewLifecycleOwner) {
                AndroidUtils.hideKeyboard(requireView())
                viewModel.load()
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}