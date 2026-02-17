package me.Ghostinq.lessTalk.PunishManip;

import me.Ghostinq.lessTalk.DiscordIntegration.discordMain;
import me.Ghostinq.lessTalk.FileManip.discordConfigFile;
import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.FileManip.mutedFile;
import me.Ghostinq.lessTalk.FileManip.mutedPlayerFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatPunishment {

    private static final String prefix = LessTalk.getInstance().getConfig().getString("prefix");
    private static final String reasonNull = messagesFile.getString("null_reason");
    private static final String unmuted = mutedPlayerFile.getString("player_unmuted");
    private static final String staffUnmute = mutedPlayerFile.getString("staff_unmuted");
    private static final String staffMute = mutedPlayerFile.getString("staff_muted");
    private static final boolean discordLog = discordConfigFile.getBoolean("enabled");

    private static final String muted = mutedPlayerFile.getString("player_muted");



    public static void sendMute(Player p, Player target, String[] args) {
        boolean logMuted = discordConfigFile.getBoolean("log_mute");
        String muteEmbedTitle = discordConfigFile.getString("muted_player_title");
        List<String> muteDescription = discordConfigFile.getStringList("muted_description");
        String mutedColor = discordConfigFile.getString("muted_player_color");
        String webHookUrl = discordConfigFile.getString("webhook_url");

        Map<String, String> values = new HashMap<>();
        values.put("%player%", target.getName());
        values.put("%player_displayname%", target.getDisplayName());
        values.put("%staff%", p.getName());


        if (target == null) {
            String nullTarget = messagesFile.getString("player_null");
            p.sendMessage(ChatColor.format(prefix + " &7" + nullTarget));
            return;
        }
//        if (target.hasPermission("LessTalk.Staff")) {
//            p.sendMessage(ChatColor.format(prefix + " &cYou cannot mute that Player!"));
//            return;
//        }
        if (mutedFile.containsPlayer(target)) {
            p.sendMessage(ChatColor.format(prefix + " &cThat player is already muted!"));
            return;
        }
        String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        if (reason.trim().isEmpty()) {
            p.sendMessage(ChatColor.format(prefix + " " + reasonNull));
        }
        mutedFile.addMutedPlayer(target, p);
        target.sendMessage(ChatColor.format(muted));
        p.sendMessage(ChatColor.format(StringUtil.applyPlaceholders(staffMute, values)));

        if (discordLog && logMuted) {
            values.put("%reason%", reason);
            String listDescription = String.join("\n", muteDescription);
            discordMain.discordLog(webHookUrl, muteEmbedTitle, StringUtil.applyPlaceholders(listDescription, values), mutedColor);
        }

    }

    public static void sendUnmute(Player p, Player target) {
        Map<String, String> values = new HashMap<>();
        values.put("%player%", p.getName());
        values.put("%player_displayname%", p.getDisplayName());

        if (target == null) {
            p.sendMessage(ChatColor.format(prefix + " &cThat player does not exist!"));
            return;
        }
        if (!mutedFile.containsPlayer(target)) {
            p.sendMessage(ChatColor.format(prefix + " &cThat player is not muted!"));
            return;
        }
        mutedFile.removeMutedPlayer(target, p);
        p.sendMessage(ChatColor.format(StringUtil.applyPlaceholders(staffUnmute, values)));
        target.sendMessage(ChatColor.format(unmuted));
    }
}
