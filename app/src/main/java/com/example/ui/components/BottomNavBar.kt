package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BottomTab
import com.example.ui.theme.BottomNavBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.SitaraBlue
import com.example.ui.theme.TextMuted

/**
 * Reusable Compose Bottom Navigation Bar component to switch between
 * Home, Dealers, Purchase, Reports, and Settings views.
 */
@Composable
fun BottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BottomNavBg)
            .border(width = 0.8.dp, color = CardBorder)
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            BottomNavItem(
                tab = BottomTab.HOME,
                urduLabel = "ہوم",
                englishLabel = "Home",
                icon = Icons.Default.Home,
                isSelected = selectedTab == BottomTab.HOME,
                onClick = { onTabSelected(BottomTab.HOME) },
                testTag = "bottom_nav_home"
            )

            // 2. Dealers
            BottomNavItem(
                tab = BottomTab.DEALERS,
                urduLabel = "ڈیلرز",
                englishLabel = "Dealers",
                icon = Icons.Default.Group,
                isSelected = selectedTab == BottomTab.DEALERS,
                onClick = { onTabSelected(BottomTab.DEALERS) },
                testTag = "bottom_nav_dealers"
            )

            // 3. Purchase
            BottomNavItem(
                tab = BottomTab.PURCHASES,
                urduLabel = "خریداری",
                englishLabel = "Purchase",
                icon = Icons.Default.ShoppingBag,
                isSelected = selectedTab == BottomTab.PURCHASES,
                onClick = { onTabSelected(BottomTab.PURCHASES) },
                testTag = "bottom_nav_purchase"
            )

            // 4. Reports
            BottomNavItem(
                tab = BottomTab.REPORTS,
                urduLabel = "رپورٹس",
                englishLabel = "Reports",
                icon = Icons.Default.BarChart,
                isSelected = selectedTab == BottomTab.REPORTS,
                onClick = { onTabSelected(BottomTab.REPORTS) },
                testTag = "bottom_nav_reports"
            )

            // 5. Settings
            BottomNavItem(
                tab = BottomTab.SETTINGS,
                urduLabel = "ترتیبات",
                englishLabel = "Settings",
                icon = Icons.Default.Settings,
                isSelected = selectedTab == BottomTab.SETTINGS,
                onClick = { onTabSelected(BottomTab.SETTINGS) },
                testTag = "bottom_nav_settings"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: BottomTab,
    urduLabel: String,
    englishLabel: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val activeColor = SitaraBlue
    val inactiveColor = TextMuted

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = 200),
        label = "TabColorAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$englishLabel ($urduLabel)",
            tint = animatedColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = urduLabel,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = animatedColor
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Active indicator line matching reference design under active tab
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(22.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SitaraBlue)
            )
        } else {
            Spacer(modifier = Modifier.height(2.5.dp))
        }
    }
}
