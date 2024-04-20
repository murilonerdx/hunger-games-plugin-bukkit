package org.murilonerdx.utils;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.World;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;


public class EntityUtils {

    // Método para fazer a entidade atacar com força explosiva
    public static void explosiveAttack(LivingEntity attacker, double power) {
        attacker.getWorld().createExplosion(attacker.getLocation(), (float) power, true, true);
    }

    // Método para fazer uma entidade morrer e nascer outras
    public static void deathSpawnOthers(LivingEntity entity, EntityType spawnType, int count) {
        entity.setHealth(40); // Mata a entidade
        for (int i = 0; i < count; i++) {
            entity.getWorld().spawnEntity(entity.getLocation(), spawnType);
        }
    }

    // Método para fazer uma entidade morrer e soltar raios
    public static void deathStrikeLightning(LivingEntity entity, int count) {
        entity.setHealth(40); // Mata a entidade
        for (int i = 0; i < count; i++) {
            entity.getWorld().strikeLightning(entity.getLocation());
        }
    }

    public static void teleportRandomly(LivingEntity entity, double radius) {
        Location loc = entity.getLocation().clone();
        loc.add((Math.random() - 0.5) * 2 * radius, 0, (Math.random() - 0.5) * 2 * radius);
        entity.teleport(loc);
    }

    // 2. Curar entidade quando chove
    public static void healInRain(Player entity, int amount) {
        if (entity.getWorld().hasStorm()) {
            entity.setHealth(Math.min(entity.getHealth() + amount, entity.getMaxHealth()));
        }
    }

