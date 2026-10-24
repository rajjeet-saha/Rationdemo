package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.DraftStatus
import com.example.data.model.MemberRole
import com.example.data.model.Provenance
import com.example.data.model.StockState
import com.example.data.model.VerificationState

class Converters {
    @TypeConverter
    fun fromProvenance(value: Provenance?): String = value?.name ?: Provenance.SYNTHETIC_DEMO.name

    @TypeConverter
    fun toProvenance(value: String?): Provenance = try {
        value?.let { Provenance.valueOf(it) } ?: Provenance.SYNTHETIC_DEMO
    } catch (_: Exception) {
        Provenance.SYNTHETIC_DEMO
    }

    @TypeConverter
    fun fromVerificationState(value: VerificationState?): String = value?.name ?: VerificationState.VERIFIED.name

    @TypeConverter
    fun toVerificationState(value: String?): VerificationState = try {
        value?.let { VerificationState.valueOf(it) } ?: VerificationState.VERIFIED
    } catch (_: Exception) {
        VerificationState.VERIFIED
    }

    @TypeConverter
    fun fromStockState(value: StockState?): String = value?.name ?: StockState.UNKNOWN.name

    @TypeConverter
    fun toStockState(value: String?): StockState = try {
        value?.let { StockState.valueOf(it) } ?: StockState.UNKNOWN
    } catch (_: Exception) {
        StockState.UNKNOWN
    }

    @TypeConverter
    fun fromDraftStatus(value: DraftStatus?): String = value?.name ?: DraftStatus.LOCAL_DRAFT.name

    @TypeConverter
    fun toDraftStatus(value: String?): DraftStatus = try {
        value?.let { DraftStatus.valueOf(it) } ?: DraftStatus.LOCAL_DRAFT
    } catch (_: Exception) {
        DraftStatus.LOCAL_DRAFT
    }

    @TypeConverter
    fun fromMemberRole(value: MemberRole?): String = value?.name ?: MemberRole.ADULT_MEMBER.name

    @TypeConverter
    fun toMemberRole(value: String?): MemberRole = try {
        value?.let { MemberRole.valueOf(it) } ?: MemberRole.ADULT_MEMBER
    } catch (_: Exception) {
        MemberRole.ADULT_MEMBER
    }
}
