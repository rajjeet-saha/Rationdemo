import os
import sys
from http.server import HTTPServer, BaseHTTPRequestHandler

PORT = 3000
APK_PATHS = [
    "/app/build/outputs/apk/debug/app-debug.apk",
    "/app/applet/app/build/outputs/apk/debug/app-debug.apk",
    ".build-outputs/app-debug.apk"
]

def find_apk():
    for path in APK_PATHS:
        if os.path.exists(path) and os.path.getsize(path) > 0:
            return path
    return None

HTML_PAGE = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>RationSaathi — Public Distribution, Without the Runaround</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
  <!-- Three.js from CDN with fallback handling -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
  <style>
    :root {
      --bg: #0A0F0D;
      --surface: #121A15;
      --surface-elevated: #18221C;
      --surface-border: #1F3025;
      --primary: #1E4D2B;
      --primary-light: #2D6A4F;
      --primary-bright: #4EBA74;
      --accent-gold: #F59E0B;
      --accent-gold-light: #FCD34D;
      --danger: #EF4444;
      --danger-bg: rgba(239, 68, 68, 0.12);
      --text-main: #F3F4F6;
      --text-muted: #9CA3AF;
      --font: 'Plus Jakarta Sans', -apple-system, sans-serif;
      --mono: 'JetBrains Mono', monospace;
    }

    * { box-sizing: border-box; margin: 0; padding: 0; }
    html { scroll-behavior: smooth; }
    body {
      background: var(--bg);
      color: var(--text-main);
      font-family: var(--font);
      overflow-x: hidden;
      line-height: 1.6;
    }

    /* Ambient Spotlight & Grid Pattern (Aceternity style) */
    .bg-grid {
      position: fixed;
      top: 0; left: 0; right: 0; bottom: 0;
      background-image: 
        linear-gradient(to right, rgba(30, 77, 43, 0.08) 1px, transparent 1px),
        linear-gradient(to bottom, rgba(30, 77, 43, 0.08) 1px, transparent 1px);
      background-size: 40px 40px;
      pointer-events: none;
      z-index: 0;
    }
    .ambient-glow {
      position: fixed;
      top: 10%;
      left: 50%;
      transform: translateX(-50%);
      width: 800px;
      height: 450px;
      background: radial-gradient(ellipse at center, rgba(46, 106, 79, 0.25) 0%, rgba(10, 15, 13, 0) 70%);
      filter: blur(80px);
      pointer-events: none;
      z-index: 0;
    }

    /* Floating Glassmorphic Navbar */
    .navbar {
      position: fixed;
      top: 1.25rem;
      left: 50%;
      transform: translateX(-50%);
      width: calc(100% - 2.5rem);
      max-width: 1080px;
      padding: 0.75rem 1.25rem;
      background: rgba(18, 26, 21, 0.75);
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 9999px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      z-index: 100;
      box-shadow: 0 20px 40px rgba(0,0,0,0.4);
    }
    .nav-brand {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      text-decoration: none;
      color: white;
      font-weight: 800;
      font-size: 1.1rem;
    }
    .nav-brand-icon {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: linear-gradient(135deg, #2D6A4F, #1E4D2B);
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--accent-gold);
      border: 1px solid rgba(245, 158, 11, 0.4);
    }
    .nav-links {
      display: flex;
      gap: 1.5rem;
      list-style: none;
    }
    .nav-links a {
      color: var(--text-muted);
      text-decoration: none;
      font-size: 0.9rem;
      font-weight: 500;
      transition: color 0.2s;
    }
    .nav-links a:hover { color: white; }
    .nav-cta {
      background: linear-gradient(135deg, #2D6A4F 0%, #1E4D2B 100%);
      color: white;
      padding: 0.5rem 1.2rem;
      border-radius: 9999px;
      text-decoration: none;
      font-size: 0.85rem;
      font-weight: 700;
      border: 1px solid rgba(78, 186, 116, 0.4);
      box-shadow: 0 4px 15px rgba(30, 77, 43, 0.4);
      transition: all 0.2s;
    }
    .nav-cta:hover {
      transform: translateY(-1px);
      box-shadow: 0 6px 20px rgba(78, 186, 116, 0.4);
    }

    /* Main Container */
    main { position: relative; z-index: 1; }

    /* Hero Section with 3D Canvas */
    .hero {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      text-align: center;
      padding: 8rem 1.5rem 4rem;
      position: relative;
    }
    .canvas-container {
      width: 340px;
      height: 340px;
      margin: 0 auto 1.5rem;
      position: relative;
    }
    #three-canvas {
      width: 100%;
      height: 100%;
      display: block;
      border-radius: 50%;
    }
    .hero-pill {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      background: rgba(45, 106, 79, 0.25);
      border: 1px solid rgba(78, 186, 116, 0.35);
      padding: 0.4rem 1rem;
      border-radius: 9999px;
      font-size: 0.85rem;
      font-weight: 600;
      color: var(--primary-bright);
      margin-bottom: 1.5rem;
    }
    .hero-pill .dot {
      width: 8px;
      height: 8px;
      background: var(--primary-bright);
      border-radius: 50%;
      box-shadow: 0 0 10px var(--primary-bright);
    }
    .hero h1 {
      font-size: clamp(2.5rem, 6vw, 4.5rem);
      font-weight: 800;
      letter-spacing: -0.03em;
      line-height: 1.1;
      max-width: 900px;
      margin-bottom: 1.5rem;
      background: linear-gradient(180deg, #FFFFFF 0%, #A7BFA9 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    .hero p {
      font-size: clamp(1.1rem, 2vw, 1.35rem);
      color: var(--text-muted);
      max-width: 680px;
      margin-bottom: 2.5rem;
    }
    .hero-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 1rem;
      justify-content: center;
    }
    .btn-primary {
      display: inline-flex;
      align-items: center;
      gap: 0.75rem;
      background: linear-gradient(135deg, #2D6A4F 0%, #1E4D2B 100%);
      color: white;
      text-decoration: none;
      padding: 1rem 2.2rem;
      border-radius: 0.85rem;
      font-weight: 700;
      font-size: 1.05rem;
      border: 1px solid rgba(78, 186, 116, 0.4);
      box-shadow: 0 10px 30px rgba(30, 77, 43, 0.5);
      transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
    }
    .btn-primary:hover {
      transform: translateY(-2px) scale(1.02);
      box-shadow: 0 15px 40px rgba(78, 186, 116, 0.45);
    }
    .btn-secondary {
      display: inline-flex;
      align-items: center;
      gap: 0.75rem;
      background: rgba(255, 255, 255, 0.05);
      color: white;
      text-decoration: none;
      padding: 1rem 2rem;
      border-radius: 0.85rem;
      font-weight: 600;
      border: 1px solid rgba(255, 255, 255, 0.12);
      transition: all 0.2s;
    }
    .btn-secondary:hover {
      background: rgba(255, 255, 255, 0.1);
      border-color: rgba(255, 255, 255, 0.25);
    }

    /* Section Headers */
    .section-wrap {
      max-width: 1120px;
      margin: 0 auto;
      padding: 5rem 1.5rem;
    }
    .section-badge {
      font-size: 0.8rem;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      color: var(--accent-gold);
      margin-bottom: 0.5rem;
    }
    .section-title {
      font-size: clamp(2rem, 4vw, 2.75rem);
      font-weight: 800;
      letter-spacing: -0.02em;
      margin-bottom: 1rem;
    }
    .section-subtitle {
      color: var(--text-muted);
      font-size: 1.1rem;
      max-width: 600px;
      margin-bottom: 3rem;
    }

    /* Bento Grid Layout */
    .bento-grid {
      display: grid;
      grid-template-columns: repeat(12, 1fr);
      gap: 1.5rem;
    }
    .bento-card {
      background: var(--surface);
      border: 1px solid var(--surface-border);
      border-radius: 1.25rem;
      padding: 2rem;
      position: relative;
      overflow: hidden;
      transition: transform 0.3s ease, border-color 0.3s ease;
    }
    .bento-card:hover {
      transform: translateY(-4px);
      border-color: rgba(78, 186, 116, 0.4);
    }
    .bento-card::after {
      content: "";
      position: absolute;
      top: 0; left: 0; right: 0; height: 1px;
      background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
    }
    .col-8 { grid-column: span 8; }
    .col-4 { grid-column: span 4; }
    .col-6 { grid-column: span 6; }
    .col-12 { grid-column: span 12; }
    @media (max-width: 860px) {
      .col-8, .col-4, .col-6 { grid-column: span 12; }
      .nav-links { display: none; }
    }

    .bento-icon {
      width: 48px;
      height: 48px;
      border-radius: 0.85rem;
      background: rgba(45, 106, 79, 0.2);
      border: 1px solid rgba(78, 186, 116, 0.3);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 1.5rem;
      color: var(--primary-bright);
    }
    .bento-card h3 {
      font-size: 1.35rem;
      font-weight: 700;
      margin-bottom: 0.75rem;
    }
    .bento-card p {
      color: var(--text-muted);
      font-size: 0.95rem;
    }

    /* Live Interactive Simulator Card */
    .simulator-box {
      background: var(--surface-elevated);
      border: 1px solid var(--surface-border);
      border-radius: 1.5rem;
      padding: 2.5rem;
      margin-top: 2rem;
      box-shadow: 0 30px 60px rgba(0,0,0,0.3);
    }
    .sim-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 1.25rem;
      margin-bottom: 1.5rem;
    }
    .sim-input-group label {
      display: block;
      font-size: 0.85rem;
      font-weight: 600;
      margin-bottom: 0.5rem;
      color: var(--text-muted);
    }
    .sim-input {
      width: 100%;
      background: rgba(0,0,0,0.3);
      border: 1px solid rgba(255, 255, 255, 0.15);
      padding: 0.85rem 1rem;
      border-radius: 0.65rem;
      color: white;
      font-size: 0.95rem;
      font-family: inherit;
      outline: none;
      transition: border 0.2s;
    }
    .sim-input:focus { border-color: var(--primary-bright); }
    .sim-checkbox {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 1rem;
      background: rgba(239, 68, 68, 0.08);
      border: 1px solid rgba(239, 68, 68, 0.25);
      border-radius: 0.75rem;
      cursor: pointer;
    }
    .sim-checkbox input { width: 18px; height: 18px; cursor: pointer; accent-color: var(--danger); }
    .sim-btn {
      width: 100%;
      background: linear-gradient(135deg, #2D6A4F, #1E4D2B);
      color: white;
      border: 1px solid rgba(78, 186, 116, 0.4);
      padding: 1rem;
      border-radius: 0.75rem;
      font-size: 1.05rem;
      font-weight: 700;
      cursor: pointer;
      box-shadow: 0 4px 15px rgba(30, 77, 43, 0.4);
      transition: all 0.2s;
    }
    .sim-btn:hover { background: #1E4D2B; }

    .sim-result {
      margin-top: 1.5rem;
      padding: 1.5rem;
      border-radius: 0.85rem;
      display: none;
      animation: fadeIn 0.3s ease;
    }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(6px); } to { opacity: 1; transform: translateY(0); } }
    .sim-result.blocked {
      background: var(--danger-bg);
      border: 1px solid rgba(239, 68, 68, 0.4);
      display: block;
    }
    .sim-result.allowed {
      background: rgba(45, 106, 79, 0.2);
      border: 1px solid rgba(78, 186, 116, 0.4);
      display: block;
    }

    /* Scenario Timeline */
    .timeline {
      position: relative;
      padding-left: 2.5rem;
      border-left: 2px solid var(--surface-border);
      margin-left: 1rem;
    }
    .timeline-item {
      position: relative;
      margin-bottom: 2.5rem;
    }
    .timeline-dot {
      position: absolute;
      left: calc(-2.5rem - 7px);
      top: 4px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: var(--primary-bright);
      box-shadow: 0 0 12px var(--primary-bright);
    }
    .timeline-item h4 {
      font-size: 1.15rem;
      font-weight: 700;
      margin-bottom: 0.35rem;
    }
    .timeline-item p { color: var(--text-muted); font-size: 0.95rem; }

    /* Download Banner */
    .download-banner {
      background: linear-gradient(135deg, #18221C 0%, #121A15 100%);
      border: 1px solid rgba(78, 186, 116, 0.3);
      border-radius: 2rem;
      padding: 4rem 2rem;
      text-align: center;
      position: relative;
      overflow: hidden;
      margin-top: 4rem;
    }
    .download-banner::before {
      content: "";
      position: absolute;
      top: -50%; left: -50%; width: 200%; height: 200%;
      background: radial-gradient(circle at center, rgba(78, 186, 116, 0.1) 0%, transparent 60%);
      pointer-events: none;
    }

    footer {
      border-top: 1px solid var(--surface-border);
      padding: 3rem 1.5rem;
      text-align: center;
      color: var(--text-muted);
      font-size: 0.9rem;
    }
  </style>
</head>
<body>
  <div class="bg-grid"></div>
  <div class="ambient-glow"></div>

  <!-- Navbar -->
  <nav class="navbar">
    <a href="#" class="nav-brand">
      <div class="nav-brand-icon">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
      </div>
      RationSaathi
    </a>
    <ul class="nav-links">
      <li><a href="#simulator">Safety Simulator</a></li>
      <li><a href="#features">Pillars</a></li>
      <li><a href="#scenario">Scenario</a></li>
      <li><a href="#download">Download APK</a></li>
    </ul>
    <a href="/download/RationSaathi.apk" class="nav-cta">⬇️ Download APK</a>
  </nav>

  <main>
    <!-- Hero Section with Three.js 3D Rendering Scene -->
    <section class="hero">
      <div class="hero-pill">
        <div class="dot"></div>
        <span>ONORC & NFSA 2013 Civic-Tech Architecture</span>
      </div>

      <!-- 3D Canvas Container -->
      <div class="canvas-container">
        <canvas id="three-canvas"></canvas>
      </div>

      <h1>Public Distribution,<br>Without the Runaround.</h1>
      <p>A mission-critical civic application designed for interstate migrant households to safely navigate foodgrain entitlements, simulate clerical record corrections without quota loss, and track statutory grievances.</p>

      <div class="hero-actions">
        <a href="/download/RationSaathi.apk" class="btn-primary">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
          Download Android APK (v1.0)
        </a>
        <a href="#simulator" class="btn-secondary">
          ⚡ Try Interactive Safety Simulator
        </a>
      </div>
    </section>

    <!-- Bento Grid Section -->
    <section class="section-wrap" id="features">
      <div class="section-badge">Core Capabilities</div>
      <h2 class="section-title">Built with Civic Safety Invariants</h2>
      <p class="section-subtitle">Designed specifically for seasonal workers who share a phone, experience intermittent connectivity, and navigate unfamiliar destination ration shops.</p>

      <div class="bento-grid">
        <div class="bento-card col-8">
          <div class="bento-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
          </div>
          <h3>Zero Entitlement Loss Guarantee</h3>
          <p>Before any modification is submitted to state ONORC portals, the safety engine calculates whether the operation severs active food grain allocations. Naive member deletions or unlinked identity updates are mathematically intercepted and blocked.</p>
        </div>

        <div class="bento-card col-4">
          <div class="bento-icon" style="color: var(--accent-gold); background: rgba(245, 158, 11, 0.15); border-color: rgba(245, 158, 11, 0.3);">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
          </div>
          <h3>Stale Stock Detection</h3>
          <p>Depleted commodity feeds older than 12 hours are marked stale. The system automatically routes users to authorized alternative shops with verified stock.</p>
        </div>

        <div class="bento-card col-4">
          <div class="bento-icon" style="color: #60A5FA; background: rgba(96, 165, 250, 0.15); border-color: rgba(96, 165, 250, 0.3);">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
          </div>
          <h3>Shared-Phone Privacy Wipe</h3>
          <p>One-tap session wipeout flushes in-memory profile caches, ensuring zero data leakage when a facilitator tablet or shared family phone changes hands.</p>
        </div>

        <div class="bento-card col-8">
          <div class="bento-icon" style="color: var(--danger); background: var(--danger-bg); border-color: rgba(239, 68, 68, 0.3);">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
          </div>
          <h3>21-Day Statutory Grievance Redressal</h3>
          <p>Calculates elapsed days against the mandatory 15-day citizen charter deadline. If unanswered, the engine automatically drafts a formal legal escalation citing Section 15 of NFSA 2013 to the District Grievance Redressal Officer (DGRO).</p>
        </div>
      </div>
    </section>

    <!-- Interactive Simulator Section -->
    <section class="section-wrap" id="simulator">
      <div class="section-badge">Live Interactive Test</div>
      <h2 class="section-title">Test the Safety Correction Simulator</h2>
      <p class="section-subtitle">Experience how the pre-submission blocking gate intercepts destructive edits before they sever citizen rations.</p>

      <div class="simulator-box">
        <div style="margin-bottom: 1.5rem; padding-bottom: 1rem; border-bottom: 1px solid var(--surface-border); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
          <div>
            <div style="font-size: 0.8rem; color: var(--text-muted);">Current Official Database Record</div>
            <strong style="font-size: 1.15rem; color: white;">Dev Prasad • Age: 24 • Quota: 5 kg/month</strong>
          </div>
          <span style="font-size: 0.8rem; background: rgba(245, 158, 11, 0.2); color: var(--accent-gold); padding: 0.3rem 0.8rem; border-radius: 9999px; border: 1px solid rgba(245, 158, 11, 0.4);">Aadhaar Mismatch Flagged</span>
        </div>

        <div class="sim-row">
          <div class="sim-input-group">
            <label>Proposed Name Correction</label>
            <input type="text" id="propName" class="sim-input" value="Devi Prasad">
          </div>
          <div class="sim-input-group">
            <label>Proposed Age</label>
            <input type="number" id="propAge" class="sim-input" value="26">
          </div>
        </div>

        <div style="margin-bottom: 1.5rem;">
          <label class="sim-checkbox">
            <input type="checkbox" id="delAttempt">
            <div>
              <strong style="color: var(--danger); font-size: 0.95rem;">Attempt naive member deletion to re-enrol later</strong>
              <div style="font-size: 0.8rem; color: var(--text-muted);">Simulate what happens when an operator tries to delete the mismatched member record.</div>
            </div>
          </label>
        </div>

        <button class="sim-btn" onclick="runSim()">Evaluate Correction Risk</button>

        <div id="simResult" class="sim-result"></div>
      </div>
    </section>

    <!-- Demonstration Journey Section -->
    <section class="section-wrap" id="scenario">
      <div class="section-badge">Evaluation Scenario</div>
      <h2 class="section-title">End-to-End Demonstration Journey</h2>
      <p class="section-subtitle">Six real-world public distribution challenges seeded deterministically in every test run.</p>

      <div class="timeline">
        <div class="timeline-item">
          <div class="timeline-dot"></div>
          <h4>1. Interstate Seasonal Migration Corridor</h4>
          <p>A 5-member household migrates from Saran (Chhapra), Bihar to Hadapsar, Pune, Maharashtra for construction work under One Nation One Ration Card (ONORC).</p>
        </div>
        <div class="timeline-item">
          <div class="timeline-dot"></div>
          <h4>2. Absent Head of Household</h4>
          <p>Head of household Ram Ashray Prasad is absent at a remote brick kiln. Adult spouse Sunita Devi is authorized for biometric/OTP e-PoS transactions.</p>
        </div>
        <div class="timeline-item">
          <div class="timeline-dot"></div>
          <h4>3. Identity Mismatch & Blocked Deletion</h4>
          <p>Son Devi Prasad has a database record spelling ("Dev Prasad", age 24) conflicting with Aadhaar ("Devi Prasad", age 26). The safety engine blocks deletion to preserve his 5 kg grain allocation.</p>
        </div>
        <div class="timeline-item">
          <div class="timeline-dot"></div>
          <h4>4. Stale Wheat Shortage & Alternative Routing</h4>
          <p>Destination Fair Price Shop #204 has rice available (420 kg), but wheat is depleted (18-hour stale report). System routes family to alternative FPS #118 nearby (1.4 km, 310 kg verified stock).</p>
        </div>
        <div class="timeline-item">
          <div class="timeline-dot"></div>
          <h4>5. 21-Day Grievance Escalation</h4>
          <p>Biometric auth failure grievance token #MH-PDS-2024-88392 exceeds the 15-day citizen charter limit by 6 days. Automated engine produces a formal DGRO legal escalation notice.</p>
        </div>
      </div>
    </section>

    <!-- Download Section -->
    <section class="section-wrap" id="download">
      <div class="download-banner">
        <div class="section-badge" style="color: var(--primary-bright);">Get The App</div>
        <h2 style="font-size: clamp(2rem, 5vw, 3rem); font-weight: 800; margin-bottom: 1rem;">Download RationSaathi Today</h2>
        <p style="color: var(--text-muted); max-width: 600px; margin: 0 auto 2rem; font-size: 1.1rem;">Install the native Android package on your device for offline draft management, instant language switching, and shared-device privacy.</p>
        
        <a href="/download/RationSaathi.apk" class="btn-primary" style="font-size: 1.2rem; padding: 1.1rem 2.8rem;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
          Download APK (~23 MB)
        </a>
        <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 1rem;">Direct browser download • Compatible with Android 7.0 and up</div>
      </div>
    </section>
  </main>

  <footer>
    <p>© 2026 RationSaathi. Civic-tech implementation under the National Food Security Act (NFSA) and One Nation One Ration Card (ONORC).</p>
  </footer>

  <!-- Three.js 3D Procedural Scene Script -->
  <script>
    window.addEventListener('DOMContentLoaded', () => {
      const canvas = document.getElementById('three-canvas');
      if (!canvas || typeof THREE === 'undefined') return;

      const scene = new THREE.Scene();
      const camera = new THREE.PerspectiveCamera(45, 1, 0.1, 100);
      camera.position.set(0, 0, 7);

      const renderer = new THREE.WebGLRenderer({ canvas, alpha: true, antialias: true });
      renderer.setSize(340, 340);
      renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

      // Lighting
      const ambientLight = new THREE.AmbientLight(0xffffff, 0.7);
      scene.add(ambientLight);

      const keyLight = new THREE.DirectionalLight(0xF59E0B, 2.0);
      keyLight.position.set(5, 5, 5);
      scene.add(keyLight);

      const fillLight = new THREE.DirectionalLight(0x4EBA74, 1.5);
      fillLight.position.set(-5, -3, 3);
      scene.add(fillLight);

      // Group holding the composite procedural model
      const mainGroup = new THREE.Group();

      // Outer Torus Ring (Protective verification ring)
      const ringGeo = new THREE.TorusGeometry(2.1, 0.08, 16, 100);
      const ringMat = new THREE.MeshStandardMaterial({
        color: 0x2D6A4F,
        metalness: 0.7,
        roughness: 0.2
      });
      const ring = new THREE.Mesh(ringGeo, ringMat);
      mainGroup.add(ring);

      // Inner Golden Sheaf Ring
      const goldRingGeo = new THREE.TorusGeometry(1.65, 0.06, 16, 80);
      const goldRingMat = new THREE.MeshStandardMaterial({
        color: 0xF59E0B,
        metalness: 0.8,
        roughness: 0.3
      });
      const goldRing = new THREE.Mesh(goldRingGeo, goldRingMat);
      goldRing.rotation.x = Math.PI / 4;
      mainGroup.add(goldRing);

      // Central Shield/Diamond Core
      const coreGeo = new THREE.OctahedronGeometry(1.2, 0);
      const coreMat = new THREE.MeshStandardMaterial({
        color: 0x1E4D2B,
        roughness: 0.2,
        metalness: 0.6,
        wireframe: false
      });
      const core = new THREE.Mesh(coreGeo, coreMat);
      mainGroup.add(core);

      // Golden Stalk Accents (8 surrounding petals)
      const stalkCount = 8;
      for (let i = 0; i < stalkCount; i++) {
        const angle = (i / stalkCount) * Math.PI * 2;
        const stalkGeo = new THREE.ConeGeometry(0.12, 0.7, 8);
        const stalkMat = new THREE.MeshStandardMaterial({
          color: 0xFBBF24,
          metalness: 0.7,
          roughness: 0.3
        });
        const stalk = new THREE.Mesh(stalkGeo, stalkMat);
        stalk.position.set(Math.cos(angle) * 1.3, Math.sin(angle) * 1.3, 0);
        stalk.rotation.z = angle - Math.PI / 2;
        mainGroup.add(stalk);
      }

      scene.add(mainGroup);

      // Floating Particle Dust
      const particleCount = 40;
      const particleGeo = new THREE.BufferGeometry();
      const positions = new Float32Array(particleCount * 3);
      for (let i = 0; i < particleCount * 3; i += 3) {
        positions[i] = (Math.random() - 0.5) * 6;
        positions[i+1] = (Math.random() - 0.5) * 6;
        positions[i+2] = (Math.random() - 0.5) * 4;
      }
      particleGeo.setAttribute('position', new THREE.BufferAttribute(positions, 3));
      const particleMat = new THREE.PointsMaterial({
        color: 0xF59E0B,
        size: 0.08,
        transparent: true,
        opacity: 0.7
      });
      const particles = new THREE.Points(particleGeo, particleMat);
      scene.add(particles);

      // Interaction & Mouse Tracking
      let mouseX = 0, mouseY = 0;
      window.addEventListener('mousemove', (e) => {
        const rect = canvas.getBoundingClientRect();
        mouseX = ((e.clientX - rect.left) / rect.width - 0.5) * 2;
        mouseY = -((e.clientY - rect.top) / rect.height - 0.5) * 2;
      });

      // Render Loop
      let clock = new THREE.Clock();
      function animate() {
        requestAnimationFrame(animate);
        const elapsed = clock.getElapsedTime();

        mainGroup.rotation.y = elapsed * 0.4 + mouseX * 0.4;
        mainGroup.rotation.x = Math.sin(elapsed * 0.3) * 0.2 + mouseY * 0.3;
        ring.rotation.z = elapsed * 0.2;
        goldRing.rotation.y = elapsed * -0.3;
        particles.rotation.y = elapsed * 0.05;

        renderer.render(scene, camera);
      }
      animate();
    });

    // Interactive Simulator Script
    function runSim() {
      const name = document.getElementById('propName').value.trim();
      const age = parseInt(document.getElementById('propAge').value);
      const isDel = document.getElementById('delAttempt').checked;
      const res = document.getElementById('simResult');

      if (isDel) {
        res.className = 'sim-result blocked';
        res.innerHTML = `
          <h4 style="color: var(--danger); font-size: 1.1rem; margin-bottom: 0.5rem; display: flex; align-items: center; gap: 0.5rem;">
            🛑 PRE-SUBMISSION GATE: OPERATION BLOCKED
          </h4>
          <p style="color: #FCA5A5; font-size: 0.95rem; margin-bottom: 0.75rem;">
            Attempting to delete 'Dev Prasad' will permanently sever an active monthly foodgrain quota of <strong>5 kg (3kg Rice + 2kg Wheat)</strong>. Re-adding deleted members requires fresh state biometric re-enrolment.
          </p>
          <div style="background: rgba(0,0,0,0.4); padding: 0.75rem 1rem; border-radius: 0.5rem; font-size: 0.85rem; color: #E5E7EB;">
            <strong>Mandatory Evidence Dossier:</strong> Form 4A Clerical Correction Declaration + Panchayat Residence Certificate + Aadhaar e-KYC Seed Token.
          </div>
        `;
      } else if (name !== 'Dev Prasad' && Math.abs(age - 24) >= 2) {
        res.className = 'sim-result blocked';
        res.innerHTML = `
          <h4 style="color: var(--danger); font-size: 1.1rem; margin-bottom: 0.5rem; display: flex; align-items: center; gap: 0.5rem;">
            ⚠️ DUAL IDENTITY & AGE CONFLICT DETECTED
          </h4>
          <p style="color: #FCA5A5; font-size: 0.95rem; margin-bottom: 0.75rem;">
            Modifying both name ('Dev Prasad' ➔ '${name}') and age (24 ➔ ${age}) triggers an interstate anti-fraud flag in ONORC databases. Direct submission without linked evidence threatens temporary suspension of the 5 kg monthly quota.
          </p>
          <div style="background: rgba(0,0,0,0.4); padding: 0.75rem 1rem; border-radius: 0.5rem; font-size: 0.85rem; color: #E5E7EB;">
            <strong>Safe Resolution:</strong> Proceed via 'Assisted Correction Draft' mode with Form 4A and joint affidavit.
          </div>
        `;
      } else {
        res.className = 'sim-result allowed';
        res.innerHTML = `
          <h4 style="color: var(--primary-bright); font-size: 1.1rem; margin-bottom: 0.5rem;">
            ✅ SAFE CLERICAL RECTIFICATION
          </h4>
          <p style="color: #D1FAE5; font-size: 0.95rem;">
            No entitlement loss risk detected. The correction does not alter active household foodgrain quotas. Eligible for standard single-point e-KYC update.
          </p>
        `;
      }
    }
  </script>
</body>
</html>
"""

class Handler(BaseHTTPRequestHandler):
    def do_HEAD(self):
        apk_file = find_apk()
        if self.path in ["/download/RationSaathi.apk", "/app-debug.apk", "/download", "/RationSaathi.apk"]:
            if apk_file and os.path.exists(apk_file):
                self.send_response(200)
                self.send_header("Content-Type", "application/vnd.android.package-archive")
                self.send_header("Content-Disposition", 'attachment; filename="RationSaathi.apk"')
                self.send_header("Content-Length", str(os.path.getsize(apk_file)))
                self.end_headers()
            else:
                self.send_response(404)
                self.end_headers()
        else:
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            self.send_header("Content-Length", str(len(HTML_PAGE.encode('utf-8'))))
            self.end_headers()

    def do_GET(self):
        apk_file = find_apk()
        if self.path in ["/download/RationSaathi.apk", "/app-debug.apk", "/download", "/RationSaathi.apk"]:
            if apk_file and os.path.exists(apk_file):
                self.send_response(200)
                self.send_header("Content-Type", "application/vnd.android.package-archive")
                self.send_header("Content-Disposition", 'attachment; filename="RationSaathi.apk"')
                self.send_header("Content-Length", str(os.path.getsize(apk_file)))
                self.end_headers()
                with open(apk_file, "rb") as f:
                    while chunk := f.read(65536):
                        self.wfile.write(chunk)
            else:
                self.send_response(404)
                self.end_headers()
                self.wfile.write(b"APK build file not found. Please compile with gradle assembleDebug.")
        else:
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            self.send_header("Content-Length", str(len(HTML_PAGE.encode('utf-8'))))
            self.end_headers()
            self.wfile.write(HTML_PAGE.encode('utf-8'))

    def log_message(self, format, *args):
        pass

if __name__ == "__main__":
    print(f"Starting RationSaathi Scrollable Website & APK server on port {PORT}...")
    httpd = HTTPServer(("0.0.0.0", PORT), Handler)
    httpd.serve_forever()
