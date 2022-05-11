package com.example.storyapp.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val userResponse = MutableLiveData<UserStoryResponse>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val listStory = MutableLiveData<ArrayList<ListStoryItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _intent = MutableLiveData<Boolean>()
    val intent: LiveData<Boolean> = _intent

    private val _messege = MutableLiveData<String>()
    val messege: LiveData<String> = _messege

    //    ================================================================================
    fun getMessage(): LiveData<UserStoryResponse> = userResponse

    fun get(): LiveData<ArrayList<ListStoryItem>> = listStory

    fun getLogin(): LiveData<LoginResponse> = loginResponse

    fun addFile(imageMultipart: MultipartBody.Part, description: RequestBody, token: String) {
        val service =
            ApiConfig.getApi().uploadImage(imageMultipart, description, token = "Bearer $token")
        service.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _messege.value = response.body()?.message
                    }
                } else {
                    _messege.value = response.message()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun setAdapter(token: String) {
        _isLoading.value = true
        val setAdapter = ApiConfig.getApi().getAllStories(token = "Bearer $token")
        setAdapter.enqueue(object : Callback<AllStoryResponse> {
            override fun onResponse(
                call: Call<AllStoryResponse>,
                response: Response<AllStoryResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listStory.postValue(response.body()?.listStory)
                    _messege.value = response.body()?.message
                } else {
                    _messege.value = response.message()
                }
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val login = ApiConfig.getApi().login(email, password)
        login.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    loginResponse.postValue(response.body())
                    _intent.value = true
                    _messege.value = response.body()?.message
                } else {
                    _messege.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Failure", t.message.toString())
            }

        })
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        val regist = ApiConfig.getApi().registerUser(name, email, password)
        regist.enqueue(object : Callback<UserStoryResponse> {
            override fun onResponse(
                call: Call<UserStoryResponse>,
                response: Response<UserStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userResponse.postValue(response.body())
                    _intent.value = true
                } else {
                    _messege.value = response.message()
                }
            }

            override fun onFailure(call: Call<UserStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Failure", t.message.toString())
            }
        })
    }
}