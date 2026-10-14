# ZNClient — Full Minecraft Fabric Client Source

Independent client project targeting **Minecraft Java 1.21.11** with Fabric and Java 21.

## Includes
- Fabric client entry point and tick/render event handling
- Module model and registry (`ClientModule`, `ModuleManager`)
- Persistent settings in `.minecraft/config/znclient.properties`
- Right Shift to open the responsive animated ClickGUI
- Working toggles: Watermark, FPS Counter, Coordinates, Clock, Direction, Crosshair Info, Status HUD
- Scrollable categories; unavailable modules explicitly marked as nonfunctional previews
- GitHub Actions workflow to build a JAR

## Build without local Gradle
1. Create a GitHub repository and upload the **contents** of this ZIP (including `.github`).
2. Open **Actions** → **Build ZNClient JAR** → **Run workflow**.
3. If build succeeds, download the artifact, extract the JAR, and put it in `.minecraft/mods` with matching Fabric API.

**Not compilation-tested.** Fabric/Minecraft APIs change; if the build fails, inspect Actions logs for errors. This is original source, not extracted proprietary GolemsClient code. No stealth, anti-cheat evasion, or process-hiding features.
