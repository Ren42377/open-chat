# Mock Data and Feature Flag Architecture

Open Chat replaces all network clients (REST APIs, Server-Sent Events, WebSockets, authentication providers, billing clients, and telemetry SDKs) with deterministic in-memory mock repositories and runtime state controllers located in `app/src/main/java/io/openchat/data/mock/`.

## Architectural Components

### 1. `LocalMockRepository` (`LocalMockRepository.kt`)

`LocalMockRepository` serves as the single offline source of truth for application data:
- **Conversations and Messages:** Multi-turn threads featuring expandable `Reasoning Trace` accordions, `Code Execution` terminal outputs (`c2h0.java`), side-by-side `Variants In Stream` comparison cards, web search citations, and PIN-locked threads.
- **Models and Capabilities:** Supplies 6 selectable models (`Open Chat 5.4 Thinking`, `o3-pro Deep Research`, `Open Chat 4.5 Instant`, `4o Multimodal`, `4.1 Coding`, `o4-mini Fast`), 5 reasoning effort levels (`Standard`, `Light`, `Medium`, `Extended`, `High`), and 7 composer modes.
- **Library, Pages, and Sites Lite:** Supplies categorized files, collaborative `Pages`, and published `Sites Lite` web apps with traffic analytics (`Visitors`, `Page views`, `Likes`, `Forks`) and environment secrets.
- **Discovery Hub, Gizmos, and Spaces:** Supplies custom assistants (`Gizmos`), 5 OAuth/Bazaar connectors (`GitHub`, `Google Drive`, `Notion`, `Slack`, `Linear`), `Spaces` (`Snorlax` project workspaces), and `Scheduled Tasks` (`AutomationsScreen`).
- **Codex, Orbit, Calpico, Finance, and Safety:** Supplies Codex remote sessions, all 9 ASCII `Codex Companion Pets`, Orbit `Dots` (`Tibro`), `Calpico` social group threads, `Finances` net-worth accounts, `Wallet` cards, `Shopping` virtual try-on products, `Live Sports` (`/football`) matches, `Parental Controls`, and `Trusted Contacts`.

### 2. `FakeStateController` (`FakeStateController.kt`)

`FakeStateController` exposes a reactive `StateFlow<AppSnapshot>` and mutation actions for controlling UI branches across every screen:
- **Screen Data State (`UiSimState`):** Switch any screen between `SUCCESS`, `LOADING`, `EMPTY`, and `ERROR` states at runtime via `StateSimulationHost` or the persistent top `QuickStateSwitcherBar`.
- **Subscription Tier Override (`SubscriptionTier`):** Switch between `FREE`, `GO`, `PLUS`, `PRO`, `BUSINESS`, and `ENTERPRISE` tiers at runtime.
- **Theme and Accent Palette:** Switch between `SYSTEM`, `LIGHT`, and `DARK` modes and all 12 personalization accent colors (`Blue`, `Green`, `Yellow`, `Red`, `Orange`, `Purple`, `Pink`, `Black`, `Gray`, `Sunset`, `Terminal`, `Glow`).

### 3. In-App Inspector (`ExperimentInspectorScreen`)

Reconstructed from `_decompiled_ref/sources/p000/f6v.java` (`Experiment Override`), the Inspector screen is accessible from the top app bar flask icon on every screen or via the navigation drawer. It provides:
- Instant route jumping across all 14 `OpenChatDestination` screens.
- Global `UiSimState` and `SubscriptionTier` overrides.
- Searchable toggle switches for all 24 experimental feature flags (`Enable All 24` and `Reset All Mock State`).
- Visual catalog of all 48 `ds_*` vector drawables.

## Feature Flag Registry (Default: All 24 Enabled)

All 24 feature flags discovered in `_decompiled_ref/` default to `true` (`enabled = true`) in `LocalMockRepository.defaultFeatureFlags()` so every beta and experimental surface is unlocked out of the box:

| Key | Title | Category | Decompiled Reference |
|---|---|---|---|
| `codex_remote_next_enabled` | Codex Cloud & Remote Next | Codex & Engineering | `AndroidManifest.xml#L351`, `m1v.java`, `bmp0.java` |
| `codex_companion_pets_enabled` | Codex Companion Pets | Codex & Engineering | `res/values/strings.xml#L1272-L1324` |
| `orbit_tibro_dots_enabled` | Orbit / Tibro Proactive Dots | Proactive Agents | `AndroidManifest.xml#L372`, `strings.xml#L4424-L4789` |
| `calpico_group_chats_enabled` | Calpico Group Chats | Social & Collaboration | `res/values/strings.xml#L513-L774`, `h110.java` |
| `sites_lite_hosting_analytics` | Sites Lite Hosting & Analytics | Library & Artifacts | `res/values/strings.xml#L7087-L7235` |
| `pages_collaborative_docs` | Pages & Collaborative Docs | Library & Artifacts | `res/values/strings.xml#L3293-L3376` |
| `personal_finances_dashboard` | Personal Finances Dashboard | Verticals & Commerce | `AndroidManifest.xml#L365`, `strings.xml#L3377-L3406` |
| `wallet_instant_checkout` | Wallet & Instant Checkout | Verticals & Commerce | `res/values/strings.xml#L7917-L7966` |
| `shopping_virtual_try_on` | Shopping & Virtual Try-On | Verticals & Commerce | `AndroidManifest.xml#L360`, `strings.xml#L6891-L6924` |
| `live_sports_football_hub` | Live Sports (/football) | Verticals & Commerce | `AndroidManifest.xml#L356` |
| `locked_chats_account_pin` | Locked Chats & Account PIN | Privacy & Safety | `res/values/strings.xml#L3976-L4010` |
| `lockdown_mode_protection` | Lockdown Mode | Privacy & Safety | `res/values/strings.xml#L3971-L3974` |
| `clinician_mode_hipaa` | Clinician Mode (Healthcare) | Privacy & Safety | `res/values/strings.xml#L1270-L1271` |
| `parental_controls_family` | Parental Controls & Guardrails | Privacy & Safety | `AndroidManifest.xml#L369`, `strings.xml#L5610-L5630` |
| `trusted_contact_alerts` | Trusted Contact Safety Alerts | Privacy & Safety | `res/values/strings.xml#L7289-L7350` |
| `daybreak_wellbeing_prompts` | Daybreak Wellbeing Reminders | Privacy & Safety | `res/values/strings.xml#L2446-L2447` |
| `conversations_variants_in_stream` | Variants In Stream | Core Conversation | `res/values/strings.xml#L2240-L2246` |
| `study_together_socratic_mode` | Study Together Mode | Core Conversation | `res/values/strings.xml#L1943` |
| `deep_research_multi_step` | Deep Research Agent | Core Conversation | `res/values/strings.xml#L1767` |
| `ads_in_stream_contextual` | Sponsored Contextual Ads & Controls | Monetization & Privacy | `res/values/strings.xml#L225-L299` |
| `voice_debug_overlay` | Realtime Voice Debug Overlay | Voice & Multimodal | `res/values/strings.xml#L7743-L7751`, `h0w0.java` |
| `voice_live_camera_sharing` | Live Camera in Voice Mode | Voice & Multimodal | `res/values/strings.xml#L7790-L7815` |
| `snorlax_project_spaces` | Spaces (Snorlax Projects) | Productivity | `AndroidManifest.xml#L317`, `strings.xml#L6960-L6995` |
| `scheduled_automations_tasks` | Scheduled Tasks & Automations | Productivity | `AndroidManifest.xml#L321`, `AutomationsScreen` |
