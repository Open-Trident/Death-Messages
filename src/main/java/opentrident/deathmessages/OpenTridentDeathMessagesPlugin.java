package opentrident.deathmessages;

import opentrident.deathmessages.commands.DeathMessagesCommand;
import opentrident.deathmessages.config.VanillaConfig;
import opentrident.deathmessages.listeners.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public class OpenTridentDeathMessagesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("OpenTrident DeathMessages has been enabled!");

        // Example check to verify if Folia's RegionScheduler is available
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
            getLogger().info("Folia is detected and supported!");
        } catch (ClassNotFoundException e) {
            getLogger().info("Running on standard Paper (Folia not detected).");
        }

        // Initialize Vanilla Death Messages configuration
        VanillaConfig vanillaConfig = new VanillaConfig(this);
        getLogger().info("Loaded vanilla.yml configuration.");

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(vanillaConfig), this);
        getLogger().info("Registered PlayerDeathListener.");

        // Register Commands using reflection (Paper plugin.yml doesn't support commands
        // block)
        try {
            java.lang.reflect.Constructor<org.bukkit.command.PluginCommand> constructor = org.bukkit.command.PluginCommand.class
                    .getDeclaredConstructor(String.class, org.bukkit.plugin.Plugin.class);
            constructor.setAccessible(true);
            org.bukkit.command.PluginCommand cmd = constructor.newInstance("deathmessages", this);

            DeathMessagesCommand commandExecutor = new DeathMessagesCommand(vanillaConfig);
            cmd.setExecutor(commandExecutor);
            cmd.setTabCompleter(commandExecutor);
            cmd.setAliases(java.util.Collections.singletonList("ot-dm"));
            cmd.setDescription("Core command for OpenTrident DeathMessages");
            cmd.setPermission("opentrident.deathmessages.admin");

            getServer().getCommandMap().register("opentrident", cmd);
            getLogger().info("Registered /deathmessages command dynamically.");
        } catch (Exception e) {
            getLogger().severe("Failed to register /deathmessages command!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("OpenTrident DeathMessages has been disabled!");
    }
}
