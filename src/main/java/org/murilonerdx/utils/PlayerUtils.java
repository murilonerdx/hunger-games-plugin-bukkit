package org.murilonerdx.utils;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;

import java.util.*;

import static org.murilonerdx.utils.ItemsUtils.*;


public class PlayerUtils {


    public static void trapPlayerInIce(Location player) {
        World world = player.getWorld();
        createIceCube(world, player.clone().add(0, 1, 0)); // Evita prender os pés do jogador no gelo

        // Agendar para derreter o gelo
        Bukkit.getScheduler().runTaskLater(Hungergames.instance, () -> meltIceCube(world, player.clone().add(0, 1, 0)), 100L);
    }

    public static void applyHeavyGravity(Location player) {
        int duration = 20 * 30; // 30 segundos, por exemplo
        int amplifier = 3; // Intensidade do efeito de lentidão

        PotionEffect heavyGravityEffect = new PotionEffect(PotionEffectType.SLOW, duration, amplifier);

        Objects.requireNonNull(player.getWorld()).getPlayers().forEach(
                t -> t.addPotionEffect(heavyGravityEffect)
        );
    }

    private static void meltIceCube(World world, Location center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location loc = center.clone().add(x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.ICE) {
                        world.getBlockAt(loc).setType(Material.WATER);
                    }
                }
            }
        }
    }

    public static void createIceCube(World world, Location center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -5; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location loc = center.clone().add(x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.AIR) {
                        world.getBlockAt(loc).setType(Material.ICE);
                    }
                }
            }
        }
    }

    public static void applyWitherEffect(Location player) {
        Objects.requireNonNull(player.getWorld()).getPlayers().forEach(
                t -> t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 1))
        );
    }

    public static void createPoisonousFog(Location player) {
        List<Player> players = player.getWorld().getPlayers();

        World world = player.getWorld();



        Bukkit.getScheduler().runTaskTimer(Hungergames.instance, (it) -> {
            if (!world.isThundering()) {
                it.cancel();
                return;
            }
            world.spawnParticle(Particle.SPELL_MOB, player, 30, 3, 3, 3, 1);
            players.forEach(
                    t -> t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1))
            );
        }, 0L, 500L);

        // Parar após 10 segundos
        Bukkit.getScheduler().runTaskLater(Hungergames.instance, () -> {
            players.forEach(
                    t -> t.removePotionEffect(PotionEffectType.POISON)
            );
        }, 600L);
    }

    public static boolean isLightSourceNearby(Player player) {
        int radius = 20; // Define o raio de busca por fontes de luz
        Location loc = player.getLocation();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);

                    // Verifica se o bloco é uma fonte de luz
                    if (isLightEmittingBlock(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Location getRandomLocationWithinBorder(World world, WorldBorder border) {
        Random random = new Random();
        double size = border.getSize();
        Location center = border.getCenter();

        double x = center.getX() + random.nextDouble() * size - size / 2;
        double z = center.getZ() + random.nextDouble() * size - size / 2;
        double y = world.getHighestBlockYAt((int) x, (int) z); // Obtém a altura do bloco mais alto naquela posição

        return new Location(world, x, y, z);
    }

    private static boolean isLightEmittingBlock(Block block) {
        // Lista de materiais que emitem luz. Adicione mais conforme necessário.
        Material[] lightEmittingBlocks = {
                Material.TORCH,
                Material.LANTERN,
                Material.GLOWSTONE,
                Material.LAVA, // Note que lava também é considerada uma fonte de luz
                // Adicione outros blocos que emitem luz aqui
        };

        for (Material mat : lightEmittingBlocks) {
            if (block.getType() == mat) {
                return true;
            }
        }

        return false;
    }

    public static void checkDarkness(Player player) {
        if (player.getLocation().getBlock().getLightLevel() < 7 && !isLightSourceNearby(player)) {
            // Aplica o efeito de cegueira se estiver escuro e não houver fonte de luz por perto
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 60, 1));
        } else {
            // Remove o efeito de cegueira se a iluminação for suficiente ou houver uma fonte de luz por perto
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    public static void summonZombieAttack(Location player) {
        for (int i = 0; i < 20; i++) {
            EntityUtils.createCustomZombie(player);
        }
    }

    public static void summonSkeletonAttack(Location player) {


        for (int i = 0; i < 30; i++) {
            EntityUtils.createCustomSkeleton(player);
        }
    }

    public static void createChestWithItems(Location location) {
        if (location != null) {

            // Determinar a direção para onde o jogador está olhando e ajustar a localização
            location = location.add(location.getDirection().multiply(2));
            location.setY(Math.floor(location.getY() + 1)); // Garantir que a localização está no chão

            // Criar o baú
            Block block = location.getBlock();
            block.setType(Material.CHEST);

            // Verificar se o bloco é realmente um baú (para evitar erros)
            if (block.getState() instanceof Chest chest) {
                Inventory chestInventory = chest.getBlockInventory();

                KitEnum[] kits = KitEnum.values();

                KitEnum kit = kits[new Random().nextInt(kits.length)];
                List<ItemStack> kitShow = getKit(kit.name());

                kitShow.forEach(t ->
                        chestInventory.addItem(new ItemStack(t.getType(), t.getAmount()))
                );

                Hungergames.playersInGame.forEach(p -> {
                    Player player = Bukkit.getPlayer(p);
                    player.sendMessage(kit.getColor() + "Items com " + kit.getDescricao() + " foram dropados em um bau nessa localização ->" + ChatColor.BLUE + " X: "
                            + chestInventory.getLocation().getX()
                            + ChatColor.LIGHT_PURPLE + " Y: " + chestInventory.getLocation().getY() + ChatColor.GREEN + " Z: " + chestInventory.getLocation().getZ()
                    );
                });
            }
        }

    }

    public static List<ItemStack> getKit(String kitName) {
        return switch (kitName.toUpperCase()) {
            case "KIT_RARO" -> List.of(
                    new ItemStack(Material.DIAMOND_SWORD, 1),
                    new ItemStack(Material.DIAMOND_HELMET, 1),
                    new ItemStack(Material.DIAMOND_BOOTS, 1),
                    new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                    new ItemStack(Material.GOLDEN_APPLE, 8),
                    new ItemStack(Material.DARK_OAK_WOOD, 44),
                    new ItemStack(Material.DIAMOND, 18),
                    new ItemStack(Material.ARROW, 21),
                    new ItemStack(Material.BOW, 1),
                    createLightningProtectionItem(),
                    createJumpBoots(),
                    createSpecialBow("IceWallBow"),
                    createSpecialArrow("IceWallArrow", 18)
            );
            case "KIT_FULL" -> List.of(
                    new ItemStack(Material.DIAMOND_SWORD, 1),
                    new ItemStack(Material.DIAMOND_HELMET, 1),
                    new ItemStack(Material.DIAMOND_BOOTS, 1),
                    new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                    new ItemStack(Material.GOLDEN_APPLE, 8),
                    new ItemStack(Material.DARK_OAK_WOOD, 64),
                    new ItemStack(Material.DIAMOND, 31),
                    new ItemStack(Material.ARROW, 61),
                    new ItemStack(Material.BOW, 1),
                    createFireBoots()
            );
            case "KIT_CURA" -> List.of(
                    new ItemStack(Material.GOLDEN_APPLE, 18),
                    new ItemStack(Material.BEETROOT_SOUP, 18),
                    new ItemStack(Material.MELON, 28),
                    new ItemStack(Material.EGG, 18),
                    createSpeedInvisibilityItem(), 
                    createItemCustomCure(),
                    createItemCustomCure(),
                    createSpecialBow("LightningBow"),
                    createSpecialArrow("LightningArrow", 18)
            );

            case "KIT_BASICO" -> List.of(
                    new ItemStack(Material.GOLDEN_APPLE, 2),
                    new ItemStack(Material.IRON_SWORD, 1),
                    new ItemStack(Material.MELON, 28),
                    new ItemStack(Material.EGG, 5),
                    new ItemStack(Material.ARROW, 34),
                    new ItemStack(Material.BOW, 1),
                    createwWaterIceBoots(),
                    createSpecialBow("ExplosiveBow"),
                    createSpecialArrow("ExplosiveArrow", 18)
            );
            default -> null;
        };
    }

    public static void startPoisonRain(Location location) {
        List<Player> players = location.getWorld().getPlayers();


        int dx = (int) (Math.random() * 40) - 20;
        int dz = (int) (Math.random() * 40) - 20;
        int x = location.getBlockX() + dx;
        int z = location.getBlockZ() + dz;
        int y = location.getWorld().getHighestBlockYAt(x, z);

        Location loc = new Location(location.getWorld(), x, y, z);

        Bukkit.getScheduler().runTaskLater(Hungergames.instance, (it) -> {
            if (!location.getWorld().isThundering()) {
                it.cancel();
                return;
            }

            players.forEach(
                    p -> {
                        if (Objects.requireNonNull(loc.getWorld()).hasStorm() && isPlayerOutdoors(p)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1)); // Duração de 5 segundos
                        }
                    }
            );
        }, 500L);

        // Parar após 10 segundos
        Bukkit.getScheduler().runTaskLater(Hungergames.instance, () -> {
            players.forEach(
                    t -> t.removePotionEffect(PotionEffectType.POISON)
            );
        }, 600L);

    }

    public static void startBlindnessRain(Location player) {
        List<Player> players = Objects.requireNonNull(player.getWorld()).getPlayers();
        boolean stormBlindnessActive = true;

        Bukkit.getScheduler().runTaskLater(Hungergames.instance, (it) -> {
            if (!player.getWorld().isThundering()) {
                it.cancel();
                return;
            }
            players.forEach(
                    p -> {
                        if (Objects.requireNonNull(player.getWorld()).hasStorm() && isPlayerOutdoors(p) && stormBlindnessActive) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1)); // Duração de 5 segundos
                        }
                    }
            );
        }, 500L);

        // Parar após 10 segundos
        Bukkit.getScheduler().runTaskLater(Hungergames.instance, () -> {
            players.forEach(
                    t -> t.removePotionEffect(PotionEffectType.BLINDNESS)
            );
        }, 600L);
    }

    public static Location findHighestDensityLocation() {
        Map<Location, Integer> playerDensity = new HashMap<>();

        // Percorre todos os jogadores no jogo e calcula a densidade em cada localização
        for (UUID uuid : Hungergames.playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            Location playerLocation = player.getLocation();
            Location roundedLocation = roundLocation(playerLocation);

            playerDensity.put(roundedLocation, playerDensity.getOrDefault(roundedLocation, 0) + 1);
        }

        // Encontra a localização com a maior densidade
        Location highestDensityLocation = null;
        double highestDensity = 0;

        for (Location location : playerDensity.keySet()) {
            int density = playerDensity.get(location);
            if (density > highestDensity) {
                highestDensity = density;
                highestDensityLocation = location;
            }
        }

        return highestDensityLocation;
    }

    private static Location roundLocation(Location location) {
        // Ajuste estes valores para alterar a "resolução" da sua grade
        int gridBlockSize = 100;

        double x = Math.round(location.getX() / gridBlockSize) * gridBlockSize;
        double y = Math.round(location.getY() / gridBlockSize) * gridBlockSize;
        double z = Math.round(location.getZ() / gridBlockSize) * gridBlockSize;

        return new Location(location.getWorld(), x, y, z);
    }

    private static boolean isPlayerOutdoors(Player player) {
        Location loc = player.getLocation();
        return loc.getWorld().getHighestBlockYAt(loc) <= loc.getY();
    }

    public static void spawnAngryBees(Location player) {
        World world = player.getWorld();

        for (int i = 0; i < 5; i++) { // Spawnar 5 abelhas
            Bee bee = (Bee) world.spawnEntity(player, EntityType.BEE);
            bee.setAI(true);
            bee.setAdult();
            bee.setPersistent(true);
        }
    }

    public static void floodAreaAroundPlayer(Location player) {
        World world = player.getWorld();

        int radius = 20; // Raio da inundação
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location loc = player.clone().add(x, 0, z);
                loc.setY(world.getHighestBlockYAt(loc)); // Encontra o bloco mais alto
                if (loc.getBlock().getType() == Material.AIR) {
                    loc.getBlock().setType(Material.WATER);
                }
            }
        }

        // Agendar para remover a água após um tempo
        Bukkit.getScheduler().runTaskLater(Hungergames.instance, () -> {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = player.clone().add(x, 0, z);
                    if (loc.getBlock().getType() == Material.WATER) {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }, 100L); // 5 segundos em ticks
    }

    public static void removeNearbyEndermen(Player player, double radius) {
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Enderman) {
                entity.remove(); // Remove o Enderman
            }
        }
    }

    public static void startCustomized(Location player, Particle particle, Material material) {
        World world = player.getWorld();
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(1200); // Duração da tempestade em ticks (5 minutos)
        world.setThunderDuration(1200);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < 20; i++) {
                    double x = player.getX() + (Math.random() - 0.5) * 30;
                    double z = player.getZ() + (Math.random() - 0.5) * 30;
                    double y = world.getHighestBlockYAt((int) x, (int) z);

                    Location location = new Location(world, x, y, z);
                    world.spawnParticle(particle, location, 30, 0.5, 0.5, 0.5, 0.01);
                    world.setType(location, material);
                    // Causar dano ou explosão

                    Location strikeLocation = new Location(player.getWorld(), x, y, z);
                    player.getWorld().strikeLightning(strikeLocation);
                    world.createExplosion(strikeLocation, 0.8F, true, true); // Cria uma explosão sem fogo
                }
            }
        }.runTaskTimer(Hungergames.instance, 0L, 20L); // Executa a cada segundo
    }

    public static void freezePlayer(Location location, long duration) {
        location.getWorld().getPlayers().forEach(
                player -> {
                    PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, (int) duration, 255, false, false, false);
                    player.addPotionEffect(slowness);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }.runTaskLater(Hungergames.instance, duration);
                }
        );

    }

    public static void applyNauseaEffect(Location player) {
        int duration = 10 * 20; // 10 segundos (20 ticks por segundo)
        int amplifier = 1; // Intensidade do efeito

        PotionEffect nauseaEffect = new PotionEffect(PotionEffectType.CONFUSION, duration, amplifier);
        player.getWorld().getPlayers().forEach(t -> {
            t.addPotionEffect(nauseaEffect);
        });
    }

    public static void spawnParticlesNearPlayer(Location location, Particle particle, int qtdParticles) {

        // Ajuste a localização conforme necessário
        location.add(location.getX(), 1, location.getZ()); // Exemplo: partículas 1 bloco acima do jogador

        location.getWorld().getPlayers().forEach(player -> {
            player.getWorld().spawnParticle(particle, location, qtdParticles); // 10 é o número de partículas
        });
        // Spawn de partículas
    }

    public static void giveCursedItem(Player player) {
        ItemStack cursedItem = new ItemStack(Material.DIAMOND_SWORD); // Exemplo de item
        ItemMeta meta = cursedItem.getItemMeta();
        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "cursed_speed", -0.1, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.OFF_HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedModifier);

        // Exemplo: Aumento de dano
        AttributeModifier damageModifier = new AttributeModifier(UUID.randomUUID(), "cursed_damage", -0.4, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_LUCK, damageModifier);

        // Exemplo: Aumento de dano
        AttributeModifier random = new AttributeModifier(UUID.randomUUID(), "cursed_life", -0.8, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, random);

        cursedItem.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        meta.setDisplayName("Item Amaldiçoado"); // Nome do item
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.setLocalizedName("Os demonios do inferno te deram de presente");
        // Adicionar identificador único, pode ser um lore ou algo similar
        meta.setLore(Arrays.asList("Este item está amaldiçoado"));
        cursedItem.setItemMeta(meta);

        player.getInventory().addItem(cursedItem);
    }

    public static void strikePlayerWithLightning(Location locationWorld) {
        // Obtem a localização atual do jogador


        // Obtem o mundo em que o jogador está
        World world = locationWorld.getWorld();

        // Faz um raio cair na localização do jogador
        world.strikeLightning(locationWorld);
    }


    public static void startSuperStorm(Location locationWorld) {
        World world = locationWorld.getWorld();
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(1200); // Duração da tempestade em ticks (5 minutos)
        world.setThunderDuration(1200);

        // Cria raios frequentes e explosões
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!locationWorld.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                // Raios aleatórios e pequenas explosões
                for (int i = 0; i < 10; i++) {
                    // Escolher uma localização aleatória próxima ao jogador
                    double x = locationWorld.getX() + (Math.random() - 0.5) * 50;
                    double z = locationWorld.getZ() + (Math.random() - 0.5) * 50;
                    double y = locationWorld.getWorld().getHighestBlockYAt((int) x, (int) z);

                    Location strikeLocation = new Location(locationWorld.getWorld(), x, y, z);
                    locationWorld.getWorld().strikeLightning(strikeLocation);

                    // Para explosões, você pode adicionar:
                    locationWorld.getWorld().createExplosion(strikeLocation, 6F); // Explosão sem fogo
                    locationWorld.getWorld().spawnEntity(strikeLocation, EntityType.CREEPER);
                }


            }
        }.runTaskTimer(Hungergames.instance, 0L, 20L); // Executa a cada segundo

        new BukkitRunnable() {
            @Override
            public void run() {
                world.setStorm(false);
                world.setThundering(false);
                world.setWeatherDuration(0);
                world.setThunderDuration(0);
            }
        }.runTaskLater(Hungergames.instance, 1200L);
    }

    public static void startWaterStorm(Location player, int durationMinutes) {
        World world = player.getWorld();

        int durationtotal = durationMinutes * 1200;

        player.getWorld().setStorm(true);
        player.getWorld().setThundering(true);
        player.getWorld().setWeatherDuration(durationtotal); // Duração da tempestade em ticks (5 minutos)
        player.getWorld().setThunderDuration(durationtotal);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                // Gera água em um raio de 20 blocos ao redor do jogador
                for (int i = 0; i < 10; i++) {
                    int dx = (int) (Math.random() * 40) - 20;
                    int dz = (int) (Math.random() * 40) - 20;
                    int x = player.getBlockX() + dx;
                    int z = player.getBlockZ() + dz;
                    int y = world.getHighestBlockYAt(x, z);

                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();

                    // Verifica se o bloco pode ser substituído por água
                    if (block.getType() == Material.AIR || block.getType() == Material.GRASS) {
                        block.setType(Material.WATER);
                    }
                }
            }
        }.runTaskTimer(Hungergames.instance, 20L, 20L); // Executa a cada segundo
    }

    public static void spawnSoundNearPlayer(Player player, Sound sound) {
        Location location = player.getLocation();
        // Ajuste a localização conforme necessário
        location.add(location.getX(), 1, location.getZ()); // Exemplo: partículas 1 bloco acima do jogador
        player.playSound(location, sound, 20, 20);
    }

    private boolean playerMovedOutOfLavaTrap(Location currentLocation, int size) {
        // Pega as coordenadas do centro do cubo de lava
        double centerX = currentLocation.getX();
        double centerY = currentLocation.getY();
        double centerZ = currentLocation.getZ();

        int halfSize = size / 2;

        // Verifica se o jogador está fora dos limites do cubo de lava
        return currentLocation.getX() > centerX + halfSize ||
                currentLocation.getX() < centerX - halfSize ||
                currentLocation.getY() > centerY + halfSize ||
                currentLocation.getY() < centerY - halfSize ||
                currentLocation.getZ() > centerZ + halfSize ||
                currentLocation.getZ() < centerZ - halfSize;
    }

    private void createLavaCube(Location center, int size) {
        World world = center.getWorld();
        int halfSize = size / 2;

        for (int x = -halfSize; x <= halfSize; x++) {
            for (int y = -halfSize; y <= halfSize; y++) {
                for (int z = -halfSize; z <= halfSize; z++) {
                    if (x == halfSize || x == -halfSize || y == halfSize || y == -halfSize || z == halfSize || z == -halfSize) {
                        Location loc = center.clone().add(x, y, z);
                        world.getBlockAt(loc).setType(Material.LAVA);
                    }
                }
            }
        }
    }

}
