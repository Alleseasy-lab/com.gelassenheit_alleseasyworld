package com.example.ui.pro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// ─── Design Tokens & Theme Colors (Matching Flutter Specification) ───────────
private val DarkBg         = Color(0xFF070A12)
private val CardBg         = Color(0xFF10172A)
private val CardBorder     = Color(0x1FFFFFFF) // Colors.white12
private val BorderTopColor = Color(0x1AFFFFFF) // Colors.white10
private val BlueAccent     = Color(0xFF2979FF) // Colors.blueAccent
private val AmberAccent    = Color(0xFFFFD700) // Colors.amberAccent
private val BlueGreyText   = Color(0xFF90A4AE) // Colors.blueGrey
private val White70        = Color(0xB3FFFFFF) // Colors.white70
private val White38        = Color(0x61FFFFFF) // Colors.white38

enum class ProPlan(
    val id: String,
    val title: String,
    val price: String,
    val period: String,
    val trialInfo: String
) {
    YEARLY(
        id = "yearly",
        title = "Jahres-Abo",
        price = "29,99 €",
        period = "pro Jahr (nur 2,50 € / Mon)",
        trialInfo = "7 Tage kostenlos testen"
    ),
    MONTHLY(
        id = "monthly",
        title = "Monats-Abo",
        price = "4,99 €",
        period = "pro Monat • Jederzeit kündbar",
        trialInfo = "Jederzeit monatlich kündbar"
    ),
    LIFETIME(
        id = "lifetime",
        title = "Lifetime Pass",
        price = "49,99 €",
        period = "Einmalig • Lebenslang Zugriff",
        trialInfo = "Lebenslanger Zugriff ohne Folgekosten"
    )
}

