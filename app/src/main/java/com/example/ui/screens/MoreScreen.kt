package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    onOpenBannerSettings: () -> Unit,
    onOpenExcelImportExport: () -> Unit,
    onOpenRateComparison: () -> Unit,
    onAddNewDealer: () -> Unit,
    onAddNewProduct: () -> Unit,
    onOpenAlerts: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "مزید ترتیبات اور ٹولز",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardSurface)
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Store Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(SitaraDarkBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "ستارہ مارٹ گروسری اسٹور",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "غلہ منڈی، لاہور • سپلائر مینجمنٹ سسٹم",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "مالک: محمد عبداللہ وحید",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // Section 1: Management Tools
            item {
                Text(
                    text = "انتظامی ٹولز",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SitaraBlue,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            item {
                MoreActionRow(
                    title = "پروموشنل بینر کی ترتیبات (ایڈمن)",
                    subtitle = "تصویر، عنوان، اور بینر شو/ہائیڈ کریں",
                    icon = Icons.Default.ViewCarousel,
                    iconTint = SitaraBlue,
                    iconBg = SitaraLightBlue,
                    onClick = onOpenBannerSettings,
                    testTag = "more_banner_settings"
                )
            }

            item {
                MoreActionRow(
                    title = "ایکسل و سی ایس وی امپورٹ / ایکسپورٹ",
                    subtitle = "سپلائر ڈیٹا اور ریٹس فائل سے لوڈ کریں",
                    icon = Icons.Default.TableChart,
                    iconTint = ExcelShortcutIcon,
                    iconBg = ExcelShortcutBg,
                    onClick = onOpenExcelImportExport,
                    testTag = "more_excel_import"
                )
            }

            item {
                MoreActionRow(
                    title = "ریٹس کا تفصیلی موازنہ",
                    subtitle = "مختلف سپلائرز کے نرخ ناموں کا جائزہ لیں",
                    icon = Icons.Default.ShowChart,
                    iconTint = PurpleShortcutIcon,
                    iconBg = PurpleShortcutBg,
                    onClick = onOpenRateComparison,
                    testTag = "more_rate_comparison"
                )
            }

            item {
                MoreActionRow(
                    title = "اسٹاک اور ریٹ الرٹس",
                    subtitle = "قیمتوں کے اتار چڑھاؤ اور کم اسٹاک کی تاریخ",
                    icon = Icons.Default.Notifications,
                    iconTint = Color(0xFFEF4444),
                    iconBg = RateRedBg,
                    onClick = onOpenAlerts,
                    testTag = "more_alerts"
                )
            }

            // Section 2: Quick Registrations
            item {
                Text(
                    text = "فوری اندراج",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SitaraBlue,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            item {
                MoreActionRow(
                    title = "نیا سپلائر / ڈیلر شامل کریں",
                    subtitle = "نام، فون نمبر، واٹس ایپ اور منڈی کا پتہ",
                    icon = Icons.Default.PersonAdd,
                    iconTint = TealShortcutIcon,
                    iconBg = TealShortcutBg,
                    onClick = onAddNewDealer,
                    testTag = "more_add_dealer"
                )
            }

            item {
                MoreActionRow(
                    title = "نئی پروڈکٹ / جنس شامل کریں",
                    subtitle = "نام، کیٹیگری، یونٹ اور الرٹ حد",
                    icon = Icons.Default.AddShoppingCart,
                    iconTint = SitaraBlue,
                    iconBg = SitaraLightBlue,
                    onClick = onAddNewProduct,
                    testTag = "more_add_product"
                )
            }
        }
    }
}

@Composable
fun MoreActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
