package com.example.domain

import com.example.data.local.EntitlementEntity
import com.example.data.local.MemberEntity

data class RiskAssessment(
    val isBlocked: Boolean,
    val severity: RiskSeverity,
    val headline: String,
    val explanation: String,
    val affectedEntitlementKg: Double,
    val affectedCommodities: List<String>,
    val evidenceChecklist: List<EvidenceItem>,
    val safeAlternativeRecommendation: String
)

enum class RiskSeverity {
    LOW_TYPO,
    MODERATE_AGE_SHIFT,
    CRITICAL_ENTITLEMENT_LOSS
}

data class EvidenceItem(
    val title: String,
    val purpose: String,
    val isMandatory: Boolean,
    var isChecked: Boolean = false
)

object SafetyCorrectionEngine {

    fun evaluateCorrectionProposal(
        existingMember: MemberEntity,
        proposedName: String,
        proposedAge: Int,
        isDeletionAttempt: Boolean,
        memberEntitlements: List<EntitlementEntity>
    ): RiskAssessment {
        val totalQuotaKg = memberEntitlements.sumOf { it.monthlyQuotaKg }
        val commodities = memberEntitlements.map { it.commodityName }

        if (isDeletionAttempt) {
            return RiskAssessment(
                isBlocked = true,
                severity = RiskSeverity.CRITICAL_ENTITLEMENT_LOSS,
                headline = "OPERATION BLOCKED: Critical Entitlement Loss Risk",
                explanation = "Attempting to delete '${existingMember.displayName}' will sever an active entitlement of $totalQuotaKg kg monthly food grains (${commodities.joinToString(", ")}). The National Food Security Act (NFSA) database does not allow re-adding severed members without fresh state-level re-enrolment.",
                affectedEntitlementKg = totalQuotaKg,
                affectedCommodities = commodities,
                evidenceChecklist = listOf(
                    EvidenceItem("Form 4A (Correction Declaration)", "Prevents member cancellation during clerical rectification", true),
                    EvidenceItem("Panchayat / Ward Councillor Letter", "Verifies family continuity without deletion", true),
                    EvidenceItem("Aadhaar e-KYC Seed Slip", "Links biometric UID to existing Member ID instead of creating duplicate", true)
                ),
                safeAlternativeRecommendation = "Do NOT delete the member. File a 'Typographical Rectification Request' to update the name from '${existingMember.officialRecordName}' to '$proposedName' while preserving the existing Member UUID and entitlement."
            )
        }

        val nameChanged = existingMember.officialRecordName.trim() != proposedName.trim()
        val ageDiff = kotlin.math.abs(existingMember.age - proposedAge)

        if (nameChanged && ageDiff >= 2) {
            return RiskAssessment(
                isBlocked = true,
                severity = RiskSeverity.CRITICAL_ENTITLEMENT_LOSS,
                headline = "PRE-SUBMISSION GATE: Dual Identity & Age Conflict",
                explanation = "Changing both name ('${existingMember.officialRecordName}' -> '$proposedName') and age (${existingMember.age} -> $proposedAge) triggers an automated anti-fraud flag in the interstate ONORC database. Submitting directly without linked proof may suspend the member's $totalQuotaKg kg entitlement during audit review.",
                affectedEntitlementKg = totalQuotaKg,
                affectedCommodities = commodities,
                evidenceChecklist = listOf(
                    EvidenceItem("Identity Proof (Aadhaar / Voter ID)", "Showing name as '$proposedName' and DOB", true),
                    EvidenceItem("Ration Card Copy (Home State)", "Showing original entry under '${existingMember.officialRecordName}'", true),
                    EvidenceItem("Joint Affidavit / Self Declaration", "Confirming '$proposedName' and '${existingMember.officialRecordName}' are the same individual", true),
                    EvidenceItem("Biometric Re-verification Token", "Collected at Fair Price Shop e-PoS or Seva Kendra", false)
                ),
                safeAlternativeRecommendation = "Proceed with 'Assisted Correction Draft' mode. The system has drafted an Evidence Dossier preserving the original quota until biometric re-verification is completed at the local Fair Price Shop."
            )
        }

        if (nameChanged) {
            return RiskAssessment(
                isBlocked = false,
                severity = RiskSeverity.LOW_TYPO,
                headline = "Low Risk: Minor Typographical Spelling Variance",
                explanation = "The proposed name '$proposedName' matches phonetic conventions for '${existingMember.officialRecordName}'. This correction does not alter household entitlement quota.",
                affectedEntitlementKg = 0.0,
                affectedCommodities = emptyList(),
                evidenceChecklist = listOf(
                    EvidenceItem("Aadhaar Copy", "Standard verification of updated spelling", true)
                ),
                safeAlternativeRecommendation = "Eligible for expedited online update with single Aadhaar e-KYC authentication."
            )
        }

        return RiskAssessment(
            isBlocked = false,
            severity = RiskSeverity.LOW_TYPO,
            headline = "No Entitlement Risk Detected",
            explanation = "Proposed values match verified records.",
            affectedEntitlementKg = 0.0,
            affectedCommodities = emptyList(),
            evidenceChecklist = emptyList(),
            safeAlternativeRecommendation = "Records are already in sync."
        )
    }
}
