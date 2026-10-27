package net.user.jcraftstanddrops;

import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Method;

/**
 * Talks to JCraft through reflection so this addon does not need the JCraft jar to compile.
 * Uses JComponentPlatformUtils.getStandComponent(LivingEntity).getType() and treats a null
 * type or the "none" stand type as "no stand".
 */
final class JCraftBridge {
    private static boolean initialised;
    private static boolean available;
    private static Method getStandComponent;
    private static Method getType;
    private static Method getId;
    private static Method getUserIfStand;
    private static Method getMaster;

    private JCraftBridge() {}

    private static synchronized void init() {
        if (initialised) return;
        initialised = true;
        try {
            Class<?> utils = Class.forName("net.arna.jcraft.platform.JComponentPlatformUtils");
            Class<?> component = Class.forName("net.arna.jcraft.api.component.living.CommonStandComponent");
            Class<?> standType = Class.forName("net.arna.jcraft.api.stand.StandType");
            getStandComponent = utils.getMethod("getStandComponent", LivingEntity.class);
            getType = component.getMethod("getType");
            getId = standType.getMethod("getId");
            available = true;
            try {
                Class<?> jutils = Class.forName("net.arna.jcraft.common.util.JUtils");
                getUserIfStand = jutils.getMethod("getUserIfStand", net.minecraft.world.entity.Entity.class);
            } catch (Throwable ignored) { }
            try {
                getMaster = Class.forName("net.arna.jcraft.common.util.IOwnable").getMethod("getMaster");
            } catch (Throwable ignored) { }
        } catch (Throwable t) {
            available = false;
            org.apache.logging.log4j.LogManager.getLogger(JCraftStandDrops.MOD_ID)
                    .error("Could not hook into JCraft's stand API; stand drops are disabled.", t);
        }
    }

    static boolean hasStand(LivingEntity entity) {
        init();
        if (!available) return false;
        try {
            Object comp = getStandComponent.invoke(null, entity);
            if (comp == null) return false;
            Object type = getType.invoke(comp);
            if (type == null) return false;
            Object id = getId.invoke(type);
            return id == null || !id.toString().endsWith(":none");
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Follows stands / owned JCraft entities (bombs, clones, etc.) / projectiles back to the
     * player behind them. Returns the entity itself if nothing better is found.
     */
    static net.minecraft.world.entity.Entity resolveOwner(net.minecraft.world.entity.Entity start) {
        init();
        net.minecraft.world.entity.Entity cur = start;
        for (int i = 0; i < 4 && cur != null && !(cur instanceof net.minecraft.world.entity.player.Player); i++) {
            net.minecraft.world.entity.Entity next = null;
            try {
                if (available && getUserIfStand != null) {
                    Object o = getUserIfStand.invoke(null, cur);
                    if (o instanceof net.minecraft.world.entity.Entity e && e != cur) next = e;
                }
                if (next == null && getMaster != null && getMaster.getDeclaringClass().isInstance(cur)) {
                    Object o = getMaster.invoke(cur);
                    if (o instanceof net.minecraft.world.entity.Entity e && e != cur) next = e;
                }
            } catch (Throwable ignored) { }
            if (next == null && cur instanceof net.minecraft.world.entity.projectile.Projectile p) {
                next = p.getOwner();
            }
            if (next == null) break;
            cur = next;
        }
        return cur;
    }
}
