package com.example.jetpackapp.di

import com.example.jetpackapp.repository.WisataRepository

object Injection {
    fun provideRepository(): WisataRepository {
        return WisataRepository.getInstance()
    }
}