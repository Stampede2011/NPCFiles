package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Delete implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (args.<String>getOne(Text.of("name")).isPresent()) {
            String name = args.<String>getOne(Text.of("name")).get();
            try {
                if (Utils.getDataMap().get(name) == null) {
                    src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cAn NPC file of this type doesn't exist."));
                } else {
                    Utils.writeToGSON(name, null, null, true);
                    src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &a&l" + name + " &awas removed from file."));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            src.sendMessage(Utils.toText("&8(&b&lNPCFiles&8) &cCommand syntax is invalid!"));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.delete.base")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .executor(new Delete())
                .build();
    }
}