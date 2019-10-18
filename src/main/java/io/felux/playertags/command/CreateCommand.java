package io.felux.playertags.command;

import io.felux.playertags.PlayerTags;
import io.felux.playertags.api.Tag;
import io.felux.playertags.command.util.Command;
import io.felux.playertags.config.Lang;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CreateCommand {
    @Command(aliases = {"create"}, about = "Create a tag.", permission = "playertags.create", usage = "create <id>")
    public static void execute(final CommandSender sender, final PlayerTags plugin, final String[] args) {

        if (args.length == 0) {
            // no id specified
            Lang.COMMAND_INVALID_SYNTAX.send(sender, Lang.PREFIX.asString());
            return;
        }

        String tagName = args[0];

        if (plugin.getTagManager().getTag(tagName) != null) {
            Lang.COMMAND_TAG_EXISTS.send(sender, Lang.PREFIX.asString());
            return;
        }

        String hasPermName = Lang.GUI_TAG_HAS_PERM_NAME.asString();
        List<String> hasPermLore = Arrays.asList(Lang.GUI_TAG_HAS_PERM_LORE.asString().split("\n"));

        String hasNoPermName = Lang.GUI_TAG_HAS_NO_PERM_NAME.asString();
        List<String> hasNoPermLore = Arrays.asList(Lang.GUI_TAG_HAS_NO_PERM_LORE.asString().split("\n"));

        Material hasPermItem = Material.valueOf(plugin.getConfig().getString("tags." + tagName + ".item.has-perm.item", plugin.getConfig().getString("settings.gui.item.has-perm")));
        Material hasNoPermItem = Material.valueOf(plugin.getConfig().getString("tags." + tagName + ".item.no-perm.item", plugin.getConfig().getString("settings.gui.item.no-perm")));

        String tagPrefix = args.length > 1 ? args[1] : null;
        Tag tag = new Tag(tagName).withPrefix(tagPrefix != null ? tagPrefix : "&7[" + tagName + "]").withDescription("Default description for " + tagName + " tag..").withPermission(true).withSlot(-1).withItem(hasPermName, hasPermLore, hasPermItem, 0, true).withPersist(true).withItem(hasNoPermName, hasNoPermLore, hasNoPermItem, 0, false);
        plugin.getTagManager().addTag(tag);
        plugin.getConfig().set("tags." + tagName + ".prefix", tag.getPrefix());
        plugin.getConfig().set("tags." + tagName + ".description", tag.getDescription());
        plugin.getConfig().set("tags." + tagName + ".permission", tag.requirePermission());
        plugin.saveConfig();
        plugin.setupTags();
        Lang.CREATE_COMMAND.send(sender, Lang.PREFIX.asString(), tagName);
    }
}
