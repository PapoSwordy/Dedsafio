package net.noobsters.core.paper.Listeners;

import java.util.Random;

import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.noobsters.core.paper.PERMADED;

public class Monsters implements Listener{

    PERMADED instance;
    Random random = new Random();

    Monsters(PERMADED instance){
        this.instance = instance;
    }
    
    @EventHandler
    public void monsterSpawns(CreatureSpawnEvent e) {
        var game = instance.getGame();
        if (e.getEntity() instanceof Spider && game.getDifficultyChange() >= 2) {
            var spider = (Spider) e.getEntity();
            spider.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*10000, 1));
            spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10000, 1));
            spider.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*10000, 0));
            spider.setCustomName(ChatColor.RED + "Monster Spider");
        }

    }
}