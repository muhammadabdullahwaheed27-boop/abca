package com.example.data.repository

import com.example.data.dao.SitaraMartDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SitaraMartRepository(private val dao: SitaraMartDao) {

    val allDealers: Flow<List<Dealer>> = dao.getAllDealers()
    val allProducts: Flow<List<Product>> = dao.getAllProducts()
    val allRates: Flow<List<DealerProductRate>> = dao.getAllRates()
    val allPurchases: Flow<List<PurchaseEntry>> = dao.getAllPurchases()
    val bannerConfig: Flow<BannerConfig?> = dao.getBannerConfig()
    val allAlerts: Flow<List<InventoryAlert>> = dao.getAllAlerts()

    // Combined Flow for Recent Rates Display in Reference UI
    val recentRates: Flow<List<ProductWithRates>> = combine(
        dao.getAllProducts(),
        dao.getAllRates(),
        dao.getAllDealers()
    ) { products, rates, dealers ->
        val dealerMap = dealers.associateBy { it.id }
        products.mapNotNull { product ->
            val productRates = rates.filter { it.productId == product.id }
            if (productRates.isEmpty()) null
            else {
                // Pick latest updated or lowest rate
                val topRate = productRates.maxByOrNull { it.lastUpdated } ?: productRates.first()
                val dealer = dealerMap[topRate.dealerId]
                ProductWithRates(
                    product = product,
                    dealer = dealer,
                    currentRate = topRate.currentRate,
                    previousRate = topRate.previousRate,
                    change = topRate.currentRate - topRate.previousRate,
                    unit = product.unit,
                    dealerCount = productRates.size
                )
            }
        }
    }

    suspend fun getDealerWithProducts(dealerId: Long): DealerWithProducts? {
        val dealer = dao.getDealerById(dealerId) ?: return null
        return DealerWithProducts(dealer = dealer)
    }

    fun getRatesForProduct(productId: Long): Flow<List<DealerProductRate>> {
        return dao.getRatesForProduct(productId)
    }

    suspend fun insertPurchase(purchase: PurchaseEntry) {
        dao.insertPurchase(purchase)
        // Also update the dealer product rate if needed
        val product = dao.getProductById(purchase.productId)
        if (product != null) {
            // Update stock
            val updatedProduct = product.copy(currentStock = product.currentStock + purchase.quantity)
            dao.updateProduct(updatedProduct)
        }
    }

    suspend fun insertDealer(dealer: Dealer): Long = dao.insertDealer(dealer)

    suspend fun insertProduct(product: Product): Long = dao.insertProduct(product)

    suspend fun insertRate(rate: DealerProductRate): Long = dao.insertRate(rate)

    suspend fun updateBanner(config: BannerConfig) = dao.insertOrUpdateBanner(config)

    suspend fun markAllAlertsRead() = dao.markAllAlertsRead()

    suspend fun clearAllAlerts() = dao.clearAllAlerts()

    suspend fun addAlert(alert: InventoryAlert) = dao.insertAlert(alert)

    // Excel / CSV Bulk Import
    suspend fun importDataFromCsv(csvContent: String): Int {
        var importedCount = 0
        val lines = csvContent.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return 0

        // Format: ProductUrdu,ProductName,Category,Unit,DealerUrdu,DealerPhone,Rate,PrevRate,Stock
        val startIndex = if (lines[0].contains("Product", ignoreCase = true) || lines[0].contains("نام")) 1 else 0

        for (i in startIndex until lines.size) {
            val parts = lines[i].split(",").map { it.trim() }
            if (parts.size >= 5) {
                try {
                    val pUrdu = parts[0]
                    val pName = parts.getOrNull(1) ?: pUrdu
                    val category = parts.getOrNull(2) ?: "جنرل"
                    val unit = parts.getOrNull(3) ?: "فی کلو"
                    val dUrdu = parts.getOrNull(4) ?: "نامعلوم سپلائر"
                    val rate = parts.getOrNull(6)?.toDoubleOrNull() ?: 100.0
                    val prevRate = parts.getOrNull(7)?.toDoubleOrNull() ?: rate

                    val pId = dao.insertProduct(
                        Product(
                            name = pName,
                            urduName = pUrdu,
                            category = category,
                            unit = unit,
                            currentStock = 100.0,
                            minStockAlert = 50.0
                        )
                    )

                    val dId = dao.insertDealer(
                        Dealer(
                            name = dUrdu,
                            urduName = dUrdu,
                            contactPerson = dUrdu,
                            phone = parts.getOrNull(5) ?: "03001234567",
                            whatsapp = "923001234567",
                            address = "لاہور",
                            categories = pUrdu
                        )
                    )

                    dao.insertRate(
                        DealerProductRate(
                            dealerId = dId,
                            productId = pId,
                            currentRate = rate,
                            previousRate = prevRate
                        )
                    )
                    importedCount++
                } catch (e: Exception) {
                    // skip malformed row
                }
            }
        }
        return importedCount
    }
}
