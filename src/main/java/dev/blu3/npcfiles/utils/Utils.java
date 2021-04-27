package dev.blu3.npcfiles.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmongenerations.common.entity.npcs.*;
import com.pixelmongenerations.core.enums.EnumNPCType;
import dev.blu3.npcfiles.NPCFiles;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class Utils {
    public static HashMap<UUID, Boolean> inSelectMode = new HashMap<>();

    public static HashMap<UUID, String> npcFileName = new HashMap<>();

    public static String regex(String line) {
        String regex = "&(?=[0123456789abcdefklmnor])";
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            line = line.replaceAll(regex, "§");
        return line;
    }

    public static Entity createNPC(BlockPos pos, World world, EnumNPCType npcType, NBTTagCompound tag, EntityPlayerMP player) {
        System.out.println("Create NPC = " + tag);
        NPCNurseJoy nurseJoy;
        NPCTrainer trainer;
        NPCChatting chatting;
        NPCRelearner relearner;
        NPCShopkeeper shopkeeper;
        NPCTrader trader;
        NPCTutor tutor;
        switch (npcType) {
            case NurseJoy:
                nurseJoy = new NPCNurseJoy(world);
                nurseJoy.readFromNBT(tag);
                nurseJoy.setUniqueId(MathHelper.getRandomUUID());
                nurseJoy.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return nurseJoy;
            case Trainer:
                trainer = new NPCTrainer(world);
                if (tag != null)
                    trainer.readFromNBT(tag);
                trainer.setUniqueId(MathHelper.getRandomUUID());
                trainer.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return trainer;
            case ChattingNPC:
                chatting = new NPCChatting(world);
                chatting.readFromNBT(tag);
                chatting.setUniqueId(MathHelper.getRandomUUID());
                chatting.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return chatting;
            case Relearner:
                relearner = new NPCRelearner(world);
                relearner.readFromNBT(tag);
                relearner.setUniqueId(MathHelper.getRandomUUID());
                relearner.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return relearner;
            case Shopkeeper:
                shopkeeper = new NPCShopkeeper(world);
                shopkeeper.readFromNBT(tag);
                shopkeeper.setUniqueId(MathHelper.getRandomUUID());
                shopkeeper.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return shopkeeper;
            case Trader:
                trader = new NPCTrader(world);
                trader.readFromNBT(tag);
                trader.setUniqueId(MathHelper.getRandomUUID());
                trader.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return trader;
            case Tutor:
                tutor = new NPCTutor(world);
                tutor.readFromNBT(tag);
                tutor.setUniqueId(MathHelper.getRandomUUID());
                tutor.setPosition(pos.getX(), pos.getY(), pos.getZ());
                return tutor;
        }
        player.sendMessage(new TextComponentString(regex("Error creating NPC of type: " + npcType)));
        return null;
    }

    static Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private static HashMap<String, HashMap<EnumNPCType, String>> dataMap;

    public static void writeNewGSON() throws IOException {
        FileWriter gsonWriter = new FileWriter(NPCFiles.file);
        DataGSON datajson = new DataGSON();
        HashMap<String, HashMap<EnumNPCType, String>> dataInfoMap = new HashMap<>();
        datajson.setData(dataInfoMap);
        gsonWriter.write(gson.toJson(datajson));
        gsonWriter.close();
    }

    public static HashMap<String, HashMap<EnumNPCType, String>> getDataMap() throws IOException {
        FileReader gsonReader = new FileReader(NPCFiles.file);
        dataMap = (gson.fromJson(gsonReader, DataGSON.class)).getData();
        gsonReader.close();
        return dataMap;
    }

    public static NBTTagCompound getNBT(String string) throws IOException {
        try {
            System.out.println("Readout == " + JsonToNBT.getTagFromJson(string));
            return JsonToNBT.getTagFromJson(string);
        } catch (NBTException e) {
            throw new IOException("Failed to read NBT", e);
        }
    }

    public static void writeToGSON(String key, EnumNPCType npcType, String nbt, boolean removeKey) throws IOException {
        HashMap<String, HashMap<EnumNPCType, String>> dataInfoMap;
        FileWriter gsonWriter = new FileWriter(NPCFiles.file);
        FileReader gsonReader = new FileReader(NPCFiles.file);
        DataGSON data = gson.fromJson(gsonReader, DataGSON.class);
        if (data != null)
            dataMap = data.getData();
        if (dataMap != null) {
            dataInfoMap = dataMap;
        } else {
            dataInfoMap = new HashMap<>();
        }
        DataGSON datajson = new DataGSON();
        HashMap<EnumNPCType, String> npcDataMap = new HashMap<>();
        npcDataMap.put(npcType, nbt);
        dataInfoMap.put(key, npcDataMap);
        if (removeKey)
            dataInfoMap.remove(key);
        datajson.setData(dataInfoMap);
        gsonWriter.write(gson.toJson(datajson));
        gsonWriter.close();
    }
}
