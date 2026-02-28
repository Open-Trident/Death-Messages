package opentrident.deathmessages.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import opentrident.deathmessages.config.VanillaConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {

    private final VanillaConfig vanillaConfig;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public PlayerDeathListener(VanillaConfig vanillaConfig) {
        this.vanillaConfig = vanillaConfig;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Component deathMessage = event.deathMessage();
        if (deathMessage == null)
            return;

        // Ensure the death message is translatable (Vanilla behavior)
        if (deathMessage instanceof TranslatableComponent translatableComponent) {
            String translationKey = translatableComponent.key();
            List<Component> args = translatableComponent.args();

            // Fetch a random replacement message
            String customMessageFormat = vanillaConfig.getRandomMessage(translationKey);
            if (customMessageFormat != null && !customMessageFormat.isEmpty()) {

                // Prepare argument placeholders
                List<TagResolver> resolvers = new ArrayList<>();

                // Add index-based aliases <arg0>, <arg1>, <arg2>
                for (int i = 0; i < args.size(); i++) {
                    resolvers.add(Placeholder.component("arg" + i, args.get(i)));
                }

                // Add friendly aliases based on vanilla argument structure
                if (!args.isEmpty()) {
                    resolvers.add(Placeholder.component("player", args.get(0)));
                }
                if (args.size() > 1) {
                    resolvers.add(Placeholder.component("killer", args.get(1)));
                }
                if (args.size() > 2) {
                    resolvers.add(Placeholder.component("item", args.get(2)));
                }

                // Parse the modified component via MiniMessage
                Component finalMessage = miniMessage.deserialize(customMessageFormat, TagResolver.resolver(resolvers));

                // Replace the original death message
                event.deathMessage(finalMessage);
            }
        }
    }
}
