package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.gianlucaparadise.githubbrowser.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.user_card_view.view.*

class UserCardView @JvmOverloads constructor(
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

    var loginId: String?
        get() {
            return card_header.subtitle
        }
        set(value) {
            card_header.subtitle = value
        }

    var bio: String?
        get() {
            return user_bio.text.toString()
        }
        set(value) {
            user_bio.text = value
            user_bio.isVisible = !value.isNullOrBlank()
        }

    var avatarUrl: String?
        get() = card_header.avatarUrl
        set(value) {
            card_header.avatarUrl = value
        }

    /**
     * When not specified, this uses colorOnSurface from theme.
     * Background color of the view is colorSurface from theme.
     */
    @get:ColorInt
    var textColor: Int
        get() = card_header.textColor
        set(value) {
            user_bio.setTextColor(value)
            card_header.textColor = value
        }

    init {
        inflate(context, R.layout.user_card_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.UserCardView)

        this.title = attributes.getString(R.styleable.UserCardView_title) ?: ""
        this.loginId = attributes.getString(R.styleable.UserCardView_loginId) ?: ""
        this.bio =
            attributes.getString(R.styleable.UserCardView_bio) ?: ""
        this.avatarUrl = attributes.getString(R.styleable.UserCardView_avatarUrl)

        // Text color
        val theme = context.theme

        val themeTextColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorOnSurface, themeTextColorTypedValue, true)
        this.textColor =
            attributes.getColor(R.styleable.UserCardView_textColor, themeTextColorTypedValue.data)

        // Background color
        val themeBackgroundColorTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, themeBackgroundColorTypedValue, true)
        this.setBackgroundColor(themeBackgroundColorTypedValue.data)

        attributes.recycle()
    }
}