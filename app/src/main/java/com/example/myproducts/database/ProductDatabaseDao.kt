package com.example.myproducts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myproducts.DATABASE_NAME
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.Product

@Dao
interface ProductDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductDomain)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products : List<ProductDomain>)

    @Query("SELECT * FROM $DATABASE_NAME WHERE id = :key")
    suspend fun get(key: Int): ProductDomain?

    @Query("SELECT * FROM $DATABASE_NAME")
    fun getAll() : LiveData<List<ProductDomain>>

    @Delete
    suspend fun delete(product: ProductDomain)

    @Query("DELETE FROM $DATABASE_NAME")
    suspend fun clear()
}