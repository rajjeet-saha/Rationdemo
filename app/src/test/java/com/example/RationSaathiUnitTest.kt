package com.example

import com.example.data.repository.InitialSeedData
import com.example.domain.GrievanceEscalationEngine
import com.example.domain.PortabilityEngine
import com.example.domain.RiskSeverity
import com.example.domain.SafetyCorrectionEngine
import com.example.ui.state.AppLanguage
import com.example.ui.state.LanguageManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RationSaathiUnitTest {

    @Test
    fun testSafetySimulatorBlocksDangerousDeletion() {
        val member = InitialSeedData.getMembers().first { it.hasMismatch }
        val entitlements = InitialSeedData.getEntitlements().filter { it.memberId == member.id }

        val assessment = SafetyCorrectionEngine.evaluateCorrectionProposal(
            existingMember = member,
            proposedName = member.officialRecordName,
            proposedAge = member.age,
            isDeletionAttempt = true,
            memberEntitlements = entitlements
        )

        assertTrue("Dangerous deletion must be blocked", assessment.isBlocked)
        assertEquals(RiskSeverity.CRITICAL_ENTITLEMENT_LOSS, assessment.severity)
        assertEquals(5.0, assessment.affectedEntitlementKg, 0.01)
        assertTrue("Checklist must contain Form 4A", assessment.evidenceChecklist.any { it.title.contains("Form 4A") })
    }

    @Test
    fun testSafetySimulatorDetectsDualConflict() {
        val member = InitialSeedData.getMembers().first { it.hasMismatch }
        val entitlements = InitialSeedData.getEntitlements().filter { it.memberId == member.id }

        val assessment = SafetyCorrectionEngine.evaluateCorrectionProposal(
            existingMember = member,
            proposedName = "Devi Prasad",
            proposedAge = 26, // Age difference 2 years from record 24
            isDeletionAttempt = false,
            memberEntitlements = entitlements
        )

        assertTrue("Dual conflict must trigger pre-submission gate", assessment.isBlocked)
        assertEquals(RiskSeverity.CRITICAL_ENTITLEMENT_LOSS, assessment.severity)
        assertTrue(assessment.evidenceChecklist.size >= 3)
    }

    @Test
    fun testPortabilityEngineDetectsDestinationWheatDepletion() {
        val household = InitialSeedData.getHousehold()
        val members = InitialSeedData.getMembers()
        val entitlements = InitialSeedData.getEntitlements()
        val stocks = InitialSeedData.getShopStocks()

        val summary = PortabilityEngine.evaluateTodayPortability(household, members, entitlements, stocks)

        assertTrue("Rice should be ready for collection", summary.canCollectRiceToday)
        assertFalse("Destination wheat is depleted", summary.canCollectWheatToday)
        assertNotNull("Alternative shop must be provided", summary.alternativeShop)
        assertTrue("Alternative shop is FPS-118", summary.alternativeShop!!.shopCode == "FPS-118")
    }

    @Test
    fun testGrievanceOverdueCalculation() {
        val grievance = InitialSeedData.getGrievances().first()
        val household = InitialSeedData.getHousehold()

        val escalation = GrievanceEscalationEngine.generateEscalation(grievance, household)

        assertTrue(escalation.isStatutoryDeadlineExceeded)
        assertEquals(21, escalation.daysElapsed)
        assertEquals(6, escalation.daysOverdue)
        assertTrue(escalation.formalLetterBody.contains("Section 15, National Food Security Act"))
        assertTrue(escalation.formalLetterBody.contains("6 days"))
    }

    @Test
    fun testMultilingualTranslations() {
        val enTitle = LanguageManager.getString("app_title", AppLanguage.EN)
        val hiTitle = LanguageManager.getString("app_title", AppLanguage.HI)
        val mrTitle = LanguageManager.getString("app_title", AppLanguage.MR)

        assertEquals("RationSaathi", enTitle)
        assertEquals("राशन साथी", hiTitle)
        assertEquals("रेशन साथी", mrTitle)
    }
}
