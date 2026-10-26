# Artifact Library, Pages, Sites Lite, Discovery Hub, and Spaces

## Reference Mapping

- **Decompiled Sources:**
  - `_decompiled_ref/AndroidManifest.xml` (`/library`, `/discovery-hub`, `/gpts`, `/g/`, `/spaces/`, `/scheduled`, `/tasks` deep-link routes)
  - `_decompiled_ref/res/values/strings.xml` (`files_library_*`, `sites_analytics_*`, `discovery_hub_*`, `snorlax_space_*`, `AutomationsScreen`)
  - `_decompiled_ref/sources/p000/h110.java`, `_decompiled_ref/sources/p000/f6v.java`
- **Clean-Room Implementation:**
  - `app/src/main/java/io/openchat/ui/screens/LibraryAndSettingsScreens.kt` (`ArtifactLibraryScreen`, `DiscoveryHubScreen`, `SpacesAndTasksScreen`)

## Reconstructed Components

1. **Artifact Library, Collaborative Pages & Sites Lite (`ArtifactLibraryScreen`):**
   - Category tabs (`All`, `Files`, `Pages`, `Sites`, `Images`, `Favorites`, `Shared`, `Recently deleted`) and folder filter pills with `New folder` creation.
   - Search bar (`Search Pages and files`) and artifact cards with favorite/star toggles.
   - **Sites Lite Hosting & Analytics (`strings.xml#L7087-L7235`):** Live published URL (`https://design-audit.openchat.site`), visibility badge (`Public (Anyone with link)`), time-range selector (`24H`, `7D`, `30D`), metric tiles (`Visitors`, `Page views`, `Likes`, `Forks`), `Traffic sources` progress breakdown, `Top pages` table, and `Environment secrets` manager (`Add secret`).
2. **Discovery Hub, Gizmo Store & Apps Directory (`DiscoveryHubScreen`):**
   - Category filter (`All`, `Featured`, `Coding`, `Research`, `Writing`, `Productivity`) for custom assistants (`Gizmos`) with ratings, conversation counts, pinned state, and `Start chat` action.
   - `Apps Directory, Skills & Bazaar Connectors` section (`strings.xml#L5695-L5726`) with interactive connection switches (`GitHub`, `Google Drive`, `Notion Workspace`, `Slack`, `Linear`) and security verification badges (`Verified by Open Chat`, `Read/Write OAuth`, `Read-Only`).
3. **Project Spaces (Snorlax) & Scheduled Automations (`SpacesAndTasksScreen`):**
   - Interactive Space cards with custom icons, color hex badges, chat/source counters, project goals, custom instructions, and `Create Space` form.
   - `Scheduled Tasks & Automations` (`AutomationsScreen`) list with cadence badges, next-run timestamps, active/paused switches, and `Add scheduled task` form.

## Definition of Done (DoD) Verification

- [x] All tabs, Sites Analytics panels, Gizmo cards, Bazaar connectors, Spaces, and Automations match `_decompiled_ref/res/values/strings.xml`.
- [x] Interactive states (`Loading`, `Empty`, `Error`, `Success`) are wired via `StateSimulationHost` and `FakeStateController`.
- [x] Zero lint warnings and unit tests passing on GitHub Actions CI.
