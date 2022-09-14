package com.example.myproducts.repositories

import com.example.myproducts.CALL_NOT_EXECUTED
import com.example.myproducts.EMPTY_DATA
import com.example.myproducts.api.ApiService
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import com.example.myproducts.entity.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsRepository(private val apiService: ApiService) : CacheRepository() {
    private suspend fun getProductsApi() = apiService.getProducts()
    private suspend fun getProductApi(id: Int) = apiService.getProduct(id)

    suspend fun getProducts(productDatabaseDao: ProductDatabaseDao): StateData<List<Product>> {
        return withContext(Dispatchers.IO) {
            fetchAndCache(
                readFromLocalDatabase = {
                    readAllProductsFromLocalDatabase(productDatabaseDao)
                },
                fetch = {
                    fetchAllProducts()
                },
                cache = {
                    productDatabaseDao.insertAll(it)
                }
            )
        }
    }

    suspend fun getProduct(
        productDatabaseDao: ProductDatabaseDao,
        id: Int
    ): StateData<Product> {
        return withContext(Dispatchers.IO) {
            fetchAndCache(
                readFromLocalDatabase = {
                    productDatabaseDao.get(id)
                },
                fetch = {
                    fetchProduct(id)
                },
                cache = {
                    productDatabaseDao.insert(it)
                }
            )
        }
    }


    private suspend fun readAllProductsFromLocalDatabase(productDatabaseDao: ProductDatabaseDao): List<Product>? {
        return if (productDatabaseDao.getAll() == null || productDatabaseDao.getAll()!!
                .isEmpty()
        ) null
        else productDatabaseDao.getAll()
    }

    private suspend fun fetchAllProducts(): StateData<List<Product>> {

        val productsResponse: Response<Products>?
        try {
            productsResponse = getProductsApi()
        } catch (e: Exception) {
            // Call is not executed....
            return StateData(StateData.Status.ERROR, null, CALL_NOT_EXECUTED, e.message)
        }

        if (productsResponse.isSuccessful) {
            productsResponse.body()?.let { copyProducts ->

                return StateData(
                    StateData.Status.SUCCESS,
                    copyProducts.products,
                    null,
                    productsResponse.message()
                )

            } ?: run {
                // Empty data --- error
                return StateData(
                    StateData.Status.ERROR,
                    null,
                    EMPTY_DATA,
                    productsResponse.message()
                )
            }
        } else {
            return StateData(
                StateData.Status.ERROR,
                null,
                productsResponse.code(),
                productsResponse.message()
            )
        }

    }

    private suspend fun fetchProduct(id: Int): StateData<Product> {

        val productResponse: Response<Product>?
        try {
            productResponse = getProductApi(id)
        } catch (e: Exception) {
            // Call is not executed....
            return StateData(StateData.Status.ERROR, null, CALL_NOT_EXECUTED, e.message)
        }

        if (productResponse.isSuccessful) {
            productResponse.body()?.let { copyProduct ->

                return StateData(
                    StateData.Status.SUCCESS,
                    copyProduct,
                    null,
                    productResponse.message()
                )

            } ?: run {
                // Empty data --- error
                return StateData(
                    StateData.Status.ERROR,
                    null,
                    EMPTY_DATA,
                    productResponse.message()
                )
            }
        } else {
            return StateData(
                StateData.Status.ERROR,
                null,
                productResponse.code(),
                productResponse.message()
            )
        }

    }
}
