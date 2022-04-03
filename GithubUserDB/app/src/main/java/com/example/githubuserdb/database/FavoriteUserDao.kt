package com.example.githubuserdb.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite")
    fun getFavorite() : LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favorite WHERE favorite.id =:id")
    fun userCheck(id:Int) : Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun userDelete(id:Int) : Int


}