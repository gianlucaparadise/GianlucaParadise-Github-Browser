package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gianlucaparadise.githubbrowser.R
import kotlinx.android.synthetic.main.card_header_view.view.*

class CardHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    var title: String
        get() {
            return header_title.text.toString()
        }
        set(value) {
            header_title.text = value
        }

    var subtitle: String?
        get() {
            return header_subtitle.text.toString()
        }
        set(value) {
            updateSubtitle(value)
        }

    private var _avatarUrl: String? = null
    var avatarUrl: String?
        get() = _avatarUrl
        set(value) {
            _avatarUrl = value
            updateAvatar(value)
        }

    private var _subtitleVisible: Boolean = true
    var subtitleVisible: Boolean
        get() = _subtitleVisible
        set(value) {
            _subtitleVisible = value
            updateSubtitle(subtitle)
        }

    private var _avatarVisible: Boolean = true
    var avatarVisible: Boolean
        get() = _avatarVisible
        set(value) {
            _avatarVisible = value
            updateAvatar(avatarUrl)
        }

    private fun updateSubtitle(subtitle: String?) {
        header_subtitle.text = subtitle
        header_subtitle.isVisible = subtitleVisible && !subtitle.isNullOrBlank()
    }

    private fun updateAvatar(url: String?) {
        header_avatar.isVisible = avatarVisible && !url.isNullOrBlank()
        if (!header_avatar.isVisible) return

        Glide.with(header_avatar.context)
            .load(url)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(header_avatar)
    }

    init {
        inflate(context, R.layout.card_header_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardHeaderView)

        this.subtitleVisible = attributes.getBoolean(R.styleable.CardHeaderView_subtitleVisible, true)
        this.avatarVisible = attributes.getBoolean(R.styleable.CardHeaderView_avatarVisible, true)
        this.title = attributes.getString(R.styleable.CardHeaderView_title) ?: ""
        this.subtitle = attributes.getString(R.styleable.CardHeaderView_subtitle) ?: ""
        this.avatarUrl = attributes.getString(R.styleable.CardHeaderView_avatarUrl)

        attributes.recycle()
    }
}