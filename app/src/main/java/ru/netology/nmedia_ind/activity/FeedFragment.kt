package ru.netology.nmedia_ind.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import okhttp3.internal.wait
import ru.netology.nmedia_ind.R
import ru.netology.nmedia_ind.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia_ind.activity.PostDetailsFragment.Companion.idArg
import ru.netology.nmedia_ind.adapter.PostEventListener
import ru.netology.nmedia_ind.adapter.PostsAdapter
import ru.netology.nmedia_ind.databinding.FragmentFeedBinding
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.model.FeedModel
import ru.netology.nmedia_ind.viewmodel.PostViewModel
import kotlin.system.exitProcess


class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostsAdapter(object : PostEventListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(
                    intent,
                    getString(R.string.chooser_share_post)
                )
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onRefresh() {
                binding.swipeRefresh.isRefreshing = true
                viewModel.load()
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onVideo(post: Post) {
                val appIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("vnd.youtube:" + post.attachment?.url)
                )
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + post.attachment?.url)
                )
                try {
                    startActivity(appIntent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(webIntent)
                }
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postDetailsFragment,
                    Bundle().apply {
                        idArg = post.id
                    }
                )
            }
        })

        binding.list.adapter = adapter
        (binding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.data.observe(viewLifecycleOwner) { state ->
            with(binding) {
                emptyText.isVisible = state.empty
                errorGroup.isVisible = state.error
                progressBar.isVisible = state.loading
                waitGroup.isVisible = state.waiting
            }
            adapter.submitList(state.posts)
        }

        binding.retry.setOnClickListener {
            viewModel.load()
        }

        binding.wait.setOnClickListener {
            viewModel.load()
        }

        binding.close.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }

        binding.create.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }
}
