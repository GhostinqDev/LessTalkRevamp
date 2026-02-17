package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.Functions.staffChat;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class staffChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String format = LessTalk.getInstance().getConfig().getString("staff_chat_format");

        if (staffChat.containsPlayer(p)) {
            e.setCancelled(true);
            for (Player onlineStaff : Bukkit.getOnlinePlayers()) {
                if (!onlineStaff.hasPermission("LessTalk.Staff")) return;
                String msg = StringUtil.replaceChar(format, p);
                onlineStaff.sendMessage(ChatColor.format(msg + e.getMessage()));
            }
        }
    }

}
