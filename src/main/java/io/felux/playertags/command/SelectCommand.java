package io.felux.playertags.command;

import io.felux.playertags.PlayerTags;
import io.felux.playertags.api.Tag;
import io.felux.playertags.command.util.Command;
import io.felux.playertags.config.Lang;
import io.felux.playertags.storage.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectCommand {
    @Command(aliases = {"select"}, about = "Select a players tag.", permission = "playertags.select", usage = "select <id> [player] [silent]", requiredArgs = 1)
    public static void execute(final CommandSender sender, final PlayerTags plugin, final String[] args) {
        final Tag tag = plugin.getTagManager().getTag(args[0]);

        if (tag == null) {
            Lang.COMMAND_TAG_UNKNOWN.send(sender, Lang.PREFIX.asString());
            return;
        }

        if (args.length == 1) {
            // No target player.
            if (!(sender instanceof Player)) {
                Lang.COMMAND_PLAYER_ONLY.send(sender, Lang.PREFIX.asString());
                return;
            }

            final Player player = (Player) sender;
            final PlayerData playerData = PlayerData.get(player.getUniqueId());
            playerData.setTag(tag.getId());
            Lang.TAG_SELECTED.send(sender, Lang.PREFIX.asString(), tag.getId(), tag.getPrefix());
            return;
        }

        final Player target = Bukkit.getPlayer(args[1]);

        if (!sender.hasPermission("playertags.select.other")) {
            Lang.COMMAND_NO_PERMISSION.send(sender, Lang.PREFIX.asString());
            return;
        }

        if (target == null) {
            // invalid player.
            Lang.COMMAND_UNKNOWN.send(sender, Lang.PREFIX.asString());
            return;
        }

        final PlayerData playerData = PlayerData.get(target.getUniqueId());
        playerData.setTag(tag.getId());

        final boolean isSilent = args.length == 3 && args[2].equalsIgnoreCase("-s");

        if ((sender instanceof Player) && ((Player) sender).getUniqueId() == target.getUniqueId()) {
            Lang.TAG_SELECTED.send(sender, Lang.PREFIX.asString(), tag.getId(), tag.getPrefix());
            return;
        }

        if (!isSilent)
            Lang.TAG_SELECTED_OTHER.send(sender, Lang.PREFIX.asString(), target.getName(), tag.getId(), tag.getPrefix());
        Lang.TAG_CHANGED_TO.send(target, Lang.PREFIX.asString(), tag.getId(), tag.getPrefix());
    }
}
