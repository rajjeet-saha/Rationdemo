# RationSaathi: Public Distribution, Without the Runaround

A civic-tech Android application designed for interstate migrant households and assisted-service facilitators to safely navigate Public Distribution System (PDS) entitlements, detect record mismatch risks, verify commodity availability, track grievances, and manage offline drafts without risking entitlement loss or compromising personal privacy.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> **Key Architecture Confirmations**:
> - **Native Android Platform (Jetpack Compose + Kotlin)**: Tailored to the container's Android runtime and streaming emulator, delivering a touch-friendly, responsive, offline-first civic experience.
> - **Zero Entitlement Loss Safeguard**: A strict pre-submission correction simulator that blocks dangerous edits (e.g. deletions or identity changes that sever commodity allocations) and flags mismatch risks with clear explanations.
> - **Privacy Isolation & Shared-Device Security**: Robust profile switching and session wipeouts ensuring that shared mobile phones and facilitator terminals leave zero cached data or cross-member leakage.
> - **Deterministic Simulation Boundary**: Clear visual banners differentiating synthetic demo data and simulated submission adapters from official government systems.

- **Confirmed Decision 1**: Building RationSaathi as a native Android Jetpack Compose application with local Room persistence, deterministic business logic, and simulated network synchronization.
- **Confirmed Decision 2**: Multi-language support (Hindi, English, Bengali, Telugu, and Marathi) tailored for seasonal migrant corridors with high-contrast accessibility controls.
- **Confirmed Decision 3**: Complete synthetic test fixtures covering 5 household members, an absent head of household, identity mismatches, an out-of-stock commodity, an unsent offline draft, and a 21-day unanswered grievance.

---

## 1. Overview & Core Concept

### What It Does
RationSaathi provides migrant households with transparent answers to:
> *"What can I safely do today, what information is verified, what still needs confirmation, and who is responsible for the next step?"*

The application enables household members and community facilitators to:
1. **Safely Verify Entitlements**: Inspect commodity-level entitlements, draw histories, and verified vs. household-reported data without risking silent reduction.
2. **Navigate Interstate Portability**: Determine whether wheat, rice, or coarse grains can be collected at destination fair-price shops under One Nation One Ration Card (ONORC) policies, accounting for stock uncertainty.
3. **Simulate Record Corrections**: Compare existing member records with proposed updates before submission to prevent accidental member deletion or loss of rations.
4. **Draft Offline Applications**: Prepare documentation and evidence checklists without an active internet connection, clearly badged as `DRAFT — NOT SUBMITTED`.
5. **Track Grievances & Generate Escalations**: Monitor unresolved grievances with automated day-count calculations and generate privacy-preserving formal escalation drafts.
6. **Protect Shared-Device Privacy**: Enable quick, safe member switching, role-based access for facilitators, and full session memory purges on shared phones.

### Target Audience / Persona
- **Interstate Migrant Workers**: Families working away from their home state who share a single phone, have intermittent network access, and navigate unfamiliar ration shops.
- **Household Members & Dependents**: Individuals seeking their personal eligible share even when the household head is away.
- **Assisted-Service Facilitators**: Community help-desk workers assisting multiple families daily on shared tablets.
- **Public Observers**: Privacy-conscious monitors viewing aggregated, anonymized service statistics without access to individual records.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Daily Action Assessment**: User opens the dashboard -> Views today's verified status -> Sees immediate alerts (e.g., wheat out of stock at destination shop, grievance pending 21 days).
2. **Portability & Stock Check**: User selects "Portability Navigator" -> Checks destination shop status -> Sees `OUT_OF_STOCK_REPORTED` warning with timestamp and recommended alternative shops.
3. **Safety Correction Simulator**: User views mismatched member (Devi Prasad vs. Dev Prasad) -> Attempts naive deletion/update -> Simulator flags severe risk of entitlement cancellation -> System blocks unsafe action and generates a safe correction draft with required evidence checklist.
4. **Offline Draft & Submission Simulation**: Facilitator drafts address update offline -> App marks draft clearly -> Reconnecting network displays `Sync Complete - Unsubmitted` -> User triggers simulated submission adapter to generate an official acknowledgment receipt.
5. **Grievance Escalation**: User opens grievance filed 21 days ago -> App calculates statutory response window -> Generates a redacted formal escalation letter ready for printing or submission.
6. **Session Wipeout & Handover**: Facilitator clicks "End Session & Clear Data" -> App purges in-memory state and temporary caches -> Displays cryptographic zero-leakage confirmation screen.

