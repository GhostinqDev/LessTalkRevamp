package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class duplicateMessage implements Listener {

    private static HashMap<Player, String> check = new HashMap<>();


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        String repeatedMsg = messagesFile.getString("duplicate_message");
        boolean enabled = LessTalk.getInstance().getConfig().getBoolean("module_toggle.duplicate_message_check");

        if (!enabled) return;

        if (!p.hasPermission("LessTalk.Staff") || !p.hasPermission("LessTalk.Bypass")) {
            if (check.containsKey(p)) {
                if (!msg.equalsIgnoreCase(check.get(p))) {
                    check.replace(p, check.get(p), msg);
                } else {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.format(repeatedMsg));
                }
            } else {
                check.put(p, msg);
            }
        }
    }
}
