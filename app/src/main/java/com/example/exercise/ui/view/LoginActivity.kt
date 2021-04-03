package com.example.exercise.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.exercise.R
import com.example.exercise.databinding.LoginActivityBinding
import com.example.exercise.ui.util.CommonUtil
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var loginActivitybinding: LoginActivityBinding
    private val TAG: String = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivitybinding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(loginActivitybinding.root)

        loginActivitybinding.firstNameEditText.addTextChangedListener { charSequence ->
            if (charSequence.toString().isNotEmpty() && loginActivitybinding.lastNameEditText.text != null && loginActivitybinding.lastNameEditText.text!!.isNotEmpty())
                enableOrDisableSubmitButton(true)
            else {
                enableOrDisableSubmitButton(false)
            }
        }

        loginActivitybinding.firstNameEditText.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                Log.d(TAG, "firstNameEditText onFocusChange is called , hasFocus = " + hasFocus)
                loginActivitybinding.firstName.setTextColor(getColorBasedOnFocus(hasFocus))
            }
        }

        loginActivitybinding.lastNameEditText.addTextChangedListener { charSequence ->
            if (charSequence.toString().isNotEmpty() && loginActivitybinding.firstNameEditText.text != null && loginActivitybinding.firstNameEditText.text!!.isNotEmpty())
                enableOrDisableSubmitButton(true)
            else {
                enableOrDisableSubmitButton(false)
            }
        }


        loginActivitybinding.lastNameEditText.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                Log.d(TAG, "lastNameEditText onFocusChange is called ")
                loginActivitybinding.lastName.setTextColor(getColorBasedOnFocus(hasFocus))
            }
        }

        loginActivitybinding.submitButton.setOnClickListener { view: View ->

            var firstName: String = loginActivitybinding.firstNameEditText.text.toString()
            var lastName = loginActivitybinding.lastNameEditText.text.toString()
            if (!isValidName(firstName)) {
                val firstNameErrorTitle = resources.getString(R.string.firs_name_error_title)
                val firstNameErrorMessage = resources.getString(R.string.firs_name_error_message)
                CommonUtil.showDialog(this@LoginActivity, firstNameErrorTitle, firstNameErrorMessage)
            } else if (!isValidName(lastName)) {
                val lastNameErrorTitle = resources.getString(R.string.last_name_error_title)
                val lasttNameErrorMessage = resources.getString(R.string.last_name_error_message)
                CommonUtil.showDialog(this@LoginActivity, lastNameErrorTitle, lasttNameErrorMessage)
            } else {
                val intent = Intent(this, JokesDisplayActivity::class.java)
                intent.putExtra(CommonUtil.FIRST_NAME, firstName)
                intent.putExtra(CommonUtil.LAST_NAME, lastName)
                startActivity(intent)
            }
        }
    }

    fun enableOrDisableSubmitButton(flag: Boolean) {
        loginActivitybinding.submitButton.isEnabled = flag
    }

    fun getColorBasedOnFocus(hasFocus: Boolean): Int {
        val color: Int
        if (hasFocus) {
            color = ContextCompat.getColor(applicationContext, R.color.focus_color)
        } else {
            color = ContextCompat.getColor(applicationContext, R.color.disabled_color)
        }
        return color
    }

    fun isValidName(name: String): Boolean {
        val inputStr: String = name
        val strBuffer = StringBuffer()
        strBuffer.append("^[a-zA-Z\\s]*$")
        val pattern: Pattern = Pattern.compile(String(strBuffer))
        val matcher: Matcher = pattern.matcher(inputStr)
        return matcher.matches()
    }

}