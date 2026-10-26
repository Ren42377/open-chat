# Open Chat

[![CI](https://github.com/Ren42377/open-chat/actions/workflows/ci.yml/badge.svg)](https://github.com/Ren42377/open-chat/actions/workflows/ci.yml)
[![Decompile Reference APK](https://github.com/Ren42377/open-chat/actions/workflows/decompile.yml/badge.svg)](https://github.com/Ren42377/open-chat/actions/workflows/decompile.yml)

Open Chat is an open-source, clean-room Android UI/UX reconstruction written in Kotlin and Jetpack Compose. Every screen, component hierarchy, color token, typography scale, animation specification, and branching state is driven by local mock repositories and runtime state controllers so the entire interface can be explored and inspected offline without a live backend or account login.

## Legal Disclaimer

- **Clean-Room Reconstruction:** This repository contains only clean-room Kotlin, Jetpack Compose, and XML source code written from scratch. It does not contain, distribute, or store any decompiled source code or proprietary binary assets.
- **No Affiliation:** This project is independent and is not affiliated with, endorsed by, or sponsored by OpenAI.
- **Trademark Notice:** "ChatGPT" and related marks are trademarks of OpenAI. This project uses the name **Open Chat** (`io.openchat`) and original launcher branding to prevent brand confusion.
- **Independent Reference Supply:** Any reference APK processed by the cloud decompilation pipeline is provided independently by the repository owner for personal reference and development verification only.

## Project Highlights

- **100% Cloud-Compiled Workflow:** No local JDK, Android SDK, NDK, or Gradle installation is required or used on contributor machines. All compilation, lint verification, unit testing, and APK packaging run exclusively in GitHub Actions.
- **Deterministic Reference Extraction:** `.github/workflows/decompile.yml` runs pinned versions of Apktool (`3.0.3`) and JADX (`1.5.6`) in GitHub Actions to produce downloadable reference artifacts (`decompiled_ref-manifest-res` and `decompiled_ref-sources`) kept strictly in `.gitignore` (`_decompiled_ref/`).
- **Offline Mock Architecture:** All network calls, authentication sessions, subscription tiers, and experimental feature flags are backed by `LocalMockRepository`, `FakeStateController`, and an interactive In-App Inspector so every screen state (`loading`, `empty`, `error`, `success`, and experimental flags) can be toggled at runtime.

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
| **F1 - Design System** | Extracted color palette, typography, shapes, dimensions, and custom Compose theme | Pending `_decompiled_ref/` extraction |
| **F2 - Core Screens** | Chat drawer/list, conversation screen, message composer, voice and attachment sheets | Pending F1 |
| **F3 - Library and Settings** | Image library, GPT exploration, user settings, account and subscription sheets | Pending F2 |
| **F4 - Hidden and Beta Screens** | Experimental feature screens and runtime In-App Inspector / Debug Switcher | Pending F3 |
| **F5 - Final Audit** | Complete screen DoD verification, lint clean sweep, and documentation finalization | Pending F4 |

## License

Released under the [MIT License](LICENSE).
