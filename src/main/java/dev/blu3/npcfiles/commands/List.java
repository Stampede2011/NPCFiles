package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class List implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        try {
            if (Utils.getDataMap().keySet().size() > 0) {
                src.sendMessage(Utils.toText("&d-=-=-= Listing NPC Files =-=-="));
                for (String string : Utils.getDataMap().keySet()) {
                    src.sendMessage(Utils.toText("&6- " + string));
                }
            } else {
                src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cThere are no saved NPC files."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.list.base")
                .executor(new List())
                .build();
    }
}