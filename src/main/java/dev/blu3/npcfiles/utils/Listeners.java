package dev.blu3.npcfiles.utils;

import com.pixelmongenerations.common.entity.npcs.*;
import com.pixelmongenerations.core.enums.EnumNPCType;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class Listeners {
    @SubscribeEvent
    public void onNPCInteract(PlayerInteractEvent.EntityInteract event) throws IOException {
        if (Utils.inSelectMode.containsKey(event.getEntityPlayer().getUniqueID())) {
            EntityPlayer player = event.getEntityPlayer();
            if (Utils.inSelectMode.get(player.getUniqueID())) {
                if (event.getTarget() instanceof EntityNPC) {
                    String name = Utils.npcFileName.get(player.getUniqueID());
                    EnumNPCType npcType = null;
                    if (event.getTarget() instanceof NPCNurseJoy) {
                        npcType = EnumNPCType.NurseJoy;
                    } else if (event.getTarget() instanceof NPCTrainer) {
                        npcType = EnumNPCType.Trainer;
                    } else if (event.getTarget() instanceof NPCChatting) {
                        npcType = EnumNPCType.ChattingNPC;
                    } else if (event.getTarget() instanceof NPCRelearner) {
                        npcType = EnumNPCType.Relearner;
                    } else if (event.getTarget() instanceof NPCShopkeeper) {
                        npcType = EnumNPCType.Shopkeeper;
                    } else if (event.getTarget() instanceof NPCTrader) {
                        npcType = EnumNPCType.Trader;
                    } else if (event.getTarget() instanceof NPCTutor) {
                        npcType = EnumNPCType.Tutor;
                    }
                    System.out.println(event.getTarget().serializeNBT().toString());
                    Utils.writeToGSON(name, npcType, event.getTarget().serializeNBT().toString(), false);
                    player.sendMessage(new TextComponentString(Utils.regex("&e" + name + " &bwas stored to file.")));
                } else {
                    player.sendMessage(new TextComponentString(Utils.regex("&cThis is not a Pixelmon NPC.")));
                }
                Utils.inSelectMode.put(player.getUniqueID(), false);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (Utils.inSelectMode.containsKey(event.player.getUniqueID()))
            Utils.inSelectMode.put(event.player.getUniqueID(), Boolean.FALSE);
    }
}