    // 3. Fazer a entidade perseguir o jogador mais próximo
    public static void pursueNearestPlayer(Mob entity) {
        Collection<Player> players = entity.getWorld().getPlayers();
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Player player : players) {
            double distance = player.getLocation().distanceSquared(entity.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        if (nearestPlayer != null) {
            entity.setTarget(nearestPlayer);
        }
    }

    // 4. Fazer a entidade evitar a luz do sol (para criaturas que queimam)
    public static void avoidSunlight(Mob entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
    }

    // 5. Fazer a entidade se camuflar (ficar invisível temporariamente)
    public static void camouflage(LivingEntity entity, int duration) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1));
    }

    // 6. Fazer a entidade deixar um rastro de fogo
    public static void leaveFireTrail(LivingEntity entity) {
        Location loc = entity.getLocation();
        Block block = loc.subtract(0, 1, 0).getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(Material.FIRE);
        }
    }

    // Método para lançar o jogador para trás ao ser atacado
    public static void knockbackOnAttack(Player player, double strength) {
        Vector direction = player.getLocation().getDirection();
        player.setVelocity(direction.multiply(-strength));
    }

    // Método para criar uma explosão quando a entidade é invocada
    public static void explodeOnSummon(Location location, float power) {
        Objects.requireNonNull(location.getWorld()).createExplosion(location, power);
    }

    // Método para spawnar uma entidade customizada
    public static LivingEntity spawnCustomEntity(Location location, EntityType entityType) {
        World world = location.getWorld();
        if (world == null) return null;

        LivingEntity entity = (LivingEntity) world.spawnEntity(location, entityType);

        // Exemplo: aplicar customizações aqui
        customizeEntity(entity);

        return entity;
    }

    // Método para personalizar a entidade
    private static void customizeEntity(LivingEntity entity) {
        // Exemplo: definir saúde
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        entity.setHealth(20.0);

        // Exemplo: aplicar efeito de poção
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        // Exemplo: equipar a entidade
        equipEntity(entity);
    }

    // Método para equipar a entidade
    private static void equipEntity(LivingEntity entity) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return;

        // Exemplo: equipar com armadura e itens
        equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
        equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        equipment.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.IRON_BOOTS));
        equipment.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
    }

    // Método para aplicar efeito de poção em uma entidade
    public static void applyPotionEffect(LivingEntity entity, PotionEffectType type, int duration, int amplifier) {
        PotionEffect potionEffect = new PotionEffect(type, duration, amplifier);
        entity.addPotionEffect(potionEffect);
    }

    // Métodos já existentes (spawnCustomEntity, customizeEntity, equipEntity, applyPotionEffect)

    // Método para dar itens a uma entidade
    public static void giveItemToEntity(LivingEntity entity, ItemStack item) {
        entity.getEquipment().setItemInMainHand(item);
    }

    // Método para aumentar o dano de uma entidade
    public static void increaseEntityDamage(LivingEntity entity, double amount) {
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(amount);
    }

    // Método para fazer a entidade spawnar outras entidades
    public static void spawnEntitiesAround(Entity entity, EntityType type, int count) {
        for (int i = 0; i < count; i++) {
            entity.getWorld().spawnEntity(entity.getLocation(), type);
        }
    }

    // Método para definir um alvo específico para a entidade
    public static void setEntityTarget(Mob entity, Entity target) {
        entity.setTarget((LivingEntity) target);
    }

    // Método para alterar o tamanho de uma entidade (Slime e Magma Cube)
    public static void setSize(Slime entity, int size) {
        entity.setSize(size);
    }

    public static Location getRandomLocationNearby(Location center, double radius) {
        Random random = new Random();

        double x = center.getX() + (radius * (200 * random.nextDouble() - 1));
        double y = center.getY();
        double z = center.getZ() + (radius * (200 * random.nextDouble() - 1));

        return new Location(center.getWorld(), x, y, z);
    }

    public static LivingEntity createCustomZombie(Location location) {
        World world = location.getWorld();
        if (world == null) return null;
        Location spawnLocation = getRandomLocationNearby(location, 5.0);

        Zombie zombie = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
        zombie.setAdult();
        applyPotionEffect(zombie, PotionEffectType.SPEED, 200, 100);
        increasePerceptionRange(zombie, 100);
        zombie.setHealth(4.0);

        EntityEquipment equipment = zombie.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
            equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            equipment.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            equipment.setBoots(new ItemStack(Material.IRON_BOOTS));
            equipment.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            equipment.setItemInOffHand(new ItemStack(Material.SHIELD));
        }

        AttributeInstance attackAttribute = zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackAttribute != null) {
            attackAttribute.setBaseValue(4.0);
        }

        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        return zombie;
    }

    public static LivingEntity createCustomSkeleton(Location location) {
        World world = location.getWorld();
        if (world == null) return null;
        Location spawnLocation = getRandomLocationNearby(location, 10.0);

        Skeleton skeleton = (Skeleton) world.spawnEntity(spawnLocation, EntityType.SKELETON);
        applyPotionEffect(skeleton, PotionEffectType.SPEED, 500, 100);
        increasePerceptionRange(skeleton, 200);
        skeleton.setHealth(20.0);

        EntityEquipment equipment = skeleton.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
            equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            equipment.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            equipment.setBoots(new ItemStack(Material.IRON_BOOTS));
        }

        AttributeInstance attackAttribute = skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackAttribute != null) {
            attackAttribute.setBaseValue(10.0);
        }


        return skeleton;
    }


    // Método para aplicar efeito de invisibilidade
    public static void applyInvisibility(LivingEntity entity, int duration) {
        applyPotionEffect(entity, PotionEffectType.INVISIBILITY, duration, 0);
    }

    public static void setBreakDoors(Entity entity, boolean breakDoor) {
        if (entity instanceof Zombie) {
            ((Zombie) entity).setCanBreakDoors(breakDoor);
        }
        if (entity instanceof Skeleton) {
            ((Skeleton) entity).setAware(breakDoor);
        }
    }

    // Método para aumentar a percepção (range) de uma entidade
    public static void increasePerceptionRange(LivingEntity entity, double range) {
        entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(range);
    }

    // Método para fazer uma entidade voar (apenas para morcegos)
    public static void makeEntityFly(Bat entity, boolean canFly) {
        entity.setAwake(canFly);
    }

    // Método para fazer uma entidade explodir ao morrer
    public static void explodeOnDeath(LivingEntity entity, float power) {
        entity.getWorld().createExplosion(entity.getLocation(), power);
    }
}
