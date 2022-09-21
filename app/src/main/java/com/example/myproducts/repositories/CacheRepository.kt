package com.example.myproducts.repositories

import android.util.Log

open class CacheRepository<T>(
    private val remoteDataSource: RemoteDataSource<T>,
    private val localDataSource: LocalDataSource<T>
) {

    suspend fun fetchAllAndCache(): CacheResponse<List<T>> {
        localDataSource.getAll()?.let {
            Log.d("IVONA", "[fetchAllAndCache] local $it")
            if (it.isNotEmpty()) {
                return CacheResponse(true, it, null, null)
            }
        }

        val remoteResponse = remoteDataSource.fetchAll()
        Log.d("IVONA", "[fetchAllAndCache] fetch ${remoteResponse.body}")
        if (remoteResponse.isSuccessful) {
            remoteResponse.body?.let {
                localDataSource.cacheAll(it)
            }
        }
        return remoteResponse
    }

    suspend fun fetchItemAndCache(id: Int): CacheResponse<T> {
        localDataSource.getItem(id)?.let {
            Log.d("IVONA", "[fetchItemAndCache] local $it")
            return CacheResponse(true, it, null, null)
        }
        val remoteResponse = remoteDataSource.fetch(id)
        Log.d("IVONA", "[fetchItemAndCache] fetch ${remoteResponse.body}")
        if (remoteResponse.isSuccessful) {
            remoteResponse.body?.let {
                localDataSource.cacheItem(it)
            }
        }
        return remoteResponse
    }
}

interface RemoteDataSource<T> {
    suspend fun fetchAll(): CacheResponse<List<T>>
    suspend fun fetch(id: Int): CacheResponse<T>
}

interface LocalDataSource<T> {
    suspend fun getAll(): List<T>?
    suspend fun getItem(id: Int): T?
    suspend fun cacheItem(item: T)
    suspend fun cacheAll(items: List<T>)
}

data class CacheResponse<T>(
    var isSuccessful: Boolean,
    var body: T?,
    var code: Int?,
    var message: String?
)