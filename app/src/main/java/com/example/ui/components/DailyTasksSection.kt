package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DailyTasksSection(
    onRecordPurchaseClick: () -> Unit,
    onRateComparisonClick: () -> Unit,
    onExcelImportClick: () -> Unit,
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

            // "روزانہ کے کام"
            Text(
                text = "روزانہ کے کام",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3 Shortcut Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Shortcut 1: Record Purchase
            DailyTaskCard(
                title = "خریداری درج کریں",
                subtitle = "نئی خریداری کا ریکارڈ بنائیں",
                icon = Icons.Default.NoteAdd,
                iconBg = TealShortcutBg,
                iconTint = TealShortcutIcon,
                testTag = "task_record_purchase",
                onClick = onRecordPurchaseClick,
                modifier = Modifier.weight(1f)
            )

            // Shortcut 2: Compare Rates
            DailyTaskCard(
                title = "ریٹ کا موازنہ",
                subtitle = "بہترین ڈیلر اور کم ترین ریٹ",
                icon = Icons.Default.ShowChart,
                iconBg = PurpleShortcutBg,
                iconTint = PurpleShortcutIcon,
                testTag = "task_rate_compare",
                onClick = onRateComparisonClick,
                modifier = Modifier.weight(1f)
            )

            // Shortcut 3: Excel Import
            DailyTaskCard(
                title = "ایکسل امپورٹ",
                subtitle = "فائل سے ڈیٹا شامل کریں",
                icon = Icons.Default.TableChart,
                iconBg = ExcelShortcutBg,
                iconTint = ExcelShortcutIcon,
                testTag = "task_excel_import",
                onClick = onExcelImportClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DailyTaskCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rounded Icon Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Subtitle
            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
