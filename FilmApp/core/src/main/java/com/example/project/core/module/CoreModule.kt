package com.example.project.core.module

import androidx.room.Room
import com.example.project.core.data.FilmAppRepository
import com.example.project.core.data.source.local.LocalDataSource
import com.example.project.core.data.source.local.room.FilmDatabase
import com.example.project.core.data.source.remote.RemoteDataSource
import com.example.project.core.data.source.remote.network.ApiService
import com.example.project.core.domain.repository.DomainFilmAppRepository
import com.example.project.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<FilmDatabase>().filmDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("sqltxt".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FilmDatabase::class.java, "movie.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/discover/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<DomainFilmAppRepository> { FilmAppRepository(get(), get(), get()) }
}