package com.example.myproducts.database

import androidx.room.*
import com.example.myproducts.DATABASE_NAME
import com.example.myproducts.entity.Product

@Dao
interface ProductDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products : List<Product>)

    @Query("SELECT * FROM $DATABASE_NAME WHERE id = :key")
    suspend fun get(key: Int): Product?

    @Query("SELECT * FROM $DATABASE_NAME")
    suspend fun getAll() : List<Product>?

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM $DATABASE_NAME")
    suspend fun clear()
}