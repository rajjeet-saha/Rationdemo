package com.example.ui.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.EntitlementEntity
import com.example.data.local.GrievanceEntity
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.OfflineDraftEntity
import com.example.data.local.RationSaathiDatabase
import com.example.data.local.ShopStockEntity
import com.example.data.repository.RationSaathiRepository
import com.example.domain.PortabilityEngine
import com.example.domain.TodayActionSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    OVERVIEW,
    ENTITLEMENTS,
    PORTABILITY,
    SAFETY_SIMULATOR,
    OFFLINE_DRAFTS,
    GRIEVANCES,
    FACILITATOR,
    PUBLIC_METRICS,
    DEMO_CONSOLE
}

class RationSaathiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RationSaathiRepository

    init {
        val database = RationSaathiDatabase.getInstance(application)
        repository = RationSaathiRepository(database.dao())
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    private val _selectedTab = MutableStateFlow(ScreenTab.OVERVIEW)
    val selectedTab: StateFlow<ScreenTab> = _selectedTab.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _activeMember = MutableStateFlow<MemberEntity?>(null)
    val activeMember: StateFlow<MemberEntity?> = _activeMember.asStateFlow()

    private val _isOnlineSimulated = MutableStateFlow(true)
    val isOnlineSimulated: StateFlow<Boolean> = _isOnlineSimulated.asStateFlow()

    private val _showWipeConfirmation = MutableStateFlow(false)
    val showWipeConfirmation: StateFlow<Boolean> = _showWipeConfirmation.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    // Database streams
    val household: StateFlow<HouseholdEntity?> = repository.household.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val members: StateFlow<List<MemberEntity>> = repository.members.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val entitlements: StateFlow<List<EntitlementEntity>> = repository.entitlements.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val shopStocks: StateFlow<List<ShopStockEntity>> = repository.shopStocks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val offlineDrafts: StateFlow<List<OfflineDraftEntity>> = repository.offlineDrafts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val grievances: StateFlow<List<GrievanceEntity>> = repository.grievances.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Derived today action assessment
    val todaySummary: StateFlow<TodayActionSummary?> = combine(
        household,
        members,
        entitlements,
        shopStocks
    ) { hh, mems, ents, stocks ->
        if (hh != null && mems.isNotEmpty() && ents.isNotEmpty()) {
            PortabilityEngine.evaluateTodayPortability(hh, mems, ents, stocks)
        } else {
            null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun navigateTo(tab: ScreenTab) {
        _selectedTab.value = tab
    }

    fun selectMember(member: MemberEntity?) {
        _activeMember.value = member
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        _showLanguageDialog.value = false
    }

    fun toggleLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun toggleNetworkSimulation() {
        _isOnlineSimulated.value = !_isOnlineSimulated.value
        val status = if (_isOnlineSimulated.value) "Online (Simulated)" else "Offline (Simulated)"
        _feedbackMessage.value = "Network mode switched to: $status"
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun simulateSubmitDraft(draftId: String) {
        viewModelScope.launch {
            if (!_isOnlineSimulated.value) {
                _feedbackMessage.value = "Cannot submit: Device is currently in Offline Mode. Switch network to online to submit."
                return@launch
            }
            val ack = repository.simulateSubmitDraft(draftId)
            _feedbackMessage.value = "Draft Submitted via Simulation Adapter! Official Ack: $ack"
        }
    }

    fun simulateSyncDraft(draftId: String) {
        viewModelScope.launch {
            val synced = repository.simulateSyncDraft(draftId, _isOnlineSimulated.value)
            if (synced) {
                _feedbackMessage.value = "Draft synced to staging server. Note: Marked as SYNCED — NOT OFFICIALLY SUBMITTED."
            } else {
                _feedbackMessage.value = "Sync failed: Device is offline or draft is already synced."
            }
        }
    }

    fun toggleStockStatus(shopId: String, commodity: String) {
        viewModelScope.launch {
            repository.toggleStockStatus(shopId, commodity)
            _feedbackMessage.value = "Toggled stock status for $commodity at selected shop."
        }
    }

    fun resetScenario() {
        viewModelScope.launch {
            repository.resetToInitialDemoScenario()
            _activeMember.value = null
            _selectedTab.value = ScreenTab.OVERVIEW
            _feedbackMessage.value = "Scenario reset to initial state (Bihar -> Maharashtra, 5 members, 1 mismatch, 1 stale out-of-stock, 21d grievance)."
        }
    }

    fun requestWipeSession() {
        _showWipeConfirmation.value = true
    }

    fun dismissWipeDialog() {
        _showWipeConfirmation.value = false
    }

    fun confirmWipeSession() {
        _activeMember.value = null
        _selectedTab.value = ScreenTab.OVERVIEW
        _showWipeConfirmation.value = false
        _feedbackMessage.value = "Session terminated: In-memory buffers cleared. Zero data left on shared device."
    }
}
