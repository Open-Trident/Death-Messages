package opentrident.deathmessages.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VanillaConfig {

    private final JavaPlugin plugin;
    private final File configFile;
    private YamlConfiguration config;
    private final Map<String, List<String>> deathMessages = new HashMap<>();
    private final Random random = new Random();

    public VanillaConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "vanilla.yml");
        load();
    }

    public void load() {
        if (!configFile.exists()) {
            plugin.saveResource("vanilla.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        deathMessages.clear();

        if (config.contains("messages")) {
            for (String key : config.getConfigurationSection("messages").getKeys(true)) {
                List<String> messages = config.getStringList("messages." + key);
                if (messages != null && !messages.isEmpty()) {
                    deathMessages.put(key, messages);
                }
            }
            plugin.getLogger()
                    .info("Successfully loaded " + deathMessages.size() + " death message translation key mappings.");
        }
    }

    public void reload() {
        load();
    }

    /**
     * Gets a random death message for a given vanilla translation key.
     * 
     * @param translationKey the vanilla translation key
     * @return a randomly chosen custom message, or null if none defined
     */
    public String getRandomMessage(String translationKey) {
        List<String> messages = deathMessages.getOrDefault(translationKey, Collections.emptyList());
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(random.nextInt(messages.size()));
    }
}
