package dev.blu3.npcfiles.commands;

import com.pixelmongenerations.common.entity.npcs.NPCTrainer;
import com.pixelmongenerations.common.entity.npcs.registry.PokemonForm;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumNPCType;
import com.pixelmongenerations.core.enums.EnumTrainerAI;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import dev.blu3.npcfiles.utils.Utils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTrainer implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            if (args.<User>getOne(Text.of("player")).isPresent()) {
                User player = args.<User>getOne(Text.of("player")).get();
                EntityPlayerMP srcPlayer = (EntityPlayerMP) src;

                try {
                    if (Utils.getDataMap().get(player.getName()+"npc") != null) {
                        src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cA saved NPC with this name already exists."));
                    } else {
                        NPCTrainer trainer = (NPCTrainer) Utils.createNPC(srcPlayer.posX, srcPlayer.posY, srcPlayer.posZ, srcPlayer.world, EnumNPCType.Trainer, null, (Player) srcPlayer);
                        if(trainer != null){
                            PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueId()).get();
                            List<NBTTagCompound> pokemonList = new ArrayList<>();
                            if (party != null) {
                                for (int slot = 0; slot < party.count(); slot++) {
                                    EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(slot), srcPlayer.world);
                                    if (pokemon != null && !pokemon.isEgg)
                                        pokemonList.add(pokemon.serializeNBT());
                                }
                            }

                            NBTTagCompound[] pokemonArray = new NBTTagCompound[pokemonList.size()];
                            pokemonArray = pokemonList.toArray(pokemonArray);

                            trainer.getPokemonStorage().setPokemon(pokemonArray);
                            trainer.setCustomSteveTexture(player.getName());
                            trainer.setName(player.getName());
                            trainer.setAIMode(EnumTrainerAI.StandStill);
                            trainer.setTextureIndex(5);
                            Utils.writeToGSON(player.getName() + "npc", EnumNPCType.Trainer, trainer.serializeNBT().toString(), false);
                            src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &a&l" + player.getName() + "npc &awas stored to file."));
                        } else {
                            src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cError creating this NPC."));
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cCommand syntax is invalid!"));
            }
        } else {
            src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cOnly players can use this command!"));
        }



        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.playertrainer.base")
                .arguments(GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
                .executor(new PlayerTrainer())
                .build();
    }
}