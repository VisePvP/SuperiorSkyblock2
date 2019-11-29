package com.bgsoftware.superiorskyblock.commands.command;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.commands.ICommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CmdBan implements ICommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("ban");
    }

    @Override
    public String getPermission() {
        return "superior.island.ban";
    }

    @Override
    public String getUsage() {
        return "island ban <" + Locale.COMMAND_ARGUMENT_PLAYER_NAME.getMessage() + ">";
    }

    @Override
    public String getDescription() {
        return Locale.COMMAND_DESCRIPTION_BAN.getMessage();
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
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

        if(!superiorPlayer.hasPermission(IslandPermission.BAN_MEMBER)){
            Locale.NO_BAN_PERMISSION.send(superiorPlayer, island.getRequiredPlayerRole(IslandPermission.BAN_MEMBER));
            return;
        }

        SuperiorPlayer targetPlayer = SSuperiorPlayer.of(args[1]);

        if(targetPlayer == null){
            Locale.INVALID_PLAYER.send(superiorPlayer, args[1]);
            return;
        }

        if(superiorPlayer.getIsland().isMember(targetPlayer) &&
                !targetPlayer.getPlayerRole().isLessThan(superiorPlayer.getPlayerRole())) {
            Locale.BAN_PLAYERS_WITH_LOWER_ROLE.send(superiorPlayer);
            return;
        }

        if(island.isBanned(targetPlayer)){
            Locale.PLAYER_ALREADY_BANNED.send(superiorPlayer);
            return;
        }

        island.banMember(targetPlayer);

        if(!Locale.BAN_ANNOUNCEMENT.isEmpty())
            island.sendMessage(Locale.BAN_ANNOUNCEMENT.getMessage(targetPlayer.getName(), superiorPlayer.getName()));

        Locale.GOT_BANNED.send(targetPlayer, island.getOwner().getName());
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(sender);
        Island island = superiorPlayer.getIsland();

        if(args.length == 2 && island != null && superiorPlayer.hasPermission(IslandPermission.BAN_MEMBER)){
            List<String> list = new ArrayList<>();

            for(SuperiorPlayer targetPlayer : island.getIslandMembers(false)){
                if(targetPlayer.getPlayerRole().isLessThan(superiorPlayer.getPlayerRole()) &&
                        targetPlayer.getName().toLowerCase().startsWith(args[1].toLowerCase())){
                    list.add(targetPlayer.getName());
                }
            }

            return list;
        }

        return new ArrayList<>();
    }
}
