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

        config = new YamlConfiguration();
        config.options().pathSeparator('|');
        try {
            config.load(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Could not fully load vanilla.yml!");
            e.printStackTrace();
        }

        deathMessages.clear();

        org.bukkit.configuration.ConfigurationSection messageSection = config.getConfigurationSection("messages");
        if (messageSection != null) {
            for (String key : messageSection.getKeys(false)) {
                if (messageSection.isList(key)) {
                    List<String> messages = messageSection.getStringList(key);
                    if (!messages.isEmpty()) {
                        deathMessages.put(key, messages);
                    }
                }
            }
        }
        plugin.getLogger()
                .info("Successfully loaded " + deathMessages.size() + " death message translation key mappings.");
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

    /**
     * Gets all loaded translation keys.
     * 
     * @return an unmodifiable set of all loaded keys
     */
    public java.util.Set<String> getKeys() {
        return Collections.unmodifiableSet(deathMessages.keySet());
    }

    /**
     * Gets a completely random key that has been loaded.
     * 
     * @return a random key, or null if none are loaded
     */
    public String getRandomKey() {
        if (deathMessages.isEmpty()) {
            return null;
        }
        int index = random.nextInt(deathMessages.size());
        int i = 0;
        for (String key : deathMessages.keySet()) {
            if (i == index)
                return key;
            i++;
        }
        return null;
    }
}
