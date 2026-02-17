package me.Ghostinq.lessTalk.PunishManip;

import me.Ghostinq.lessTalk.FileManip.banScreenFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WarnPunish {

    private static List<String> banReason = banScreenFile.getStringList("ban_details");
    private static String banTitle = banScreenFile.getString("title");

    public static String setSeperate(List<String> toSeperate){

        return toSeperate.stream()
                .map(seperate -> ChatColor.format(seperate))
                .collect(Collectors.joining("\n"));
    }

    public static String getUnit() {
        String configDuration = LessTalk.getInstance().getConfig().getString("punishment_duration");
        Pattern p = Pattern.compile("(\\d+)([a-zA-Z])");
        Matcher m = p.matcher(configDuration);

        if (m.matches()) {
            return m.group(2);
        }
        return null;
    }

    public static Integer getDuration() {
        String configDuration = LessTalk.getInstance().getConfig().getString("punishment_duration");
        Pattern p = Pattern.compile("(\\d+)([a-zA-Z])");
        Matcher m = p.matcher(configDuration);

        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    public static void sendTempBan(Player p, String reason) {
        String prefix = LessTalk.getInstance().getConfig().getString("prefix");


        p.kickPlayer(ChatColor.format(prefix) + "\n" + ChatColor.format("&cYou have been banned!\n&7Reason; " + setSeperate(banReason)));

        String unit = getUnit();
        int duration = getDuration();

        Map<String, String> values = new HashMap<>();
        values.put("%player%", p.getName());
        values.put("%player_displayname%", p.getDisplayName());
        values.put("%reason%", reason);
        values.put("%server_name%", Bukkit.getServer().getName());
        values.put("%prefix%", prefix);
        if (unit.equalsIgnoreCase("d")) {
            values.put("%duration%", duration + " Days");
        }
        if (unit.equalsIgnoreCase("m")) {
            values.put("%duration%", duration + " Months");
        }

        String details = setSeperate(banReason);
        String title = banScreenFile.getString("title");
        String banScreen = StringUtil.applyPlaceholders(details, values);
        String banTitle = StringUtil.applyPlaceholders(title, values);



        if (unit.equalsIgnoreCase("d")) {
            p.ban("\n" + ChatColor.format(banTitle) + "\n" + ChatColor.format(banScreen)
                    , Duration.ofDays(duration), "LessTalk", true);
        }
        if (unit.equalsIgnoreCase("m")) {
            int months = duration * 30;
            p.ban("\n" + ChatColor.format(banTitle) + "\n" + ChatColor.format(banScreen)
                    , Duration.ofDays(months), "LessTalk", true);
        }
    }

    public static void sendBan(Player p, String reason) {

        String prefix = LessTalk.getInstance().getConfig().getString("prefix");

        Map<String, String> values = new HashMap<>();
        values.put("%player%", p.getName());
        values.put("%player_displayname%", p.getDisplayName());
        values.put("%reason%", reason);
        values.put("%server_name%", banScreenFile.getString("server_name"));
        values.put("%prefix%", prefix);
        values.put("%duration%", "Never");

        String details = setSeperate(banReason);
        String title = banScreenFile.getString("title");
        String banScreen = StringUtil.applyPlaceholders(details, values);
        String banTitle = StringUtil.applyPlaceholders(title, values);


        p.kickPlayer(ChatColor.format(banTitle) + "\n" + ChatColor.format(banScreen));
        p.ban("\n" + ChatColor.format(banTitle) + "\n" + ChatColor.format(banScreen)
                , (Duration) null, "LessTalk", true);
    }

    public static void sendKick(Player p, String reason) {
        p.kickPlayer(reason);
    }

}
