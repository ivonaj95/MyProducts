package com.example.myproducts.repositories

import com.example.myproducts.CALL_NOT_EXECUTED
import com.example.myproducts.EMPTY_DATA
import com.example.myproducts.api.ApiService
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.domain.ProductMapper
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import com.example.myproducts.entity.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsRepository(private val apiService: ApiService) : CacheRepository() {
    private val productMapper = ProductMapper()

    private suspend fun getProductsApi() = apiService.getProducts()
    private suspend fun getProductApi(id: Int) = apiService.getProduct(id)

    suspend fun getProducts(productDatabaseDao: ProductDatabaseDao): StateData<List<ProductDomain>> {
        return withContext(Dispatchers.IO) {
            fetchAndCache(
                readFromLocalDatabase = {
                    readAllProductsFromLocalDatabase(productDatabaseDao)
                },
                fetch = {
                    fetchAllProducts()
                },
                cache = {
                    productDatabaseDao.insertAll(productMapper.toEntityList(it))
                }
            )
        }
    }

    suspend fun getProduct(
        productDatabaseDao: ProductDatabaseDao,
        id: Int
    ): StateData<ProductDomain> {
        return withContext(Dispatchers.IO) {
            fetchAndCache(
                readFromLocalDatabase = {
                    readProductFromLocalDatabase(id, productDatabaseDao)
                },
                fetch = {
                    fetchProduct(id)
                },
                cache = {
                    productDatabaseDao.insert(productMapper.mapToEntity(it))
                }
            )
        }
    }

    private suspend fun readProductFromLocalDatabase(
        id: Int,
        productDatabaseDao: ProductDatabaseDao
    ): ProductDomain? {
        productDatabaseDao.get(id)?.let {
            return productMapper.mapFromEntity(it)
        } ?: run {
            return null
        }
    }


    private suspend fun readAllProductsFromLocalDatabase(productDatabaseDao: ProductDatabaseDao): List<ProductDomain>? {
        productDatabaseDao.getAll()?.let {
            if (it.isEmpty()) {
                return null
            } else {
                return productMapper.fromEntityList(it)
            }
        } ?: run {
            return null
        }

    }

    private suspend fun fetchAllProducts(): StateData<List<ProductDomain>> {

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
                    copyProducts.products?.let { productMapper.fromEntityList(it) },
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

    private suspend fun fetchProduct(id: Int): StateData<ProductDomain> {

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
                    productMapper.mapFromEntity(copyProduct),
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
