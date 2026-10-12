package com.example.ui.modals

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.screens.launchCall
import com.example.ui.screens.launchSms
import com.example.ui.screens.launchWhatsApp
import com.example.ui.theme.*

// 1. Dealer Detail Sheet with Functional WhatsApp, Call, SMS, and Purchase List
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealerDetailModal(
    dealer: Dealer,
    products: List<Product>,
    rates: List<DealerProductRate>,
    onDismiss: () -> Unit,
    onRecordPurchase: (Dealer, Product?) -> Unit,
    onUpdateRate: (Long, Long, Double, Double) -> Unit
) {
    val context = LocalContext.current
    val dealerRates = remember(rates, dealer) {
        rates.filter { it.dealerId == dealer.id }
    }
    val productMap = remember(products) { products.associateBy { it.id } }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = dealer.urduName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${dealer.name} • ${dealer.contactPerson}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Info Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("فون نمبر: ${dealer.phone}", fontSize = 13.sp, color = TextPrimary)
                            Text("درجہ بندی: ⭐ ${dealer.rating}", fontSize = 13.sp, color = Color(0xFFD97706))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("پتہ: ${dealer.address}", fontSize = 12.sp, color = TextSecondary)
                        Text("سپلائی اجناس: ${dealer.categories}", fontSize = 12.sp, color = SitaraBlue)
                        if (dealer.balanceDue > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "بقايا واجبات: PKR ${dealer.balanceDue.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RateRedText
                            )
                        }
                    }
                }
            }

            // Functional Actions Row: WhatsApp, Call, SMS, Purchase List
            item {
                Text(
                    text = "فوری رابطہ و اقدامات",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // WhatsApp
                    Button(
                        onClick = { launchWhatsApp(context, dealer.whatsapp, dealer.urduName) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dealer_whatsapp_action")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("واٹس ایپ", fontSize = 11.sp)
                    }

                    // Call
                    Button(
                        onClick = { launchCall(context, dealer.phone) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dealer_call_action")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("کال کریں", fontSize = 11.sp)
                    }

                    // SMS
                    Button(
                        onClick = { launchSms(context, dealer.phone, "ستارہ مارٹ: ریٹ لسٹ مطلوب ہے۔") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SitaraBlueViolet),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dealer_sms_action")
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ایس ایم ایس", fontSize = 11.sp)
                    }
                }
            }

            // Purchase List Button
            item {
                Button(
                    onClick = { onRecordPurchase(dealer, null) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SitaraDarkBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dealer_purchase_list_action")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("خریداری کی فہرست / نیا آرڈر درج کریں", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Products Supplied by this Dealer
            item {
                Text(
                    text = "اس ڈیلر کی مصنوعات اور ریٹس (${dealerRates.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (dealerRates.isEmpty()) {
                item {
                    Text(
                        text = "اس ڈیلر کے لیے ابھی کوئی ریٹ درج نہیں ہے۔",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            } else {
                items(dealerRates) { rate ->
                    val product = productMap[rate.productId]
                    if (product != null) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = product.urduName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "موجودہ اسٹاک: ${product.currentStock.toInt()} ${product.unit}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "PKR ${rate.currentRate.toInt()}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SitaraBlue
                                        )
                                        Text(
                                            text = product.unit,
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    IconButton(
                                        onClick = { onRecordPurchase(dealer, product) },
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(SitaraLightBlue)
                                    ) {
                                        Icon(
                                            Icons.Default.AddShoppingCart,
                                            contentDescription = "Order",
                                            tint = SitaraBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 2. Product Comparison Modal showing all suppliers with rates, contact actions, and best rate highlight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductComparisonModal(
    product: Product,
    dealers: List<Dealer>,
    rates: List<DealerProductRate>,
    onDismiss: () -> Unit,
    onRecordPurchase: (Dealer, Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dealerMap = remember(dealers) { dealers.associateBy { it.id } }

    val productRates = remember(rates, product) {
        rates.filter { it.productId == product.id }.sortedBy { it.currentRate }
    }
    val lowestRate = remember(productRates) { productRates.firstOrNull()?.currentRate }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Product Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = product.urduName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${product.name} • ${product.category} • اسٹاک: ${product.currentStock.toInt()} ${product.unit}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Title & count
            item {
                Text(
                    text = "تمام دستیاب سپلائرز کے ریٹس کا موازنہ (${productRates.size} ڈیلرز)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SitaraBlue
                )
            }

            if (productRates.isEmpty()) {
                item {
                    Text(
                        text = "اس پروڈکٹ کے لیے کوئی ریٹ دستیاب نہیں ہے۔",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(productRates) { rate ->
                    val dealer = dealerMap[rate.dealerId]
                    val isBestPrice = lowestRate != null && rate.currentRate == lowestRate

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isBestPrice) Color(0xFFF0FDF4) else CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isBestPrice) 1.5.dp else 1.dp,
                                color = if (isBestPrice) Color(0xFF22C55E) else CardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
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
                                    Column {
                                        Text(
                                            text = dealer?.urduName ?: "ڈیلر #${rate.dealerId}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = dealer?.address ?: "",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                if (isBestPrice) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "★ بہترین ریٹ",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "PKR ${rate.currentRate.toInt()} ${product.unit}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isBestPrice) Color(0xFF15803D) else SitaraBlue
                                    )
                                    if (rate.previousRate > 0) {
                                        Text(
                                            text = "پچھلا ریٹ: PKR ${rate.previousRate.toInt()}",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                // Actions for this dealer
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (dealer != null) {
                                        IconButton(
                                            onClick = { launchWhatsApp(context, dealer.whatsapp, dealer.urduName) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFFDCFCE7))
                                        ) {
                                            Icon(
                                                Icons.Default.Chat,
                                                contentDescription = "WhatsApp",
                                                tint = Color(0xFF16A34A),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { launchCall(context, dealer.phone) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(SitaraLightBlue)
                                        ) {
                                            Icon(
                                                Icons.Default.Phone,
                                                contentDescription = "Call",
                                                tint = SitaraBlue,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (dealer != null) {
                                                onRecordPurchase(dealer, product)
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("خریداری کریں", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. New Purchase Entry Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPurchaseModal(
    dealers: List<Dealer>,
    products: List<Product>,
    rates: List<DealerProductRate>,
    prefilledDealerId: Long?,
    prefilledProductId: Long?,
    onDismiss: () -> Unit,
    onSubmit: (invoiceNo: String, dealerId: Long, productId: Long, quantity: Double, rate: Double, notes: String) -> Unit
) {
    var selectedDealerId by remember {
        mutableStateOf(prefilledDealerId ?: dealers.firstOrNull()?.id ?: 0L)
    }
    var selectedProductId by remember {
        mutableStateOf(prefilledProductId ?: products.firstOrNull()?.id ?: 0L)
    }

    val matchedRate = remember(selectedDealerId, selectedProductId, rates) {
        rates.find { it.dealerId == selectedDealerId && it.productId == selectedProductId }?.currentRate ?: 100.0
    }

    var quantityText by remember { mutableStateOf("50") }
    var rateText by remember(matchedRate) { mutableStateOf(matchedRate.toInt().toString()) }
    var invoiceNo by remember { mutableStateOf("SM-${(1000..9999).random()}") }
    var notes by remember { mutableStateOf("") }

    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val total = quantity * rate

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "خریداری درج کریں",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Dealer Selector
            Text("سپلائر منتخب کریں:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            var dealerExpanded by remember { mutableStateOf(false) }
            val currentDealer = dealers.find { it.id == selectedDealerId }

            ExposedDropdownMenuBox(
                expanded = dealerExpanded,
                onExpandedChange = { dealerExpanded = !dealerExpanded }
            ) {
                OutlinedTextField(
                    value = currentDealer?.urduName ?: "منتخب کریں",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dealerExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = dealerExpanded,
                    onDismissRequest = { dealerExpanded = false }
                ) {
                    dealers.forEach { dealer ->
                        DropdownMenuItem(
                            text = { Text(dealer.urduName) },
                            onClick = {
                                selectedDealerId = dealer.id
                                dealerExpanded = false
                            }
                        )
                    }
                }
            }

            // Product Selector
            Text("پروڈکٹ / جنس منتخب کریں:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            var productExpanded by remember { mutableStateOf(false) }
            val currentProduct = products.find { it.id == selectedProductId }

            ExposedDropdownMenuBox(
                expanded = productExpanded,
                onExpandedChange = { productExpanded = !productExpanded }
            ) {
                OutlinedTextField(
                    value = currentProduct?.urduName ?: "منتخب کریں",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = productExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = productExpanded,
                    onDismissRequest = { productExpanded = false }
                ) {
                    products.forEach { prod ->
                        DropdownMenuItem(
                            text = { Text("${prod.urduName} (${prod.unit})") },
                            onClick = {
                                selectedProductId = prod.id
                                productExpanded = false
                            }
                        )
                    }
                }
            }

            // Quantity & Rate row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("مقدار (${currentProduct?.unit ?: "کلو"})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rateText,
                    onValueChange = { rateText = it },
                    label = { Text("خریداری ریٹ (PKR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            // Total Amount Display
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SitaraLightBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("کل لاگت:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        "PKR ${total.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SitaraBlue
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (selectedDealerId > 0 && selectedProductId > 0 && quantity > 0 && rate > 0) {
                        onSubmit(invoiceNo, selectedDealerId, selectedProductId, quantity, rate, notes)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_purchase_button")
            ) {
                Text("خریداری محفوظ کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 4. Admin Banner Customizer Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerSettingsModal(
    config: BannerConfig,
    onDismiss: () -> Unit,
    onSave: (title: String, subtitle: String, isVisible: Boolean, imageUrl: String) -> Unit
) {
    var title by remember { mutableStateOf(config.titleUrdu) }
    var subtitle by remember { mutableStateOf(config.subtitleUrdu) }
    var isVisible by remember { mutableStateOf(config.isVisible) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "پروموشنل بینر کی ترتیبات (ایڈمن)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Visibility Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppBackground)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("بینر ظاہر کریں (Visibility)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("ہوم اسکرین پر بینر شو یا ہائیڈ کریں", fontSize = 11.sp, color = TextMuted)
                }
                Switch(
                    checked = isVisible,
                    onCheckedChange = { isVisible = it },
                    modifier = Modifier.testTag("banner_visibility_switch")
                )
            }

            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("بینر کا عنوان (اردو)") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("banner_title_input")
            )

            // Subtitle Field
            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("بینر کا ذیلی عنوان") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("banner_subtitle_input")
            )

            // Save Button
            Button(
                onClick = { onSave(title, subtitle, isVisible, config.imageUrl) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_banner_settings_button")
            ) {
                Text("ترتیبات محفوظ کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 5. Excel / CSV Import & Export Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcelImportExportModal(
    dealers: List<Dealer>,
    products: List<Product>,
    rates: List<DealerProductRate>,
    onDismiss: () -> Unit,
    onImportCsv: (String) -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    var csvText by remember { mutableStateOf("") }

    val sampleCsv = """
چینی,Sugar,کریانہ,فی کلو,علی سپلائرز,03001234567,155,150,100
چاول,Rice,کریانہ,فی کلو,بلال سپلائرز,03219876543,142,145,150
گھی,Ghee,آئل و گھی,فی کلو,طارق آئل ملز,03124448888,480,470,80
آٹا,Atta,فلور ملز,فی کلو,پنجاب فلور ملز,03017779999,120,120,200
دال چنا,Daal,دالیں,فی کلو,احمد ٹریڈرز,03335551234,260,270,90
    """.trimIndent()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ایکسل / سی ایس وی امپورٹ اور ایکسپورٹ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "آپ ایکسل سے ڈیٹا کاپی کر کے نیچے پیسٹ کر سکتے ہیں یا نمونہ ڈیٹا لوڈ کر سکتے ہیں:",
                fontSize = 12.sp,
                color = TextSecondary
            )

            // Button to load sample
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { csvText = sampleCsv },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("نمونہ ڈیٹا لوڈ کریں", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = {
                        val exported = buildString {
                            appendLine("ProductUrdu,ProductName,Category,Unit,DealerUrdu,Phone,CurrentRate,PreviousRate")
                            products.forEach { p ->
                                val r = rates.find { it.productId == p.id }
                                val d = dealers.find { it.id == r?.dealerId }
                                appendLine("${p.urduName},${p.name},${p.category},${p.unit},${d?.urduName ?: ""},${d?.phone ?: ""},${r?.currentRate ?: 0},${r?.previousRate ?: 0}")
                            }
                        }
                        clipboard.setText(AnnotatedString(exported))
                        csvText = exported
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("موجودہ ڈیٹا ایکسپورٹ کریں", fontSize = 12.sp)
                }
            }

            OutlinedTextField(
                value = csvText,
                onValueChange = { csvText = it },
                label = { Text("CSV / ایکسل ٹیکسٹ پیسٹ کریں") },
                shape = RoundedCornerShape(12.dp),
                maxLines = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("csv_input_field")
            )

            Button(
                onClick = {
                    if (csvText.isNotBlank()) {
                        onImportCsv(csvText)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExcelShortcutIcon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("import_csv_button")
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("ایکسل ڈیٹا امپورٹ کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 6. Real-time Inventory Alerts Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsModal(
    alerts: List<InventoryAlert>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit,
    onClearAll: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "اطلاعات اور الرٹس",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Row {
                    TextButton(onClick = onMarkAllRead) {
                        Text("سب پڑھ لیا", fontSize = 12.sp, color = SitaraBlue)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            if (alerts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("کوئی الرٹ موجود نہیں ہے", color = TextMuted)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(alerts) { alert ->
                        val isHike = alert.alertType == "RATE_HIKE"
                        val isLowStock = alert.alertType == "LOW_STOCK"
                        val iconBg = when {
                            isLowStock -> RateRedBg
                            isHike -> Color(0xFFFEF3C7)
                            else -> RateGreenBg
                        }
                        val iconTint = when {
                            isLowStock -> RateRedText
                            isHike -> Color(0xFF92400E)
                            else -> RateGreenText
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = if (alert.isRead) AppBackground else CardSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(iconBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isLowStock) Icons.Default.Warning else Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = iconTint,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = alert.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = alert.message,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 7. Add Dealer Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDealerModal(
    onDismiss: () -> Unit,
    onSave: (urduName: String, name: String, phone: String, whatsapp: String, address: String, categories: String) -> Unit
) {
    var urduName by remember { mutableStateOf("") }
    var englishName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "نیا سپلائر / ڈیلر شامل کریں",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            OutlinedTextField(
                value = urduName,
                onValueChange = { urduName = it },
                label = { Text("ڈیلر کا نام (اردو) *") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("فون نمبر (کال کے لیے) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("واٹس ایپ نمبر (بشمول 92)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("منڈی کا پتہ / شہر") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categories,
                onValueChange = { categories = it },
                label = { Text("اجناس / کیٹیگریز (مثلاً: چینی، چاول)") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (urduName.isNotBlank() && phone.isNotBlank()) {
                        onSave(urduName, englishName, phone, whatsapp, address, categories)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("محفوظ کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 8. Add Product Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductModal(
    onDismiss: () -> Unit,
    onSave: (urduName: String, name: String, category: String, unit: String, stock: Double, minAlert: Double) -> Unit
) {
    var urduName by remember { mutableStateOf("") }
    var englishName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("کریانہ") }
    var unit by remember { mutableStateOf("فی کلو") }
    var stockText by remember { mutableStateOf("100") }
    var alertText by remember { mutableStateOf("30") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "نئی پروڈکٹ شامل کریں",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            OutlinedTextField(
                value = urduName,
                onValueChange = { urduName = it },
                label = { Text("پروڈکٹ کا نام (اردو) *") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("کیٹیگری") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("یونٹ (مثلاً: فی کلو)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("موجودہ اسٹاک") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = alertText,
                    onValueChange = { alertText = it },
                    label = { Text("کم از کم الرٹ حد") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    if (urduName.isNotBlank()) {
                        val stock = stockText.toDoubleOrNull() ?: 0.0
                        val alert = alertText.toDoubleOrNull() ?: 0.0
                        onSave(urduName, englishName, category, unit, stock, alert)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("محفوظ کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
