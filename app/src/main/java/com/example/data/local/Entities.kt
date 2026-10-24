package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.DraftStatus
import com.example.data.model.MemberRole
import com.example.data.model.Provenance
import com.example.data.model.StockState
import com.example.data.model.VerificationState

@Entity(tableName = "households")
data class HouseholdEntity(
    @PrimaryKey val id: String,
    val syntheticCardNo: String,
    val homeState: String,
    val homeDistrict: String,
    val destinationState: String,
    val destinationDistrict: String,
    val headMemberId: String,
    val headIsAbsent: Boolean = true,
    val category: String = "PHH (Priority Household)",
    val lastVerifiedTimestamp: Long = System.currentTimeMillis(),
    val provenance: Provenance = Provenance.SYNTHETIC_DEMO
)

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val householdId: String,
    val displayName: String,
    val age: Int,
    val gender: String,
    val relationship: String,
    val role: MemberRole,
    val officialRecordName: String,
    val identityDocName: String,
    val hasMismatch: Boolean = false,
    val mismatchDetails: String = "",
    val verificationState: VerificationState = VerificationState.VERIFIED,
    val provenance: Provenance = Provenance.OFFICIAL_VERIFIED,
    val lastSyncTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "entitlements")
data class EntitlementEntity(
    @PrimaryKey val id: String,
    val memberId: String,
    val commodityName: String,
    val monthlyQuotaKg: Double,
    val drawnKg: Double,
    val remainingKg: Double,
    val unitPriceInr: Double,
    val monthYear: String = "October 2026",
    val isPortabilityEligible: Boolean = true,
    val provenance: Provenance = Provenance.OFFICIAL_VERIFIED,
    val verificationStatus: VerificationState = VerificationState.VERIFIED
)

@Entity(tableName = "shop_stocks")
data class ShopStockEntity(
    @PrimaryKey val id: String,
    val shopCode: String,
    val shopName: String,
    val locationAddress: String,
    val distanceKm: Double,
    val commodityName: String,
    val stockKg: Double,
    val stockState: StockState,
    val lastUpdatedHoursAgo: Int,
    val isDestinationShop: Boolean = false,
    val provenance: Provenance = Provenance.OFFICIAL_LAST_SYNCED
)

@Entity(tableName = "offline_drafts")
data class OfflineDraftEntity(
    @PrimaryKey val id: String,
    val memberId: String,
    val title: String,
    val draftType: String,
    val description: String,
    val status: DraftStatus = DraftStatus.LOCAL_DRAFT,
    val createdAtTimestamp: Long = System.currentTimeMillis(),
    val payloadJson: String = "",
    val officialAckToken: String? = null,
    val submittedAtTimestamp: Long? = null
)

@Entity(tableName = "grievances")
data class GrievanceEntity(
    @PrimaryKey val id: String,
    val tokenNo: String,
    val memberId: String,
    val category: String,
    val summary: String,
    val filedDaysAgo: Int = 21,
    val statutoryMaxDays: Int = 15,
    val status: String = "Pending District Action",
    val jurisdictionOfficer: String = "District Grievance Redressal Officer (DGRO), Pune",
    val responseReceived: Boolean = false,
    val lastActionNotes: String = "Forwarded to Taluka Supply Officer for biometric failure inquiry."
)
