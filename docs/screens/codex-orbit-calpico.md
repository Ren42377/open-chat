# Codex Cloud, Remote Next, Companion Pets, Orbit Dots, and Calpico

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (`/codex/remote-next`, `/dots`, `/calpico` deep-link routes)
  - `_decompiled_ref/res/values/strings.xml` (lines 1272-1510 for Codex Remote & Companion Pets; lines 4424-4789 for Orbit/Tibro Dots; lines 513-774 for Calpico)
  - `_decompiled_ref/sources/p000/m1v.java`, `_decompiled_ref/sources/p000/bmp0.java`, `_decompiled_ref/sources/p000/h110.java`
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/ExperimentalAndInspectorScreens.kt` (`CodexRemoteScreen`, `OrbitTibroScreen`, `CalpicoSocialScreen`)

## Reconstructed Components

1. **Codex Cloud, Remote Next & Companion Pets (`CodexRemoteScreen`):**
   - **Desktop QR / Code Pairing:** 8-character pairing code input (`A1B2-C3D4`), `Connect to desktop` confirmation, and connection status badge (`Desktop Paired`).
   - **Codex Companion Pets (`strings.xml#L1272-L1324`):** Interactive selector for all 9 hidden ASCII companion pets (`Codex`, `Dewey`, `Fireball`, `Hoots`, `Null Signal`, `Rocky`, `Seedy`, `Stacky`, `BSOD`) with live ASCII terminal preview, tagline, and full lore description.
   - **Remote Sessions & Sandbox Approval:** Cloud/Desktop session cards showing repository, branch, diff summary (`+312 / -14`), live terminal log, and interactive `Approve command` button for sandboxed shell execution.
   - **Remote Slash Composer:** Quick slash command chips (`/status`, `/compact`, `/MCP`, `/Model`, `/Permissions`) and command input.
2. **Orbit / Tibro Proactive Dots (`OrbitTibroScreen`):**
   - 3-tab navigation (`Dots`, `Ideas`, `You`) matching `_decompiled_ref/res/values/strings.xml#L4424-L4789`.
   - **Dots Tab:** Proactive agent cards with status badges (`Ready to view`, `Working on it`), category chips, schedule labels, `Run now` action, and `Talk to Orbit` voice shortcut.
   - **Ideas Tab:** Curated feed of proactive workflows that can be promoted into active Dots.
   - **You Tab:** Avatar style selector (`Pixel matrix`, `Geometric prism`, `Waveform pulse`, `Monochrome glyph`, `Blueprint grid`) and custom Orbit rules (`Always do`, `Never do`) editor.
3. **Calpico Social Group Chats (`CalpicoSocialScreen`):**
   - Filter bar (`Inbox`, `Message requests`, `Muted`) for multi-user group threads (`Open Chat with Friends`).
   - Magic invite link banner (`https://openchat.local/calpico/invite/...`) and per-thread `Assistant auto-response` switch.

## Definition of Done (DoD) Verification

- [x] All 9 Codex Companion Pets, Desktop Pairing flows, Orbit `Dots`/`Ideas`/`You` tabs, and Calpico threads match `_decompiled_ref/res/values/strings.xml`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
