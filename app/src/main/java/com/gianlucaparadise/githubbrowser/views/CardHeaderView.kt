package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.card_header_view.view.*
import androidx.annotation.ColorInt
import android.util.TypedValue
import com.gianlucaparadise.githubbrowser.R

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

    /**
     * When not specified, this uses colorOnSurface from theme.
     * Background color of the view is colorSurface from theme.
     */
    @get:ColorInt
    var textColor: Int
        get() = header_title.currentTextColor
        set(value) {
            header_title.setTextColor(value)
            header_subtitle.setTextColor(value)
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

        this.orientation = HORIZONTAL

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardHeaderView)

        this.subtitleVisible =
            attributes.getBoolean(R.styleable.CardHeaderView_subtitleVisible, true)
        this.avatarVisible = attributes.getBoolean(R.styleable.CardHeaderView_avatarVisible, true)
        this.title = attributes.getString(R.styleable.CardHeaderView_title) ?: ""
        this.subtitle = attributes.getString(R.styleable.CardHeaderView_subtitle) ?: ""
        this.avatarUrl = attributes.getString(R.styleable.CardHeaderView_avatarUrl)

        // Text color
        val theme = context.theme

        val themeTextColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorOnSurface, themeTextColorTypedValue, true)
        this.textColor = attributes.getColor(R.styleable.CardHeaderView_textColor, themeTextColorTypedValue.data)

        // Background color
        val themeBackgroundColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, themeBackgroundColorTypedValue, true)
        this.setBackgroundColor(themeBackgroundColorTypedValue.data)

        attributes.recycle()
    }
}