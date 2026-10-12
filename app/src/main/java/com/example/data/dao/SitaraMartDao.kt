package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SitaraMartDao {

    // Dealers
    @Query("SELECT * FROM dealers ORDER BY name ASC")
    fun getAllDealers(): Flow<List<Dealer>>

    @Query("SELECT * FROM dealers WHERE id = :id")
    suspend fun getDealerById(id: Long): Dealer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealer(dealer: Dealer): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealers(dealers: List<Dealer>): List<Long>

    @Update
    suspend fun updateDealer(dealer: Dealer)

    @Delete
    suspend fun deleteDealer(dealer: Dealer)

    // Products
    @Query("SELECT * FROM products ORDER BY urduName ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>): List<Long>

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    // Rates
    @Query("SELECT * FROM dealer_rates ORDER BY lastUpdated DESC")
    fun getAllRates(): Flow<List<DealerProductRate>>

    @Query("SELECT * FROM dealer_rates WHERE productId = :productId ORDER BY currentRate ASC")
    fun getRatesForProduct(productId: Long): Flow<List<DealerProductRate>>

    @Query("SELECT * FROM dealer_rates WHERE dealerId = :dealerId")
    fun getRatesForDealer(dealerId: Long): Flow<List<DealerProductRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: DealerProductRate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<DealerProductRate>): List<Long>

    @Update
    suspend fun updateRate(rate: DealerProductRate)

    // Purchases
    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntry>>

    @Query("SELECT * FROM purchases WHERE dealerId = :dealerId ORDER BY date DESC")
    fun getPurchasesForDealer(dealerId: Long): Flow<List<PurchaseEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: PurchaseEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchases(purchases: List<PurchaseEntry>): List<Long>

    @Update
    suspend fun updatePurchase(purchase: PurchaseEntry)

    // Banner Config
    @Query("SELECT * FROM banner_config WHERE id = 1 LIMIT 1")
    fun getBannerConfig(): Flow<BannerConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBanner(banner: BannerConfig)

    // Alerts
    @Query("SELECT * FROM inventory_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<InventoryAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: InventoryAlert): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<InventoryAlert>): List<Long>

    @Query("UPDATE inventory_alerts SET isRead = 1")
    suspend fun markAllAlertsRead()

    @Query("DELETE FROM inventory_alerts")
    suspend fun clearAllAlerts()
}
