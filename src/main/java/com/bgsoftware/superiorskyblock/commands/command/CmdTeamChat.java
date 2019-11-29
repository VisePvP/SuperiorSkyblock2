package com.bgsoftware.superiorskyblock.commands.command;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.commands.ICommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CmdTeamChat implements ICommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("teamchat", "chat", "tc");
    }

    @Override
    public String getPermission() {
        return "superior.island.teamchat";
    }

    @Override
    public String getUsage() {
        return "island teamchat [" + Locale.COMMAND_ARGUMENT_MESSAGE.getMessage() + "]";
    }

    @Override
    public String getDescription() {
        return Locale.COMMAND_DESCRIPTION_TEAM_CHAT.getMessage();
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(sender);
        Island island = superiorPlayer.getIsland();

        if(island == null){
            Locale.INVALID_ISLAND.send(superiorPlayer);
            return;
        }

        if(args.length == 1){
            if(superiorPlayer.hasTeamChatEnabled()){
                Locale.TOGGLED_TEAM_CHAT_OFF.send(superiorPlayer);
            }else{
                Locale.TOGGLED_TEAM_CHAT_ON.send(superiorPlayer);
            }
            superiorPlayer.toggleTeamChat();
        }

        else{
            StringBuilder message = new StringBuilder();

            for(int i = 1; i < args.length; i++)
                message.append(" ").append(args[i]);

            island.sendMessage(Locale.TEAM_CHAT_FORMAT.getMessage(superiorPlayer.getPlayerRole(), superiorPlayer.getName(),
                    message.toString().substring(1)));
        }

    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
