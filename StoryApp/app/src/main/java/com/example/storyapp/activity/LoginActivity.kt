package com.example.storyapp.activity


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.customView.EmailEditText
import com.example.storyapp.customView.PasswordEditText
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.preferences.UserPreference
import com.example.storyapp.view.MainViewModel
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory

private val Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var customPassword: PasswordEditText
    private lateinit var customEmail: EmailEditText
    private lateinit var loginViewModel: MainViewModel
    private lateinit var loginPref: UserPreference
    private lateinit var settingViewModel: SettingMainViewModel
    val timer: Long = 2000
    private lateinit var name: String
    private lateinit var userId: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        customPassword = loginBinding.password
        customEmail = loginBinding.email
        customPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        playAnimation()
        setDarkMode()
        setupView()
        setupViewModel()
        LoginPref()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loginBinding.apply {
            login.setOnClickListener { setLogin() }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

    }

    private fun LoginPref() {
        loginPref = UserPreference(this)
        with(loginViewModel) {
            getMessage().observe(this@LoginActivity) {
                info(it.message)
            }
            isLoading.observe(this@LoginActivity) { load ->
                showLoading(load)
            }
            getLogin().observe(this@LoginActivity) {
                name = it.loginResult.name
                userId = it.loginResult.userId
                token = it.loginResult.token
                loginPref.setUser(name, userId, token)
            }
            intent.observe(this@LoginActivity) {
                if (it) {
                    messege.observe(this@LoginActivity) {
                        info(it)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }, timer)
                } else {
                    messege.observe(this@LoginActivity) {
                        info(it)
                    }
                }
            }
        }
    }

    private fun setLogin() {
        loginBinding.apply {
            loginViewModel.messege.observe(this@LoginActivity) {
                info(it)
            }
            val email = customEmail.text.toString()
            val password = customPassword.text.toString()
            when {
                email.isEmpty() -> {
                    customEmail.error = resources.getString(R.string.text_empty)
                }
                password.isEmpty() -> {
                    customPassword.error = resources.getString(R.string.text_empty)
                }
            }
            loginViewModel.login(email, password)
        }
        loginBinding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginBinding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(loginBinding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email =
            ObjectAnimator.ofFloat(loginBinding.email, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(loginBinding.password, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(loginBinding.login, View.ALPHA, 1f).setDuration(500)
        val or = ObjectAnimator.ofFloat(loginBinding.textView7, View.ALPHA, 1f).setDuration(500)
        val register =
            ObjectAnimator.ofFloat(loginBinding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(loginBinding.title, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(or, register)
        }

        AnimatorSet().apply {
            playSequentially(title, email, password, login, together)
            start()
        }
    }

    private fun setButtonEnabled() {
        val resultPassword = customPassword.text
        val resultUsername = customEmail.text
        loginBinding.login.isEnabled =
            resultPassword != null && resultPassword.toString().isNotEmpty() &&
                    resultUsername != null && resultUsername.toString().isNotEmpty()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            loginBinding.progressBar.visibility = View.VISIBLE
        } else {
            loginBinding.progressBar.visibility = View.GONE
        }
    }

    private fun info(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setDarkMode() {
        val pref = SettingPreferences.getInstance(dataTheme)
        settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        ).get(SettingMainViewModel::class.java)
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}

