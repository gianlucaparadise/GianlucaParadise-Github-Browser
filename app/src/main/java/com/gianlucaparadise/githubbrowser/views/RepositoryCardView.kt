package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.gianlucaparadise.githubbrowser.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.repository_card_view.view.*

class RepositoryCardView @JvmOverloads constructor(
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

    init {
        inflate(context, R.layout.repository_card_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RepositoryCardView)

        this.ownerVisible = attributes.getBoolean(R.styleable.RepositoryCardView_ownerVisible, true)
        this.title = attributes.getString(R.styleable.RepositoryCardView_title) ?: ""
        this.owner = attributes.getString(R.styleable.RepositoryCardView_owner) ?: ""
        this.shortDescription =
            attributes.getString(R.styleable.RepositoryCardView_shortDescription) ?: ""
        this.primaryLanguage =
            attributes.getString(R.styleable.RepositoryCardView_primaryLanguage) ?: ""
        this.totalStars = attributes.getInteger(R.styleable.RepositoryCardView_totalStars, 0)
        this.avatarUrl = attributes.getString(R.styleable.RepositoryCardView_avatarUrl)

        attributes.recycle()
    }
}