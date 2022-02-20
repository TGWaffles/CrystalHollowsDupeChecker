package club.thom.crystalhollowsdupechecker.commands;

import club.thom.crystalhollowsdupechecker.listeners.GuiEventListener;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class DupeResetCommand extends CommandBase {


    @Override
    public String getCommandName() {
        return "dupereset";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/dupereset - resets the dupe tag on all known duped items.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        GuiEventListener.dupedUuids.clear();
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reset known duped items."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
