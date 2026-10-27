package net.user.jcraftstanddrops;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class StandDropHandler {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        if (victim instanceof Player && !Config.INCLUDE_PLAYERS.get()) return;

        if (Config.REQUIRE_PLAYER_KILL.get() && !killedByPlayer(event)) return;

        if (!JCraftBridge.hasStand(victim)) return;

        double chance = Config.DROP_CHANCE.get() + Config.LOOTING_BONUS.get() * event.getLootingLevel();
        if (victim.getRandom().nextDouble() >= chance) return;

        ResourceLocation id = ResourceLocation.tryParse(Config.DROP_ITEM.get());
        if (id == null) return;
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == Items.AIR) return;

        int min = Config.MIN_COUNT.get();
        int max = Math.max(min, Config.MAX_COUNT.get());
        int count = min + victim.getRandom().nextInt(max - min + 1);

        ItemEntity drop = new ItemEntity(victim.level(), victim.getX(), victim.getY(), victim.getZ(),
                new ItemStack(item, count));
        drop.setDefaultPickUpDelay();
        event.getDrops().add(drop);
    }

    /** True if a player, or a stand / owned entity / projectile belonging to a player, dealt the kill. */
    private static boolean killedByPlayer(LivingDropsEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (killer != null && JCraftBridge.resolveOwner(killer) instanceof Player) return true;
        return direct != null && JCraftBridge.resolveOwner(direct) instanceof Player;
    }
}
