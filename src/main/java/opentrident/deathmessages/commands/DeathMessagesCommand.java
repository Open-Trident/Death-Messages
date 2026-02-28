package opentrident.deathmessages.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import opentrident.deathmessages.config.VanillaConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        sender.sendMessage(Component.text("Unknown subcommand. Usage: /" + label + " reload", NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("opentrident.deathmessages.admin")) {
                return Collections.singletonList("reload");
            }
        }
        return Collections.emptyList();
    }
}
