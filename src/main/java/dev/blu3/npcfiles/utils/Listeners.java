package dev.blu3.npcfiles.utils;

import com.pixelmongenerations.common.entity.npcs.*;
import com.pixelmongenerations.core.enums.EnumNPCType;
import net.minecraft.entity.Entity;
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
        if (Utils.inSelectMode.containsKey(p.getUniqueId())) {
            if (Utils.inSelectMode.get(p.getUniqueId()).booleanValue()) {
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
                    p.sendMessage(Utils.toText("&8(&bNPCFiles&8) &a&l" + name + " &awas stored to file."));
                } else {
                    p.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cThis is not a Pixelmon NPC."));
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
