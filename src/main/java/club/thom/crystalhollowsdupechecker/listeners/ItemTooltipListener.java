package club.thom.crystalhollowsdupechecker.listeners;

import club.thom.crystalhollowsdupechecker.utils.CheckHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemTooltipListener {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemToolTipEvent(ItemTooltipEvent event) {
        ItemStack item = event.itemStack;
        if (CheckHelper.checkDuped(item.serializeNBT())) {
            String text = EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED.toString() +
                    "|||" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "POSSIBLY DUPED" +
                    EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED + "|||";
            event.toolTip.add(1, text + EnumChatFormatting.GRAY + " (hold shift)");
            // Add to top AND bottom.
            event.toolTip.add(text);
            if (GuiContainer.isShiftKeyDown()) {
               event.toolTip.add(2, EnumChatFormatting.WHITE +
                       "Likely duped in the Crystal Hollows dupe of Jan 2022.");
               event.toolTip.add(3, EnumChatFormatting.WHITE +
                       "Has originTag: ITEM_STASH.");
            }
        }
    }



}
