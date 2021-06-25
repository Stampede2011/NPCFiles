package dev.blu3.npcfiles.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmongenerations.common.entity.npcs.*;
import com.pixelmongenerations.core.enums.EnumNPCType;
import dev.blu3.npcfiles.NPCFiles;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Utils {
    public static HashMap<UUID, Boolean> inSelectMode = new HashMap();
    public static HashMap<UUID, String> npcFileName = new HashMap();

    public static Text toText(String msg) {
        return TextSerializers.FORMATTING_CODE.deserialize(msg);
    }

    public static Entity createNPC(double x, double y, double z, World world, EnumNPCType npcType, NBTTagCompound tag, Player player) {
        NPCTutor tutor;
        NPCTrader trader;
        NPCShopkeeper shopkeeper;
        NPCRelearner relearner;
        NPCChatting chatting;
        NPCTrainer trainer;
        NPCNurseJoy nurseJoy;

        switch (npcType) {
            case NurseJoy:
                nurseJoy = new NPCNurseJoy(world);
                nurseJoy.func_70037_a(tag);
                nurseJoy.setUniqueId(MathHelper.getRandomUUID());
                nurseJoy.setPosition(x, y, z);
                return nurseJoy;
            case Trainer:
                trainer = new NPCTrainer(world);
                if (tag != null) {
                    trainer.func_70037_a(tag);
                }
                trainer.setUniqueId(MathHelper.getRandomUUID());
                trainer.setPosition(x, y, z);
                return trainer;
            case ChattingNPC:
                chatting = new NPCChatting(world);
                chatting.func_70037_a(tag);
                chatting.setUniqueId(MathHelper.getRandomUUID());
                chatting.setPosition(x, y, z);
                return chatting;
            case Relearner:
                relearner = new NPCRelearner(world);
                relearner.func_70037_a(tag);
                relearner.setUniqueId(MathHelper.getRandomUUID());
                relearner.setPosition(x, y, z);
                return relearner;
            case Shopkeeper:
                shopkeeper = new NPCShopkeeper(world);
                shopkeeper.func_70037_a(tag);
                shopkeeper.setUniqueId(MathHelper.getRandomUUID());
                shopkeeper.setPosition(x, y, z);
                return shopkeeper;
            case Trader:
                trader = new NPCTrader(world);
                trader.func_70037_a(tag);
                trader.setUniqueId(MathHelper.getRandomUUID());
                trader.setPosition(x, y, z);
                return trader;
            case Tutor:
                tutor = new NPCTutor(world);
                tutor.func_70037_a(tag);
                tutor.setUniqueId(MathHelper.getRandomUUID());
                tutor.setPosition(x, y, z);
                return tutor;
        }
        player.sendMessage(Utils.toText("Error creating NPC of type: " + npcType));
        return null;
    }

    static Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private static HashMap<String, HashMap<EnumNPCType, String>> dataMap;

    public static void writeNewGSON() throws IOException {
        FileWriter gsonWriter = new FileWriter(NPCFiles.getFile());
        DataGSON datajson = new DataGSON();
        HashMap<String, HashMap<EnumNPCType, String>> dataInfoMap = new HashMap<>();
        datajson.setData(dataInfoMap);
        gsonWriter.write(gson.toJson(datajson));
        gsonWriter.close();
    }

    public static HashMap<String, HashMap<EnumNPCType, String>> getDataMap() {
        final FileReader gsonReader;
        try {
            gsonReader = new FileReader(NPCFiles.getFile());

            dataMap = gson.fromJson(gsonReader, DataGSON.class).getData();
            gsonReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }


    public static NBTTagCompound getNBT(String string) throws IOException {
        try {
            return JsonToNBT.getTagFromJson(string);
        } catch (NBTException e) {
            throw new IOException("Failed to read NBT", e);
        }
    }
    public static void writeToGSON(String key, EnumNPCType npcType, String nbt, boolean removeKey) throws IOException {
        HashMap<String, HashMap<EnumNPCType, String>> dataInfoMap;
        FileWriter gsonWriter = new FileWriter(NPCFiles.getFile());
        FileReader gsonReader = new FileReader(NPCFiles.getFile());
        DataGSON data = gson.fromJson(gsonReader, DataGSON.class);
        if (data != null) dataMap = data.getData();

        if (dataMap != null) {
            dataInfoMap = dataMap;
        } else {
            dataInfoMap = new HashMap<>();
        }
        DataGSON datajson = new DataGSON();
        HashMap<EnumNPCType, String> npcDataMap = new HashMap<>();
        npcDataMap.put(npcType, nbt);
        dataInfoMap.put(key, npcDataMap);

        if (removeKey) {
            dataInfoMap.remove(key);
        }

        datajson.setData(dataInfoMap);
        gsonWriter.write(gson.toJson(datajson));
        gsonWriter.close();
    }
}