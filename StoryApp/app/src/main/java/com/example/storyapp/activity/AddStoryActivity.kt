package com.example.storyapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddBinding
import com.example.storyapp.preferences.SettingPreferences
import com.example.storyapp.preferences.UserPreference
import com.example.storyapp.utils.reduceImage
import com.example.storyapp.utils.rotateBitmap
import com.example.storyapp.utils.uriToFile
import com.example.storyapp.view.MainViewModel
import com.example.storyapp.view.SettingMainViewModel
import com.example.storyapp.view.SettingViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File



class AddStoryActivity : AppCompatActivity() {
    private val Context.dataTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")
    private lateinit var add: ActivityAddBinding
    private lateinit var settingViewModel: SettingMainViewModel
    private lateinit var mainModel: MainViewModel
    private lateinit var userPref: UserPreference
    private val timer : Long = 2000
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        add = ActivityAddBinding.inflate(layoutInflater)
        setContentView(add.root)
        supportActionBar?.title = getString(R.string.add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDarkMode()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        mainModel = ViewModelProvider(this)[MainViewModel::class.java]
        userPref = UserPreference(this)
        add.cameraXButton.setOnClickListener { startCameraX() }
        add.gallery.setOnClickListener { startGallery() }
        add.uploadButton.setOnClickListener { uploadImage() }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun uploadImage() {
        if(getFile == null && add.desc.text.toString() == "") {
            Toast.makeText(this, (getString(R.string.text_empty)), Toast.LENGTH_SHORT).show()
        }else if (getFile != null) {
            val file = reduceImage(getFile as File)
            val description = add.desc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile)

            val loginToken = userPref.getUser().token.toString()
            mainModel.addFile(imageMultipart, description, loginToken)
            if(add.desc.text.toString() == "") {
                Toast.makeText(this, (getString(R.string.desc_msg_null)), Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, (getString(R.string.upload_success)), Toast.LENGTH_SHORT)
                    .show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, timer)
            }
        }else {
            Toast.makeText(this, (resources.getString(R.string.picture_empty)), Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            add.previewImageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            add.previewImageView.setImageBitmap(result)
        }
    }

    private fun setDarkMode() {
        val pref = SettingPreferences.getInstance(dataTheme)
        settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingMainViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}