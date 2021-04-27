package dev.blu3.npcfiles;

import dev.blu3.npcfiles.commands.NPCFilesCMD;
import dev.blu3.npcfiles.utils.Listeners;
import dev.blu3.npcfiles.utils.Utils;
import java.io.File;
import java.io.IOException;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "npcfiles", name = "NPCFiles", version = "1.0.1", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]")
public class NPCFiles {
    public static final String MOD_ID = "npcfiles";

    public static final String MOD_NAME = "NPCFiles";

    public static final String VERSION = "1.0.1";

    public static File file;

    @Instance("npcfiles")
    public static NPCFiles INSTANCE;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register(new Listeners());
        File directory = new File(event.getModConfigurationDirectory(), "NPCFiles");
        if (!directory.exists())
            directory.mkdir();
        file = new File(directory, "data.json");
        if (!file.exists()) {
            file.createNewFile();
            Utils.writeNewGSON();
        }
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand)new NPCFilesCMD());
    }
}
