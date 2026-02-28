# Open Trident - Death Messages Plugin

## Development Log

### Date: 2026-02-28

- **Project Structure**: Scaffolded a Maven project tailored for Minecraft 1.21 using Paper and Folia APIs.
- **Dependencies**: Added `io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT` to `pom.xml`.
- **Build System**: Configured Java 21 compatibility using `maven-compiler-plugin` and set up the `maven-shade-plugin` for packaging the final JAR artifact.
- **Metadata**: Created `src/main/resources/plugin.yml` with essential identifiers and specifically included `folia-supported: true` to ensure the plugin can load on Folia servers.
- **Main Class**: Created `opentrident.deathmessages.OpenTridentDeathMessagesPlugin`, which logs startup/shutdown events and implements a reflection-based check for the presence of the Folia `RegionScheduler`.
- **Initial Build**: Successfully compiled the project using `mvn clean package`.
- **Git**: Added `.gitignore` to ignore the `target` directory and other auto-generated files.

### Vanilla Death Messages Support

- **Full Coverage Definition**: Downloaded the official Minecraft 1.21 `en_us.json` language file, parsed out every `death.attack.*` and `death.fell.*` key (approx 97 keys), and injected them all into `src/main/resources/vanilla.yml`.
- **Config Management**: Added `VanillaConfig.java` to load and randomly fetch messages from the configuration file.
- **Event Listener**: Created `PlayerDeathListener`, registered at `HIGHEST` priority, to intercept `PlayerDeathEvent`, extract the translation keys via `TranslatableComponent`, and serialize arguments back into custom messages using the MiniMessage API handles (`<player>`, `<killer>`, `<item>`).
- **Commands**: Added `/deathmessages reload` (with `ot-dm` alias), secured by the `opentrident.deathmessages.admin` permission, to reload the YAML configuration on the fly.
- **Main Setup**: Initialized config, event listeners, and commands in the plugin's `onEnable` method.
- **Testing**: Re-compiled via `mvn clean package` and verified there are no build errors.
