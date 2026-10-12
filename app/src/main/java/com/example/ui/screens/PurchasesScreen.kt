package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
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
import com.example.data.model.Dealer
import com.example.data.model.Product
import com.example.data.model.PurchaseEntry
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreen(
    purchases: List<PurchaseEntry>,
    dealers: List<Dealer>,
    products: List<Product>,
    onRecordPurchaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dealerMap = remember(dealers) { dealers.associateBy { it.id } }
    val productMap = remember(products) { products.associateBy { it.id } }

    val totalSpend = remember(purchases) { purchases.sumOf { it.totalAmount } }
    val pendingOrders = remember(purchases) { purchases.filter { it.status == "Pending" } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "خریداری اور وینڈر آرڈرز",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                },
                actions = {
                    Button(
                        onClick = onRecordPurchaseClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("record_purchase_action_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("خریداری درج کریں", fontSize = 12.sp)
                    }
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
            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Total Spend Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SitaraLightBlue),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("کل لاگت خریداری", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "PKR ${totalSpend.toInt()}",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = SitaraDarkBlue
                            )
                            Text("${purchases.size} انوائسز", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    // Pending Orders Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PurpleShortcutBg),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("زیر التواء وینڈر آرڈرز", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${pendingOrders.size} آرڈرز",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleShortcutIcon
                            )
                            Text("ڈیلیوری متوقع", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }

            // Invoices Header
            item {
                Text(
                    text = "حالیہ وینڈر انوائسز",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Invoices List
            if (purchases.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("کوئی خریداری کا ریکارڈ موجود نہیں", color = TextMuted)
                        }
                    }
                }
            } else {
                items(purchases) { purchase ->
                    val dealer = dealerMap[purchase.dealerId]
                    val product = productMap[purchase.productId]
                    PurchaseInvoiceCard(
                        purchase = purchase,
                        dealerName = dealer?.urduName ?: "سپلائر #${purchase.dealerId}",
                        productName = product?.urduName ?: "پروڈکٹ #${purchase.productId}",
                        unit = product?.unit ?: "کلو"
                    )
                }
            }
        }
    }
}

@Composable
fun PurchaseInvoiceCard(
    purchase: PurchaseEntry,
    dealerName: String,
    productName: String,
    unit: String
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val isPending = purchase.status == "Pending"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .testTag("purchase_card_${purchase.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SitaraLightBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = purchase.invoiceNo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SitaraBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateFormat.format(Date(purchase.date)),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPending) Color(0xFFFEF3C7) else RateGreenBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isPending) "زیر التواء" else "وصول شدہ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPending) Color(0xFF92400E) else RateGreenText
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$productName • $dealerName",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "مقدار: ${purchase.quantity.toInt()} $unit  |  ریٹ: PKR ${purchase.rate.toInt()}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    if (purchase.notes.isNotBlank()) {
                        Text(
                            text = "نوٹ: ${purchase.notes}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "PKR ${purchase.totalAmount.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SitaraDarkBlue
                    )
                    Text(
                        text = "کل رقم",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
