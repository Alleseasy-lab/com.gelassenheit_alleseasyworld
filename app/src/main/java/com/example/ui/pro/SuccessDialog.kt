package com.example.ui.pro

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ─── Design Tokens ───────────────────────────────────────────────────────────
private val DialogDarkBg  = Color(0xFF0D1322)
private val DialogCardBg  = Color(0xFF131C31)
private val AmberGold     = Color(0xFFFFD700)
private val EmeraldGreen  = Color(0xFF10B981)
private val SubtleBorder  = Color(0x2EFFFFFF)
private val TextMuted     = Color(0xFF94A3B8)

/**
 * Wiederverwendbares 'SuccessDialog'-Widget:
 * Zeigt ein Häkchen-Icon (Checkmark), den Titel "Danke!" und eine Erfolgsmeldung
 * über den freigeschalteten Premium-Zugriff an.
 */
@Composable
fun SuccessDialog(
    title: String = "Danke!",
    subtitle: String = "Willkommen bei Gelassenheit PRO ✨",
    message: String = "Deine Zahlung war erfolgreich. Dein persönlicher Premium-Zugang ist ab sofort freigeschaltet – genieße unbegrenzte Entspannung, geführte Meditationen und heilsame 432 Hz Klangwelten.",
    planTitle: String = "Gelassenheit PRO",
    planPrice: String = "",
    paymentMethodTitle: String = "",
    transactionId: String = "",
    buttonText: String = "Jetzt durchstarten & entspannen",
    onDismiss: () -> Unit
) {
    // Sanfte Puls-Animation für das Häkchen-Icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconPulse"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.84f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .clip(RoundedCornerShape(28.dp))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AmberGold.copy(alpha = 0.85f),
                                Color(0xFF2563EB).copy(alpha = 0.45f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = DialogDarkBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ─── Checkmark Icon mit Pulsieren ──────────────────────────
                    Box(
                        modifier = Modifier
                            .scale(iconScale)
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        EmeraldGreen.copy(alpha = 0.35f),
                                        Color(0xFF047857).copy(alpha = 0.15f)
                                    )
                                )
                            )
                            .border(2.5.dp, EmeraldGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Erfolg Checkmark",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // ─── "Danke!" Titel ─────────────────────────────────────────
                    Text(
                        text = title,
                        color = AmberGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // ─── Erfolgsnachricht ─────────────────────────────────────
                    Text(
                        text = message,
                        color = TextMuted,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ─── Freigeschaltete Vorteile ──────────────────────────────
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(DialogCardBg)
                            .border(1.dp, SubtleBorder, RoundedCornerShape(18.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "DEINE FREIGESCHALTETEN PRO-VORTEILE",
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        SuccessFeatureRow(
                            icon = Icons.Default.Headphones,
                            iconTint = Color(0xFF60A5FA),
                            title = "Alle 432 Hz Frequenzen & Klangwelten",
                            subtitle = "Unbegrenzter Zugriff auf Natur- & Meditationsklänge"
                        )

                        SuccessFeatureRow(
                            icon = Icons.Default.SelfImprovement,
                            iconTint = Color(0xFF34D399),
                            title = "Geführte Meditationen & Atemreisen",
                            subtitle = "Alle 5-Sinne-Übungen & Entspannungstechniken"
                        )

                        SuccessFeatureRow(
                            icon = Icons.Default.Podcasts,
                            iconTint = Color(0xFFF472B6),
                            title = "Exklusive Podcast Deep Dives",
                            subtitle = "Wissenschaftliche Impulse zur Stressreduktion"
                        )

                        SuccessFeatureRow(
                            icon = Icons.Default.WorkspacePremium,
                            iconTint = AmberGold,
                            title = "100% werbefrei & Offline nutzbar",
                            subtitle = "Jederzeit und überall Ruhe finden"
                        )
                    }

                    if (transactionId.isNotBlank() || planPrice.isNotBlank() || paymentMethodTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // ─── Transaktions-Info ──────────────────────────────────
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF070B14))
                                .border(1.dp, SubtleBorder, RoundedCornerShape(14.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = planTitle,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (planPrice.isNotBlank()) {
                                        Text(
                                            text = planPrice,
                                            color = AmberGold,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (transactionId.isNotBlank() || paymentMethodTitle.isNotBlank()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (paymentMethodTitle.isNotBlank()) {
                                            Text(
                                                text = "Bezahlt via $paymentMethodTitle",
                                                color = TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                        if (transactionId.isNotBlank()) {
                                            Text(
                                                text = "ID: $transactionId",
                                                color = TextMuted,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ─── Bestätigen & Schließen Button ────────────────────────
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberGold,
                            contentColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = buttonText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.4.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Weiter",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessFeatureRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(17.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
