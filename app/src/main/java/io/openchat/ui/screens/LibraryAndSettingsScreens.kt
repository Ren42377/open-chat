package io.openchat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.openchat.data.mock.AccentColorOption
import io.openchat.data.mock.AppSnapshot
import io.openchat.data.mock.ArtifactCategory
import io.openchat.data.mock.ConversationMode
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.SubscriptionTier
import io.openchat.data.mock.ThemeMode
import io.openchat.data.mock.UiSimState
import io.openchat.ui.components.DsIcon
import io.openchat.ui.components.SectionHeader
import io.openchat.ui.components.StateSimulationHost
import io.openchat.ui.components.StatusBadge
import io.openchat.ui.theme.OpenChatDimens
import io.openchat.ui.theme.OpenChatIcons

@Composable
fun ArtifactLibraryScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateToChat: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var newFolderName by remember { mutableStateOf("") }
    var selectedArtifactId by remember { mutableStateOf<String?>("art-site-1") }
    var analyticsRange by remember { mutableStateOf("30D") }

    val filteredArtifacts = snapshot.artifacts.filter { item ->
        val matchesCategory = when (snapshot.selectedArtifactCategory) {
            ArtifactCategory.ALL -> item.category != ArtifactCategory.RECENTLY_DELETED
            ArtifactCategory.FAVORITES -> item.isFavorite
            ArtifactCategory.SHARED -> item.isShared
            else -> item.category == snapshot.selectedArtifactCategory
        }
        val matchesQuery = searchQuery.isBlank() ||
            item.title.contains(searchQuery, ignoreCase = true) ||
            item.subtitle.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    val selectedArtifact = snapshot.artifacts.find { it.id == selectedArtifactId }
        ?: filteredArtifacts.firstOrNull()

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading library, Pages & Sites...",
        emptyTitle = "No Pages or files found",
        emptySubtitle = "Now you can ask Open Chat about files added to past chats. Upload once, use anytime.",
        emptyActionLabel = "Create sample folder",
        onEmptyAction = {
            controller.setUiSimState(UiSimState.SUCCESS)
            controller.createArtifactFolder("Research Highlights")
        },
        errorTitle = "Unable to open this Library item.",
        errorSubtitle = "Couldn't load Pages or Sites. Check your connection and try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Storage Management Banner (`com/openai/navigation/destinations/h.java`)
            Surface(
                shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Upload once, use anytime",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "Storage: 1.8 GB of 15.0 GB used",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        StatusBadge(text = "Manage storage")
                    }
                    LinearProgressIndicator(
                        progress = { 0.12f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search library, Pages, Sites, or folders") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
            )

            // Category Tabs (`All`, `Images`, `Files`, `Folders`, `Pages`, `Sites`, `Favorites`, `Shared`, `Recently deleted`)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ArtifactCategory.entries.forEach { category ->
                    FilterChip(
                        selected = snapshot.selectedArtifactCategory == category,
                        onClick = { controller.selectArtifactCategory(category) },
                        label = { Text(category.label, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            // Create New Folder bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    placeholder = { Text("New folder name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        controller.createArtifactFolder(newFolderName.ifBlank { "Untitled Folder" })
                        newFolderName = ""
                    },
                ) {
                    Text("New folder")
                }
            }

            // Artifact List
            filteredArtifacts.forEach { item ->
                val isSelected = selectedArtifact?.id == item.id
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedArtifactId = item.id },
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    },
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f),
                            ) {
                                DsIcon(
                                    drawableRes = when (item.category) {
                                        ArtifactCategory.SITES -> OpenChatIcons.DeskGlobe
                                        ArtifactCategory.PAGES -> OpenChatIcons.Notebook
                                        ArtifactCategory.IMAGES -> OpenChatIcons.Photo
                                        ArtifactCategory.FOLDERS -> OpenChatIcons.Folder
                                        else -> OpenChatIcons.Book
                                    },
                                    contentDescription = null,
                                    size = 20.dp,
                                )
                                Column {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Text(
                                        text = "${item.subtitle} | ${item.sizeLabel}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            StatusBadge(text = item.category.label)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            OutlinedButton(onClick = { controller.toggleArtifactFavorite(item.id) }) {
                                Text(
                                    text = if (item.isFavorite) "Remove from Favorites" else "Add to Favorites",
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                            if (item.category == ArtifactCategory.RECENTLY_DELETED) {
                                Button(onClick = { controller.restoreDeletedArtifact(item.id) }) {
                                    Text("Restore", style = MaterialTheme.typography.labelSmall)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = {
                                        controller.sendUserMessage("Chat about this library item: ${item.title}")
                                        onNavigateToChat()
                                    },
                                ) {
                                    Text("Chat about this", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            // Detailed Preview & Sites Lite Viewer (`com/openai/navigation/destinations/i.java`)
            if (selectedArtifact != null) {
                HorizontalDivider()
                SectionHeader(
                    title = "Preview: ${selectedArtifact.title}",
                    subtitle = "${selectedArtifact.updatedRelative} | ${selectedArtifact.sharedBy?.let { "Shared by $it" } ?: "Private"}",
                )
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = selectedArtifact.previewContent,
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        if (selectedArtifact.category == ArtifactCategory.SITES) {
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Site Analytics & Traffic Over Time",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("7D", "30D", "Max").forEach { range ->
                                        FilterChip(
                                            selected = analyticsRange == range,
                                            onClick = { analyticsRange = range },
                                            label = { Text(range, style = MaterialTheme.typography.labelSmall) },
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Page views ($analyticsRange)", style = MaterialTheme.typography.labelSmall)
                                        Text(
                                            text = "${selectedArtifact.siteTrafficViews}",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text("+18% from previous period", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Unique visitors ($analyticsRange)", style = MaterialTheme.typography.labelSmall)
                                        Text(
                                            text = "${selectedArtifact.siteUniqueVisitors}",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text("Anyone with the link", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }

                            if (selectedArtifact.envVars.isNotEmpty()) {
                                Text(
                                    text = "Environment variables & Secrets",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                selectedArtifact.envVars.forEach { (key, value) ->
                                    Text(
                                        text = "$key = $value",
                                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoveryHubScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateToChat: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf(
        "All",
        "Developer Tools",
        "Research and Knowledge",
        "Creativity and Design",
        "Business and Analytics",
        "Productivity",
    )

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading Gizmo Store & Plugins Directory...",
        emptyTitle = "No GPTs or plugins found",
        emptySubtitle = "Try a different category or search query.",
        errorTitle = "Unable to load Explore",
        errorSubtitle = "Something went wrong. Please try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Explore GPTs",
                subtitle = "Custom versions of ChatGPT that combine instructions, extra knowledge, and skills.",
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            val visibleGizmos = snapshot.gizmos.filter {
                selectedCategory == "All" || it.category == selectedCategory
            }

            visibleGizmos.forEach { gizmo ->
                Surface(
                    shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = gizmo.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "By ${gizmo.author} | ${gizmo.rating} | ${gizmo.conversationsCount}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            StatusBadge(text = gizmo.category)
                        }
                        Text(
                            text = gizmo.description,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            gizmo.capabilities.forEach { cap ->
                                StatusBadge(
                                    text = cap,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                        Button(
                            onClick = {
                                controller.startNewChat(
                                    mode = ConversationMode.GIZMO,
                                    gizmoId = gizmo.id,
                                )
                                controller.sendUserMessage(gizmo.starterPrompts.first())
                                onNavigateToChat()
                            },
                        ) {
                            Text("Start chat")
                        }
                    }
                }
            }

            HorizontalDivider()

            // Apps, Plugins & Bazaar Connectors (`defpackage/yxu.java`, `ygv.java`, `pxu.java`)
            SectionHeader(
                title = "Connected Apps & Connectors",
                subtitle = "ChatGPT can access information from connected apps, based on what you're authorized to view.",
            )

            snapshot.connectors.forEach { conn ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Wrench,
                                    contentDescription = null,
                                    size = 20.dp,
                                )
                                Column {
                                    Text(
                                        text = conn.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Text(
                                        text = "${conn.developer} | ${conn.category} | ${conn.permissionMode}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            OutlinedButton(onClick = { controller.toggleConnector(conn.id) }) {
                                Text(if (conn.isConnected) "Disconnect" else "Connect")
                            }
                        }
                        Text(
                            text = conn.description,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            conn.skills.forEach { skill ->
                                StatusBadge(text = "Skill: $skill")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpacesAndTasksScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateToChat: () -> Unit,
) {
    var spaceNameInput by remember { mutableStateOf("") }
    var spaceInstructionsInput by remember { mutableStateOf("") }
    var taskTitleInput by remember { mutableStateOf("") }
    var taskCadenceInput by remember { mutableStateOf("Daily") }
    var taskPromptInput by remember { mutableStateOf("") }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading projects & scheduled tasks...",
        emptyTitle = "No projects or tasks yet",
        emptySubtitle = "Create a project workspace or schedule a recurring task below.",
        errorTitle = "Couldn't load projects or tasks",
        errorSubtitle = "Check your connection and try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Projects",
                subtitle = "Dedicated project workspaces with custom instructions, sources, and collaborators.",
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Create a new Space / Project",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = spaceNameInput,
                        onValueChange = { spaceNameInput = it },
                        label = { Text("Project name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = spaceInstructionsInput,
                        onValueChange = { spaceInstructionsInput = it },
                        label = { Text("Custom instructions for this project") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Button(
                        onClick = {
                            controller.createProjectSpace(
                                name = spaceNameInput.ifBlank { "Untitled Space" },
                                instructions = spaceInstructionsInput,
                            )
                            spaceNameInput = ""
                            spaceInstructionsInput = ""
                        },
                    ) {
                        Text("Create Space")
                    }
                }
            }

            snapshot.projectSpaces.forEach { space ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = space.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "${space.updatedRelative} | ${space.conversationCount} chats",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    controller.startNewChat(
                                        mode = ConversationMode.PROJECT_HOME,
                                        spaceId = space.id,
                                    )
                                    onNavigateToChat()
                                },
                            ) {
                                Text("Open Space")
                            }
                        }
                        Text(text = space.description, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Instructions: ${space.customInstructions}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "People with access: ${space.collaborators.joinToString(", ")}",
                            style = MaterialTheme.typography.labelSmall,
                        )
                        if (space.sourceFiles.isNotEmpty()) {
                            Text(
                                text = "Project sources: ${space.sourceFiles.joinToString(", ")}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Scheduled Tasks & Automations (`/scheduled`, `/tasks`)
            SectionHeader(
                title = "Scheduled Tasks, Goals & Automations",
                subtitle = "Recurring scheduled prompts and condition-based monitoring.",
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Create a goal or scheduled task",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = taskTitleInput,
                        onValueChange = { taskTitleInput = it },
                        label = { Text("Title / Goal name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf("Hourly", "Daily", "Weekdays", "Weekly", "Monthly", "Monitoring").forEach { freq ->
                            FilterChip(
                                selected = taskCadenceInput == freq,
                                onClick = { taskCadenceInput = freq },
                                label = { Text(freq, style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                    OutlinedTextField(
                        value = taskPromptInput,
                        onValueChange = { taskPromptInput = it },
                        label = { Text("Instructions / Prompt") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Button(
                        onClick = {
                            controller.createScheduledTask(
                                title = taskTitleInput.ifBlank { "New Scheduled Automation" },
                                cadence = taskCadenceInput,
                                prompt = taskPromptInput,
                            )
                            taskTitleInput = ""
                            taskPromptInput = ""
                        },
                    ) {
                        Text("Save task")
                    }
                }
            }

            snapshot.scheduledTasks.forEach { task ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "${task.scheduleSummary} | ${task.lastRunSummary}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            StatusBadge(text = task.status)
                        }
                        Text(
                            text = task.promptInstruction,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { controller.toggleScheduledTaskPause(task.id) }) {
                                Text(if (task.status == "PAUSED") "Resume" else "Pause")
                            }
                            OutlinedButton(
                                onClick = {
                                    controller.sendUserMessage(task.promptInstruction)
                                    onNavigateToChat()
                                },
                            ) {
                                Text("Run now")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsAndProfileScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    var fullName by remember(snapshot.userFullName) { mutableStateOf(snapshot.userFullName) }
    var username by remember(snapshot.userUsername) { mutableStateOf(snapshot.userUsername) }
    var occupation by remember(snapshot.userOccupation) { mutableStateOf(snapshot.userOccupation) }
    var traits by remember(snapshot.customInstructionsTraits) { mutableStateOf(snapshot.customInstructionsTraits) }
    var newMemoryText by remember { mutableStateOf("") }
    var newDictWord by remember { mutableStateOf("") }
    var showMemorySummary by remember { mutableStateOf(false) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading settings...",
        emptyTitle = "No matching settings",
        emptySubtitle = "Restore default settings to continue.",
        errorTitle = "Could not load settings. Tap to retry.",
        errorSubtitle = "Check your connection and try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Appearance & Theme Color Picker (`strings.xml#L24-L39`)
            SectionHeader(
                title = "Appearance & Theme Color",
                subtitle = "Choose light/dark mode and personalize your accent color.",
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ThemeMode.entries.forEach { mode ->
                    FilterChip(
                        selected = snapshot.themeMode == mode,
                        onClick = { controller.setThemeMode(mode) },
                        label = { Text(mode.label, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AccentColorOption.entries.forEach { option ->
                    FilterChip(
                        selected = snapshot.accentColor == option,
                        onClick = { controller.setAccentColor(option) },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(option.hexPreview)),
                                )
                                Text(option.label, style = MaterialTheme.typography.labelSmall)
                            }
                        },
                    )
                }
            }

            HorizontalDivider()

            // Profile, Custom Instructions & Fun Facts (`jov.java`, `mov.java`)
            SectionHeader(
                title = "Profile, Custom Instructions & Fun Facts",
                subtitle = "Your profile helps people recognize you in group chats and shapes how Open Chat responds.",
                actionLabel = "Shuffle fun facts",
                onActionClick = { controller.shuffleFunFacts() },
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = occupation,
                onValueChange = { occupation = it },
                label = { Text("Your occupation") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = traits,
                onValueChange = { traits = it },
                label = { Text("Base style, tone, and characteristics") },
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { controller.updateUserProfile(fullName, username, occupation, traits) },
            ) {
                Text("Save profile & instructions")
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Fun Facts (Shown on your Calpico group chat profile)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    snapshot.userFunFacts.forEach { fact ->
                        Text("* $fact", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            HorizontalDivider()

            // Memory & Memory Summary (`cpv.java`, `rcv.java`)
            SectionHeader(
                title = "Memory & Personal Context",
                subtitle = "Open Chat can now reference your past chats and saved memories for more personalized replies.",
                actionLabel = if (showMemorySummary) "Hide summary" else "View summary",
                onActionClick = { showMemorySummary = !showMemorySummary },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Use memory", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Remember info from past chats for more personalized replies",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isMemoryEnabled,
                    onCheckedChange = { controller.toggleMemoryEnabled() },
                )
            }

            AnimatedVisibility(visible = showMemorySummary) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "Synthesized Memory Summary",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Alex Rivera is a Principal Android & Systems Architect focused on Kotlin 2.4+, Jetpack Compose Material 3, clean-room reverse engineering, and CI-only verification pipelines.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = newMemoryText,
                    onValueChange = { newMemoryText = it },
                    placeholder = { Text("Add a new saved memory") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        controller.addMemory(newMemoryText)
                        newMemoryText = ""
                    },
                ) {
                    Text("Add")
                }
            }

            snapshot.memories.forEach { memory ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(memory.content, style = MaterialTheme.typography.bodySmall)
                            Text(
                                "${memory.sourceChatTitle} | ${memory.createdRelative}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        OutlinedButton(onClick = { controller.deleteMemory(memory.id) }) {
                            Text("Delete", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            HorizontalDivider()

            // Custom Dictionary (`defpackage/u4v.java`)
            SectionHeader(
                title = "Custom Dictionary & Dictation",
                subtitle = "Dictation will recognize these words more accurately.",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = newDictWord,
                    onValueChange = { newDictWord = it },
                    placeholder = { Text("Add technical term or name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        controller.addDictionaryWord(newDictWord)
                        newDictWord = ""
                    },
                ) {
                    Text("Add to dictionary")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                snapshot.customDictionary.forEach { word ->
                    StatusBadge(text = word)
                }
            }

            HorizontalDivider()

            // Data Controls, Privacy Center, Ads Controls & Clinician Mode
            SectionHeader(
                title = "Data Controls, Privacy Center & Ads Transparency",
                subtitle = "Control how your chats are saved, exported, and used.",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Improve the model for everyone", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Allow your content to be used to improve our models.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isImproveModelEnabled,
                    onCheckedChange = { controller.toggleImproveModel() },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Personalize ads", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Your conversations stay private and are never shared with advertisers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isPersonalizedAdsEnabled,
                    onCheckedChange = { controller.togglePersonalizedAds() },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Open Chat for Clinicians Mode", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Toggle clinician mode on or off",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isClinicianModeEnabled,
                    onCheckedChange = { controller.toggleClinicianMode() },
                )
            }

            HorizontalDivider()

            // Active Sessions (`strings.xml#L6009-L6040`)
            SectionHeader(
                title = "Security & Active Sessions",
                subtitle = "See all devices and sessions that have accessed your account.",
            )

            snapshot.securitySessions.forEach { sess ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(sess.deviceName, style = MaterialTheme.typography.titleSmall)
                            Text(
                                sess.locationAndTime,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        StatusBadge(
                            text = if (sess.isCurrentSession) "CURRENT SESSION" else "TRUSTED DEVICE",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthAndSubscriptionScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    var otpCode by remember { mutableStateOf("482910") }
    var totpSecretCopied by remember { mutableStateOf(false) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Checking subscription & passkey status...",
        emptyTitle = "No plans available",
        emptySubtitle = "Restore default plans to compare Free, Go, Plus, Pro, and Enterprise.",
        errorTitle = "We couldn't finish signing you in",
        errorSubtitle = "This passkey request has expired. Choose another verification method or start again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Do More with Open Chat (Subscription Plans)",
                subtitle = "Get higher limits and access to advanced frontier intelligence.",
            )

            SubscriptionTier.entries.forEach { tier ->
                val isCurrent = snapshot.subscriptionTier == tier
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { controller.setSubscriptionTier(tier) },
                    color = if (isCurrent) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    },
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${tier.label} (${tier.monthlyPrice})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            StatusBadge(text = if (isCurrent) "Subscribed" else tier.badge)
                        }
                        Text(
                            text = tier.summary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            HorizontalDivider()

            // Passkey, OTP & Authenticator MFA (`pdv.java`, `afv.java`, `siv.java`)
            SectionHeader(
                title = "Passkeys, OTP & Authenticator MFA",
                subtitle = "Phishing-resistant sign-in and multi-factor authentication.",
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Log in faster with a passkey",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "We've found a passkey for ${snapshot.userEmail}. Your device or password manager will securely handle your login.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { controller.setSubscriptionTier(snapshot.subscriptionTier) }) {
                            Text("Continue with passkey")
                        }
                        OutlinedButton(onClick = { totpSecretCopied = true }) {
                            Text(if (totpSecretCopied) "TOTP Key Copied" else "Copy Authenticator Key")
                        }
                    }

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("Enter 6-digit verification code (SMS / Authenticator)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
