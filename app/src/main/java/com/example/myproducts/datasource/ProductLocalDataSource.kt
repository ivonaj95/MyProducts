package com.example.myproducts.datasource

import com.example.myproducts.MyProductApplication
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.entity.Product
import com.example.myproducts.repositories.LocalDataSource

class ProductLocalDataSource : LocalDataSource<Product> {

    private val productDatabaseDao =
        ProductDatabase.getInstance(MyProductApplication.instance).productDatabaseDao

    override suspend fun getAll(): List<Product>? {
        return productDatabaseDao.getAll()
    }

    override suspend fun getItem(id: Int): Product? {
        return productDatabaseDao.get(id)
    }

    override suspend fun cacheItem(item: Product) {
        productDatabaseDao.insert(item)
    }

    override suspend fun cacheAll(items: List<Product>) {
        productDatabaseDao.insertAll(items)
    }

}