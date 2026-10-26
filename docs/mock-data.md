# Mock Data and Feature Flag Architecture

Open Chat replaces all network clients (REST APIs, Server-Sent Events, WebSockets, authentication providers, billing clients, and telemetry SDKs) with deterministic in-memory mock repositories and runtime state controllers.

## Architectural Components

### 1. `LocalMockRepository`

`LocalMockRepository` serves as the single offline source of truth for application data:
- **Conversations and Messages:** Provides realistic sample threads, multi-turn markdown responses, code blocks, image generation cards, voice transcripts, web search citations, and reasoning trace steps.
- **Models and Capabilities:** Supplies the catalog of selectable models, reasoning effort levels, and tool toggles extracted from the reference UI.
- **Custom GPTs and Library Items:** Supplies categorized assistants, generated images, and shared links for the Library and Explore screens.
- **User Profile and Settings:** Supplies mock account metadata, workspace tiers (`Free`, `Plus`, `Pro`, `Team`, `Enterprise`), voice presets, custom instructions, and memory entries.

### 2. `FakeStateController`

`FakeStateController` exposes reactive `StateFlow` state for controlling UI branches across every screen:
- **Screen Data State:** Switch any screen between `Success`, `Loading`, `Empty`, and `Error` states at runtime to verify every UI branch.
- **Subscription Tier Override:** Toggle between `Free`, `Plus`, and `Pro` tiers to inspect tier-gated UI badges, upsell banners, and model pickers.
- **Streaming Simulation:** Simulates token-by-token message streaming, thinking indicators, and voice session state transitions (`Idle`, `Connecting`, `Listening`, `Thinking`, `Speaking`).

### 3. In-App Inspector / Debug Switcher

The In-App Inspector is a floating or drawer-accessible developer overlay that allows inspecting and toggling all feature flags, A/B experiment variants, subscription states, and screen states at runtime without restarting the app.

## Feature Flag Registry

All feature flags discovered during decompilation of `_decompiled_ref/` are registered in `FakeStateController` with their default value set to `true` (unlocked) so hidden and experimental screens are immediately accessible.

This document is updated during Phases F1 through F4 with the exact feature flag keys and source references extracted from `_decompiled_ref/`.
