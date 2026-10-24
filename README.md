# RationSaathi (रेशन साथी / राशन साथी)
### *Public Distribution, Without the Runaround*

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Database-Room%20SQLite-005C84)](https://developer.android.com/training/data-storage/room)
[![Policy](https://img.shields.io/badge/Compliance-NFSA%202013%20%7C%20ONORC-1E4D2B)](#legal--policy-framework)
[![Tests](https://img.shields.io/badge/Tests-Passing%20(7%2F7)-brightgreen)](#automated-testing)

---

## 📲 App Download & Live Cloud Preview

Experience and download **RationSaathi** directly:

* 🌐 **Live Web Preview (Streaming Emulator)**:  
  [https://ais-pre-h3bge5ixavvkbthbixhtxo-384071980207.asia-northeast1.run.app](https://ais-pre-h3bge5ixavvkbthbixhtxo-384071980207.asia-northeast1.run.app)

* 🛠️ **Development App Instance**:  
  [https://ais-dev-h3bge5ixavvkbthbixhtxo-384071980207.asia-northeast1.run.app](https://ais-dev-h3bge5ixavvkbthbixhtxo-384071980207.asia-northeast1.run.app)

* 📦 **Download Android APK**:  
  In the Google AI Studio project interface, open the **Settings (gear icon)** or the **Export** menu in the top-right toolbar and click **Download APK** (or run `gradle assembleDebug` to build `app-debug.apk` locally).

---

## 🎯 Product Mission

Under the **One Nation One Ration Card (ONORC)** and **National Food Security Act (NFSA)** frameworks, interstate migrant workers frequently navigate unfamiliar Fair Price Shops (FPS), experience biometric mismatches, encounter unverified stock shortages, and wait weeks for unanswered grievances.

RationSaathi is a civic-tech Android application designed to give migrant households and assisted-service facilitators a decisive answer to:

> **"What can I safely do today, what information is verified, what still needs confirmation, and who is responsible for the next step?"**

---

## ✨ Core Pillars & Features

### 1. 🛡️ Safety-Critical Record-Correction Simulator (Zero Entitlement Loss)
* **Pre-Submission Blocking Gate**: Compares existing PDS database records with proposed corrections. If an operation threatens to sever a member's active foodgrain entitlement (such as deleting a member to re-add later, or applying unlinked identity changes), the action is **mathematically blocked** (`CRITICAL_ENTITLEMENT_LOSS`).
* **Preserves Active Quota**: Original member allocations (e.g. 5 kg/month) are preserved on file.
* **Evidence Dossier Checklist**: Automatically compiles a step-by-step checklist (Form 4A clerical declaration, Panchayat/Ward Councillor residence certificate, Aadhaar e-KYC slip) to achieve safe resolution without quota cancellation.

### 2. 🌾 Deterministic Portability & Stock Uncertainty Engine
* Evaluates destination Fair Price Shop stock feeds against individual household quotas.
* **Stale Stock Detection**: Automatically flags stock reports older than 12 hours as stale/unconfirmed (`OUT_OF_STOCK_REPORTED`).
* **Authorised Alternative Routing**: If wheat or rice is depleted at the assigned destination shop, the engine routes the family to authorized nearby alternative shops (e.g., Fair Price Shop #118, 1.4 km away) where verified stock exists.
* **Uncertainty Warnings**: Displays conspicuous cautions advising citizens not to incur travel expenses without verified local confirmation.

### 3. ⚖️ 21-Day Grievance Tracking & Statutory Escalation Generator
* Monitors unresolved citizen grievances against the mandatory **15-working-day statutory Citizen Charter limit**.
* Calculates elapsed days and flags overdue tickets (e.g., 21 days elapsed = 6 days overdue).
* **Automated Legal Escalation Generator**: Generates formal, legally grounded escalation letters citing **Section 15 of the National Food Security Act (NFSA) 2013** addressed to the **District Grievance Redressal Officer (DGRO)**.

### 4. 🔒 Shared-Device Privacy & Session Isolation
* **Per-Member Privacy Views**: Lets individual members view their personal eligible entitlement without exposing other household records.
* **Emergency Session Wipeout**: Provides a prominent **"End Session & Clear Data"** control that purges in-memory active states, clears draft buffers, resets navigation stacks, and generates a zero-leakage cleanup confirmation on shared devices.

### 5. 📝 Offline Drafts & Simulated Submission Adapter
* Clearly marks offline kiosk drafts as **`DRAFT — NOT SUBMITTED`**.
* **Sync Integrity**: Synchronizing data to a staging server does *not* claim official government acceptance.
* **Simulated Submission Adapter**: Issues official test acknowledgment tokens (`SIM-ACK-MH-XXXXXX`) through an explicit test boundary without pretending to connect to live government servers.

### 6. 🗣️ Multilingual Support for Migrant Corridors
* Built-in instant language switcher supporting:
  * **English**
  * **Hindi (हिंदी)**
  * **Marathi (मराठी)** *(Maharashtra destination corridor)*
  * **Bengali (বাংলা)**
  * **Telugu (తెలుగు)**

---

## 🧪 Synthetic Demonstration Scenario

The application includes a deterministic, repeatable demonstration scenario containing 6 real-world public distribution challenges:

| Evaluation Fact | Seed Scenario Details |
|---|---|
| **Household Corridor** | 5 members migrated from **Saran (Chhapra), Bihar** to **Hadapsar, Pune, Maharashtra** (Card: `NFSA-1092-8821-4401`) |
| **Absent Household Head** | `Ram Ashray Prasad` (Age 52) is working at an interstate brick kiln; adult spouse `Sunita Devi` (Age 48) is authorized for e-PoS biometric authentication |
| **Record Mismatch** | `Devi Prasad`: PDS database lists **"Dev Prasad" (Age 24)**; identity document lists **"Devi Prasad" (Age 26)** |
| **Entitlement Risk** | Attempting to delete or unlink Devi Prasad threatens loss of **5 kg monthly grain quota** (3 kg Rice + 2 kg Wheat) |
| **Destination Shop Shortage** | Destination Shop `FPS #204`: Rice is **420 kg verified available**; Wheat is **reported out-of-stock (18 hours stale)** |
| **Alternative Shop Routing** | Alternative Shop `FPS #118` (1.4 km away): **310 kg Wheat verified available** |
| **Overdue Grievance** | Token `#MH-PDS-2024-88392` (Biometric Auth Failure) filed **21 days ago** with zero response (exceeds 15-day statutory deadline) |
| **Offline Draft** | Form `DRAFT-2024-001` prepared at offline kiosk awaiting network synchronization |

*The scenario can be reset to its initial seed state at any time via the **Console** tab.*

---

## 🏗️ Technical Architecture

```
┌────────────────────────────────────────────────────────────────────────┐
│                        RATIONSAATHI ANDROID APP                        │
├────────────────────────────────────────────────────────────────────────┤
│                           PRESENTATION LAYER                           │
│  ┌───────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ CivicDrawer           │  │ CivicTopBar       │  │ DemoModeBanner │  │
│  │ (ModalNavigation)     │  │ (Profile/Language)│  │ (Status Strip) │  │
│  └───────────────────────┘  └───────────────────┘  └────────────────┘  │
│  ┌───────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ DashboardScreen       │  │ EntitlementScreen │  │ Portability    │  │
│  │ SafetySimulatorScreen │  │ OfflineDrafts     │  │ GrievanceTrack │  │
│  └───────────────────────┘  └───────────────────┘  └────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                      VIEWMODEL & STATE CONTROLLER                      │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ RationSaathiViewModel                                            │  │
│  │ - selectedTab: StateFlow<ScreenTab>                              │  │
│  │ - activeMember: StateFlow<MemberEntity?>                         │  │
│  │ - isOnlineSimulated: StateFlow<Boolean>                          │  │
│  │ - todaySummary: StateFlow<TodayActionSummary?>                   │  │
│  └──────────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                         DETERMINISTIC DOMAIN                           │
│  ┌────────────────────────┐  ┌───────────────────┐  ┌────────────────┐  │
│  │ SafetyCorrectionEngine │  │ PortabilityEngine │  │ GrievanceEngine│  │
│  │ - evaluateRisk()       │  │ - evaluateToday() │  │ - generateDGRO │  │
│  │ - generateChecklist()  │  │ - routeAlternative│  │ - checkDeadline│  │
│  └────────────────────────┘  └───────────────────┘  └────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                       DATA & PERSISTENCE LAYER                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ RationSaathiRepository                                           │  │
│  │ - InitialSeedData (5 members, mismatch, stale stock, draft)      │  │
│  │ - Room Database: RationSaathiDao & SQLite tables                 │  │
│  │ - SimulatedSubmissionAdapter (Network toggle & ACK generation)   │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
app/src/
├── main/
│   ├── AndroidManifest.xml
│   ├── java/com/example/
│   │   ├── MainActivity.kt               # Edge-to-edge Compose shell & drawer
│   │   ├── data/
│   │   │   ├── model/Enums.kt            # Provenance, StockState, DraftStatus
│   │   │   ├── local/
│   │   │   │   ├── Entities.kt           # Room entities for Household, Member, etc.
│   │   │   │   ├── Converters.kt         # Room TypeConverters
│   │   │   │   ├── RationSaathiDao.kt    # Reactive Flow DAOs
│   │   │   │   └── RationSaathiDatabase.kt # SQLite database singleton
│   │   │   └── repository/
│   │   │       ├── InitialSeedData.kt    # 6 evaluation facts fixture generator
│   │   │       └── RationSaathiRepository.kt # Repository & simulation adapter
│   │   ├── domain/
│   │   │   ├── SafetyCorrectionEngine.kt # Zero-entitlement loss safety gate
│   │   │   ├── PortabilityEngine.kt      # "What can I do today?" rules engine
│   │   │   └── GrievanceEscalationEngine.kt # Legal escalation letter generator
│   │   └── ui/
│   │       ├── theme/                    # Civic Green, Amber, Red tokens
│   │       ├── state/                    # ViewModel & Language Manager
│   │       ├── components/               # CivicTopBar, CivicDrawer, BottomNav, Banners
│   │       └── screens/                  # Dashboard, Entitlements, Portability, etc.
│   └── res/
│       ├── drawable/ic_launcher_*.xml    # Custom adaptive launcher icon assets
│       ├── mipmap-*/                     # Non-adaptive & round PNG launcher icons
│       └── values/strings.xml            # App branding
└── test/
    └── java/com/example/
        ├── RationSaathiUnitTest.kt       # Safety gate, portability & grievance tests
        └── ExampleRobolectricTest.kt     # Android context & resource verification
```

---

## 🛠️ Local Build & Installation

### Prerequisites
* Android Studio Iguana / Ladybug or newer
* JDK 17 or JDK 21
* Android SDK 36 (minSdk 24)

### Clone & Compile
```bash
# Clone the repository
git clone https://github.com/your-username/RationSaathi.git
cd RationSaathi

# Build Debug APK
gradle assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk
```

### Install onto Device / Emulator via ADB
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🧪 Automated Testing

The codebase includes local JVM and Robolectric test suites verifying the core safety invariants:

```bash
gradle :app:testDebugUnitTest
```

### Verified Test Cases:
1. `testSafetySimulatorBlocksDangerousDeletion`: Ensures deleting a member with active rations triggers `CRITICAL_ENTITLEMENT_LOSS` and preserves quota.
2. `testSafetySimulatorDetectsDualConflict`: Confirms dual name and age discrepancies trigger the pre-submission gate and output an evidence dossier.
3. `testPortabilityEngineDetectsDestinationWheatDepletion`: Verifies destination stock depletion detection and routing to alternative Fair Price Shop #118.
4. `testGrievanceOverdueCalculation`: Validates calculation of the 15-day statutory citizen charter limit against 21 days elapsed (6 days overdue) and verifies the Section 15 NFSA legal citation.
5. `testMultilingualTranslations`: Validates Indic translation integrity across English, Hindi, and Marathi.
6. `read string from context`: Validates Android resource resolution and launcher label compliance.

---

## ⚖️ Legal & Privacy Framework
* **Data Minimization**: The application does not collect real Aadhaar numbers, biometric fingerprints, or actual government records. All identities and card numbers are synthetic test fixtures.
* **Deterministic Transparency**: Every field displays its provenance classification (`OFFICIAL_VERIFIED`, `OFFICIAL_LAST_SYNCED`, `HOUSEHOLD_REPORTED`, `FACILITATOR_ENTERED`, `SYNTHETIC_DEMO`).
* **Simulation Boundaries**: All submission and sync interactions are explicitly labelled as simulation adapters to prevent any confusion with live government portals.

---

## 📄 License
Distributed under the **Apache 2.0 License**. Developed as a civic-tech contribution for accessible, runaround-free public distribution.
