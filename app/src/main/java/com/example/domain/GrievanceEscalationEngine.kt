package com.example.domain

import com.example.data.local.GrievanceEntity
import com.example.data.local.HouseholdEntity

data class EscalationDraft(
    val recipientTitle: String,
    val subject: String,
    val legalCitation: String,
    val daysElapsed: Int,
    val daysOverdue: Int,
    val formalLetterBody: String,
    val isStatutoryDeadlineExceeded: Boolean
)

object GrievanceEscalationEngine {

    fun generateEscalation(grievance: GrievanceEntity, household: HouseholdEntity?): EscalationDraft {
        val daysElapsed = grievance.filedDaysAgo
        val maxDays = grievance.statutoryMaxDays
        val isOverdue = daysElapsed > maxDays
        val overdueBy = if (isOverdue) daysElapsed - maxDays else 0

        val subject = "FORMAL ESCALATION: Unresolved Grievance #${grievance.tokenNo} regarding Interstate PDS Portability"
        val citation = "Section 15, National Food Security Act (NFSA) 2013 read with State Public Services Guarantee Citizen Charter (15-day resolution limit)."

        val letterBody = """
To,
The District Grievance Redressal Officer (DGRO),
District Collectorate, Pune, Maharashtra

Subject: $subject
Legal Reference: $citation

Respected Officer,

I am writing regarding Grievance Token #${grievance.tokenNo}, registered on behalf of interstate migrant household (Ration Card: ${household?.syntheticCardNo ?: "NFSA-1092-8821"}), which has remained pending without official redress for $daysElapsed calendar days.

Under the mandated Public Services Citizen Charter, interstate biometric failure and portability grievances must be disposed within $maxDays working days. This statutory timeline has now been exceeded by $overdueBy days.

Summary of Issue:
${grievance.summary}

Request for Immediate Action:
1. Issue an immediate directive to the Taluka Supply Officer, Hadapsar to authorize manual verification / OTP override at Fair Price Shop #204.
2. Ensure uninterrupted distribution of entitled food grains (Rice & Wheat) under the One Nation One Ration Card (ONORC) scheme.
3. Record an entry in the State Redressal Portal acknowledging citizen charter compliance failure.

Yours faithfully,
Authorised Household Representative
Dated: October 2026
        """.trimIndent()

        return EscalationDraft(
            recipientTitle = grievance.jurisdictionOfficer,
            subject = subject,
            legalCitation = citation,
            daysElapsed = daysElapsed,
            daysOverdue = overdueBy,
            formalLetterBody = letterBody,
            isStatutoryDeadlineExceeded = isOverdue
        )
    }
}
