package com.gianlucaparadise.githubbrowser.adapters

import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton

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


@BindingAdapter("icon")
fun bindButtonIconDrawable(view: Button, drawable: Drawable?) {
    // regular binding was accepting only ResId as input parameter
    // this one lets you pass also drawable objects
    if (view is MaterialButton) {
        view.icon = drawable
    }
}