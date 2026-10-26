# ADR 0002: Clean-Room UI Reconstruction and Local Mock Architecture

Status: Accepted

Date: 2026-10-26

## Context

The reference Android application relies on authenticated remote backend services, streaming network protocols, subscription entitlement checks, and server-driven feature flags. Directly porting network clients or keeping obfuscated compiler output would prevent offline exploration of the UI and violate clean-room and trademark requirements.

## Decision

1. **Clean-Room Jetpack Compose Translation:**
   - All screens, navigation graphs, themes, and components are written in idiomatic Kotlin and Jetpack Compose under the `io.openchat` package namespace with the application name `Open Chat` and an original launcher icon.
   - Layout hierarchies, resource tokens (`colors.xml`, `strings.xml`, `dimens.xml`, `drawable/`), and animation specifications are derived strictly from `_decompiled_ref/` and documented per screen in `docs/screens/<screen-name>.md`.

2. **Mock Backend and Runtime State Control:**
   - All data access is fulfilled by `LocalMockRepository` in memory without network permissions or remote calls.
   - `FakeStateController` manages screen states (`Loading`, `Empty`, `Error`, `Success`), subscription tier simulation, and feature flags.
   - Every feature flag discovered in the decompiled sources defaults to `true` (unlocked) and can be toggled at runtime through the In-App Inspector overlay.

## Consequences

- Every screen, dialog, bottom sheet, and hidden experimental feature can be navigated and tested immediately after installing the debug APK without login credentials.
- Each screen has a traceable audit checklist in `docs/screens/` linking its clean-room implementation to the reference files consulted.

## References

- Jetpack Compose Documentation: https://developer.android.com/develop/ui/compose/documentation
- Jetpack Compose BOM Mapping: https://developer.android.com/jetpack/compose/bom/bom-mapping
