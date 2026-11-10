package com.Qesnisu9.Madqis.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

fun Modifier.clickableRole(
    onClick: () -> Unit,
    description: String,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null
): Modifier = this
    .semantics { contentDescription = description }
    .clickable(
        interactionSource = interactionSource,
        indication = ripple(),
        enabled = enabled,
        role = Role.Button,
        onClick = onClick
    )
