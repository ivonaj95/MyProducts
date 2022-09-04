package com.example.myproducts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myproducts.DATABASE_NAME
import com.example.myproducts.entity.Product

const val DATABASE_VERSION = 1

@Database(entities = [Product::class], version = DATABASE_VERSION)
@TypeConverters(ListConverters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDatabaseDao : ProductDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}