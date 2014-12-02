package moze_intel.ssr;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import moze_intel.ssr.commands.KillCMD;
import moze_intel.ssr.events.CreateShardEvent;
import moze_intel.ssr.events.PlayerKillEntityEvent;
import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(modid = SSRCore.ID, name = SSRCore.NAME, version = SSRCore.VERSION)
public class SSRCore
{
    public static final String ID = "SSR";
    public static final String NAME = "Soul Shards Reborn";
    public static final String VERSION = "@VERSION@";

    public static File CONFIG_DIR;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CONFIG_DIR = new File(event.getModConfigurationDirectory(), "SSR");

        if (!CONFIG_DIR.exists())
        {
            CONFIG_DIR.mkdirs();
        }

        SSRConfig.init(new File(CONFIG_DIR, "SSRMain.cfg"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ObjHandler.registerObjs();

        MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
        MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        EntityMapper.init();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent eventt)
    {
        eventt.registerServerCommand(new KillCMD());
    }
}
