package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.SitaraMartDatabase
import com.example.data.model.*
import com.example.data.repository.SitaraMartRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class BottomTab(val titleUrdu: String, val titleEnglish: String) {
    HOME("ہوم", "Home"),
    DEALERS("ڈیلرز", "Dealers"),
    PURCHASES("خریداری", "Purchase"),
    REPORTS("رپورٹس", "Reports"),
    SETTINGS("ترتیبات", "Settings");

    companion object {
        val MORE get() = SETTINGS
    }
}

sealed class ActiveModal {
    object None : ActiveModal()
    data class DealerDetail(val dealer: Dealer) : ActiveModal()
    data class ProductComparison(val product: Product) : ActiveModal()
    data class NewPurchase(val prefilledDealerId: Long? = null, val prefilledProductId: Long? = null) : ActiveModal()
    object RateComparison : ActiveModal()
    object ExcelImportExport : ActiveModal()
    object BannerSettings : ActiveModal()
    object AddDealer : ActiveModal()
    object AddProduct : ActiveModal()
    object AlertsSheet : ActiveModal()
}

class SitaraMartViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SitaraMartDatabase.getDatabase(application, viewModelScope)
    private val repository = SitaraMartRepository(db.dao())

    // UI States
    private val _currentTab = MutableStateFlow(BottomTab.HOME)
    val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeModal = MutableStateFlow<ActiveModal>(ActiveModal.None)
    val activeModal: StateFlow<ActiveModal> = _activeModal.asStateFlow()

    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

    // Data Flows from Repository
    val allDealers: StateFlow<List<Dealer>> = repository.allDealers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRates: StateFlow<List<DealerProductRate>> = repository.allRates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPurchases: StateFlow<List<PurchaseEntry>> = repository.allPurchases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bannerConfig: StateFlow<BannerConfig> = repository.bannerConfig
        .map { it ?: BannerConfig() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BannerConfig())

    val recentRates: StateFlow<List<ProductWithRates>> = repository.recentRates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAlerts: StateFlow<List<InventoryAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered lists based on search query
    val filteredRecentRates: StateFlow<List<ProductWithRates>> = combine(
        recentRates,
        _searchQuery
    ) { rates, query ->
        if (query.isBlank()) rates
        else rates.filter { item ->
            item.product.urduName.contains(query, ignoreCase = true) ||
                    item.product.name.contains(query, ignoreCase = true) ||
                    (item.dealer?.urduName?.contains(query, ignoreCase = true) == true) ||
                    (item.dealer?.name?.contains(query, ignoreCase = true) == true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDealers: StateFlow<List<Dealer>> = combine(
        allDealers,
        _searchQuery
    ) { dealers, query ->
        if (query.isBlank()) dealers
        else dealers.filter { dealer ->
            dealer.urduName.contains(query, ignoreCase = true) ||
                    dealer.name.contains(query, ignoreCase = true) ||
                    dealer.categories.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadAlertsCount: StateFlow<Int> = allAlerts.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Actions
    fun selectTab(tab: BottomTab) {
        _currentTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun openModal(modal: ActiveModal) {
        _activeModal.value = modal
    }

    fun closeModal() {
        _activeModal.value = ActiveModal.None
    }

    fun dismissNotification() {
        _notificationMessage.value = null
    }

    fun recordPurchase(
        invoiceNo: String,
        dealerId: Long,
        productId: Long,
        quantity: Double,
        rate: Double,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val total = quantity * rate
            val entry = PurchaseEntry(
                invoiceNo = invoiceNo.ifBlank { "INV-${System.currentTimeMillis() % 10000}" },
                dealerId = dealerId,
                productId = productId,
                quantity = quantity,
                rate = rate,
                totalAmount = total,
                status = "Delivered",
                notes = notes
            )
            repository.insertPurchase(entry)
            _notificationMessage.value = "خریداری کامیابی سے درج کر لی گئی ہے (PKR ${total.toInt()})"
            closeModal()
        }
    }

    fun updateBanner(title: String, subtitle: String, isVisible: Boolean, imageUrl: String) {
        viewModelScope.launch {
            repository.updateBanner(
                BannerConfig(
                    id = 1,
                    isVisible = isVisible,
                    titleUrdu = title,
                    subtitleUrdu = subtitle,
                    imageUrl = imageUrl
                )
            )
            _notificationMessage.value = "بینر کی ترتیبات اپ ڈیٹ کر دی گئیں"
            closeModal()
        }
    }

    fun addDealer(
        urduName: String,
        name: String,
        phone: String,
        whatsapp: String,
        address: String,
        categories: String
    ) {
        viewModelScope.launch {
            repository.insertDealer(
                Dealer(
                    name = name.ifBlank { urduName },
                    urduName = urduName,
                    contactPerson = urduName,
                    phone = phone,
                    whatsapp = whatsapp.ifBlank { phone },
                    address = address,
                    categories = categories
                )
            )
            _notificationMessage.value = "نیا ڈیلر شامل ہو گیا: $urduName"
            closeModal()
        }
    }

    fun addProduct(
        urduName: String,
        name: String,
        category: String,
        unit: String,
        stock: Double,
        minAlert: Double
    ) {
        viewModelScope.launch {
            repository.insertProduct(
                Product(
                    name = name.ifBlank { urduName },
                    urduName = urduName,
                    category = category,
                    unit = unit,
                    currentStock = stock,
                    minStockAlert = minAlert
                )
            )
            _notificationMessage.value = "نئی پروڈکٹ شامل ہو گئی: $urduName"
            closeModal()
        }
    }

    fun updateProductRate(
        dealerId: Long,
        productId: Long,
        newRate: Double,
        previousRate: Double
    ) {
        viewModelScope.launch {
            repository.insertRate(
                DealerProductRate(
                    dealerId = dealerId,
                    productId = productId,
                    currentRate = newRate,
                    previousRate = previousRate,
                    lastUpdated = System.currentTimeMillis()
                )
            )
            // Also generate alert if price change > 0
            val diff = newRate - previousRate
            if (diff != 0.0) {
                val type = if (diff > 0) "RATE_HIKE" else "RATE_DROP"
                val sign = if (diff > 0) "+" else ""
                repository.addAlert(
                    InventoryAlert(
                        title = "ریٹ میں تبدیلی ($sign$diff PKR)",
                        message = "نئے ریٹس لاگو کر دیے گئے ہیں۔",
                        alertType = type
                    )
                )
            }
            _notificationMessage.value = "نیا ریٹ محفوظ ہو گیا ہے: PKR $newRate"
        }
    }

    fun importCsvData(content: String) {
        viewModelScope.launch {
            val count = repository.importDataFromCsv(content)
            _notificationMessage.value = "$count آئٹمز کامیابی سے امپورٹ ہو گئے ہیں"
            closeModal()
        }
    }

    fun markAlertsAsRead() {
        viewModelScope.launch {
            repository.markAllAlertsRead()
        }
    }

    fun clearAlerts() {
        viewModelScope.launch {
            repository.clearAllAlerts()
        }
    }
}
