package me.Ghostinq.lessTalk.Util;

public class ChatColor {

    public static String format(String s) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
    }

}
