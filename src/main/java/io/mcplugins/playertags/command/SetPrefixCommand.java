package io.mcplugins.playertags.command;

import io.mcplugins.playertags.PlayerTags;
import io.mcplugins.playertags.api.Tag;
import io.mcplugins.playertags.command.util.Command;
import io.mcplugins.playertags.config.Lang;
import io.mcplugins.playertags.util.Common;
import org.bukkit.command.CommandSender;

public class SetPrefixCommand {
    @Command(aliases = {"set"}, about = "Set a tags prefix.", permission = "playertags.setprefix", usage = "setprefix <id> <prefix>")
    public static void execute(final CommandSender sender, final PlayerTags plugin, final String[] args) {

        if (args.length < 1) {
            Lang.COMMAND_INVALID_SYNTAX.send(sender, Lang.PREFIX.asString());
            return;
        }

        String tagName = args[0];
        Tag tag = plugin.getTagManager().getTag(tagName);
        if (tag == null) {
            Lang.COMMAND_TAG_UNKNOWN.send(sender, Lang.PREFIX.asString());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);
        }

        String prefix = Common.translate(sb.toString());
        tag.setPrefix(prefix);
        plugin.getTagManager().getTags().put(tag.getId(), tag);
        plugin.saveTags();
        plugin.setupTags();
        Lang.SET_PREFIX_COMMAND.send(sender, Lang.PREFIX.asString(), tagName, prefix);
    }
}
