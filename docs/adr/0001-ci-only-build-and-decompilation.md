# ADR 0001: Cloud-Only Build and Deterministic Decompilation Pipeline

Status: Accepted

Date: 2026-10-26

## Context

The project requires reconstructing a complex Android UI from a reference APK while maintaining a strict zero-local-footprint policy: no JDK, Android SDK, Gradle distribution, Apktool jar, or JADX binary may be executed or installed on the local development machine. At the same time, the repository must never store or distribute decompiled code or binary assets.

Additionally, Android Gradle Plugin (AGP) 9.4.1 introduces built-in Kotlin support bundled with Kotlin Gradle Plugin 2.2.10 by default, whereas the project uses the latest stable Kotlin release (`2.4.21`) and Compose BOM (`2026.09.00`).

## Decision

1. **GitHub Actions Build Pipeline (`.github/workflows/ci.yml`):**
   - Runs on `ubuntu-24.04` using `actions/checkout@v7`, `actions/setup-java@v6` (Temurin JDK 17), and `gradle/actions/setup-gradle@v6` with Gradle `9.8.2`.
   - Generates the Gradle wrapper on the runner if missing, executes `./gradlew lintDebug testDebugUnitTest assembleDebug`, and uploads the resulting debug APK as the `open-chat-debug` artifact via `actions/upload-artifact@v7`.
   - Explicitly declares `classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")` in the root `build.gradle.kts` `buildscript` block to upgrade the AGP 9 built-in Kotlin compiler to `2.4.21`.

2. **GitHub Actions Decompilation Pipeline (`.github/workflows/decompile.yml`):**
   - Fetches the reference APK from the `apk-src` GitHub release asset.
   - Runs pinned Apktool `3.0.3` and JADX `1.5.6` on `ubuntu-24.04` with JDK 21.
   - Uploads two separate artifacts (`decompiled_ref-manifest-res` and `decompiled_ref-sources`) so they can be downloaded selectively via `gh run download` into `_decompiled_ref/`, which is excluded via `.gitignore`.

## Consequences

- Local machines remain completely free of build toolchains and decompiler binaries.
- Every commit is verified against Android Lint, unit tests, and full debug APK compilation in a clean environment.
- Decompilation output is reproducible across contributors while keeping the Git history 100% clean-room.

## References

- Android Gradle Plugin 9.x Release Notes: https://developer.android.com/build/releases/gradle-plugin
- Gradle Setup Action (`gradle/actions/setup-gradle`): https://github.com/gradle/actions
- Apktool Releases (`v3.0.3`): https://github.com/iBotPeaches/Apktool/releases/tag/v3.0.3
- JADX Releases (`v1.5.6`): https://github.com/skylot/jadx/releases/tag/v1.5.6
