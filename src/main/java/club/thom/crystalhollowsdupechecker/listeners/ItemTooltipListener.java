package club.thom.crystalhollowsdupechecker.listeners;

import club.thom.crystalhollowsdupechecker.utils.CheckHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemTooltipListener {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemToolTipEvent(ItemTooltipEvent event) {
        ItemStack item = event.itemStack;
        if (CheckHelper.checkDuped(item.serializeNBT())) {
           event.toolTip.add(1, EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED.toString() +
                   "|||" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "POSSIBLY DUPED" +
                   EnumChatFormatting.AQUA + EnumChatFormatting.OBFUSCATED + "|||");
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiContainer)) {
            return;
        }
        GuiContainer container = (GuiContainer) event.gui;
        Container inventorySlots = container.inventorySlots;
        String[] methodNames = new String[]{"drawGradientRect", "func_73733_a"};
        Method drawGradientMethod = ReflectionHelper.findMethod(Gui.class, container, methodNames, int.class,
                int.class, int.class, int.class, int.class, int.class);
        // xSize obfuscated
        Field xSizeField = ReflectionHelper.findField(GuiContainer.class, "xSize", "field_146999_f");
        // ySize obfuscated
        Field ySizeField = ReflectionHelper.findField(GuiContainer.class, "ySize", "field_147000_g");
        int xOffset;
        int yOffset;
        try {
            xOffset = (int) xSizeField.get(container);
            yOffset = (int) ySizeField.get(container);
        } catch (IllegalAccessException e) {
            return;
        }
        for (Slot slot : inventorySlots.inventorySlots) {
            ItemStack stack = slot.getStack();
            if (stack == null) {
                continue;
            }
            NBTTagCompound itemNbt = stack.serializeNBT();
            if (!CheckHelper.checkDuped(itemNbt)) {
                // not a skyblock item!
                continue;
            }
            // this.guiLeft = (this.width - this.xSize) / 2;
            int j1 = slot.xDisplayPosition + (container.width - xOffset) / 2;
            // this.guiTop = (this.height - this.ySize) / 2;
            int k1 = slot.yDisplayPosition + (container.height - yOffset) / 2;
            int colour = (80 << 24) + (255 << 16);
            drawGradientMethod.setAccessible(true);
            try {
                drawGradientMethod.invoke(container, j1, k1, j1 + 16, k1 + 16, colour, colour);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return;
            }
            drawGradientMethod.setAccessible(false);
        }
    }

}
