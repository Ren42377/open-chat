# Open Chat

[![CI](https://github.com/Ren42377/open-chat/actions/workflows/ci.yml/badge.svg)](https://github.com/Ren42377/open-chat/actions/workflows/ci.yml)
[![Decompile Reference APK](https://github.com/Ren42377/open-chat/actions/workflows/decompile.yml/badge.svg)](https://github.com/Ren42377/open-chat/actions/workflows/decompile.yml)

Open Chat (`io.openchat`) is an open-source, clean-room Android UI/UX reconstruction written in Kotlin and Jetpack Compose. Every screen, component hierarchy, color token, typography scale, spatial spring animation specification, and branching state is driven by local mock repositories (`LocalMockRepository`) and runtime state controllers (`FakeStateController`) so the entire interface can be explored and inspected offline without a live backend or account login.

## Legal Disclaimer

- **Clean-Room Reconstruction:** This repository contains only clean-room Kotlin, Jetpack Compose, and XML source code written from scratch. It does not contain, distribute, or store any decompiled source code or proprietary binary assets.
- **No Affiliation:** This project is independent and is not affiliated with, endorsed by, or sponsored by OpenAI.
- **Trademark Notice:** "ChatGPT" and related marks are trademarks of OpenAI. This project uses the name **Open Chat** (`io.openchat`) and original launcher branding (`ds_openchat_emblem_24.xml`) to prevent brand confusion.
- **Independent Reference Supply:** Any reference APK processed by the cloud decompilation pipeline is provided independently by the repository owner for personal reference and development verification only.

## Project Highlights

- **100% Cloud-Compiled Workflow:** No local JDK, Android SDK, NDK, or Gradle installation is required or used on contributor machines. All compilation, lint verification, unit testing, and APK packaging run exclusively in GitHub Actions.
- **Deterministic Reference Extraction:** `.github/workflows/decompile.yml` runs pinned versions of Apktool (`3.0.3`) and JADX (`1.5.6`) in GitHub Actions to produce downloadable reference artifacts (`decompiled_ref-manifest-res` and `decompiled_ref-sources`) kept strictly in `.gitignore` (`_decompiled_ref/`).
- **Complete 14-Destination Feature Coverage:** Implements all core, library, settings, and hidden/experimental screens discovered in the reference APK, backed by 24 toggleable feature flags (all enabled by default) and 48 clean-room `ds_*` vector icons.
- **Runtime State Simulation & Experiment Override:** Every screen supports live switching between `Success`, `Loading`, `Empty`, and `Error` states and `Free`, `Go`, `Plus`, `Pro`, `Business`, and `Enterprise` subscription tiers via the top `QuickStateSwitcherBar` or the full `Experiment Override & Inspector` screen (`f6v.java`).

## Reconstructed Screens & Deep Links

| Phase | Screen / Surface | Route (`OpenChatDestination`) | Deep Link | Key Capabilities |
|---|---|---|---|---|
| **F2** | Chat, Variants In Stream & Drawer | `CONVERSATION` | `/c/` | Modal drawer (`360.dp`), model & reasoning effort picker, `Variants In Stream`, code output, citations, sponsored ad card, 7-mode composer |
| **F2** | Realtime Voice & Voice Debug | `VOICE_CALL` | `/voice` | Animated `#0285FF` Voice Orb, 7 voices (`Sol`, `Spruce`, `Cove`, `Ember`, `Breeze`, `Juniper`, `Vale`), Live Camera, WebRTC `Voice Debug` overlay |
| **F2** | Camera, Sketch & Stickers | `CAMERA_SKETCH` | `/sketch` | Interactive freehand drawing `Canvas`, 6 brush colors, aspect ratio selector (`1:1`, `3:4`, `4:3`), sticker maker |
| **F3** | Library, Pages & Sites Lite | `ARTIFACT_LIBRARY` | `/library` | File/Folder organizer, collaborative `Pages`, published `Sites Lite` analytics (`Visitors`, `Page views`, `Traffic sources`, `Env secrets`) |
| **F3** | Explore GPTs, Skills & Bazaar | `DISCOVERY_HUB` | `/discovery-hub` | Gizmo Store categories, ratings, and OAuth/Bazaar connector toggles (`GitHub`, `Google Drive`, `Notion`, `Slack`, `Linear`) |
| **F3** | Spaces (Snorlax) & Automations | `SPACES_AND_TASKS` | `/spaces/` | Project workspaces with custom instructions/sources, plus `Scheduled Tasks` (`AutomationsScreen`) |
| **F3** | Settings, Memory & Clinician Mode | `SETTINGS_AND_PROFILE` | `/settings` | `System`/`Light`/`Dark` themes, 12 accent colors, `Memory` summary, `Custom Dictionary`, `Data Controls`, `Clinician Mode` (HIPAA), active sessions |
| **F3** | Plans, Passkeys & MFA | `AUTH_AND_SUBSCRIPTION` | `/subscription` | Interactive switcher for all 6 tiers (`Free` to `Enterprise`), Passkey manager, OTP & TOTP authenticator setup |
| **F4** | Codex Cloud, Remote Next & Pets | `CODEX_REMOTE` | `/codex/remote-next` | Desktop QR/code pairing (`m1v`, `bmp0`), all 9 ASCII `Codex Companion Pets`, sandbox command approval, remote slash commands |
| **F4** | Orbit Proactive Dots (Tibro) | `ORBIT_TIBRO` | `/dots` | `Dots`, `Ideas`, and `You` tabs, avatar style customizer, `Always do` / `Never do` agent rules |
| **F4** | Open Chat with Friends (Calpico) | `CALPICO_SOCIAL` | `/calpico` | Multi-user group threads, magic invite link sharing, message requests, assistant auto-response toggle |
| **F4** | Finances, Wallet, Shopping & Sports | `FINANCE_AND_VERTICALS` | `/finances` | Net worth dashboard, Wallet instant checkout approval, Shopping Virtual Try-On, Live Sports (`/football`) |
| **F4** | Locked Chats Vault & Family Safety | `LOCKED_CHATS_AND_SAFETY` | `/parentalcontrols` | Account PIN vault (`2468`), `Lockdown Mode`, `Daybreak` break reminders, `Parental Controls`, `Trusted Contact` |
| **F4** | Experiment Override & Inspector | `EXPERIMENT_INSPECTOR` | `/experiments` | Clean-room `f6v.java` override console: 24 feature flags, 14-route jumper, state/tier overrides, 48-icon catalog |

## Quickstart

Prerequisites on your local machine:
1. `git`
2. GitHub CLI (`gh`), authenticated via `gh auth login`

Clone the repository and inspect the latest CI build:

```bash
git clone https://github.com/Ren42377/open-chat.git
cd open-chat
gh run list --limit 5
```

Download the latest debug APK built by GitHub Actions:

```bash
gh run download --name open-chat-debug --dir ./build-artifacts
```

Download the decompiled reference artifacts into the ignored `_decompiled_ref/` directory:

```bash
gh run download <decompile-run-id> -n decompiled_ref-manifest-res -D _decompiled_ref/
gh run download <decompile-run-id> -n decompiled_ref-sources -D _decompiled_ref/
```

## Documentation Index

- [Getting Started (CI-Driven Setup)](docs/getting-started.md)
- [Contributing Guidelines](docs/contributing.md)
- [Mock Data and Feature Flag Architecture](docs/mock-data.md)
- [Architecture Decision Records (ADRs)](docs/adr/)
- [Screen Reconstruction Checklists](docs/screens/)

## Roadmap Status

| Phase | Scope | Status |
|---|---|---|
| **F0 - Foundation** | Repository setup, `.gitignore`, `ci.yml`, `decompile.yml`, base documentation, MIT license | Completed |
| **F1 - Design System** | Extracted color palette, 12 accents, typography, shapes, dimensions, spatial springs, 48 `ds_*` vector icons, `LocalMockRepository`, `FakeStateController` | Completed |
| **F2 - Core Screens** | Navigation drawer, conversation stream, `Variants In Stream`, 7-mode composer, Realtime Voice (`Voice Debug`), Camera & Sketch studio | Completed |
| **F3 - Library and Settings** | Artifact Library, `Pages`, `Sites Lite` analytics, Discovery Hub (`Gizmos` & `Bazaar`), `Spaces`, `Scheduled Tasks`, Settings, `Clinician Mode`, Subscriptions, Passkeys | Completed |
| **F4 - Hidden and Beta Screens** | `Codex Remote Next`, 9 `Companion Pets`, `Orbit Dots` (`Tibro`), `Calpico`, `Finances`, `Wallet`, `Shopping Try-On`, `Sports`, `Locked Chats`, `Parental Controls`, `Experiment Override` | Completed |
| **F5 - Final Audit** | Complete screen DoD verification, unit tests, lint clean sweep, and documentation finalization | Completed |

## License

Released under the [MIT License](LICENSE).
