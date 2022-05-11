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
import com.example.storyapp.customView.NameEditText
import com.example.storyapp.customView.PasswordEditText
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.view.MainViewModel
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory

private val Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var etName: NameEditText
    private lateinit var etPassword: PasswordEditText
    private lateinit var etEmail: EmailEditText
    private lateinit var registerViewModel: MainViewModel
    private lateinit var settingViewModel: SettingMainViewModel
    val timer: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        etName = registerBinding.name
        etPassword = registerBinding.regPassword
        etEmail = registerBinding.email
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        setupView()
        playAnimation()
        registerViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        registerBinding.apply {
            btnCreate.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                if (name.isEmpty()) {
                    etName.error = resources.getString(R.string.text_empty)
                }
                if (email.isEmpty()) {
                    etEmail.error = resources.getString(R.string.text_empty)
                }
                if (password.isEmpty()) {
                    etPassword.error = resources.getString(R.string.text_empty)
                }
                registerViewModel.register(name, email, password)
                with(registerViewModel) {
                    getMessage().observe(this@RegisterActivity) {
                        info(it.message)
                    }
                    isLoading.observe(this@RegisterActivity) { load ->
                        showLoading(load)
                    }
                    intent.observe(this@RegisterActivity) {
                        if (it) {
                            messege.observe(this@RegisterActivity) {
                                info(it)
                            }
                            Handler(Looper.getMainLooper()).postDelayed({
                                startActivity(
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                )
                                finish()
                            }, timer)
                        } else {
                            messege.observe(this@RegisterActivity) {
                                info(it)
                            }
                        }
                    }
                }
            }
        }
        setDarkMode()

        registerBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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
        ObjectAnimator.ofFloat(registerBinding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name =
            ObjectAnimator.ofFloat(registerBinding.name, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(registerBinding.regPassword, View.ALPHA, 1f).setDuration(500)
        val login =
            ObjectAnimator.ofFloat(registerBinding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(registerBinding.email, View.ALPHA, 1f).setDuration(500)
        val register =
            ObjectAnimator.ofFloat(registerBinding.btnCreate, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(registerBinding.title, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(title, name, email, password, together)
            start()
        }
    }

    private fun setButtonEnabled() {
        val resultPassword = etPassword.text
        val resultEmail = etEmail.text
        val resultFullname = etName.text
        registerBinding.btnCreate.isEnabled =
            resultPassword != null && resultPassword.toString().isNotEmpty() &&
                    resultEmail != null && resultEmail.toString().isNotEmpty() &&
                    resultFullname != null && resultFullname.toString().isNotEmpty()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            registerBinding.progressBar.visibility = View.VISIBLE
        } else {
            registerBinding.progressBar.visibility = View.GONE
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