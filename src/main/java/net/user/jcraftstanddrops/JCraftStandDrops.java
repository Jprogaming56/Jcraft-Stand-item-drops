package net.user.jcraftstanddrops;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(JCraftStandDrops.MOD_ID)
public class JCraftStandDrops {
    public static final String MOD_ID = "jcraftstanddrops";

    public JCraftStandDrops() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(new StandDropHandler());
    }
}
