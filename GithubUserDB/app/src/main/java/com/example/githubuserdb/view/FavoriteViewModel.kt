package com.example.githubuserdb.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.database.FavoriteUserDao
import com.example.githubuserdb.database.FavoriteDatabase
import com.example.githubuserdb.repository.FavoriteRepo



class FavoriteViewModel(application : Application) : ViewModel() {
    private val  favRepo : FavoriteRepo = FavoriteRepo(application)
    private val dbFavorite: FavoriteDatabase? = FavoriteDatabase.getDatabase(application)
    private val userDao: FavoriteUserDao? = dbFavorite?.favoriteUserDao()


    fun getUserFavorite() : LiveData<List<FavoriteUser>> = favRepo.getFavoriteUser()


    fun addFavUser(favData : FavoriteUser){
        favRepo.insert(favData)
    }
    fun userCheck(id: Int) = userDao?.userCheck(id)

    fun deleteFavorite(id: Int){
        favRepo.delete(id)
    }
}
