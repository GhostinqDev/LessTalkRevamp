package me.Ghostinq.lessTalk.Util;

import org.bukkit.entity.Player;
import java.util.Map;

public class StringUtil {

    public static String applyPlaceholders(String input, Map<String, String> values) {

        if (input == null || input.isEmpty()) return "";
        String result = input;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String replaceChar(String s, Player p) {
        if (s.contains("%player_displayname%")) {
           return s.replace("%player_displayname%", p.getDisplayName());
        }
        if (s.contains("%player")) {
            return s.replace("%player%", p.getName());
        }
        return null;
    }

    public static String replaceWarn(String s, String warn) {
        if (s.contains("%warn%")) {
            return s.replace("%warn%", warn);
        }
        return null;
    }

}
