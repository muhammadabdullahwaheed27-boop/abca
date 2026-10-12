package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Dealer
import com.example.data.model.Product
import com.example.ui.components.BottomNavBar
import com.example.ui.modals.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SitaraMartApp(
    viewModel: SitaraMartViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeModal by viewModel.activeModal.collectAsStateWithLifecycle()
    val notificationMessage by viewModel.notificationMessage.collectAsStateWithLifecycle()

    val bannerConfig by viewModel.bannerConfig.collectAsStateWithLifecycle()
    val recentRates by viewModel.recentRates.collectAsStateWithLifecycle()
    val filteredRecentRates by viewModel.filteredRecentRates.collectAsStateWithLifecycle()
    val allDealers by viewModel.allDealers.collectAsStateWithLifecycle()
    val filteredDealers by viewModel.filteredDealers.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val allRates by viewModel.allRates.collectAsStateWithLifecycle()
    val allPurchases by viewModel.allPurchases.collectAsStateWithLifecycle()
    val allAlerts by viewModel.allAlerts.collectAsStateWithLifecycle()
    val unreadAlertsCount by viewModel.unreadAlertsCount.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display notifications in snackbar
    LaunchedEffect(notificationMessage) {
        notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    // Android Back button handling
    BackHandler(enabled = activeModal !is ActiveModal.None || currentTab != BottomTab.HOME || drawerState.isOpen) {
        when {
            drawerState.isOpen -> scope.launch { drawerState.close() }
            activeModal !is ActiveModal.None -> viewModel.closeModal()
            currentTab != BottomTab.HOME -> viewModel.selectTab(BottomTab.HOME)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CardSurface,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header inside Drawer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(SitaraDarkBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sitara Mart",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SitaraDarkBlue
                            )
                            Text(
                                text = "سپلائر مینجمنٹ سسٹم",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }

                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Drawer Navigation Items
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, tint = SitaraBlue) },
                        label = { Text("ہوم اسکرین", fontWeight = FontWeight.Bold) },
                        selected = currentTab == BottomTab.HOME,
                        onClick = {
                            viewModel.selectTab(BottomTab.HOME)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Group, contentDescription = null, tint = SitaraBlue) },
                        label = { Text("ڈیلرز اور سپلائرز", fontWeight = FontWeight.Bold) },
                        selected = currentTab == BottomTab.DEALERS,
                        onClick = {
                            viewModel.selectTab(BottomTab.DEALERS)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = SitaraBlue) },
                        label = { Text("خریداری اور آرڈرز", fontWeight = FontWeight.Bold) },
                        selected = currentTab == BottomTab.PURCHASES,
                        onClick = {
                            viewModel.selectTab(BottomTab.PURCHASES)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.BarChart, contentDescription = null, tint = SitaraBlue) },
                        label = { Text("اسٹاک اور رپورٹس", fontWeight = FontWeight.Bold) },
                        selected = currentTab == BottomTab.REPORTS,
                        onClick = {
                            viewModel.selectTab(BottomTab.REPORTS)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.TableChart, contentDescription = null, tint = ExcelShortcutIcon) },
                        label = { Text("ایکسل امپورٹ", fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            viewModel.openModal(ActiveModal.ExcelImportExport)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = SitaraBlue) },
                        label = { Text("بینر ترتیبات", fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            viewModel.openModal(ActiveModal.BannerSettings)
                            scope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SitaraLightBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("ستارہ مارٹ گروسری اسٹور", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SitaraDarkBlue)
                            Text("ورژن 1.0 • آفیشل سسٹم", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    ) {
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val isExpandedScreen = maxWidth >= 840.dp

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    BottomNavBar(
                        selectedTab = currentTab,
                        onTabSelected = { tab ->
                            viewModel.onSearchQueryChanged("")
                            viewModel.selectTab(tab)
                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        BottomTab.HOME -> {
                            HomeScreen(
                                bannerConfig = bannerConfig,
                                recentRates = filteredRecentRates,
                                searchQuery = searchQuery,
                                filteredDealers = filteredDealers,
                                onMenuClick = { scope.launch { drawerState.open() } },
                                onNotificationClick = { viewModel.openModal(ActiveModal.AlertsSheet) },
                                unreadAlertsCount = unreadAlertsCount,
                                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                                onEditBannerClick = { viewModel.openModal(ActiveModal.BannerSettings) },
                                onDealersTabClick = { viewModel.selectTab(BottomTab.DEALERS) },
                                onProductsClick = { viewModel.selectTab(BottomTab.REPORTS) },
                                onRecordPurchaseClick = { viewModel.openModal(ActiveModal.NewPurchase()) },
                                onRateComparisonClick = { viewModel.openModal(ActiveModal.RateComparison) },
                                onExcelImportClick = { viewModel.openModal(ActiveModal.ExcelImportExport) },
                                onProductSelected = { rateItem ->
                                    viewModel.openModal(ActiveModal.ProductComparison(rateItem.product))
                                },
                                onDealerSelected = { dealer ->
                                    viewModel.openModal(ActiveModal.DealerDetail(dealer))
                                }
                            )
                        }

                        BottomTab.DEALERS -> {
                            DealersScreen(
                                dealers = allDealers,
                                products = allProducts,
                                rates = allRates,
                                onSelectDealer = { dealer ->
                                    viewModel.openModal(ActiveModal.DealerDetail(dealer))
                                },
                                onAddNewDealerClick = { viewModel.openModal(ActiveModal.AddDealer) },
                                onRecordPurchaseForDealer = { dealer ->
                                    viewModel.openModal(ActiveModal.NewPurchase(prefilledDealerId = dealer.id))
                                },
                                onUpdateRateClick = { dealer, product, newRate, prevRate ->
                                    viewModel.updateProductRate(dealer.id, product.id, newRate, prevRate)
                                }
                            )
                        }

                        BottomTab.PURCHASES -> {
                            PurchasesScreen(
                                purchases = allPurchases,
                                dealers = allDealers,
                                products = allProducts,
                                onRecordPurchaseClick = { viewModel.openModal(ActiveModal.NewPurchase()) }
                            )
                        }

                        BottomTab.REPORTS -> {
                            ReportsScreen(
                                products = allProducts,
                                recentRates = recentRates,
                                dealers = allDealers,
                                onOrderProduct = { product ->
                                    viewModel.openModal(ActiveModal.NewPurchase(prefilledProductId = product.id))
                                }
                            )
                        }

                        BottomTab.SETTINGS -> {
                            MoreScreen(
                                onOpenBannerSettings = { viewModel.openModal(ActiveModal.BannerSettings) },
                                onOpenExcelImportExport = { viewModel.openModal(ActiveModal.ExcelImportExport) },
                                onOpenRateComparison = { viewModel.openModal(ActiveModal.RateComparison) },
                                onAddNewDealer = { viewModel.openModal(ActiveModal.AddDealer) },
                                onAddNewProduct = { viewModel.openModal(ActiveModal.AddProduct) },
                                onOpenAlerts = { viewModel.openModal(ActiveModal.AlertsSheet) }
                            )
                        }
                    }
                }
            }

            // Interactive Bottom Sheets / Modals based on activeModal state
            when (val modal = activeModal) {
                is ActiveModal.None -> {}

                is ActiveModal.DealerDetail -> {
                    DealerDetailModal(
                        dealer = modal.dealer,
                        products = allProducts,
                        rates = allRates,
                        onDismiss = { viewModel.closeModal() },
                        onRecordPurchase = { dealer, product ->
                            viewModel.openModal(
                                ActiveModal.NewPurchase(
                                    prefilledDealerId = dealer.id,
                                    prefilledProductId = product?.id
                                )
                            )
                        },
                        onUpdateRate = { dealerId, prodId, newRate, prevRate ->
                            viewModel.updateProductRate(dealerId, prodId, newRate, prevRate)
                        }
                    )
                }

                is ActiveModal.ProductComparison -> {
                    ProductComparisonModal(
                        product = modal.product,
                        dealers = allDealers,
                        rates = allRates,
                        onDismiss = { viewModel.closeModal() },
                        onRecordPurchase = { dealer, prod ->
                            viewModel.openModal(
                                ActiveModal.NewPurchase(
                                    prefilledDealerId = dealer.id,
                                    prefilledProductId = prod.id
                                )
                            )
                        }
                    )
                }

                is ActiveModal.NewPurchase -> {
                    NewPurchaseModal(
                        dealers = allDealers,
                        products = allProducts,
                        rates = allRates,
                        prefilledDealerId = modal.prefilledDealerId,
                        prefilledProductId = modal.prefilledProductId,
                        onDismiss = { viewModel.closeModal() },
                        onSubmit = { inv, dId, pId, qty, rate, notes ->
                            viewModel.recordPurchase(inv, dId, pId, qty, rate, notes)
                        }
                    )
                }

                is ActiveModal.RateComparison -> {
                    // Opens product comparison for the first product or recent rates
                    val firstProd = allProducts.firstOrNull()
                    if (firstProd != null) {
                        ProductComparisonModal(
                            product = firstProd,
                            dealers = allDealers,
                            rates = allRates,
                            onDismiss = { viewModel.closeModal() },
                            onRecordPurchase = { dealer, prod ->
                                viewModel.openModal(
                                    ActiveModal.NewPurchase(
                                        prefilledDealerId = dealer.id,
                                        prefilledProductId = prod.id
                                    )
                                )
                            }
                        )
                    }
                }

                is ActiveModal.ExcelImportExport -> {
                    ExcelImportExportModal(
                        dealers = allDealers,
                        products = allProducts,
                        rates = allRates,
                        onDismiss = { viewModel.closeModal() },
                        onImportCsv = { csvContent ->
                            viewModel.importCsvData(csvContent)
                        }
                    )
                }

                is ActiveModal.BannerSettings -> {
                    BannerSettingsModal(
                        config = bannerConfig,
                        onDismiss = { viewModel.closeModal() },
                        onSave = { title, sub, visible, img ->
                            viewModel.updateBanner(title, sub, visible, img)
                        }
                    )
                }

                is ActiveModal.AlertsSheet -> {
                    AlertsModal(
                        alerts = allAlerts,
                        onDismiss = { viewModel.closeModal() },
                        onMarkAllRead = { viewModel.markAlertsAsRead() },
                        onClearAll = { viewModel.clearAlerts() }
                    )
                }

                is ActiveModal.AddDealer -> {
                    AddDealerModal(
                        onDismiss = { viewModel.closeModal() },
                        onSave = { uName, name, phone, wa, addr, cats ->
                            viewModel.addDealer(uName, name, phone, wa, addr, cats)
                        }
                    )
                }

                is ActiveModal.AddProduct -> {
                    AddProductModal(
                        onDismiss = { viewModel.closeModal() },
                        onSave = { uName, name, cat, unit, stock, minAlert ->
                            viewModel.addProduct(uName, name, cat, unit, stock, minAlert)
                        }
                    )
                }
            }
        }
    }
}
