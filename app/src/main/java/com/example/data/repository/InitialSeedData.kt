package com.example.data.repository

import com.example.data.local.EntitlementEntity
import com.example.data.local.GrievanceEntity
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.OfflineDraftEntity
import com.example.data.local.ShopStockEntity
import com.example.data.model.DraftStatus
import com.example.data.model.MemberRole
import com.example.data.model.Provenance
import com.example.data.model.StockState
import com.example.data.model.VerificationState

object InitialSeedData {
    const val HOUSEHOLD_ID = "HH-BR-2024-9102"
    const val HEAD_MEMBER_ID = "MEM-01-RAM"
    const val MISMATCHED_MEMBER_ID = "MEM-02-DEVI"
    const val SPOUSE_MEMBER_ID = "MEM-03-SUNITA"
    const val DAUGHTER_MEMBER_ID = "MEM-04-POOJA"
    const val CHILD_MEMBER_ID = "MEM-05-AMAN"

    fun getHousehold(): HouseholdEntity {
        return HouseholdEntity(
            id = HOUSEHOLD_ID,
            syntheticCardNo = "NFSA-1092-8821-4401",
            homeState = "Bihar",
            homeDistrict = "Saran (Chhapra)",
            destinationState = "Maharashtra",
            destinationDistrict = "Pune (Hadapsar Ward)",
            headMemberId = HEAD_MEMBER_ID,
            headIsAbsent = true,
            category = "PHH (Priority Household - Migrant)",
            lastVerifiedTimestamp = System.currentTimeMillis() - (86400000L * 3),
            provenance = Provenance.OFFICIAL_LAST_SYNCED
        )
    }

