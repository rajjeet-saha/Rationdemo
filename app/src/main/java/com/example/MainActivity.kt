package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CivicBottomNav
import com.example.ui.components.CivicDrawer
import com.example.ui.components.CivicTopBar
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.SessionWipeDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DemoConsoleScreen
import com.example.ui.screens.EntitlementScreen
import com.example.ui.screens.GrievanceScreen
import com.example.ui.screens.OfflineDraftsScreen
import com.example.ui.screens.PortabilityScreen
import com.example.ui.screens.SafetySimulatorScreen
import com.example.ui.state.RationSaathiViewModel
import com.example.ui.state.ScreenTab
import com.example.ui.theme.RationSaathiTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: RationSaathiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RationSaathiTheme {
                RationSaathiApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RationSaathiApp(viewModel: RationSaathiViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val activeMember by viewModel.activeMember.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnlineSimulated.collectAsStateWithLifecycle()
    val showWipeDialog by viewModel.showWipeConfirmation.collectAsStateWithLifecycle()
    val showLangDialog by viewModel.showLanguageDialog.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()

    val household by viewModel.household.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val entitlements by viewModel.entitlements.collectAsStateWithLifecycle()
    val shopStocks by viewModel.shopStocks.collectAsStateWithLifecycle()
    val offlineDrafts by viewModel.offlineDrafts.collectAsStateWithLifecycle()
    val grievances by viewModel.grievances.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Handle back button when drawer is open
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CivicDrawer(
                selectedTab = selectedTab,
                currentLanguage = currentLanguage,
                household = household,
                activeMember = activeMember,
                onTabSelected = { tab ->
                    viewModel.navigateTo(tab)
                    coroutineScope.launch { drawerState.close() }
                },
                onEndSessionClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.requestWipeSession()
                }
            )
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                Column {
                    CivicTopBar(
                        currentLanguage = currentLanguage,
                        activeMember = activeMember,
                        allMembers = members,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onSelectMember = { viewModel.selectMember(it) },
                        onOpenLanguageDialog = { viewModel.toggleLanguageDialog(true) },
                        onEndSessionClick = { viewModel.requestWipeSession() }
                    )
                    // Status bar demo mode indicator
                    DemoModeBanner(
                        currentLanguage = currentLanguage,
                        isOnline = isOnline,
                        onToggleNetwork = { viewModel.toggleNetworkSimulation() }
                    )
                }
            },
            bottomBar = {
                CivicBottomNav(
                    selectedTab = selectedTab,
                    currentLanguage = currentLanguage,
                    onTabSelected = { viewModel.navigateTo(it) }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize().testTag("rationsaathi_root")
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    ScreenTab.OVERVIEW -> DashboardScreen(
                        currentLanguage = currentLanguage,
                        household = household,
                        members = members,
                        activeMember = activeMember,
                        todaySummary = todaySummary,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectMember = { viewModel.selectMember(it) }
                    )
                    ScreenTab.ENTITLEMENTS -> EntitlementScreen(
                        members = members,
                        entitlements = entitlements,
                        activeMember = activeMember
                    )
                    ScreenTab.PORTABILITY -> PortabilityScreen(
                        shopStocks = shopStocks,
                        onToggleStock = { shopId, commodity -> viewModel.toggleStockStatus(shopId, commodity) }
                    )
                    ScreenTab.SAFETY_SIMULATOR -> SafetySimulatorScreen(
                        members = members,
                        entitlements = entitlements,
                        onSaveCorrectionDraft = { viewModel.simulateSubmitDraft(it.id) }
                    )
                    ScreenTab.OFFLINE_DRAFTS -> OfflineDraftsScreen(
                        drafts = offlineDrafts,
                        isOnline = isOnline,
                        onSyncDraft = { viewModel.simulateSyncDraft(it) },
                        onSubmitDraft = { viewModel.simulateSubmitDraft(it) }
                    )
                    ScreenTab.GRIEVANCES -> GrievanceScreen(
                        grievances = grievances,
                        household = household
                    )
                    ScreenTab.FACILITATOR, ScreenTab.PUBLIC_METRICS, ScreenTab.DEMO_CONSOLE -> DemoConsoleScreen(
                        onResetScenario = { viewModel.resetScenario() },
                        onRequestWipeSession = { viewModel.requestWipeSession() }
                    )
                }
            }

            // Dialogs
            if (showWipeDialog) {
                SessionWipeDialog(
                    onDismiss = { viewModel.dismissWipeDialog() },
                    onConfirmWipe = { viewModel.confirmWipeSession() }
                )
            }

            if (showLangDialog) {
                LanguageSelectorDialog(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onDismiss = { viewModel.toggleLanguageDialog(false) }
                )
            }
        }
    }
}
