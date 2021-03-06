package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.bridge.world.LocationBridge;

import java.util.Objects;

public class Spawn implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            if (args.<String>getOne(Text.of("name")).isPresent()) {
                String name = args.<String>getOne(Text.of("name")).get();
                EntityPlayerMP player = (EntityPlayerMP) src;

                try {
                    if (Utils.getDataMap().get(name) == null) {
                        src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &cAn NPC file of this type doesn't exist."));
                    } else {
                        Utils.getDataMap().get(name).forEach((tag, npc) -> {
                            try {
                                player.world.spawnEntity(Objects.requireNonNull(Utils.createNPC(player.posX, player.posY, player.posZ, player.world, tag, Utils.getNBT(npc), (Player) player)));
                                src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &a&l" + name + " &awas spawned."));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                } catch (Exception ex) {
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
                .permission("npcfiles.command.spawn.base")
                .arguments(GenericArguments.withSuggestions(GenericArguments.string(Text.of("name")), Utils.getDataMap().keySet()))
                .executor(new Spawn())
                .build();
    }
}