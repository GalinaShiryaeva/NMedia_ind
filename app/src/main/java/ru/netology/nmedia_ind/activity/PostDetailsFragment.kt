package ru.netology.nmedia_ind.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia_ind.R
import ru.netology.nmedia_ind.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia_ind.adapter.PostEventListener
import ru.netology.nmedia_ind.adapter.PostViewHolder
import ru.netology.nmedia_ind.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.util.LongArg
import ru.netology.nmedia_ind.viewmodel.PostViewModel

class PostDetailsFragment : Fragment() {

    companion object {
        var Bundle.idArg: Long? by LongArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostDetailsBinding.inflate(
            inflater,
            container,
            false
        )
//
//        val viewHolder = PostViewHolder(binding.cardPost, object : PostEventListener {
//
//            override fun onLike(post: Post) {
//                viewModel.likeById(post.id)
//            }
//
//            override fun onShare(post: Post) {
//
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                    type = "text/plain"
//                }
//
//                val shareIntent = Intent.createChooser(
//                    intent,
//                    getString(R.string.chooser_share_post)
//                )
//                startActivity(shareIntent)
//                viewModel.shareById(post.id)
//            }
//
//            override fun onEdit(post: Post) {
//                viewModel.edit(post)
//                findNavController().navigate(
//                    R.id.action_postDetailsFragment_to_editPostFragment,
//                    Bundle().apply {
//                        textArg = post.content
//                    }
//                )
//            }
//
//            override fun onRemove(post: Post) {
//                viewModel.removeById(post.id)
//            }
//
//            override fun onVideo(post: Post) {
//                val appIntent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("vnd.youtube:" + post.video)
//                )
//                val webIntent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("http://www.youtube.com/watch?v=" + post.video)
//                )
//                try {
//                    startActivity(appIntent)
//                } catch (e: ActivityNotFoundException) {
//                    startActivity(webIntent)
//                }
//            }
//
//            override fun onPost(post: Post) {
//            }
//        })
////        viewModel.data.observe(viewLifecycleOwner) { posts ->
////            val post = posts.find {
////                it.id == arguments?.idArg
////            } ?: run {
////                findNavController().popBackStack()
////                return@observe
////            }
////            viewHolder.bind(post)
////        }
        return binding.root
    }
}