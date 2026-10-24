package com.example.ui.state

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    EN("en", "English", "English"),
    HI("hi", "Hindi", "हिंदी"),
    MR("mr", "Marathi", "मराठी"),
    BN("bn", "Bengali", "বাংলা"),
    TE("te", "Telugu", "తెలుగు")
}

object LanguageManager {
    fun getString(key: String, lang: AppLanguage): String {
        return translations[key]?.get(lang) ?: translations[key]?.get(AppLanguage.EN) ?: key
    }

    private val translations = mapOf(
        "app_title" to mapOf(
            AppLanguage.EN to "RationSaathi",
            AppLanguage.HI to "राशन साथी",
            AppLanguage.MR to "रेशन साथी",
            AppLanguage.BN to "রেশন সাথী",
            AppLanguage.TE to "రేషన్ సాథి"
        ),
        "tagline" to mapOf(
            AppLanguage.EN to "Public Distribution, Without the Runaround",
            AppLanguage.HI to "सार्वजनिक वितरण, बिना किसी भटकाव के",
            AppLanguage.MR to "सार्वजनिक वितरण, विना हेलपाटे",
            AppLanguage.BN to "গণবন্টন ব্যবস্থা, অযথা হয়রানি ছাড়া",
            AppLanguage.TE to "ప్రజా పంపిణీ, ఎలాంటి తిప్పలు లేకుండా"
        ),
        "demo_banner" to mapOf(
            AppLanguage.EN to "DEMO MODE — Synthetic Simulation. Not Connected to Live State Portal.",
            AppLanguage.HI to "डेमो मोड — काल्पनिक डेटा। आधिकारिक सरकारी सर्वर से जुड़ा नहीं है।",
            AppLanguage.MR to "डेमो मोड — प्रात्यक्षिक डेटा. थेट सरकारी सर्व्हरशी जोडलेले नाही.",
            AppLanguage.BN to "ডেমো মোড — কাল্পনিক তথ্য। আসল সরকারি সিস্টেম নয়।",
            AppLanguage.TE to "డెమో మోడ్ — సింథటిక్ డేటా. ప్రత్యక్ష ప్రభుత్వ పోర్టల్‌తో అనుసంధానించబడలేదు."
        ),
        "nav_overview" to mapOf(
            AppLanguage.EN to "Today's Status",
            AppLanguage.HI to "आज की स्थिति",
            AppLanguage.MR to "आजची स्थिती",
            AppLanguage.BN to "আজকের অবস্থা",
            AppLanguage.TE to "ఈరోజు స్థితి"
        ),
        "nav_entitlements" to mapOf(
            AppLanguage.EN to "Entitlements",
            AppLanguage.HI to "राशन कोटा",
            AppLanguage.MR to "रेशन कोटा",
            AppLanguage.BN to "রেশন কোটা",
            AppLanguage.TE to "కోటా వివరాలు"
        ),
        "nav_portability" to mapOf(
            AppLanguage.EN to "Portability",
            AppLanguage.HI to "पोर्टेबिलिटी",
            AppLanguage.MR to "पोर्टेबिलिटी",
            AppLanguage.BN to "পোর্টাবিলিটি",
            AppLanguage.TE to "పోర్టబిలిటీ"
        ),
        "nav_simulator" to mapOf(
            AppLanguage.EN to "Safety Simulator",
            AppLanguage.HI to "सुरक्षा सिम्युलेटर",
            AppLanguage.MR to "सुरक्षा सिम्युलेटर",
            AppLanguage.BN to "সুরক্ষা সিমুলেটর",
            AppLanguage.TE to "సేఫ్టీ సిమ్యులేటర్"
        ),
        "nav_drafts" to mapOf(
            AppLanguage.EN to "Offline Drafts",
            AppLanguage.HI to "ऑफ़लाइन ड्राफ्ट",
            AppLanguage.MR to "ऑफलाइन मसुदा",
            AppLanguage.BN to "অফলাইন ড্রাফট",
            AppLanguage.TE to "ఆఫ్‌లైన్ డ్రాఫ్ట్‌లు"
        ),
        "nav_grievances" to mapOf(
            AppLanguage.EN to "Grievances",
            AppLanguage.HI to "शिकायत निवारण",
            AppLanguage.MR to "तक्रार निवारण",
            AppLanguage.BN to "অভিযোগ নিষ্পত্তি",
            AppLanguage.TE to "ఫిర్యాదులు"
        ),
        "nav_public" to mapOf(
            AppLanguage.EN to "Public Stats",
            AppLanguage.HI to "जन आंकड़े",
            AppLanguage.MR to "सार्वजनिक आकडेवारी",
            AppLanguage.BN to "পাবলিক পরিসংখ্যান",
            AppLanguage.TE to "ప్రజా గణాంకాలు"
        ),
        "end_session" to mapOf(
            AppLanguage.EN to "End Session & Wipe Data",
            AppLanguage.HI to "सत्र समाप्त करें और डेटा हटाएं",
            AppLanguage.MR to "सत्र समाप्त करा आणि डेटा पुसा",
            AppLanguage.BN to "সেশন শেষ করুন ও ডেটা মুছুন",
            AppLanguage.TE to "సెషన్ ముగించి డేటా క్లియర్ చేయండి"
        ),
        "what_can_i_do_today" to mapOf(
            AppLanguage.EN to "What can this household do today?",
            AppLanguage.HI to "यह परिवार आज क्या कर सकता है?",
            AppLanguage.MR to "हे कुटुंब आज काय करू शकते?",
            AppLanguage.BN to "এই পরিবার আজ কী করতে পারে?",
            AppLanguage.TE to "ఈ కుటుంబం ఈరోజు ఏమి చేయగలదు?"
        ),
        "head_absent_badge" to mapOf(
            AppLanguage.EN to "Head of Household Absent",
            AppLanguage.HI to "परिवार का मुखिया अनुपस्थित",
            AppLanguage.MR to "कुटुंबप्रमुख अनुपस्थित",
            AppLanguage.BN to "পরিবার প্রধান অনুপস্থিত",
            AppLanguage.TE to "కుటుంబ యజమాని లేరు"
        ),
        "mismatch_alert" to mapOf(
            AppLanguage.EN to "Identity Record Mismatch Flagged",
            AppLanguage.HI to "पहचान रिकॉर्ड में विसंगति पाई गई",
            AppLanguage.MR to "ओळख नोंदणीत तफावत आढळली",
            AppLanguage.BN to "পরিচয় নথিতে গরমিল চিহ্নিত",
            AppLanguage.TE to "గుర్తింపు రికార్డులో వ్యత్యాసం ఉంది"
        )
    )
}
