package dev.blu3.npcfiles.commands;

import dev.blu3.npcfiles.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.common.service.pagination.SpongePaginationBuilder;

import java.util.ArrayList;

public class List implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PaginationList.Builder paginationBuilder = PaginationList.builder()
                .title(Utils.toText("&bNPCFiles"))
                .padding(Utils.toText("&8&m-&r"))
                .linesPerPage(10);

        java.util.List<Text> contents = new ArrayList<>();

        try {
            if (Utils.getDataMap().keySet().size() > 0) {
                for (String npc : Utils.getDataMap().keySet()) {
                    contents.add(Text.builder().append(Utils.toText("&6- " + npc)).onClick(TextActions.runCommand("/npcfiles spawn " + npc)).build());
                }
            } else {
                contents.add(Utils.toText("&cThere are no saved NPC files."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        paginationBuilder.contents(contents);
        paginationBuilder.sendTo(src);

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("npcfiles.command.list.base")
                .executor(new List())
                .build();
    }
}