package vn.edu.vlu.lab09

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vn.edu.vlu.lab09.databinding.ItemPhotoBinding

class PhotoAdapter(
    private val photos: List<PhotoItem>
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.binding.tvTitle.text = photo.title
        holder.binding.tvAuthor.text = photo.author
        holder.binding.ivPhoto.contentDescription = photo.title

        Glide.with(holder.itemView.context)
            .load(photo.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_broken_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivPhoto)
    }

    override fun onViewRecycled(holder: PhotoViewHolder) {
        Glide.with(holder.itemView.context).clear(holder.binding.ivPhoto)
        super.onViewRecycled(holder)
    }
}