# Realtime Voice Call, Voice Debug, and Camera / Sketch Studio

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (`/voice` and `/sketch` deep-link routes, `VoiceCallBroadcastReceiver`)
  - `_decompiled_ref/res/values/strings.xml` (`voice_settings_select_voice`, `voice_debug_*`, `sketch_title`, `image_gen_aspect_ratio_*`, `sticker_maker_*`)
  - `_decompiled_ref/res/values/colors.xml` (`voice_AccentBlue = #FF0285FF`)
  - `_decompiled_ref/sources/p000/h0w0.java` (Voice session and debug overlay controller)
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/CoreConversationScreens.kt` (`VoiceCallScreen`, `CameraSketchScreen`)

## Reconstructed Components

1. **Realtime Voice Call (`VoiceCallScreen`):**
   - Animated radial gradient Voice Orb (`#0285FF` accent blue) with continuous spatial pulse animation.
   - Live session status toggles (`Listening`, `Thinking`, `Speaking`, `Muted`) and `Live Camera` video feed simulation card.
   - Horizontal voice picker supporting all 7 reference voices (`Sol`, `Spruce`, `Cove`, `Ember`, `Breeze`, `Juniper`, `Vale`) with personality descriptions.
   - Expandable `Voice Debug` overlay (`strings.xml#L7743-L7751`) exposing live WebRTC codec (`Opus 48kHz stereo`), packet loss (`0.02%`), jitter (`4.2 ms`), round-trip latency (`68 ms`), and turn-detection metrics.
2. **Camera, Sketch & Image Studio (`CameraSketchScreen`):**
   - Tool mode selector (`Draw and annotate`, `Create sticker`, `Camera capture`).
   - Aspect ratio selector (`Square 1:1`, `Portrait 3:4`, `Landscape 4:3`).
   - Interactive freehand drawing `Canvas` with 6 brush colors (`Blue`, `Green`, `Orange`, `Red`, `Purple`, `Ink`), stroke counter, and `Clear canvas` action.
   - Prompt input with `Attach to conversation` action that injects the generated sketch or sticker directly into the active chat thread.

## Definition of Done (DoD) Verification

- [x] Layout hierarchy and `#0285FF` voice orb accent match `_decompiled_ref/res/values/colors.xml`.
- [x] All 7 voice presets and `Voice Debug` strings match `_decompiled_ref/res/values/strings.xml`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
