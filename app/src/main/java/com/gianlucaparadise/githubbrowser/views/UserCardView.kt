package com.gianlucaparadise.githubbrowser.views

import android.content.Context
import android.util.AttributeSet
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
            return user_title.text.toString()
        }
        set(value) {
            user_title.text = value
        }

    var loginId: String?
        get() {
            return user_login_id.text.toString()
        }
        set(value) {
            user_login_id.text = value
            user_login_id.isVisible = !value.isNullOrBlank()
        }

    var bio: String?
        get() {
            return user_bio.text.toString()
        }
        set(value) {
            user_bio.text = value
            user_bio.isVisible = !value.isNullOrBlank()
        }

    init {
        inflate(context, R.layout.user_card_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.UserCardView)

        this.title = attributes.getString(R.styleable.UserCardView_title) ?: ""
        this.loginId = attributes.getString(R.styleable.UserCardView_loginId) ?: ""
        this.bio =
            attributes.getString(R.styleable.UserCardView_bio) ?: ""

        attributes.recycle()
    }
}