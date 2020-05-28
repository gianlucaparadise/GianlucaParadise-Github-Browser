package com.gianlucaparadise.githubbrowser.adapters

import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    val hasImageUrl = imageUrl?.isNotBlank() == true

    view.isVisible = hasImageUrl

    if (!hasImageUrl) return

    Glide.with(view.context)
        .load(imageUrl)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}