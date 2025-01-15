package org.dimaserver.statTracker.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.dimaserver.statTracker.StatTracker;

import java.sql.SQLException;

public class CommandHandler implements CommandExecutor {
    private final StatTracker plugin;

    public CommandHandler(StatTracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (command.getName().equalsIgnoreCase("link")) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /link [discord|twitch] <userID>");
                return true;
            }

            String platform = args[0];
            String userId = args[1];

            try {
                if (platform.equalsIgnoreCase("discord")) {
                    long discordUserId = Long.parseLong(userId);
                    plugin.getDatabase().linkDiscordAccount(uuid, discordUserId);
                    sender.sendMessage("Discord account linked successfully.");
                } else if (platform.equalsIgnoreCase("twitch")) {
                    plugin.getDatabase().linkTwitchAccount(uuid, userId);
                    sender.sendMessage("Twitch account linked successfully.");
                } else {
                    sender.sendMessage("Invalid platform. Use 'discord' or 'twitch'.");
                }
            } catch (SQLException e) {
                sender.sendMessage("An error occurred while linking the account.");
                e.printStackTrace();
            }

            return true;
        }

        if (command.getName().equalsIgnoreCase("unlink")) {
            if (args.length < 1) {
                sender.sendMessage("Usage: /unlink [discord|twitch]");
                return true;
            }

            String platform = args[0];

            try {
                if (platform.equalsIgnoreCase("discord")) {
                    plugin.getDatabase().unlinkDiscordAccount(uuid);
                    sender.sendMessage("Discord account unlinked successfully.");
                } else if (platform.equalsIgnoreCase("twitch")) {
                    plugin.getDatabase().unlinkTwitchAccount(uuid);
                    sender.sendMessage("Twitch account unlinked successfully.");
                } else {
                    sender.sendMessage("Invalid platform. Use 'discord' or 'twitch'.");
                }
            } catch (SQLException e) {
                sender.sendMessage("An error occurred while unlinking the account.");
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
