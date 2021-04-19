package dev.blu3.npcfiles.utils;

import com.pixelmongenerations.common.entity.npcs.*;
import com.pixelmongenerations.core.enums.EnumNPCType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.io.IOException;

public class Listeners
{

    @Listener
    public void onNPCInteract(final InteractEntityEvent.Secondary event, @Root final Player p) throws IOException {
        Sponge.getServer().getBroadcastChannel().send(Utils.toText("!!! interact 1"));
        if (Utils.inSelectMode.containsKey(p.getUniqueId())) {
            Sponge.getServer().getBroadcastChannel().send(Utils.toText("!!! interact 2"));
            if (Utils.inSelectMode.get(p.getUniqueId()).booleanValue()) {
                Sponge.getServer().getBroadcastChannel().send(Utils.toText("!!! interact 3"));
                if (event.getTargetEntity() instanceof EntityNPC) {
                    String name = Utils.npcFileName.get(p.getUniqueId());
                    EnumNPCType npcType = null;
                    if (event.getTargetEntity() instanceof NPCNurseJoy) {
                        npcType = EnumNPCType.NurseJoy;
                    } else if (event.getTargetEntity() instanceof NPCTrainer) {
                        npcType = EnumNPCType.Trainer;
                    } else if (event.getTargetEntity() instanceof NPCChatting) {
                        npcType = EnumNPCType.ChattingNPC;
                    } else if (event.getTargetEntity() instanceof NPCRelearner) {
                        npcType = EnumNPCType.Relearner;
                    } else if (event.getTargetEntity() instanceof NPCShopkeeper) {
                        npcType = EnumNPCType.Shopkeeper;
                    } else if (event.getTargetEntity() instanceof NPCTrader) {
                        npcType = EnumNPCType.Trader;
                    } else if (event.getTargetEntity() instanceof NPCTutor) {
                        npcType = EnumNPCType.Tutor;
                    }
                    Utils.writeToGSON(name, npcType, ((Entity) event.getTargetEntity()).serializeNBT().toString(), false);
                    p.sendMessage(Utils.toText("&e" + name + " &bwas stored to file."));
                } else {
                    p.sendMessage(Utils.toText("&cThis is not a Pixelmon NPC."));
                }
                Utils.inSelectMode.put(p.getUniqueId(), false);
            }
        }
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {
        if (event.getTargetEntity().getPlayer().isPresent()) {
            if (Utils.inSelectMode.containsKey(event.getTargetEntity().getPlayer().get().getUniqueId()))
                Utils.inSelectMode.put(event.getTargetEntity().getPlayer().get().getUniqueId(), false);
        }
    }

}