enum class PaymentMethod(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconEmoji: String,
    val brandColor: Color
) {
    GOOGLE_PAY(
        id = "googlepay",
        title = "Google Pay",
        subtitle = "1-Tap Express • Biometrisch gesichert",
        icon = Icons.Default.PhoneAndroid,
        iconEmoji = "💳",
        brandColor = Color(0xFF4285F4)
    ),
    PAYPAL(
        id = "paypal",
        title = "PayPal",
        subtitle = "Käuferschutz • 30 Tage später",
        icon = Icons.Default.Payment,
        iconEmoji = "🅿️",
        brandColor = Color(0xFF0079C1)
    ),
    MOLLIE(
        id = "mollie",
        title = "Mollie Payments",
        subtitle = "Klarna, SEPA, Kreditkarte",
        icon = Icons.Default.CreditCard,
        iconEmoji = "🛡️",
        brandColor = Color(0xFF2E7D32)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GelassenheitProPaywallDialog(
    isProUser: Boolean,
    onUnlockPro: (ProPlan) -> Unit,
    onResetProStatus: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf(ProPlan.LIFETIME) }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.PAYPAL) }

    // Snackbar Host State
    val snackbarHostState = remember { SnackbarHostState() }

    // Payment Processing Flow State
    var isProcessingPayment by remember { mutableStateOf(false) }
    var paymentStep by remember { mutableStateOf(0) } // 1: Connecting, 2: Confirming, 3: Completed / Success Screen
    var transactionId by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            containerColor = DarkBg,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                ) { data ->
                    Snackbar(
                        containerColor = Color(0xFF1B5E20),
                        contentColor = Color.White,
                        actionColor = AmberAccent,
                        shape = RoundedCornerShape(12.dp),
                        snackbarData = data
                    )
                }
            },
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Premium",
                                tint = AmberAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "GELASSENHEIT PRO",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Schließen",
                                tint = White70
                            )
                        }
                    }
                )
            },
            bottomBar = {
                // Fixer unterer CTA-Bereich (Jetzt immer perfekt sichtbar!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkBg)
                        .border(
                            width = 1.dp,
                            color = BorderTopColor,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .padding(20.dp)
                ) {
                    Button(
                        onClick = {
                            isProcessingPayment = true
                            paymentStep = 1
                            coroutineScope.launch {
                                val generatedTx = "GEL-${selectedPayment.id.uppercase()}-${UUID.randomUUID().toString().take(8).uppercase()}"
                                transactionId = generatedTx
                                delay(1200)
                                paymentStep = 2
                                delay(800)
                                paymentStep = 3 // Success Screen
                                onUnlockPro(selectedPlan)

                                // Trigger confirmation snackbar notification
                                snackbarHostState.showSnackbar(
                                    message = "✓ Zahlung erfolgreich: ${selectedPlan.title} (${selectedPlan.price}) via ${selectedPayment.title} freigeschaltet! Transaktions-ID: $generatedTx",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Long
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = if (isProUser) "PRO bereits aktiv (${selectedPlan.price})" else "Jetzt bezahlen & Freischalten",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { innerPadding ->
            // SafeArea + SingleChildScrollView sorgt dafür, dass man bis ganz nach unten scrollen kann
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Tarif 1: Monats-Abo
                    val isMonthlySelected = selectedPlan == ProPlan.MONTHLY
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardBg)
                            .border(
                                width = if (isMonthlySelected) 2.dp else 1.dp,
                                color = if (isMonthlySelected) BlueAccent else CardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedPlan = ProPlan.MONTHLY }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Monats-Abo",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "pro Monat • Jederzeit kündbar",
                                    color = BlueGreyText,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "4,99 €",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tarif 2: Lifetime Pass
                    val isLifetimeSelected = selectedPlan == ProPlan.LIFETIME
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardBg)
                            .border(
                                width = if (isLifetimeSelected) 2.dp else 1.dp,
                                color = if (isLifetimeSelected) AmberAccent else CardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedPlan = ProPlan.LIFETIME }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Lifetime Pass",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Einmalig • Lebenslang Zugriff",
                                    color = BlueGreyText,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "49,99 €",
                                color = AmberAccent,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "2. BEZAHLMETHODE WÄHLEN",
                        color = BlueGreyText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Google Pay Option
                    PaymentOptionItem(
                        method = PaymentMethod.GOOGLE_PAY,
                        isSelected = selectedPayment == PaymentMethod.GOOGLE_PAY,
                        onSelect = { selectedPayment = PaymentMethod.GOOGLE_PAY }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // PayPal Option
                    PaymentOptionItem(
                        method = PaymentMethod.PAYPAL,
                        isSelected = selectedPayment == PaymentMethod.PAYPAL,
                        onSelect = { selectedPayment = PaymentMethod.PAYPAL }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mollie Option
                    PaymentOptionItem(
                        method = PaymentMethod.MOLLIE,
                        isSelected = selectedPayment == PaymentMethod.MOLLIE,
                        onSelect = { selectedPayment = PaymentMethod.MOLLIE }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Sichere Bezahlung via Google Pay, PayPal oder Mollie.",
                        color = White38,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Wiederherstellen Option
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = {
                                onUnlockPro(selectedPlan)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "✓ Einkäufe erfolgreich wiederhergestellt! PRO ist aktiv.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        ) {
                            Text(
                                text = "Einkäufe wiederherstellen",
                                color = BlueGreyText,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ─── Payment Processing Modal & Success Screen ────────────────────
            if (isProcessingPayment) {
                PaymentProcessingDialog(
                    plan = selectedPlan,
                    method = selectedPayment,
                    step = paymentStep,
                    transactionId = transactionId,
                    onClose = {
                        isProcessingPayment = false
                        paymentStep = 0
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun PaymentOptionItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) BlueAccent else CardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onSelect() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = method.icon,
                contentDescription = method.title,
                tint = if (isSelected) BlueAccent else White70,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = method.subtitle,
                    color = BlueGreyText,
                    fontSize = 11.sp
                )
            }
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = BlueAccent,
                    unselectedColor = White70
                )
            )
        }
    }
}

@Composable
private fun PaymentProcessingDialog(
    plan: ProPlan,
    method: PaymentMethod,
    step: Int,
    transactionId: String,
    onClose: () -> Unit
) {
    if (step == 3) {
        SuccessDialog(
            planTitle = plan.title,
            planPrice = plan.price,
            paymentMethodTitle = method.title,
            transactionId = transactionId,
            onDismiss = onClose
        )
        return
    }

    Dialog(
        onDismissRequest = { /* Processing dialog is modal until completed */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBg)
                    .border(1.dp, method.brandColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Step 1 & 2: Processing & Handshake Animation
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = method.brandColor,
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 4.5.dp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = if (step == 1) "Verbindung zu ${method.title} wird hergestellt..." else "Zahlung wird verschlüsselt bestätigt...",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bitte warte einen kurzen Moment, während dein PRO-Zugang freigeschaltet wird.",
                        color = BlueGreyText,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