### Visual Identity & Theme
- **Civic & Trustworthy Tone**: Clean, official, calming, and highly readable.
- **Color Palette**:
  - `Primary` (Deep Forest Green: `#1E4D2B` / `#2D6A4F`): Navigation, verified states, and safe actions.
  - `Secondary / Background` (Warm Off-White / Sand: `#FBFBF9` / `#F3F4F1`): Comfortable for outdoor sunlight reading.
  - `Surface & Cards` (Crisp White: `#FFFFFF` with `#E5E7EB` borders): Clear tactile separation.
  - `Warning / Unverified` (Warm Amber: `#D97706` / `#B45309`): Stale stock, household-reported claims, and unsubmitted drafts.
  - `Critical Risk` (Civic Crimson: `#DC2626`): Blocked deletions, entitlement loss hazards, and data conflicts.
  - `Synthetic Badge` (Indigo Pill: `#4338CA`): Conspicuous demo banner across all synthetic views.
- **Typography & Accessibility**:
  - High-legibility sans-serif with distinct weights for labels and values.
  - Touch targets strictly ≥ 48dp with tactile ripple states.
  - High-contrast text pairings exceeding WCAG AAA standards.
  - Dual encoding: every status indicator pairs color with an explicit icon and text label.

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Chosen Approach | Rationale | Alternatives Considered |
|---|---|---|---|
| **Architecture & Persistence** | Local Room SQLite + Reactive Kotlin StateFlow | Provides instantaneous offline capability, complete control over synthetic data seeding, and testable transactions. | Firebase (unnecessary network dependency for initial zero-trust offline demo). |
| **Correction Engine** | Rule-Based Safety Validator with Blocking Gates | Guarantees that any action threatening a member's entitlement is mathematically caught and prevented before form generation. | Non-blocking warnings (rejected: users might accidentally submit destructive changes). |
| **Session Isolation** | In-Memory Active Session with Explicit Purge Callback | Ensures that shared phone handovers zero out all member records, draft fields, and view histories. | Storing active profile in SharedPreferences (rejected: vulnerable to shared device data leakage). |
| **Stock Uncertainty** | Explicit Provenance & Stale Timestamp Badging | Informs the user when stock is unverified or reported outdated, preventing fruitless long-distance travel. | Assuming shop has stock if listed (rejected: causes harassment and runaround). |

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        RATIONSAATHI ANDROID APP                        │
├────────────────────────────────────────────────────────────────────────┤
│                           PRESENTATION LAYER                           │
│  ┌───────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ HouseholdDashboard    │  │ PortabilityNav    │  │ MismatchScreen │  │
│  │ EntitlementCard       │  │ StockStatusBadge  │  │ SafetySimModal │  │
│  └───────────────────────┘  └───────────────────┘  └────────────────┘  │
│  ┌───────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ OfflineDraftManager   │  │ GrievanceTracker  │  │ PublicStats    │  │
│  │ SimulatedSyncAdapter  │  │ EscalationDraft   │  │ SessionWipeout │  │
│  └───────────────────────┘  └───────────────────┘  └────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                           VIEWMODEL & STATE                            │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ RationSaathiViewModel                                            │  │
│  │ - activeProfile: StateFlow<Member?>                              │  │
│  │ - householdState: StateFlow<HouseholdWithMembers?>               │  │
│  │ - portabilityResult: StateFlow<PortabilityDecision?>             │  │
│  │ - offlineDrafts: StateFlow<List<OfflineDraft>>                   │  │
│  │ - grievanceList: StateFlow<List<GrievanceRecord>>                │  │
│  └──────────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                            DOMAIN LOGIC                                │
│  ┌────────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ SafetyCorrectionEngine │  │ PortabilityEngine │  │ PrivacyManager │  │
│  │ - evaluateRisk()       │  │ - evaluateToday() │  │ - purgeState() │  │
│  │ - generateChecklist()  │  │ - getAlternatives │  │ - switchMember │  │
│  └────────────────────────┘  └───────────────────┘  └────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                       DATA & PERSISTENCE LAYER                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ RationSaathiRepository                                           │  │
│  │ - SeedDataGenerator (5 household members, 1 mismatch, 1 draft)   │  │
│  │ - Room Database: HouseholdDao, MemberDao, EntitlementDao,        │  │
│  │   DraftDao, GrievanceDao, StockDao                               │  │
│  │ - SimulatedSubmissionAdapter (Network toggle, Acknowledgment)    │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Data Entities & Schema
- `HouseholdEntity`: ID, rationCardNo, homeState (e.g. Bihar), destinationState (e.g. Maharashtra), headOfHouseholdId, lastVerifiedTime.
- `MemberEntity`: ID, householdId, name, ageBand, gender, relationship, verificationStatus (`OFFICIAL_VERIFIED`, `MISMATCH_FLAGGED`), hasIdentityConflict.
- `EntitlementEntity`: ID, memberId, commodity (Rice, Wheat, Kerosene, Coarse Grains), monthlyQuotaKg, drawnKg, remainingKg, provenance.
- `ShopStockEntity`: shopId, shopName, jurisdiction, commodity, stockStatus (`AVAILABLE_VERIFIED`, `OUT_OF_STOCK_REPORTED`, `STALE`), lastReportedTime.
- `OfflineDraftEntity`: ID, memberId, draftType, payloadJson, status (`LOCAL_DRAFT`, `SYNCED_UNSUBMITTED`, `SUBMITTED_SIMULATED`), createdAt.
- `GrievanceEntity`: ID, tokenNo, category, filedDaysAgo (21 days), statutoryWindowDays (15 days), status, responseSummary.

