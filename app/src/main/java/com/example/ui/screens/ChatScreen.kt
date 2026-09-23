package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.BeautyViewModel
import com.example.ui.components.DemoDataNotice
import com.example.ui.theme.AmberGoldSecondary
import com.example.ui.theme.AmberSoftBg
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.TerracottaSoftBg
import com.example.ui.theme.WarmCreamSurface
import com.example.ui.theme.WarmSurfaceBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val QUICK_REPLY_OPTIONS = listOf(
    "Disponible",
    "Je suis en route",
    "Confirmez-vous le RDV ?",
    "Merci beaucoup !",
    "Indisponible"
)

@Composable
fun ChatScreen(
    viewModel: BeautyViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.currentChatMessages.collectAsState()
    var inputMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .testTag("chat_screen")
    ) {
        DemoDataNotice()

        // Chat Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(TerracottaSoftBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = TerracottaPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Amina Beauté & Tresses",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "En ligne • Répond en quelques minutes",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                    }
                }
            }
        }

        // Privacy note (Requirement 20: Ne jamais afficher inutilement le numéro)
        Surface(
            color = AmberSoftBg,
            border = BorderStroke(1.dp, WarmSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp), tint = TerracottaPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Messagerie sécurisée BEAUTY BF • Échangez en direct sans exposer votre numéro",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8A6237)
                )
            }
        }

        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }

        // Quick Responses Row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Réponses rapides :",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(QUICK_REPLY_OPTIONS) { option ->
                    Surface(
                        color = TerracottaSoftBg,
                        border = BorderStroke(1.dp, WarmSurfaceBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable {
                                viewModel.sendMessage(option, isFromClient = true)
                            }
                            .testTag("quick_reply_${option.take(6)}")
                    ) {
                        Text(
                            text = option,
                            fontSize = 12.5.sp,
                            color = TerracottaPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Text Input Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, WarmSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text("Écrivez un message...", fontSize = 14.sp, color = Color(0xFF9CA3AF)) },
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFD1D5DB),
                        focusedContainerColor = WarmCreamSurface,
                        unfocusedContainerColor = WarmCreamSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field")
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.sendMessage(inputMessage, isFromClient = true)
                            inputMessage = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(TerracottaPrimary)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Envoyer",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isClient = message.isFromClient
    val alignment = if (isClient) Alignment.End else Alignment.Start
    val bgColor = if (isClient) TerracottaPrimary else Color.White
    val textColor = if (isClient) Color.White else Color(0xFF111827)

    val timeString = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.FRENCH)
        sdf.format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bgColor,
            border = if (!isClient) BorderStroke(1.dp, WarmSurfaceBorder) else null,
            shadowElevation = 1.dp,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isClient) 16.dp else 4.dp,
                bottomEnd = if (isClient) 4.dp else 16.dp
            ),
            modifier = Modifier.width(300.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    color = textColor,
                    fontSize = 14.5.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isClient) Color.White.copy(alpha = 0.85f) else Color(0xFF6B7280)
                    )
                    if (isClient) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Lu",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
