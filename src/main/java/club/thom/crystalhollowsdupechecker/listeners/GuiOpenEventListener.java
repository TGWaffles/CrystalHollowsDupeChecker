package club.thom.crystalhollowsdupechecker.listeners;

import club.thom.crystalhollowsdupechecker.utils.CheckHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOpenEventListener {
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiContainer)) {
            // it doesn't contain items
            return;
        }
        GuiContainer container = (GuiChest) event.gui;
        Container inventorySlots = container.inventorySlots;

        for (ItemStack stack : inventorySlots.inventoryItemStacks) {
            NBTTagCompound itemNbt = stack.serializeNBT();
            if (!CheckHelper.checkDuped(itemNbt)) {
                // not a skyblock item!
                continue;
            }

        }
    }

}
