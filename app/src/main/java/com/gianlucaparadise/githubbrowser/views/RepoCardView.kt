package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.gianlucaparadise.githubbrowser.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.repo_card_view.view.*

class RepoCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {

    var title: String
        get() {
            return card_header.title
        }
        set(value) {
            card_header.title = value
        }

    var owner: String?
        get() {
            return card_header.subtitle
        }
        set(value) {
            card_header.subtitle = value
        }

    var shortDescription: String?
        get() {
            return repo_short_description.text.toString()
        }
        set(value) {
            repo_short_description.text = value
            repo_short_description.isVisible = !value.isNullOrBlank()
        }

    var primaryLanguage: String?
        get() {
            return repo_primary_language_name.text.toString()
        }
        set(value) {
            repo_primary_language_name.text = value
            repo_primary_language_name.isVisible = !value.isNullOrBlank()
        }

    var totalStars: Int
        get() {
            return repo_total_stars.text.toString().toInt()
        }
        set(value) {
            repo_total_stars.text = value.toString()
        }

    var avatarUrl: String?
        get() = card_header.avatarUrl
        set(value) {
            card_header.avatarUrl = value
        }

    private var _ownerVisible = true
    var ownerVisible: Boolean
        get() = _ownerVisible
        set(value) {
            _ownerVisible = value
            card_header.subtitleVisible = _ownerVisible
            card_header.avatarVisible = _ownerVisible
        }

    /**
     * When not specified, this uses colorOnSurface from theme.
     * Background color of the view is colorSurface from theme.
     */
    @get:ColorInt
    var textColor: Int
        get() = card_header.textColor
        set(value) {
            repo_short_description.setTextColor(value)
            repo_total_stars.setTextColor(value)
            card_header.textColor = value
        }

    init {
        inflate(context, R.layout.repo_card_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RepoCardView)

        this.ownerVisible = attributes.getBoolean(R.styleable.RepoCardView_ownerVisible, true)
        this.title = attributes.getString(R.styleable.RepoCardView_title) ?: ""
        this.owner = attributes.getString(R.styleable.RepoCardView_owner) ?: ""
        this.shortDescription =
            attributes.getString(R.styleable.RepoCardView_shortDescription) ?: ""
        this.primaryLanguage =
            attributes.getString(R.styleable.RepoCardView_primaryLanguage) ?: ""
        this.totalStars = attributes.getInteger(R.styleable.RepoCardView_totalStars, 0)
        this.avatarUrl = attributes.getString(R.styleable.RepoCardView_avatarUrl)

        // Text color
        val theme = context.theme

        val themeTextColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorOnSurface, themeTextColorTypedValue, true)
        this.textColor =
            attributes.getColor(R.styleable.RepoCardView_textColor, themeTextColorTypedValue.data)

        // Background color
        val themeBackgroundColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, themeBackgroundColorTypedValue, true)
        this.setBackgroundColor(themeBackgroundColorTypedValue.data)

        attributes.recycle()
    }
}