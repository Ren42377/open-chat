# Settings, Personalization, Memory, Clinician Mode, and Subscriptions

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (`/settings`, `/subscription` deep-link routes)
  - `_decompiled_ref/res/values/strings.xml` (`settings_*`, `personalization_color_*`, `memory_*`, `custom_dictionary_*`, `clinician_mode_*`, `passkeys_*`, `mfa_*`)
  - `_decompiled_ref/res/values/colors.xml` and `_decompiled_ref/res/values-night/colors.xml`
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/LibraryAndSettingsScreens.kt` (`SettingsAndProfileScreen`, `AuthAndSubscriptionScreen`)

## Reconstructed Components

1. **Settings, Personalization, Memory & Data Controls (`SettingsAndProfileScreen`):**
   - **Appearance & 12 Accent Colors:** Runtime switcher for `System`, `Light`, and `Dark` theme modes plus all 12 personalization accent colors (`Blue`, `Green`, `Yellow`, `Red`, `Orange`, `Purple`, `Pink`, `Black`, `Gray`, `Sunset`, `Terminal`, ` Glow`).
   - **Profile & Fun Facts:** Editable `Full name`, `Username`, `What do you do?`, `Custom instructions`, and `Fun fact` card.
   - **Memory Summary & Custom Dictionary:** `Reference saved memories` switch, memory item list with delete/add actions, and `Custom dictionary` chip editor for voice transcription accuracy.
   - **Data Controls, Ads & Clinician Mode:** Switches for `Improve the model for everyone`, `Include audio recordings`, `Include video recordings`, `Contextual ads in stream`, `Saved ad topics`, and **Clinician Mode (HIPAA Workspace)** (`clinician_mode_enabled`) which disables model training and redacts PHI.
   - **Active Security Sessions:** List of logged-in devices with `Log out` revocation for non-current sessions.
2. **Plans, Passkeys & Account Switcher (`AuthAndSubscriptionScreen`):**
   - Interactive tier cards for all 6 reference plans (`Free`, `Go`, `Plus`, `Pro`, `Business`, `Enterprise`) with feature checklists and immediate tier switching.
   - **Passkeys & Multi-Factor Authentication:** Passkey credential status (`Google Password Manager (Pixel 9 Pro)`), `Create new passkey` button, 6-digit OTP verification input, and TOTP authenticator setup key (`JBSWY3DPEHPK3PXP`).

## Definition of Done (DoD) Verification

- [x] All 12 accent colors, theme modes, Memory controls, Clinician Mode, and 6 subscription tiers match `_decompiled_ref/res/values/strings.xml`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