    fun getMembers(): List<MemberEntity> {
        return listOf(
            MemberEntity(
                id = HEAD_MEMBER_ID,
                householdId = HOUSEHOLD_ID,
                displayName = "Ram Ashray Prasad",
                age = 52,
                gender = "Male",
                relationship = "Head of Household",
                role = MemberRole.HEAD_ABSENT,
                officialRecordName = "Ram Ashray Prasad",
                identityDocName = "Ram Ashray Prasad",
                hasMismatch = false,
                verificationState = VerificationState.VERIFIED,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            MemberEntity(
                id = SPOUSE_MEMBER_ID,
                householdId = HOUSEHOLD_ID,
                displayName = "Sunita Devi",
                age = 48,
                gender = "Female",
                relationship = "Spouse (Present Adult)",
                role = MemberRole.ADULT_REPRESENTATIVE,
                officialRecordName = "Sunita Devi",
                identityDocName = "Sunita Devi",
                hasMismatch = false,
                verificationState = VerificationState.VERIFIED,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            MemberEntity(
                id = MISMATCHED_MEMBER_ID,
                householdId = HOUSEHOLD_ID,
                displayName = "Devi Prasad (Record: Dev Prasad)",
                age = 24,
                gender = "Male",
                relationship = "Son (Seasonal Worker)",
                role = MemberRole.MISMATCHED_MEMBER,
                officialRecordName = "Dev Prasad",
                identityDocName = "Devi Prasad",
                hasMismatch = true,
                mismatchDetails = "Database record lists 'Dev Prasad', Age 24. Identity document lists 'Devi Prasad', Age 26.",
                verificationState = VerificationState.MISMATCH_DETECTED,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            MemberEntity(
                id = DAUGHTER_MEMBER_ID,
                householdId = HOUSEHOLD_ID,
                displayName = "Pooja Kumari",
                age = 19,
                gender = "Female",
                relationship = "Daughter",
                role = MemberRole.ADULT_MEMBER,
                officialRecordName = "Pooja Kumari",
                identityDocName = "Pooja Kumari",
                hasMismatch = false,
                verificationState = VerificationState.VERIFIED,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            MemberEntity(
                id = CHILD_MEMBER_ID,
                householdId = HOUSEHOLD_ID,
                displayName = "Aman Prasad",
                age = 10,
                gender = "Male",
                relationship = "Son (Minor Dependent)",
                role = MemberRole.DEPENDENT_CHILD,
                officialRecordName = "Aman Prasad",
                identityDocName = "Aman Prasad",
                hasMismatch = false,
                verificationState = VerificationState.VERIFIED,
                provenance = Provenance.OFFICIAL_VERIFIED
            )
        )
    }

    fun getEntitlements(): List<EntitlementEntity> {
        val list = mutableListOf<EntitlementEntity>()
        val members = listOf(HEAD_MEMBER_ID, SPOUSE_MEMBER_ID, MISMATCHED_MEMBER_ID, DAUGHTER_MEMBER_ID, CHILD_MEMBER_ID)
        
        for (mId in members) {
            // Rice: 3 kg per member
            list.add(
                EntitlementEntity(
                    id = "ENT-$mId-RICE",
                    memberId = mId,
                    commodityName = "Rice (Fortified Grade A)",
                    monthlyQuotaKg = 3.0,
                    drawnKg = 0.0,
                    remainingKg = 3.0,
                    unitPriceInr = 0.0, // Subsidized free under PMGKAY/NFSA
                    isPortabilityEligible = true,
                    provenance = Provenance.OFFICIAL_VERIFIED,
                    verificationStatus = if (mId == MISMATCHED_MEMBER_ID) VerificationState.MISMATCH_DETECTED else VerificationState.VERIFIED
                )
            )
            // Wheat: 2 kg per member
            list.add(
                EntitlementEntity(
                    id = "ENT-$mId-WHEAT",
                    memberId = mId,
                    commodityName = "Wheat",
                    monthlyQuotaKg = 2.0,
                    drawnKg = 0.0,
                    remainingKg = 2.0,
                    unitPriceInr = 0.0,
                    isPortabilityEligible = true,
                    provenance = Provenance.OFFICIAL_VERIFIED,
                    verificationStatus = if (mId == MISMATCHED_MEMBER_ID) VerificationState.MISMATCH_DETECTED else VerificationState.VERIFIED
                )
            )
        }
        return list
    }

    fun getShopStocks(): List<ShopStockEntity> {
        return listOf(
            // Destination shop (Hadapsar East)
            ShopStockEntity(
                id = "SHOP-204-RICE",
                shopCode = "FPS-204",
                shopName = "Fair Price Shop #204 - Hadapsar East",
                locationAddress = "Plot 12, Ganj Peth Road, Hadapsar, Pune",
                distanceKm = 0.6,
                commodityName = "Rice (Fortified Grade A)",
                stockKg = 420.0,
                stockState = StockState.AVAILABLE_VERIFIED,
                lastUpdatedHoursAgo = 2,
                isDestinationShop = true,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            ShopStockEntity(
                id = "SHOP-204-WHEAT",
                shopCode = "FPS-204",
                shopName = "Fair Price Shop #204 - Hadapsar East",
                locationAddress = "Plot 12, Ganj Peth Road, Hadapsar, Pune",
                distanceKm = 0.6,
                commodityName = "Wheat",
                stockKg = 0.0,
                stockState = StockState.OUT_OF_STOCK_REPORTED,
                lastUpdatedHoursAgo = 18, // Stale report!
                isDestinationShop = true,
                provenance = Provenance.OFFICIAL_LAST_SYNCED
            ),
            // Alternative shop nearby with wheat in stock
            ShopStockEntity(
                id = "SHOP-118-RICE",
                shopCode = "FPS-118",
                shopName = "Fair Price Shop #118 - Industrial Colony",
                locationAddress = "Sector 4, Near Hadapsar Railway Gate, Pune",
                distanceKm = 1.4,
                commodityName = "Rice (Fortified Grade A)",
                stockKg = 680.0,
                stockState = StockState.AVAILABLE_VERIFIED,
                lastUpdatedHoursAgo = 3,
                isDestinationShop = false,
                provenance = Provenance.OFFICIAL_VERIFIED
            ),
            ShopStockEntity(
                id = "SHOP-118-WHEAT",
                shopCode = "FPS-118",
                shopName = "Fair Price Shop #118 - Industrial Colony",
                locationAddress = "Sector 4, Near Hadapsar Railway Gate, Pune",
                distanceKm = 1.4,
                commodityName = "Wheat",
                stockKg = 310.0,
                stockState = StockState.AVAILABLE_VERIFIED,
                lastUpdatedHoursAgo = 3,
                isDestinationShop = false,
                provenance = Provenance.OFFICIAL_VERIFIED
            )
        )
    }

    fun getOfflineDrafts(): List<OfflineDraftEntity> {
        return listOf(
            OfflineDraftEntity(
                id = "DRAFT-2024-001",
                memberId = MISMATCHED_MEMBER_ID,
                title = "Interstate Address & e-KYC Portability Update",
                draftType = "Portability Correction & Family e-KYC",
                description = "Prepared at offline facilitator desk for Devi Prasad. Contains pending photo slip and local residence proof.",
                status = DraftStatus.LOCAL_DRAFT,
                createdAtTimestamp = System.currentTimeMillis() - (86400000L * 2),
                payloadJson = """{"applicant":"Devi Prasad","targetFPS":"FPS-204","reason":"Seasonal construction work in Pune"}"""
            )
        )
    }

    fun getGrievances(): List<GrievanceEntity> {
        return listOf(
            GrievanceEntity(
                id = "GRV-MH-2024-88392",
                tokenNo = "MH-PDS-2024-88392",
                memberId = MISMATCHED_MEMBER_ID,
                category = "Interstate Portability Biometric Auth Mismatch",
                summary = "e-PoS scanner rejected biometric auth for Devi Prasad at FPS-204 due to typographical name mismatch in NFSA database.",
                filedDaysAgo = 21,
                statutoryMaxDays = 15,
                status = "Awaiting Action / Unanswered (Overdue by 6 days)",
                jurisdictionOfficer = "District Grievance Redressal Officer (DGRO), Pune",
                responseReceived = false,
                lastActionNotes = "No response recorded in 21 days. Exceeds statutory citizen charter deadline of 15 working days."
            )
        )
    }
}
