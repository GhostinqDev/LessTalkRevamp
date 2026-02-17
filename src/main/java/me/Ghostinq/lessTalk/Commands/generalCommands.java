package me.Ghostinq.lessTalk.Commands;

import me.Ghostinq.lessTalk.DiscordIntegration.discordMain;
import me.Ghostinq.lessTalk.FileManip.*;
import me.Ghostinq.lessTalk.Functions.lockChat;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.PunishManip.chatPunishment;
import me.Ghostinq.lessTalk.PunishManip.playerPunish;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class generalCommands implements CommandExecutor {

    private static final String prefix = LessTalk.getInstance().getConfig().getString("prefix");
    private static final String disabledCMD = messagesFile.getString("disabled_module");


    private static boolean getModuleStatus(String module) {
        return LessTalk.getInstance().getConfig().getBoolean("module_toggle." + module);
    }

    private static void lockChat(Player p) {
        if (getModuleStatus("chat_lock")) {
            lockChat.setChatLocked(p);
        } else {
            p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
        }
    }

    private static void clearChat(Player p) {

        // Placeholder map
        Map<String, String> values = new HashMap<>();
        values.put("%player%", p.getName());
        values.put("%player_displayname%", p.getDisplayName());

        if (getModuleStatus("clear_chat")) {
            String clearChat = broadcastFile.getString("server_chat_cleared");
            for (int i = 0; i <= 50; i++) {
                Bukkit.getServer().broadcastMessage(" ");
            }
            Bukkit.getServer().broadcastMessage(ChatColor.format(StringUtil.applyPlaceholders(clearChat, values)));
        } else {
            p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
        }
    }

    private static void returnHelp(Player p) {
        p.sendMessage(" ");
        p.sendMessage(ChatColor.format("&9&l===================="));
        p.sendMessage(ChatColor.format("&e/lt lock &7: &8Locks/Unlocks the Servers chat"));
        p.sendMessage(ChatColor.format("&e/lt mute <player> <reason> &7: &8Mutes the specified Player"));
        p.sendMessage(ChatColor.format("&e/lt unmute <player> &7: &8Unmutes the specified Player"));
        p.sendMessage(ChatColor.format("&e/lt warn <player> <reason> &7: &8Warns the player for the specified reason"));
        p.sendMessage(ChatColor.format("&e/lt reload &7: &8Reloads the LessTalk configuration files"));
        p.sendMessage(ChatColor.format("&e/lt clear &7: &8Clears the Servers chat."));
        p.sendMessage(ChatColor.format("&9&l===================="));
        p.sendMessage(" ");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        // Discord Integration
        boolean discordLog = discordConfigFile.getBoolean("enabled");
        boolean logUnmuted = discordConfigFile.getBoolean("log_unmute");
        String webHookUrl = discordConfigFile.getString("webhook_url");

        String unmuteEmbedTitle = discordConfigFile.getString("unmuted_player_title");
        String unmuteDescription = discordConfigFile.getString("unmuted_description");
        String unmuteColor = discordConfigFile.getString("unmuted_player_color");

        Player p = (Player) sender;

        if (p.hasPermission("LessTalk.Admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    try {
                        broadcastFile.reloadConfig();
                        lockedMessagesFile.reloadConfig();
                        mutedPlayerFile.reloadConfig();
                        LessTalk.getInstance().reloadConfig();
                        discordConfigFile.reloadConfig();
                        banScreenFile.reloadConfig();
                    } catch (IOException | InvalidConfigurationException e) {
                        p.sendMessage(ChatColor.format(prefix + " &cConfig failed to reload, please check console for errors!"));
                        throw new RuntimeException(e);
                    }
                    p.sendMessage(ChatColor.format(prefix + " &aConfig has been reloaded."));
                }
            }
        }

        if (p.hasPermission("LessTalk.staff")) {
            if (args.length == 0) {
                returnHelp(p);
            }
            // /lt lock
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    clearChat(p);
                }
                if (args[0].equalsIgnoreCase("lock")) {
                    lockChat(p);
                }
            }
            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("mute")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("%player%", target.getName());
                    placeholders.put("%staff%", p.getName());
                    if (!getModuleStatus("mute_players")) {
                        p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
                        return true;
                    }
                    chatPunishment.sendMute(p, target, args);
                    return true;
                }

                if (args[0].equalsIgnoreCase("warn")) {
                    // lt warn <player> <reason>
                    String reasonNull = messagesFile.getString("null_reason");
                    Player target = Bukkit.getPlayer(args[1]);

                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("%player%", target.getName());
                    placeholders.put("%staff%", p.getName());
                    if (!getModuleStatus("warn_player")) {
                        p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
                        return true;
                    }
                    playerPunish.sendWarn(p, target, args);
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unmute")) {

                    if (!getModuleStatus("muted_players")) {
                        Player target = Bukkit.getPlayer(args[1]);

                        Map<String, String> values = new HashMap<>();
                        values.put("%player%", target.getName());
                        values.put("%player_displayname%", target.getDisplayName());
                        values.put("%staff%", p.getName());
                        values.put("%staff_displayname%", p.getDisplayName());

                        chatPunishment.sendUnmute(p, target);
                        // Discord Integration
                        if (discordLog && logUnmuted) {
                            discordMain.discordLog(webHookUrl, unmuteEmbedTitle, StringUtil.applyPlaceholders(unmuteDescription, values), unmuteColor);
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
                    return true;
                }
            }
        }
        return true;
    }
}
