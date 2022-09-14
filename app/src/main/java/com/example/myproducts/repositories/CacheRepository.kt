package com.example.myproducts.repositories

import android.util.Log
import com.example.myproducts.entity.StateData

open class CacheRepository {
    protected suspend fun <T> fetchAndCache(
        readFromLocalDatabase: suspend () -> T?,
        fetch: suspend () -> StateData<T>,
        cache: suspend (data: T) -> Unit
    ): StateData<T> {
        val cachedData = readFromLocalDatabase()
        if (cachedData != null) {
            Log.d("IVONA", "return cached data $cachedData")
            return StateData(StateData.Status.SUCCESS, cachedData, null, null)
        }
        Log.d("IVONA", "fetch and cache data")
        val fetchedData = fetch()
        fetchedData.data?.let { it ->
            cache(it)
        }
        return fetchedData
    }
}