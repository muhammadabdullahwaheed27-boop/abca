package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BannerConfig
import com.example.data.model.Dealer
import com.example.data.model.ProductWithRates
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    bannerConfig: BannerConfig,
    recentRates: List<ProductWithRates>,
    searchQuery: String,
    filteredDealers: List<Dealer>,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    unreadAlertsCount: Int,
    onSearchQueryChanged: (String) -> Unit,
    onEditBannerClick: () -> Unit,
    onDealersTabClick: () -> Unit,
    onProductsClick: () -> Unit,
    onRecordPurchaseClick: () -> Unit,
    onRateComparisonClick: () -> Unit,
    onExcelImportClick: () -> Unit,
    onProductSelected: (ProductWithRates) -> Unit,
    onDealerSelected: (Dealer) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSearching = searchQuery.isNotBlank()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            TopHeader(
                onMenuClick = onMenuClick,
                onNotificationClick = onNotificationClick,
                unreadAlertsCount = unreadAlertsCount
            )
        }

        // Search Bar
        item {
            SearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onFilterClick = onRateComparisonClick
            )
        }

        // If user is searching, show real-time search results for Dealers and Products!
        if (isSearching) {
            item {
                Text(
                    text = "تلاش کے نتائج برائے: \"$searchQuery\"",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Matching Dealers
            if (filteredDealers.isNotEmpty()) {
                item {
                    Text(
                        text = "ڈیلرز (${filteredDealers.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SitaraBlue,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                items(filteredDealers) { dealer ->
                    SearchDealerItem(
                        dealer = dealer,
                        onClick = { onDealerSelected(dealer) }
                    )
                }
            }

            // Matching Products / Rates
            if (recentRates.isNotEmpty()) {
                item {
                    Text(
                        text = "پروڈکٹس اور ریٹس (${recentRates.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SitaraBlue,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                items(recentRates) { rateItem ->
                    RecentRateCard(
                        item = rateItem,
                        onClick = { onProductSelected(rateItem) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }

            if (filteredDealers.isEmpty() && recentRates.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "کوئی پروڈکٹ یا ڈیلر نہیں ملا",
                            fontSize = 15.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            // Normal Home View (Promotional Banner, Quick Search, Daily Tasks, Recent Rates)
            item {
                PromotionalBanner(
                    bannerConfig = bannerConfig,
                    onEditBannerClick = onEditBannerClick
                )
            }

            item {
                QuickSearchSection(
                    onDealersClick = onDealersTabClick,
                    onProductsClick = onProductsClick,
                    onViewAllClick = onRateComparisonClick
                )
            }

            item {
                DailyTasksSection(
                    onRecordPurchaseClick = onRecordPurchaseClick,
                    onRateComparisonClick = onRateComparisonClick,
                    onExcelImportClick = onExcelImportClick,
                    onViewAllClick = onRateComparisonClick
                )
            }

            item {
                RecentRatesSection(
                    rates = recentRates,
                    onProductClick = onProductSelected,
                    onViewAllClick = onRateComparisonClick
                )
            }
        }
    }
}

@Composable
private fun SearchDealerItem(
    dealer: Dealer,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SitaraLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = SitaraBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dealer.urduName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${dealer.categories} • ${dealer.address}",
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1
                )
            }
            Text(
                text = "تفصیلات",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = SitaraBlue
            )
        }
    }
}
