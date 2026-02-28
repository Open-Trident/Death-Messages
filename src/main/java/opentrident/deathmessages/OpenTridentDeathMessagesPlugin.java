package opentrident.deathmessages;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("OpenTrident DeathMessages has been disabled!");
    }
}