---

## 5. Implementation Roadmap & Milestones

1. **Sprint 1: Architecture & Room Database Foundation**
   - Configure Gradle dependencies (M3, Room, Navigation, Material Icons).
   - Implement Room entities, DAOs, Database, and Seed Data Generator.
   - Seed the exact demonstration scenario (5 members, mismatch on Devi Prasad, absent head, out of stock wheat, 21-day grievance, unsent draft).
2. **Sprint 2: Safety Simulator & Portability Rule Engine**
   - Implement `SafetyCorrectionEngine` to detect identity conflicts, predict entitlement loss, and generate evidence checklists.
   - Implement `PortabilityEngine` calculating interstate collection eligibility, handling shop stock uncertainty, and suggesting nearby alternatives.
3. **Sprint 3: UI Shell, Navigation & Screen System**
   - Build accessible M3 theme (`Theme.kt`, `Color.kt`, `Type.kt`).
   - Create top bar with demo badge, language selector, active member profile pill, and emergency "Wipe Session" button.
   - Develop screens: Household Overview, Member Details, Portability Navigator, Correction Simulator, Offline Drafts, Grievance Escalations, and Public Aggregates.
4. **Sprint 4: Privacy Isolation, Shared Phone Handover & Verification**
   - Implement zero-leakage profile switcher and facilitator session termination dialog.
   - Implement Simulated Submission Adapter with network toggle (`Online`/`Offline`) and simulated acknowledgment generation.
   - Run compilation and automated unit tests for safety rules, portability logic, and session purge.
