package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Dealer
import com.example.data.model.DealerProductRate
import com.example.data.model.Product
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealersScreen(
    dealers: List<Dealer>,
    products: List<Product>,
    rates: List<DealerProductRate>,
    onSelectDealer: (Dealer) -> Unit,
    onAddNewDealerClick: () -> Unit,
    onRecordPurchaseForDealer: (Dealer) -> Unit,
    onUpdateRateClick: (Dealer, Product, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredDealers = remember(dealers, searchQuery) {
        if (searchQuery.isBlank()) dealers
        else dealers.filter {
            it.urduName.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.categories.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ڈیلرز اور سپلائرز",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = onAddNewDealerClick,
                        modifier = Modifier.testTag("add_dealer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add Dealer",
                            tint = SitaraBlue
                        )
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
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ڈیلر کا نام یا اشیاء تلاش کریں...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        focusedBorderColor = SitaraBlue,
                        unfocusedBorderColor = CardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                )
            }

            // Stats summary card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
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
                        Column {
                            Text(
                                text = "کل رجسٹرڈ سپلائرز",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${dealers.size} ڈیلرز",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SitaraDarkBlue
                            )
                        }
                        Button(
                            onClick = onAddNewDealerClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SitaraBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("نیا ڈیلر", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Dealers List
            items(filteredDealers) { dealer ->
                DealerCardItem(
                    dealer = dealer,
                    onClick = { onSelectDealer(dealer) }
                )
            }
        }
    }
}

@Composable
fun DealerCardItem(
    dealer: Dealer,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("dealer_card_${dealer.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SitaraLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = SitaraBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = dealer.urduName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = dealer.contactPerson,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Balance due pill
                if (dealer.balanceDue > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "بقايا واجبات",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "PKR ${dealer.balanceDue.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RateRedText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Categories & Address
            Text(
                text = "اجناس: ${dealer.categories}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Text(
                text = "پتہ: ${dealer.address}",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: WhatsApp, Call, SMS, View Products
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // WhatsApp
                IconButton(
                    onClick = { launchWhatsApp(context, dealer.whatsapp, dealer.urduName) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDCFCE7))
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Call
                IconButton(
                    onClick = { launchCall(context, dealer.phone) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SitaraLightBlue)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        tint = SitaraBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // SMS
                IconButton(
                    onClick = { launchSms(context, dealer.phone, "ستارہ مارٹ: ریٹ لسٹ اور اسٹاک آرڈر مطلوب ہے۔") },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PurpleShortcutBg)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "SMS",
                        tint = PurpleShortcutIcon,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Detail Button
                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("اشیاء و ریٹس", fontSize = 12.sp, color = SitaraBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = SitaraBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// Functional Intents Helper Functions
fun launchWhatsApp(context: Context, whatsappNumber: String, dealerName: String) {
    try {
        val cleanNumber = whatsappNumber.replace("+", "").replace("-", "").replace(" ", "")
        val message = Uri.encode("السلام علیکم $dealerName! ستارہ مارٹ سے تازہ ریٹس اور سپلائی آرڈر کے سلسلے میں رابطہ ہے۔")
        val uri = Uri.parse("https://wa.me/$cleanNumber?text=$message")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "واٹس ایپ اوپن نہیں ہو سکی: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

fun launchCall(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "کال ممکن نہیں ہو سکی", Toast.LENGTH_SHORT).show()
    }
}

fun launchSms(context: Context, phone: String, message: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone"))
        intent.putExtra("sms_body", message)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "میسج ممکن نہیں ہو سکا", Toast.LENGTH_SHORT).show()
    }
}
