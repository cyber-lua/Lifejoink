package org.cyber.ct.lifejoink;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameMode; // Import GameMode for setting player to spectator

public class Lifejoink extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register the event listener
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();

            // Check if the victim was killed by another player
            if (victim.getKiller() instanceof Player) {
                Player killer = victim.getKiller();

                // Adjust hearts for a player kill
                double victimMaxHealth = victim.getMaxHealth();
                double killerMaxHealth = killer.getMaxHealth();

                if (victimMaxHealth > 2) {
                    victim.setMaxHealth(victimMaxHealth - 2); // Lose a heart
                }
                killer.setMaxHealth(killerMaxHealth + 2); // Gain a heart

                // Optionally, set the victim's health to their new max
                if (victim.getHealth() > victim.getMaxHealth()) {
                    victim.setHealth(victim.getMaxHealth());
                }

                killer.sendMessage("You have stolen a heart!");
                victim.sendMessage("You have lost a heart!");

                // Check if the victim's health has reached 0 and set to spectator mode
                if (victim.getMaxHealth() <= 2) { // When the victim has no hearts left
                    victim.sendMessage("You have no hearts left! You are now in spectator mode.");
                    victim.setGameMode(GameMode.SPECTATOR); // Set to spectator mode
                }
            } else {
                // Handle non-player death (falling, lava, etc.)
                double maxHealth = victim.getMaxHealth();
                if (maxHealth > 2) {
                    victim.setMaxHealth(maxHealth - 2); // Lose half a heart (1 health point)

                    // Check if the player's health has reached 0 and set to spectator mode
                    if (victim.getMaxHealth() <= 2) { // When the player has no hearts left
                        victim.sendMessage("You have no hearts left! You are now in spectator mode.");
                        victim.setGameMode(GameMode.SPECTATOR); // Set to spectator mode
                    } else {
                        victim.sendMessage("You have lost a heart!");
                    }
                }
            }
        }
    }
}
