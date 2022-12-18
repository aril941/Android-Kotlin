package com.example.jetpackapp.repository

import com.example.jetpackapp.data.OrderDataWisata
import com.example.jetpackapp.data.WisataProvider
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.Flow


class WisataRepository {
    val wisataRepo = mutableListOf<OrderDataWisata>()


    init {
        if (wisataRepo.isEmpty()) {
            WisataProvider.wisata.forEach {
                wisataRepo.add(OrderDataWisata(it))
            }
        }
    }
    fun getHomeDetailRepo(): Flow<List<OrderDataWisata>> {
        return flowOf(wisataRepo)
    }

    fun getOrderWisataById(rewardId: Long): OrderDataWisata {
        return wisataRepo.first {
            it.wisata.id == rewardId
        }
    }

    companion object {
        @Volatile
        private var instance: WisataRepository? = null

        fun getInstance(): WisataRepository =
            instance ?: synchronized(this) {
                WisataRepository().apply {
                    instance = this
                }
            }
    }
}