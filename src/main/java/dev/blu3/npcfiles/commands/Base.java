package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;

public class Base implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PaginationList.builder()
                .title(Utils.toText("&b&lNPCFiles"))
                .padding(Utils.toText("&8&m-&r"))
                .contents(
                        Utils.toText("&b/npcfiles save <name>"),
                        Utils.toText("&b/npcfiles delete <name>"),
                        Utils.toText("&b/npcfiles spawn <name>"),
                        Utils.toText("&b/npcfiles playertrainer <name>"),
                        Utils.toText("&b/npcfiles list")
                )
                .linesPerPage(10)
                .sendTo(src);

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.npcfiles.base")
                .executor(new Base())
                .child(Save.build(), "save")
                .child(Delete.build(), "delete")
                .child(Spawn.build(), "spawn")
                .child(PlayerTrainer.build(), "playertrainer")
                .child(List.build(), "list")
                .build();
    }
}