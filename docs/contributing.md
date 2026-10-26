# Contributing Guidelines

Open Chat welcomes contributions that adhere to our clean-room reconstruction standards, zero-hallucination policy, and cloud-only build workflow.

## Branching and Commits

- **Branches:** Use descriptive conventional branch names such as `feat/<area>-<description>` or `fix/<area>-<description>`.
- **Atomic Commits:** Each commit must represent one coherent change and use conventional commit prefixes:
  - `feat(scope): ...`
  - `fix(scope): ...`
  - `docs(scope): ...`
  - `ci(scope): ...`
  - `refactor(scope): ...`
- **No Force Push:** Never force-push to `main`.
- **Formatting Rules:**
  - All code comments, UI strings, commit messages, and documentation must be written in English.
  - Do not use emojis anywhere in code, UI, commit messages, or documentation.
  - Do not use the em dash character in code or documentation.

## Zero-Hallucination & Clean-Room Rules

1. **Reference-Backed UI Only:** Every screen, component layout, color value, dimension, animation spec, and string resource must trace back to an actual file in `_decompiled_ref/`. Do not invent screens or UI behaviors that have no basis in the reference APK.
2. **Clean-Room Translation:** Translate obfuscated symbols (such as `a.b.c` or compiler-generated `$composer` parameters) into idiomatic, human-readable Jetpack Compose code with descriptive names and single-responsibility functions.
3. **Never Commit Decompiled Files:** Files under `_decompiled_ref/` must remain local and ignored by Git at all times.

## Screen Definition of Done (DoD)

Every reconstructed screen must have a corresponding checklist file at `docs/screens/<screen-name>.md`. A screen is marked complete only when all items below are verified and CI is green:

- [ ] Screen route identified in decompiled reference (`AndroidManifest.xml`, navigation graph, deep link, or screen composable).
- [ ] Component hierarchy and layout structure match reference (`Column`, `Row`, `Box`, padding, sizing).
- [ ] Exact color tokens extracted from decompiled `colors.xml` and theme definitions.
- [ ] Exact text labels extracted from decompiled `strings.xml` (with brand names updated to `Open Chat` where required).
- [ ] Shapes, elevations, and animation specs (`spring`, `tween`) match reference.
- [ ] Mock data and all branching UI states work interactively (`loading`, `empty`, `error`, `success`).
- [ ] Associated feature flags are unlocked by default and controllable via the In-App Inspector.
- [ ] Clean-room translation verified: zero obfuscated identifiers remain in source code.
- [ ] Decompiled reference paths (manifest, resource files, and source classes) are documented in `docs/screens/<screen-name>.md`.

## Pull Request Checklist

1. Link the affected screen documentation in `docs/screens/<screen-name>.md`.
2. Include the decompiled reference file paths consulted in the pull request description.
3. Verify that no unused code, duplicate helpers, or dead files exist.
4. Confirm that the `CI` workflow (`lintDebug`, `testDebugUnitTest`, `assembleDebug`) passes with a green status.
