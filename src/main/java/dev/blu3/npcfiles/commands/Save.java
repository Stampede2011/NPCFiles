package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Save implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            if (args.<String>getOne(Text.of("name")).isPresent()) {
                String name = args.<String>getOne(Text.of("name")).get();
                Player player = (Player) src;

                try {
                    if (Utils.getDataMap().get(name) != null) {
                        src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cAn NPC file of this type already exists."));
                    } else {
                        Utils.inSelectMode.put(player.getUniqueId(), true);
                        Utils.npcFileName.put(player.getUniqueId(), name);
                        src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cRight-click a NPC to save."));
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
                .permission("npcfiles.command.save.base")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .executor(new Save())
                .build();
    }
}