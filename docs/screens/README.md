# Screen Reconstruction Checklists

Each document in this directory tracks the clean-room reconstruction status, decompiled reference mapping, and Definition of Done (DoD) verification checklist for the 14 application destinations and modal drawers reconstructed from `_decompiled_ref/`.

## Screen Index

| Phase | Destination (`OpenChatDestination`) | Deep Link | Checklist Document | Status |
|---|---|---|---|---|
| **F2** | `CONVERSATION` + `NavigationDrawerContent` | `/c/` | [conversation-and-drawer.md](conversation-and-drawer.md) | Completed |
| **F2** | `VOICE_CALL` | `/voice` | [voice-and-camera-sketch.md](voice-and-camera-sketch.md) | Completed |
| **F2** | `CAMERA_SKETCH` | `/sketch` | [voice-and-camera-sketch.md](voice-and-camera-sketch.md) | Completed |
| **F3** | `ARTIFACT_LIBRARY` | `/library` | [library-discovery-spaces.md](library-discovery-spaces.md) | Completed |
| **F3** | `DISCOVERY_HUB` | `/discovery-hub` | [library-discovery-spaces.md](library-discovery-spaces.md) | Completed |
| **F3** | `SPACES_AND_TASKS` | `/spaces/` | [library-discovery-spaces.md](library-discovery-spaces.md) | Completed |
| **F3** | `SETTINGS_AND_PROFILE` | `/settings` | [settings-and-subscription.md](settings-and-subscription.md) | Completed |
| **F3** | `AUTH_AND_SUBSCRIPTION` | `/subscription` | [settings-and-subscription.md](settings-and-subscription.md) | Completed |
| **F4** | `CODEX_REMOTE` | `/codex/remote-next` | [codex-orbit-calpico.md](codex-orbit-calpico.md) | Completed |
| **F4** | `ORBIT_TIBRO` | `/dots` | [codex-orbit-calpico.md](codex-orbit-calpico.md) | Completed |
| **F4** | `CALPICO_SOCIAL` | `/calpico` | [codex-orbit-calpico.md](codex-orbit-calpico.md) | Completed |
| **F4** | `FINANCE_AND_VERTICALS` | `/finances` | [finance-safety-inspector.md](finance-safety-inspector.md) | Completed |
| **F4** | `LOCKED_CHATS_AND_SAFETY` | `/parentalcontrols` | [finance-safety-inspector.md](finance-safety-inspector.md) | Completed |
| **F4** | `EXPERIMENT_INSPECTOR` | `/experiments` | [finance-safety-inspector.md](finance-safety-inspector.md) | Completed |

## Definition of Done (DoD) Criteria

Every screen in the table above satisfies all five mandatory verification checks:

1. Layout hierarchy, padding, and corner radii match `_decompiled_ref/res/values/dimens.xml`.
2. Colors, typography, and custom `ds_*` vector icons come from `io.openchat.ui.theme` (`Color.kt`, `Type.kt`, `Dimens.kt`, `Shape.kt`, `Motion.kt`, `OpenChatIcons.kt`).
3. Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
4. Zero business logic or hardcoded state outside `LocalMockRepository` and `FakeStateController`.
5. Zero lint warnings and unit tests passing on GitHub Actions CI.
