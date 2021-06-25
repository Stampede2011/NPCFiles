package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.NPCFiles;
import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Reload implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        NPCFiles.getInstance().loadConfig();

        NPCFiles.getLogger().info("NPCFiles has been reloaded!");
        src.sendMessage(Utils.toText("&8(&bNPCFiles&8) &aNPCFiles has been reloaded!"));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.reload.base")
                .executor(new Reload())
                .build();
    }
}