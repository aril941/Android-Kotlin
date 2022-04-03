package com.example.githubuserdb.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserdb.database.FavoriteDatabase
import com.example.githubuserdb.database.FavoriteUser
import com.example.githubuserdb.database.FavoriteUserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteRepo(application: Application) {
    private val favDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val dataBase = FavoriteDatabase.getDatabase(application)
        favDao = dataBase.favoriteUserDao()
    }
    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = favDao.getFavorite()

    fun insert(favData: FavoriteUser) {
        executorService.execute { favDao.addFavorite(favData) }
    }
    fun delete(id: Int) {
        executorService.execute { favDao.userDelete(id) }
    }
}