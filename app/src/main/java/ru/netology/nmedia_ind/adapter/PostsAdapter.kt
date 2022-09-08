package ru.netology.nmedia_ind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia_ind.R
import ru.netology.nmedia_ind.databinding.CardPostBinding
import ru.netology.nmedia_ind.dto.Post
import ru.netology.nmedia_ind.repository.PostDiffCallback
import ru.netology.nmedia_ind.util.validateText

interface PostEventListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onRefresh()
    fun onVideo(post: Post)
    fun onPost(post: Post)
}

class PostsAdapter(
    private val listener: PostEventListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostEventListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            video.setImageResource(R.mipmap.video_example)
            video.isVisible = !post.video.isNullOrBlank()
            if (video.isVisible) {
                video.setOnClickListener {
                    listener.onVideo(post)
                }
            }

            like.text = validateText(post.likes)
            like.isChecked = post.likedByMe
            like.setOnClickListener {
                listener.onLike(post)
            }

            share.text = validateText(post.shared)
            share.setOnClickListener {
                listener.onShare(post)
            }

            content.setOnClickListener {
                listener.onPost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.refresh -> {
                                listener.onRefresh()
                                return@setOnMenuItemClickListener true
                            }
                            else -> return@setOnMenuItemClickListener false
                        }
                    }
                }.show()
            }
        }
    }
}

