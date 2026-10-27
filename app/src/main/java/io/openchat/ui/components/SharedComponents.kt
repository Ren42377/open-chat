package io.openchat.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.openchat.data.mock.SubscriptionTier
import io.openchat.data.mock.UiSimState
import io.openchat.ui.theme.OpenChatDimens
import io.openchat.ui.theme.OpenChatIcons

@Composable
fun DsIcon(
    @DrawableRes drawableRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        painter = painterResource(id = drawableRes),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (actionLabel != null && onActionClick != null) {
            OutlinedButton(onClick = onActionClick) {
                Text(text = actionLabel, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

/**
 * Clean OAI secondary screen header matching `oai_ui` / Valdi screen headers.
 * Long-pressing the title opens the hidden Developer / State Inspector overlay.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopScreenHeader(
    title: String,
    subtitle: String? = null,
    onOpenDrawer: () -> Unit,
    onOpenInspector: () -> Unit,
    showBackArrow: Boolean = true,
    onNavigateBack: (() -> Unit)? = null,
    trailingActionIcon: Int? = null,
    trailingActionDescription: String? = null,
    onTrailingAction: (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                if (showBackArrow && onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ArrowLeft,
                            contentDescription = "Back",
                            size = 24.dp,
                        )
                    }
                } else {
                    IconButton(onClick = onOpenDrawer) {
                        DsIcon(
                            drawableRes = OpenChatIcons.SidebarMenu,
                            contentDescription = "Menu",
                            size = 24.dp,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .combinedClickable(
                            onClick = {},
                            onLongClick = onOpenInspector,
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            if (trailingActionIcon != null && onTrailingAction != null) {
                IconButton(onClick = onTrailingAction) {
                    DsIcon(
                        drawableRes = trailingActionIcon,
                        contentDescription = trailingActionDescription,
                        size = 22.dp,
                    )
                }
            }
        }
    }
}

/**
 * Developer State Switcher bar - only shown inside the hidden Debug Sheet or Experiment Override screen.
 */
@Composable
fun QuickStateSwitcherBar(
    currentState: UiSimState,
    currentTier: SubscriptionTier,
    onSelectState: (UiSimState) -> Unit,
    onSelectTier: (SubscriptionTier) -> Unit,
    onOpenFullInspector: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            StatusBadge(
                text = "STATE",
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            UiSimState.entries.forEach { simState ->
                FilterChip(
                    selected = currentState == simState,
                    onClick = { onSelectState(simState) },
                    label = {
                        Text(
                            text = simState.label,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            StatusBadge(
                text = currentTier.badge,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            listOf(SubscriptionTier.FREE, SubscriptionTier.PLUS, SubscriptionTier.PRO).forEach { tier ->
                FilterChip(
                    selected = currentTier == tier,
                    onClick = { onSelectTier(tier) },
                    label = {
                        Text(
                            text = tier.label,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
            }
            OutlinedButton(onClick = onOpenFullInspector) {
                Text(text = "Experiment Override", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/**
 * Mandatory branching wrapper rendering Loading, Empty, Error, and Success states
 * for every screen in the application.
 */
@Composable
fun StateSimulationHost(
    state: UiSimState,
    loadingTitle: String = "Loading",
    emptyTitle: String = "No items yet",
    emptySubtitle: String = "Items you create or receive will appear here.",
    emptyActionLabel: String? = null,
    onEmptyAction: (() -> Unit)? = null,
    errorTitle: String = "Something went wrong.",
    errorSubtitle: String = "Network error. Please check your internet connection and try again.",
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "UiSimStateTransition",
        modifier = modifier,
    ) { target ->
        when (target) {
            UiSimState.LOADING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.5.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = loadingTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            UiSimState.EMPTY -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.Folder,
                                contentDescription = null,
                                size = 24.dp,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            text = emptyTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = emptySubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (emptyActionLabel != null && onEmptyAction != null) {
                                Button(onClick = onEmptyAction) {
                                    Text(text = emptyActionLabel)
                                }
                            }
                            OutlinedButton(onClick = onRetry) {
                                Text(text = "Done")
                            }
                        }
                    }
                }
            }

            UiSimState.ERROR -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                            ),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.StarOfLife,
                                contentDescription = "Error",
                                size = 24.dp,
                                tint = MaterialTheme.colorScheme.error,
                            )
                            Text(
                                text = errorTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = errorSubtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Button(onClick = onRetry) {
                                Text(text = "Try again")
                            }
                        }
                    }
                }
            }

            UiSimState.SUCCESS -> {
                content()
            }
        }
    }
}
