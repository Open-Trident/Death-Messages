package opentrident.deathmessages;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("OpenTrident DeathMessages has been disabled!");
    }
}
