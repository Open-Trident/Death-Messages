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
