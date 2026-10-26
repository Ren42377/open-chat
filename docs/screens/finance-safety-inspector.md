# Finances, Wallet, Shopping, Sports, Locked Chats, Safety, and Inspector

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (`/finances`, `/shopping`, `/football`, `/parentalcontrols` deep-link routes)
  - `_decompiled_ref/res/values/strings.xml` (`finances_*`, `wallet_*`, `shopping_try_on_*`, `locked_chats_*`, `lockdown_mode_*`, `daybreak_*`, `parental_controls_*`, `trusted_contact_*`, `experiment_override_*`)
  - `_decompiled_ref/sources/p000/f6v.java` (`Experiment Override` debug screen)
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/ExperimentalAndInspectorScreens.kt` (`FinanceAndVerticalsScreen`, `LockedChatsAndSafetyScreen`, `ExperimentInspectorScreen`)

## Reconstructed Components

1. **Personal Finances, Wallet, Shopping Try-On & Live Sports (`FinanceAndVerticalsScreen`):**
   - **Finances Net Worth Dashboard:** Total net worth (`$186,740.00`), 30-day delta (`+$4,120.00`), linked institution cards (`Checking & Savings`, `Brokerage`, `Retirement`, `Credit Cards`), and `Refresh all` sync action.
   - **Wallet & Instant Checkout:** Saved payment cards with interactive `Approve $20.00` one-tap checkout confirmation and `Add card` form.
   - **Shopping Virtual Try-On:** Reference photo selector (`Selfie reference photo`, `Full-body studio reference`, `Outdoor daylight reference`) and product try-on cards (`Generate try-on`).
   - **Live Sports Hub (`/football`):** Match scoreboard cards (`Arsenal vs Bayern Munich`, `Real Madrid vs Manchester City`, `Barcelona vs Inter Milan`) with tactical win-probability breakdowns.
2. **Locked Chats Vault, Lockdown Mode & Family Safety (`LockedChatsAndSafetyScreen`):**
   - **Locked Chats PIN Vault:** 4-digit Account PIN verification (`2468` demo PIN) unlocking private conversation threads (`Locked Chats`), plus `Lock vault` relocking action.
   - **Lockdown Mode & Daybreak:** High-security `Lockdown Mode` toggle (restricting external link previews and unverified connectors) and `Daybreak` break reminder toggle (`daybreak_take_a_break`).
   - **Parental Controls & Trusted Contact:** Teen account guardrails (`Quiet hours`, `Content guardrails`, `Image generation filter`) and `Trusted Contact` safety alert configuration.
3. **Experiment Override & State Inspector (`ExperimentInspectorScreen`):**
   - Clean-room reconstruction of `f6v.java` (`Experiment Override`).
   - Global UI state switcher (`Success`, `Loading`, `Empty`, `Error`), subscription tier switcher (`Free`, `Go`, `Plus`, `Pro`, `Business`, `Enterprise`), and `Reset All Mock State`.
   - Direct route jumper to all 14 `OpenChatDestination` screens with phase badges (`F2`, `F3`, `F4`) and deep-link paths.
   - Searchable feature flag registry for all 24 flags (`Enable All 24`) and visual catalog of all 48 `ds_*` vector icons.

## Definition of Done (DoD) Verification

- [x] Finances, Wallet, Shopping Try-On, Sports, Locked Chats PIN Vault, Parental Controls, Trusted Contact, and Experiment Override match `_decompiled_ref/res/values/strings.xml` and `f6v.java`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
