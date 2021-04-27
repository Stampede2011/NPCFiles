package dev.blu3.npcfiles.commands;

import com.mojang.authlib.GameProfile;
import com.pixelmongenerations.common.entity.npcs.registry.PokemonForm;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.Pixelmon;
import com.pixelmongenerations.core.enums.EnumNPCType;
import com.pixelmongenerations.common.entity.npcs.NPCTrainer;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import dev.blu3.npcfiles.utils.Utils;

import java.util.*;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class NPCFilesCMD extends CommandBase {
    public String getName() {
        return "npcfiles";
    }

    public String getUsage(ICommandSender sender) {
        return "/npcfiles save <name> / delete <name> / spawn <name> / playertrainer <name> / list ";
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> possibleArgs = new ArrayList<>();
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (player.canUseCommand(0, "npcfiles.command.npcfiles") && args.length == 1) {
                possibleArgs.add("save");
                possibleArgs.add("delete");
                possibleArgs.add("spawn");
                possibleArgs.add("list");
                possibleArgs.add("playertrainer");
            }
            if (player.canUseCommand(0, "npcfiles.command.npcfiles") && args.length == 2) {
                if (args[0].equals("playertrainer"))
                    possibleArgs.addAll(Arrays.asList(server.getOnlinePlayerNames()));
                if (args[0].equals("save") || args[0].equals("delete") || args[0].equals("spawn"))
                    try {
                        if (Utils.getDataMap() != null)
                            Utils.getDataMap().forEach((string, hashMap) -> possibleArgs.add(string));
                    } catch (Exception exception) {}
            }
        }
        return getListOfStringsMatchingLastWord(args, possibleArgs);
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (args.length > 0) {
                switch (args[0]) {
                    case "save":
                        if (args.length > 1) {
                            try {
                                if (Utils.getDataMap().get(args[1]) != null) {
                                    player.sendMessage(new TextComponentString(Utils.regex("&cAn NPC file of this type already exists.")));
                                } else {
                                    Utils.inSelectMode.put(player.getUniqueID(), Boolean.valueOf(true));
                                    Utils.npcFileName.put(player.getUniqueID(), args[1]);
                                    player.sendMessage(new TextComponentString(Utils.regex("&dRight-click a NPC to save.")));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                            player.sendMessage(new TextComponentString(getUsage(player)));
                        }
                        return;
                    case "delete":
                        if (args.length > 1) {
                            try {
                                if (Utils.getDataMap().get(args[1]) == null) {
                                    player.sendMessage(new TextComponentString(Utils.regex("&cAn NPC file of this type doesn't exist.")));
                                } else {
                                    Utils.writeToGSON(args[1], null, null, true);
                                    player.sendMessage(new TextComponentString(Utils.regex("&e" + args[1] + " &bwas removed from file.")));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                            player.sendMessage(new TextComponentString(getUsage(player)));
                        }
                        return;
                    case "spawn":
                        if (args.length > 1) {
                            try {
                                if (Utils.getDataMap().get(args[1]) == null) {
                                    player.sendMessage(new TextComponentString(Utils.regex("&cAn NPC file of this type doesn't exist.")));
                                } else {
                                    (Utils.getDataMap().get(args[1])).forEach((tag, npc) -> {
                                        try {
                                            player.world.spawnEntity(Utils.createNPC(player.getPosition(), player.world, tag, Utils.getNBT(npc), player));
                                            player.sendMessage(new TextComponentString(Utils.regex("&bNPC preset &e" + args[1] + " &bwas spawned.")));
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            System.out.println(tag);
                                            System.out.println(npc);
                                        }
                                    });
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                            player.sendMessage(new TextComponentString(getUsage(player)));
                        }
                        return;
                    case "playertrainer":
                        if (args.length > 1) {
                            try {
                                GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
                                if (profile == null) {
                                    player.sendMessage(new TextComponentString(Utils.regex("&cInvalid player name.")));
                                } else if (Utils.getDataMap().get(args[1] + "npc") != null) {
                                    player.sendMessage(new TextComponentString(Utils.regex("&cA saved NPC with this name already exists.")));
                                } else {
                                    NPCTrainer trainer = (NPCTrainer)Utils.createNPC(player.getPosition(), player.world, EnumNPCType.Trainer, null, player);
                                    if (trainer != null) {
                                        Optional<PlayerStorage> party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueID());
                                        ArrayList<PokemonForm> pokemonList = new ArrayList<>();
                                        if (party.isPresent()) {
                                            for (int slot = 0; slot < party.get().count(); slot++) {
                                                EntityPixelmon pokemon = party.get().getPokemon(party.get().getIDFromPosition(slot), player.world);
                                                if (pokemon != null && !pokemon.isEgg)
                                                    pokemonList.add(new PokemonForm(pokemon.serializeNBT()));
                                            }
                                        }
                                        trainer.setCustomSteveTexture(profile.getName());
                                        trainer.setName(profile.getName());
                                        trainer.loadPokemon(pokemonList);
                                        trainer.setTextureIndex(5);
                                        Utils.writeToGSON(args[1] + "npc", EnumNPCType.Trainer, trainer.serializeNBT().toString(), false);
                                        player.sendMessage(new TextComponentString(Utils.regex("&e" + args[1] + "npc &bwas stored to file.")));
                                    } else {
                                        player.sendMessage(new TextComponentString(Utils.regex("&cError creating this NPC.")));
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                            player.sendMessage(new TextComponentString(getUsage(player)));
                        }
                        return;
                    case "list":
                        try {
                            if (Utils.getDataMap().keySet().size() > 0) {
                                player.sendMessage(new TextComponentString(Utils.regex("&d-=-=-= Listing NPC Files =-=-=-")));
                                for (String string : Utils.getDataMap().keySet())
                                    player.sendMessage(new TextComponentString(Utils.regex("&6- " + string)));
                            } else {
                                player.sendMessage(new TextComponentString(Utils.regex("&cThere are no saved NPC files.")));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return;
                }
                player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                player.sendMessage(new TextComponentString(getUsage(player)));
            } else {
                player.sendMessage(new TextComponentString(Utils.regex("&cInvalid usage.")));
                player.sendMessage(new TextComponentString(getUsage(player)));
            }
        } else {
            sender.sendMessage(new TextComponentString(Utils.regex("&cOnly players can use this command.")));
        }
    }
}
