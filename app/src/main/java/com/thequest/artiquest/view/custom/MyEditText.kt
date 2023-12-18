package com.thequest.artiquest.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.thequest.artiquest.R


class MyEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (id) {
                    R.id.usernameEditText -> validateUsername(s.toString())
                    R.id.emailEditText -> validateEmail(s.toString())
                    R.id.passwordEditText -> validatePassword(s.toString())
                    R.id.editTextNama -> validateName(s.toString())
                    R.id.editTextUsername -> validateUsername(s.toString())
                    R.id.editTextEmail -> validateEmail(s.toString())
                    R.id.editTextNumber -> validateNumber(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun validateName(text: String) {
        val errorMessage = when {
            text.isEmpty() -> resources.getString(R.string.name_warning_empty)
            else -> null
        }

        if (errorMessage != null) {
            error = errorMessage
        } else {
            error = null
        }
    }

    private fun validateNumber(text: String) {
        val regexPattern = Regex("^\\+?[0-9.-]+\$")
        val errorMessage = when {
            text.isEmpty() -> resources.getString(R.string.number_warning_empty)
            !regexPattern.matches(text) -> resources.getString(R.string.number_warning_invalid)
            else -> null
        }

        if (errorMessage != null) {
            error = errorMessage
        } else {
            error = null
        }
    }

    private fun validateUsername(text: String) {
        val regexPattern = Regex("^[a-zA-Z0-9_-]{3,15}\$")
        val errorMessage = when {
            text.isEmpty() -> resources.getString(R.string.username_warning_empty)
            !regexPattern.matches(text) -> resources.getString(R.string.username_warning_invalid)
            else -> null
        }

        if (errorMessage != null) {
            error = errorMessage
        } else {
            error = null
        }
    }


    private fun validateEmail(text: String) {
        val errorMessage = when {
            text.isEmpty() -> resources.getString(R.string.email_warning_empty)
            !Patterns.EMAIL_ADDRESS.matcher(text)
                .matches() -> resources.getString(R.string.email_warning_invalid)

            else -> null
        }

        if (errorMessage != null) {
            error = errorMessage
        } else {
            error = null
        }
    }

    private fun validatePassword(text: String) {
        val errorMessage = when {
            text.isEmpty() -> resources.getString(R.string.password_warning_empty)
            text.length < 8 -> resources.getString(R.string.password_warning_length)
            !isValidPassword(text) -> resources.getString(R.string.password_warning_unique)
            else -> null
        }

        if (errorMessage != null) {
            error = errorMessage
        } else {
            error = null
        }
    }

    fun setUsernameHint() {
        hint = resources.getString(R.string.username)
    }

    fun setEmailHint() {
        hint = resources.getString(R.string.email_address)
    }

    fun setPasswordHint() {
        hint = resources.getString(R.string.password)
    }

    private fun isValidPassword(password: String): Boolean {
        val upperCase = password.any { it.isUpperCase() }
        val digit = password.any { it.isDigit() }

        return upperCase && digit
    }
}