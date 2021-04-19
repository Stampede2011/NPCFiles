package dev.blu3.npcfiles.commands;

import com.pixelmongenerations.common.entity.npcs.NPCTrainer;
import com.pixelmongenerations.common.entity.npcs.registry.PokemonForm;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumNPCType;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import dev.blu3.npcfiles.utils.Utils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;

public class PlayerTrainer implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            if (args.<String>getOne(Text.of("name")).isPresent()) {
                String name = args.<String>getOne(Text.of("name")).get();
                EntityPlayerMP player = (EntityPlayerMP) src;

                try {
                    if (Utils.getDataMap().get(name + "npc") != null) {
                        src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cA saved NPC with this name already exists."));
                    } else {
                        NPCTrainer trainer = (NPCTrainer)Utils.createNPC(player.getPosition(), player.world, EnumNPCType.Trainer, null, (Player) src);
                        if (trainer != null) {
                            PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueID()).get();
                            ArrayList<PokemonForm> pokemonList = new ArrayList<>();
                            if (party != null) {
                                for (int slot = 0; slot < party.count(); slot++) {
                                    EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(slot), player.world);
                                    if (pokemon != null && !pokemon.isEgg)
                                        pokemonList.add(new PokemonForm(pokemon.serializeNBT()));
                                }
                            }
                            trainer.setCustomSteveTexture(player.getName());
                            trainer.setName(player.getName());
                            trainer.loadPokemon(pokemonList);
                            trainer.setTextureIndex(5);
                            Utils.writeToGSON(name + "npc", EnumNPCType.Trainer, trainer.serializeNBT().toString(), false);
                            src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &a&l" + name + " &anpc was stored to file."));
                        } else {
                            src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cError creating this NPC."));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cCommand syntax is invalid!"));
            }
        } else {
            src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cOnly players can use this command!"));
        }



        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.playertrainer.base")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .executor(new PlayerTrainer())
                .build();
    }
}