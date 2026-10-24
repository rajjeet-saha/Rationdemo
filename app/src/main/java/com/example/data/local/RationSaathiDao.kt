package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RationSaathiDao {
    // Households
    @Query("SELECT * FROM households LIMIT 1")
    fun getPrimaryHousehold(): Flow<HouseholdEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHousehold(household: HouseholdEntity)

    // Members
    @Query("SELECT * FROM members WHERE householdId = :householdId ORDER BY age DESC")
    fun getMembersForHousehold(householdId: String): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :memberId LIMIT 1")
    suspend fun getMemberById(memberId: String): MemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("DELETE FROM members WHERE id = :memberId")
    suspend fun deleteMember(memberId: String)

    // Entitlements
    @Query("SELECT * FROM entitlements")
    fun getAllEntitlements(): Flow<List<EntitlementEntity>>

    @Query("SELECT * FROM entitlements WHERE memberId = :memberId")
    fun getEntitlementsForMember(memberId: String): Flow<List<EntitlementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntitlements(entitlements: List<EntitlementEntity>)

    // Shop Stocks
    @Query("SELECT * FROM shop_stocks ORDER BY distanceKm ASC")
    fun getAllShopStocks(): Flow<List<ShopStockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopStocks(stocks: List<ShopStockEntity>)

    @Update
    suspend fun updateShopStock(stock: ShopStockEntity)

    // Offline Drafts
    @Query("SELECT * FROM offline_drafts ORDER BY createdAtTimestamp DESC")
    fun getAllDrafts(): Flow<List<OfflineDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: OfflineDraftEntity)

    @Update
    suspend fun updateDraft(draft: OfflineDraftEntity)

    @Query("DELETE FROM offline_drafts WHERE id = :draftId")
    suspend fun deleteDraft(draftId: String)

    // Grievances
    @Query("SELECT * FROM grievances ORDER BY filedDaysAgo DESC")
    fun getAllGrievances(): Flow<List<GrievanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrievances(grievances: List<GrievanceEntity>)

    @Update
    suspend fun updateGrievance(grievance: GrievanceEntity)

    // Clear everything for scenario reset
    @Query("DELETE FROM households")
    suspend fun clearHouseholds()

    @Query("DELETE FROM members")
    suspend fun clearMembers()

    @Query("DELETE FROM entitlements")
    suspend fun clearEntitlements()

    @Query("DELETE FROM shop_stocks")
    suspend fun clearShopStocks()

    @Query("DELETE FROM offline_drafts")
    suspend fun clearOfflineDrafts()

    @Query("DELETE FROM grievances")
    suspend fun clearGrievances()
}
