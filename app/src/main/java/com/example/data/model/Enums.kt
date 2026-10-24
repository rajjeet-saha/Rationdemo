package com.example.data.model

enum class Provenance(val label: String) {
    OFFICIAL_VERIFIED("Official Verified"),
    OFFICIAL_LAST_SYNCED("Official Last Synced"),
    HOUSEHOLD_REPORTED("Household Reported"),
    FACILITATOR_ENTERED("Facilitator Entered"),
    SYNTHETIC_DEMO("Synthetic Demo Fixture"),
    UNKNOWN("Unverified Source")
}

enum class VerificationState {
    VERIFIED,
    MISMATCH_DETECTED,
    PENDING_VERIFICATION,
    UNVERIFIED
}

enum class StockState(val displayName: String) {
    AVAILABLE_VERIFIED("Verified In-Stock"),
    REPORTED_AVAILABLE("Reported Available (Unverified)"),
    OUT_OF_STOCK_REPORTED("Reported Out of Stock"),
    OUT_OF_STOCK_VERIFIED("Verified Out of Stock"),
    STALE("Stock Status Stale (>12h)"),
    UNKNOWN("Stock Status Unknown"),
    SOURCE_UNAVAILABLE("Shop Feed Unavailable")
}

enum class DraftStatus {
    LOCAL_DRAFT,
    SYNCED_UNSUBMITTED,
    SUBMITTED_SIMULATED
}

enum class MemberRole {
    HEAD_ABSENT,
    ADULT_REPRESENTATIVE,
    MISMATCHED_MEMBER,
    ADULT_MEMBER,
    DEPENDENT_CHILD
}
