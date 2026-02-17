package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.DiscordIntegration.discordMain;
import me.Ghostinq.lessTalk.FileManip.discordConfigFile;
import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkMessage implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        boolean logFilter = discordConfigFile.getBoolean("log_chat_filter");
        boolean discordLog = discordConfigFile.getBoolean("enabled");
        String webHookUrl = discordConfigFile.getString("webhook_url");
        boolean enabled = LessTalk.getInstance().getConfig().getBoolean("module_toggle.filter_chat");

        String embedTitle = discordConfigFile.getString("chat_filter_title");
        String description = discordConfigFile.getString("chat_filter_description");
        String color = discordConfigFile.getString("chat_filter_color");

        if (!enabled) return;
        Player p = e.getPlayer();
        String message = e.getMessage();

        String error = messagesFile.getString("bad_word");
        List<String> filterWords = LessTalk.getInstance().getConfig().getStringList("filtered_chat");

        for (String s : filterWords) {
            if (message.contains(s)) {
                Map<String, String> values = new HashMap<>();
                values.put("%filtered_word%", s);
                values.put("%player%", p.getName());
                values.put("%player_displayname%", p.getDisplayName());

                e.setCancelled(true);
                p.sendMessage(ChatColor.format(error));
                if (discordLog && logFilter) {
                    discordMain.discordLog(webHookUrl, embedTitle, StringUtil.applyPlaceholders(description, values), color);
                }
            }

        }

    }

}
