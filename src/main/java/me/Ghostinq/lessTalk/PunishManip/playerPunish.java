package me.Ghostinq.lessTalk.PunishManip;

import me.Ghostinq.lessTalk.DiscordIntegration.discordMain;
import me.Ghostinq.lessTalk.FileManip.discordConfigFile;
import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.FileManip.warnsFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class playerPunish {

    private static final String prefix = LessTalk.getInstance().getConfig().getString("prefix");
    private static final String reasonNull = messagesFile.getString("null_reason");
    private static final String webHookUrl = discordConfigFile.getString("webhook_url");
    private static final int warnAmount = LessTalk.getInstance().getConfig().getInt("warn_amount");
    private static final String punishType = LessTalk.getInstance().getConfig().getString("punishment");
    private static final String punishReason = LessTalk.getInstance().getConfig().getString("punishment_reason");
    private static final boolean punishWarn = LessTalk.getInstance().getConfig().getBoolean("warn_punish");
    private static final boolean logWarn = discordConfigFile.getBoolean("log_warn");
    private static boolean discordLog = discordConfigFile.getBoolean("enabled");

    public static void sendWarn(Player p, Player target, String[] args) {

        Map<String, String> values = new HashMap<>();
        values.put("%player%", target.getName());
        values.put("%player_displayname%", target.getDisplayName());
        values.put("%staff%", p.getName());

        String warnEmbedTitle = discordConfigFile.getString("warn_player_title");
        List<String> warnDescription = discordConfigFile.getStringList("warn_description");
        String warnColor = discordConfigFile.getString("warn_color");

            if (target == null) {
                String nullTarget = messagesFile.getString("player_null");
                p.sendMessage(ChatColor.format(prefix + " &7" + nullTarget));
                return;
            }

            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            if (reason.trim().isEmpty()) {
                p.sendMessage(ChatColor.format(prefix + " " + reasonNull));
            }

//            if (target.hasPermission("LessTalk.Staff")) {
//                p.sendMessage(ChatColor.format(prefix + " &cYou cannot warn that Player!"));
//                return;
//            }

            String playerTemplate = messagesFile.getString("player_warned");
            String staffTemplate = messagesFile.getString("staff_warned");

            if (staffTemplate.contains("%player%")) {
                String replace = staffTemplate.replace("%player%", target.getName());
                p.sendMessage(ChatColor.format(prefix + " " + StringUtil.replaceWarn(replace, reason)));
            } else {
                p.sendMessage(ChatColor.format(prefix + " " + StringUtil.replaceChar(staffTemplate, target)));
            }
            target.sendMessage(ChatColor.format(prefix + " " + StringUtil.replaceWarn(playerTemplate, reason)));

        if (discordLog && logWarn) {
            values.put("%reason%", reason);
            String listDescription = String.join("\n", warnDescription);
            discordMain.discordLog(webHookUrl, warnEmbedTitle, StringUtil.applyPlaceholders(listDescription, values), warnColor);
        }

            warnsFile.addWarn(target, reason);
            if (warnsFile.getWarnAmount(target) >= warnAmount && punishWarn) {
                if (punishType.equalsIgnoreCase("ban")) {
                    WarnPunish.sendBan(target, reason);
                }
                if (punishType.equalsIgnoreCase("kick")) {
                    WarnPunish.sendKick(target, punishReason);
                }
                if (punishType.equalsIgnoreCase("tempban")) {
                    WarnPunish.sendTempBan(target, reason);
                }
            }
        }
}
