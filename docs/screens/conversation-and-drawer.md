# Conversation, Variants In Stream, Composer, and Navigation Drawer

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (Main launcher and `/c/` deep-link routing)
  - `_decompiled_ref/res/values/strings.xml` (`chat_list_search_placeholder`, `conversations_variants_in_stream_title`, `composer_placeholder`, `model_picker_*`, `ads_carousel_*`, `chat_share_privacy_notice`)
  - `_decompiled_ref/res/values/dimens.xml` (`drawer_container_width = 360dp`, `drawer_corner_size = 16dp`, `search_bar_height = 56dp`)
  - `_decompiled_ref/sources/p000/h110.java` (Composer mode chips and reasoning selector)
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/CoreConversationScreens.kt` (`NavigationDrawerContent`, `ConversationScreen`)

## Reconstructed Components

1. **Modal Navigation Drawer (`360.dp` width, `16.dp` end corners):**
   - Search input (`Search chats`) filtering conversations in real time.
   - Quick-action rows (`New chat`, `Temporary chat` toggle).
   - Categorized route jumpers across Core (F2), Library & Settings (F3), and Beta/Experimental (F4) destinations.
   - Pinned and recent conversation threads with locked-vault indicators (`PIN`) and delete actions.
   - Account footer with active subscription badge and quick link to `Experiment Override & Inspector`.
2. **Main Conversation Stream (`ConversationScreen`):**
   - Active mode banner (`Temporary chat won't appear in history or update memory`) and active Space context indicator.
   - Model selector bar (`Open Chat 5.4 Thinking`, `o3-pro Deep Research`, `Open Chat 4.5 Instant`, `4o Multimodal`, `4.1 Coding`, `o4-mini Fast`) and reasoning effort selector (`Standard`, `Light`, `Medium`, `Extended`, `High`).
   - Expandable `Reasoning Trace` (`Thought for 6s`) accordion and `Code Execution` terminal card (`c2h0.java`).
   - **Variants In Stream:** Side-by-side response comparison cards (`Variant A` vs `Variant B`) with interactive `Choose this response` buttons.
   - Web search citation pills (` AndroidX Compose 1.10`, `Apktool 3.0.3`, `JADX 1.5.6`) and message action bar (`Copy`, `Read aloud`, `Good response`, `Bad response`, `Regenerate`).
   - Contextual `Sponsored` ad card (`ads_carousel_title`) with `Hide ad` dismissal when `ads_in_stream_contextual` is enabled.
3. **Multi-Mode Composer:**
   - Horizontal mode chips (`Chat`, `Deep Research`, `Create Image`, `Web Search`, `Canvas`, `Study Together`, `Agent Mode`).
   - Quick attachment shortcuts (`Camera & Sketch`, `Pages & Files`), prompt text input, `Send`, and `Realtime Voice` trigger.

## Definition of Done (DoD) Verification

- [x] Layout hierarchy, spacing, and corner radii match `_decompiled_ref/res/values/dimens.xml`.
- [x] Colors, typography, and custom `ds_*` vector icons come from `io.openchat.ui.theme`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero hardcoded business logic outside `LocalMockRepository` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
