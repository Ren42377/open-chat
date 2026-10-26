# Getting Started (CI-Driven Workflow)

Open Chat enforces a strict **Zero Local Footprint** policy. Contributors do not install or execute Java, Gradle, the Android SDK, Apktool, or JADX on their local machines. Local machines are used solely for editing text files and interacting with GitHub via `git` and `gh`.

## Local Prerequisites

Only two tools are required locally:
1. **Git** (`git`) for version control.
2. **GitHub CLI** (`gh`), authenticated with your GitHub account:
   ```bash
   gh auth login
   ```

## Daily Development Loop

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Ren42377/open-chat.git
   cd open-chat
   ```

2. **Supply and decompile the reference APK in the cloud:**
   Upload the pinned target APK to the `apk-src` GitHub release:
   ```bash
   gh release create apk-src path/to/target.apk --title "Source APK Reference" --notes "Pinned reference APK"
   ```
   Trigger or watch the cloud decompilation workflow:
   ```bash
   gh workflow run decompile.yml
   gh run list --workflow=decompile.yml --limit 1
   gh run watch <run-id> --exit-status
   ```

3. **Download reference files into `_decompiled_ref/`:**
   ```bash
   gh run download <run-id> -n decompiled_ref-manifest-res -D _decompiled_ref/
   gh run download <run-id> -n decompiled_ref-sources -D _decompiled_ref/
   ```
   Note: `_decompiled_ref/` is listed in `.gitignore` and must never be committed to version control.

4. **Edit code and documentation locally:**
   Write clean Kotlin, Jetpack Compose, and Markdown files using any text editor.

5. **Push changes and verify via GitHub Actions:**
   ```bash
   git add <files>
   git commit -m "feat(scope): concise description"
   git push
   ```
   Monitor the CI workflow:
   ```bash
   gh run list --workflow=ci.yml --limit 1
   gh run watch <run-id> --exit-status
   ```

6. **Self-healing on CI failure:**
   If a CI run fails, inspect the failed logs directly from the terminal, fix the issue locally, and push a new commit:
   ```bash
   gh run view <run-id> --log-failed
   ```

7. **Download the compiled debug APK:**
   Every green CI run uploads the `open-chat-debug` artifact:
   ```bash
   gh run download <run-id> -n open-chat-debug -D ./build-artifacts
   ```
