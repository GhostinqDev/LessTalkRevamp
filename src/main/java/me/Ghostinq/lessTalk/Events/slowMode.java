package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class slowMode implements Listener {

    private final HashMap<Player, Long> chatTime = new HashMap<>();
    private int slowSeconds = LessTalk.getInstance().getConfig().getInt("time_between_messages");
    private boolean enabled = LessTalk.getInstance().getConfig().getBoolean("module_toggle.slow_mode");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String error = messagesFile.getString("too_fast");
        long now = System.currentTimeMillis();
        long lastTime = chatTime.getOrDefault(p, 0L);
        long delayMillis = slowSeconds * 1000L;

        if (now - lastTime < delayMillis) {
            if (!p.hasPermission("LessTalk.Bypass")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.format(error));
                return;
            }
        }

        if (enabled) {
            chatTime.put(p, now);
        }

    }


}
