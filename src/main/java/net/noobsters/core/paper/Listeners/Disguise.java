package net.noobsters.core.paper.Listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mrmicky.fastinv.ItemBuilder;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.events.UndisguiseEvent;
import net.md_5.bungee.api.ChatColor;
import net.noobsters.core.paper.PERMADED;

public class Disguise implements Listener {

    PERMADED instance;
    Random random = new Random();

    Disguise(PERMADED instance) {
        this.instance = instance;
    }

    @EventHandler
    public void deathDisguise(PlayerDeathEvent e) {
        var player = e.getEntity();
        var killer = player.getKiller();
        if (DisguiseAPI.isDisguised(player)) {
            var disguise = DisguiseAPI.getDisguise(player);
            e.setDeathMessage("");
            e.getDrops().forEach(drop -> drop.setType(Material.AIR));
            if (killer != null && killer instanceof Player && disguise.getDisguiseName().contains("Redstone")) {
                e.setDeathMessage(ChatColor.DARK_RED + "Redstone Monstrosity " + ChatColor.WHITE + "was destroyed by "
                        + killer.getName().toString());

                var amulet = new ItemBuilder(Material.RABBIT_FOOT).name(ChatColor.DARK_RED + "Redstone Amulet").build();
                var meta = amulet.getItemMeta();
                meta.setCustomModelData(142);
                amulet.setItemMeta(meta);

                e.getDrops().add(amulet);
            }
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
            player.setGameMode(GameMode.SPECTATOR);

        } else if (killer != null && killer instanceof Player && DisguiseAPI.isDisguised(killer)) {
            e.setDeathMessage(player.getName().toString() + " was tricked by artificial intelligence");
            var disguise = DisguiseAPI.getDisguise(killer);
            if (disguise.getDisguiseName().contains("Redstone")) {
                e.setDeathMessage(player.getName().toString() + " has been reduced to dust by the " + ChatColor.DARK_RED
                        + "Redstone Monstrosity");
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        var item = e.getItemDrop().getItemStack();
        var string = item.getItemMeta().getDisplayName().toString();
        if (DisguiseAPI.isDisguised(e.getPlayer()) && string != null && string.contains("Melee")
                || string.contains("Fireball") || string.contains("Explosion")
                || string.contains("Walk") || string.contains("Speed") || string.contains("Jump")
                || string.contains("Roar") || string.contains("Laugh")) {
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        if (DisguiseAPI.isDisguised(player)) {
            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void undisguise(UndisguiseEvent e) {
        var entity = e.getEntity();
        if (entity instanceof Player) {
            var player = (Player) entity;
            player.setGameMode(GameMode.SPECTATOR);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
        }

    }

    @EventHandler
    public void velocity(PlayerVelocityEvent e) {
        var player = e.getPlayer();
        if (DisguiseAPI.isDisguised(player)) {
            var disguise = DisguiseAPI.getDisguise(player);
            if (disguise.getDisguiseName().toString().contains("Redstone")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void powers(PlayerInteractEvent e){
        var player = e.getPlayer();
        var item = e.getItem();
        if(item != null && DisguiseAPI.isDisguised(player)){
            var string = item.getItemMeta().getDisplayName().toString();
            var loc = player.getLocation();
            if(string.contains("Explosion")){
                loc.createExplosion(10, false, false);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:smash master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1");
            }else if(string.contains("Walk")){
                if(player.hasPotionEffect(PotionEffectType.SPEED)){
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 3, false, false));
                //sound walk
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:steps master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1");
            }else if(string.contains("Speed")){
                if(player.hasPotionEffect(PotionEffectType.SLOW)){
                    player.removePotionEffect(PotionEffectType.SLOW);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0, false, false));
                //sound fast walk
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:steps master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1.5");
            }else if(string.contains("Jump")){
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 29, false, false));
                //sound jump
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:smash master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1");
            }else if(string.contains("Roar")){
                loc.getNearbyPlayers(40).stream().filter(p -> DisguiseAPI.isDisguised(p))
                    .forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 4, false, false)));
                //sound roar
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:roar master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1");
            }else if(string.contains("Laugh")){
                //sound laugh
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:laugh master @a " + loc.getX()
                        + " " + loc.getY() + " " + loc.getZ() + " 6 1");
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        var entity = e.getEntity();
        var bow = e.getBow().getItemMeta();
        if (entity instanceof Player && DisguiseAPI.isDisguised(entity)) {

            if (bow.hasDisplayName() && bow.getDisplayName().toString().contains("Fireball")) {
                var fireball = (Fireball) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation().add(0, 1, 0),
                        EntityType.FIREBALL);
                fireball.setYield(5);
                e.setProjectile(fireball);
            }
        }
    }

    public Integer chooseCoord(int radius) {
        var num = random.nextInt(radius);
        num = random.nextBoolean() ? ~(num) : num;
        return num;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        var entity = e.getEntity();
        var damager = e.getDamager();
        if (entity instanceof Player && DisguiseAPI.isDisguised(entity)) {
            var player = (Player) entity;
            if (player.hasPotionEffect(PotionEffectType.SLOW) && damager instanceof Projectile) {
                e.setCancelled(true);
            } else if (player.hasPotionEffect(PotionEffectType.SPEED) && !(damager instanceof Projectile)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        var entity = e.getEntity();
        if (entity instanceof Player && DisguiseAPI.isDisguised(entity)) {
            var disguise = DisguiseAPI.getDisguise(entity);
            var cause = e.getCause();
            if (disguise.getDisguiseName().contains("Redstone") && cause == DamageCause.FALL
                    || cause == DamageCause.LAVA || cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK
                    || cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.BLOCK_EXPLOSION
                    || cause == DamageCause.HOT_FLOOR || cause == DamageCause.WITHER) {
                e.setCancelled(true);
            }
        }

    }

}