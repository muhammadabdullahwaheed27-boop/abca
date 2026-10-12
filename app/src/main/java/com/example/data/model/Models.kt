package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dealers")
data class Dealer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val urduName: String,
    val contactPerson: String,
    val phone: String,
    val whatsapp: String,
    val address: String,
    val categories: String,
    val balanceDue: Double = 0.0,
    val rating: Float = 4.8f
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val urduName: String,
    val category: String,
    val unit: String = "فی کلو",
    val currentStock: Double,
    val minStockAlert: Double,
    val imageResName: String = "",
    val description: String = ""
)

@Entity(tableName = "dealer_rates")
data class DealerProductRate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dealerId: Long,
    val productId: Long,
    val currentRate: Double,
    val previousRate: Double,
    val lastUpdated: Long = System.currentTimeMillis(),
    val minOrderQty: Double = 1.0,
    val notes: String = ""
)

@Entity(tableName = "purchases")
data class PurchaseEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val invoiceNo: String,
    val dealerId: Long,
    val productId: Long,
    val quantity: Double,
    val rate: Double,
    val totalAmount: Double,
    val date: Long = System.currentTimeMillis(),
    val status: String = "Delivered", // "Delivered", "Pending", "Dispatched"
    val notes: String = ""
)

@Entity(tableName = "banner_config")
data class BannerConfig(
    @PrimaryKey val id: Int = 1,
    val isVisible: Boolean = true,
    val titleUrdu: String = "آج کی خریداری\nآسان بنائیں",
    val subtitleUrdu: String = "ریٹ دیکھیں • ڈیلر تلاش کریں",
    val imageUrl: String = "grocery_basket",
    val highlightTag: String = "سپلائر ریٹس"
)

@Entity(tableName = "inventory_alerts")
data class InventoryAlert(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val alertType: String, // "LOW_STOCK", "RATE_HIKE", "RATE_DROP", "ORDER_UPDATE"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

// UI Composite Data Structures
data class ProductWithRates(
    val product: Product,
    val dealer: Dealer?,
    val currentRate: Double,
    val previousRate: Double,
    val change: Double, // currentRate - previousRate
    val unit: String,
    val dealerCount: Int = 1
)

data class DealerWithProducts(
    val dealer: Dealer,
    val rates: List<ProductRateWithProductInfo> = emptyList()
)

data class ProductRateWithProductInfo(
    val rate: DealerProductRate,
    val product: Product
)

data class DealerRateWithDealerInfo(
    val rate: DealerProductRate,
    val dealer: Dealer
)
