package opentrident.deathmessages.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import opentrident.deathmessages.config.VanillaConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeathMessagesCommand implements CommandExecutor, TabCompleter {

    private final VanillaConfig vanillaConfig;

    public DeathMessagesCommand(VanillaConfig vanillaConfig) {
        this.vanillaConfig = vanillaConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /" + label + " reload", NamedTextColor.RED));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("opentrident.deathmessages.admin")) {
                sender.sendMessage(
                        Component.text("You do not have permission to execute this command.", NamedTextColor.RED));
                return true;
            }

            vanillaConfig.reload();
            sender.sendMessage(Component.text("OpenTrident DeathMessages configuration reloaded successfully!",
                    NamedTextColor.GREEN));
            return true;
        }

        if (args[0].equalsIgnoreCase("test")) {
            if (!sender.hasPermission("opentrident.deathmessages.admin")) {
                sender.sendMessage(
                        Component.text("You do not have permission to execute this command.", NamedTextColor.RED));
                return true;
            }

            String keyToTest = null;
            if (args.length > 1) {
                keyToTest = args[1];
                if (!vanillaConfig.getKeys().contains(keyToTest)) {
                    sender.sendMessage(Component.text("Key not found in vanilla.yml", NamedTextColor.RED));
                    return true;
                }
            } else {
                keyToTest = vanillaConfig.getRandomKey();
                if (keyToTest == null) {
                    sender.sendMessage(Component.text("No death messages loaded.", NamedTextColor.RED));
                    return true;
                }
            }

            String rawMessage = vanillaConfig.getRandomMessage(keyToTest);
            if (rawMessage == null) {
                sender.sendMessage(Component.text("No message found for key: " + keyToTest, NamedTextColor.RED));
                return true;
            }

            List<TagResolver> resolvers = new ArrayList<>();
            resolvers.add(Placeholder.component("player", Component.text(sender.getName(), NamedTextColor.AQUA)));
            resolvers.add(Placeholder.component("killer", Component.text("MockKiller", NamedTextColor.RED)));
            resolvers.add(Placeholder.component("item", Component.text("MockSword", NamedTextColor.YELLOW)));

            Component finalMessage = MiniMessage.miniMessage().deserialize(rawMessage, TagResolver.resolver(resolvers));
            sender.sendMessage(Component.text("[TEST " + keyToTest + "] ", NamedTextColor.GRAY).append(finalMessage));
            return true;
        }

        sender.sendMessage(Component.text("Unknown subcommand. Usage: /" + label + " reload", NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("opentrident.deathmessages.admin")) {
                List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "test"), completions);
                Collections.sort(completions);
                return completions;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("test")) {
            if (sender.hasPermission("opentrident.deathmessages.admin")) {
                List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], vanillaConfig.getKeys(), completions);
                Collections.sort(completions);
                return completions;
            }
        }
        return Collections.emptyList();
    }
}
