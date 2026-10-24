package com.example.data.repository

import com.example.data.local.EntitlementEntity
import com.example.data.local.GrievanceEntity
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.OfflineDraftEntity
import com.example.data.local.RationSaathiDao
import com.example.data.local.ShopStockEntity
import com.example.data.model.DraftStatus
import com.example.data.model.StockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class RationSaathiRepository(private val dao: RationSaathiDao) {

    val household: Flow<HouseholdEntity?> = dao.getPrimaryHousehold()
    val members: Flow<List<MemberEntity>> = dao.getMembersForHousehold(InitialSeedData.HOUSEHOLD_ID)
    val entitlements: Flow<List<EntitlementEntity>> = dao.getAllEntitlements()
    val shopStocks: Flow<List<ShopStockEntity>> = dao.getAllShopStocks()
    val offlineDrafts: Flow<List<OfflineDraftEntity>> = dao.getAllDrafts()
    val grievances: Flow<List<GrievanceEntity>> = dao.getAllGrievances()

    suspend fun initializeIfEmpty() {
        val existing = dao.getPrimaryHousehold().firstOrNull()
        if (existing == null) {
            resetToInitialDemoScenario()
        }
    }

    suspend fun resetToInitialDemoScenario() {
        dao.clearHouseholds()
        dao.clearMembers()
        dao.clearEntitlements()
        dao.clearShopStocks()
        dao.clearOfflineDrafts()
        dao.clearGrievances()

        dao.insertHousehold(InitialSeedData.getHousehold())
        dao.insertMembers(InitialSeedData.getMembers())
        dao.insertEntitlements(InitialSeedData.getEntitlements())
        dao.insertShopStocks(InitialSeedData.getShopStocks())
        dao.insertDraft(InitialSeedData.getOfflineDrafts().first())
        dao.insertGrievances(InitialSeedData.getGrievances())
    }

    suspend fun getMemberById(memberId: String): MemberEntity? {
        return dao.getMemberById(memberId)
    }

    suspend fun updateMember(member: MemberEntity) {
        dao.updateMember(member)
    }

    suspend fun saveDraft(draft: OfflineDraftEntity) {
        dao.insertDraft(draft)
    }

    suspend fun deleteDraft(draftId: String) {
        dao.deleteDraft(draftId)
    }

    suspend fun simulateSyncDraft(draftId: String, isOnline: Boolean): Boolean {
        if (!isOnline) return false
        val drafts = dao.getAllDrafts().firstOrNull() ?: emptyList()
        val draft = drafts.find { it.id == draftId } ?: return false
        if (draft.status == DraftStatus.LOCAL_DRAFT) {
            dao.updateDraft(draft.copy(status = DraftStatus.SYNCED_UNSUBMITTED))
            return true
        }
        return false
    }

    suspend fun simulateSubmitDraft(draftId: String): String {
        val drafts = dao.getAllDrafts().firstOrNull() ?: emptyList()
        val draft = drafts.find { it.id == draftId } ?: return ""
        val ackToken = "SIM-ACK-MH-${System.currentTimeMillis().toString().takeLast(6)}"
        dao.updateDraft(
            draft.copy(
                status = DraftStatus.SUBMITTED_SIMULATED,
                officialAckToken = ackToken,
                submittedAtTimestamp = System.currentTimeMillis()
            )
        )
        return ackToken
    }

    suspend fun toggleStockStatus(shopId: String, commodity: String) {
        val stocks = dao.getAllShopStocks().firstOrNull() ?: emptyList()
        val stock = stocks.find { it.id == shopId } ?: return
        val newState = if (stock.stockState == StockState.OUT_OF_STOCK_REPORTED) {
            StockState.AVAILABLE_VERIFIED
        } else {
            StockState.OUT_OF_STOCK_REPORTED
        }
        dao.updateShopStock(
            stock.copy(
                stockState = newState,
                stockKg = if (newState == StockState.AVAILABLE_VERIFIED) 350.0 else 0.0,
                lastUpdatedHoursAgo = 0
            )
        )
    }
}
