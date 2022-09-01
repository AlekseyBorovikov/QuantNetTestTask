package com.pro.quantnetworktesttask

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pro.quantnetworktesttask.data.UnsplashPhoto
import com.pro.quantnetworktesttask.databinding.ItemPhotoBinding
import timber.log.Timber

class PhotoAdapter(diffUtil: DiffUtil.ItemCallback<UnsplashPhoto>)
    : PagingDataAdapter<UnsplashPhoto, PhotoAdapter.PhotoListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val itemBinding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: PhotoListViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    private var onClickListener: ((photo: UnsplashPhoto) -> Unit)? = null
    fun setOnItemClickListener(listener: (photo: UnsplashPhoto) -> Unit){
        onClickListener = listener
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val news = getItem(position)
        news?.let{ holder.bind(it, onClickListener) }
    }

    inner class PhotoListViewHolder(private val itemBinding: ItemPhotoBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            photo: UnsplashPhoto,
            onClick: ((photo: UnsplashPhoto) -> Unit)? = null,
        ) {
            Timber.d("photo bind ${photo.id}")
            Glide.with(itemView)
                .load(photo.urls.small)
                .into(itemView.findViewById(R.id.item_photo_iv))

            val name: TextView = itemView.findViewById(R.id.userName)
            name.text = photo.user.name
            val description: TextView = itemView.findViewById(R.id.description)
            description.text = photo.description ?: ""

            itemView.setOnClickListener { view ->
                if (onClick != null) onClick(photo)
            }
        }

    }

}