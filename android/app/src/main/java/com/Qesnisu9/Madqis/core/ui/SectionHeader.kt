package com.Qesnisu9.Madqis.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary

@Composable
fun SectionHeader(
    title: String,
    subtitle: String?,
    onBack: () -> Unit,
    backIconRes: Int,
    backDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SurfaceCard.copy(alpha = 0.84f))
                .border(2.dp, TextPrimary.copy(alpha = 0.18f), CircleShape)
                .clickableRole(onClick = onBack, description = backDescription)
        ) {
            Image(
                painter = painterResource(backIconRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.size(14.dp))
        Column {
            Text(
                text = title,
                color = TextPrimary,
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
