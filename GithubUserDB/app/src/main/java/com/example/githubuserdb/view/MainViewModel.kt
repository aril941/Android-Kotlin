package com.example.githubuserdb.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.githubuserdb.api.ApiConfig
import com.example.githubuserdb.data.User
import com.example.githubuserdb.data.UserDetail
import com.example.githubuserdb.data.UserResponse
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.database.FavoriteUserDao
import com.example.githubuserdb.database.FavoriteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainViewModel(application: Application): AndroidViewModel(application){

    val listUser = MutableLiveData<ArrayList<User>>()
    val detailUser = MutableLiveData<UserDetail>()

    private var userDao : FavoriteUserDao
    private var  dbFavorite : FavoriteDatabase

    init {
        dbFavorite = FavoriteDatabase.getDatabase(application)
        userDao = dbFavorite.favoriteUserDao()
    }

    fun setSearch(username: String){
        ApiConfig.getApi()
            .getUser(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful){
                        listUser.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }
    fun setFollowers(username : String){
        ApiConfig.getApi()
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if(response.isSuccessful){
                        listUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }
    fun setFollowing(username : String){
        ApiConfig.getApi()
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if(response.isSuccessful){
                        listUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun setdetail(username: String){
        ApiConfig.getApi()
            .getDetail(username)
            .enqueue(object : Callback<UserDetail> {
                override fun onResponse(
                    call: Call<UserDetail>,
                    response: Response<UserDetail>
                ) {
                    if(response.isSuccessful){
                        detailUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun get(): LiveData<ArrayList<User>> = listUser
    fun getDetail(): LiveData<UserDetail> = detailUser
}