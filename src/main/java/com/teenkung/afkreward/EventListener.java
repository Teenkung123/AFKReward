package com.teenkung.afkreward;

import com.teenkung.afkreward.Utils.PlayerTimer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {


    //Player Quit Event call to remove unused AFK data From memory
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerTimer.removePlayer(event.getPlayer());
    }
}
