package dev.blu3.npcfiles;

import com.google.inject.Inject;
import dev.blu3.npcfiles.commands.Base;
import dev.blu3.npcfiles.utils.Listeners;
import dev.blu3.npcfiles.utils.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@Plugin(id = NPCFiles.ID,
        name = NPCFiles.NAME,
        version = NPCFiles.VERSION
)
public class NPCFiles
{
    public static final String ID = "npcfiles";
    public static final String NAME = "NPCFiles";
    public static final String VERSION = "1.0.1";

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File file;

    @Inject
    private Logger logger;

    private static NPCFiles instance;

    @Listener
    public void onInit(GameInitializationEvent event) {
        instance = this;

        Sponge.getEventManager().registerListeners(this, new Listeners());

        logger.info("Setting up NPCFiles...");

        this.loadConfig();

        logger.info("NPCFiles has been enabled!");
    }

    private void loadConfig() {
        try {
            // If there's no config file then create it
            if (!file.exists()) {
                file.createNewFile();
                Utils.writeNewGSON();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Listener
    public void onServerStarted(GameStartedServerEvent e) {
        Sponge.getCommandManager().register(instance, Base.build(), "npcfiles", "nfiles", "npcf", "nf");
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static File getFile() {
        return instance.file;
    }

}
