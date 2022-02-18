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
        String possiblyDupedText = EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED.toString() +
                "|||" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "POSSIBLY DUPED" +
                EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED + "|||";

        if (CheckHelper.checkDuped(item.serializeNBT())) {
            // Matches ITEM_STASH originTag.
            event.toolTip.add(1, possiblyDupedText + EnumChatFormatting.GRAY + " (hold shift)");
            // Add to top AND bottom.
            event.toolTip.add(possiblyDupedText);
            if (GuiContainer.isShiftKeyDown()) {
               event.toolTip.add(2, EnumChatFormatting.WHITE +
                       "Likely duped in the Crystal Hollows dupe of Jan 2022.");
               event.toolTip.add(3, EnumChatFormatting.WHITE +
                       "Has originTag: ITEM_STASH.");
            }
        } else {
            String uuid = CheckHelper.getUuidFromItemStack(item);
            if (uuid == null || !GuiEventListener.dupedUuids.contains(uuid)) {
                return;
            }
            // It has a duplicate UUID on the same page.
            event.toolTip.add(1, possiblyDupedText + EnumChatFormatting.GRAY + " (hold shift)");
            // Add to top AND bottom.
            event.toolTip.add(possiblyDupedText);
            if (GuiContainer.isShiftKeyDown()) {
                event.toolTip.add(2, EnumChatFormatting.WHITE +
                        "Likely duped.");
                event.toolTip.add(3, EnumChatFormatting.WHITE +
                        "Found two duplicates in an AH page while browsing the auction house.");
            }
        }
    }



}
