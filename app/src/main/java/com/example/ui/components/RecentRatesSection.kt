package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProductWithRates
import com.example.ui.theme.*

@Composable
fun RecentRatesSection(
    rates: List<ProductWithRates>,
    onProductClick: (ProductWithRates) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "تمام دیکھیں >"
            Row(
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تمام دیکھیں",
                    fontSize = 13.sp,
                    color = SitaraBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View All",
                    tint = SitaraBlue,
                    modifier = Modifier.size(14.dp)
                )
            }

            // "حالیہ ریٹس"
            Text(
                text = "حالیہ ریٹس",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cards List
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rates.forEach { rateItem ->
                RecentRateCard(
                    item = rateItem,
                    onClick = { onProductClick(rateItem) }
                )
            }
        }
    }
}

@Composable
fun RecentRateCard(
    item: ProductWithRates,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncrease = item.change > 0
    val isDecrease = item.change < 0
    val changeText = when {
        isIncrease -> "↑ +${item.change.toInt()}"
        isDecrease -> "↓ ${item.change.toInt()}"
        else -> "= 0"
    }
    val badgeBg = when {
        isIncrease -> RateGreenBg
        isDecrease -> RateRedBg
        else -> Color(0xFFF1F5F9)
    }
    val badgeText = when {
        isIncrease -> RateGreenText
        isDecrease -> RateRedText
        else -> TextMuted
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("rate_card_${item.product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image Thumbnail on Left
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                when (item.product.imageResName) {
                    "thumb_sugar" -> {
                        Image(
                            painter = painterResource(id = R.drawable.thumb_sugar),
                            contentDescription = item.product.urduName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    "thumb_rice" -> {
                        Image(
                            painter = painterResource(id = R.drawable.thumb_rice),
                            contentDescription = item.product.urduName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SitaraLightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = item.product.urduName,
                                tint = SitaraBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Product Name & Dealer Name
            Column(
                modifier = Modifier.weight(1.2f)
            ) {
                Text(
                    text = item.product.urduName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.dealer?.urduName ?: item.product.name,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }

            // Current Price & Unit
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "PKR ${item.currentRate.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SitaraBlue
                )
                Text(
                    text = item.unit,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Price Change Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBg)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = changeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeText
                )
                Text(
                    text = "(پچھلا ${item.previousRate.toInt()})",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeText.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Right Chevron Arrow
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = SitaraBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
