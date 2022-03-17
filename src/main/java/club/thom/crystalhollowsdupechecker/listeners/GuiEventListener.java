package club.thom.crystalhollowsdupechecker.listeners;

import club.thom.crystalhollowsdupechecker.utils.CheckHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class GuiEventListener {
    public static boolean hasRanInThisGui = false;
    public static boolean isInAh = false;
    private static final HashMap<String, Integer> uuidToIndex = new HashMap<>();
    public static final HashSet<String> dupedUuids = new HashSet<>();
    public static int ahWindowId = 0;

    private void cleanUp() {
        hasRanInThisGui = false;
        isInAh = false;
        uuidToIndex.clear();
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        System.out.println("resetting gui stuff");
        uuidToIndex.clear();
        // GUI Closed
        if (event.gui == null) {
            cleanUp();
        }
        if (!(event.gui instanceof GuiContainer)) {
            cleanUp();
            return;
        }
        GuiContainer guiContainer = (GuiContainer) event.gui;
        if (!(guiContainer.inventorySlots instanceof ContainerChest)) {
            cleanUp();
            return;
        }
        ContainerChest container = (ContainerChest) guiContainer.inventorySlots;
        // Current open gui isn't the AH gui.
        if (!container.getLowerChestInventory().getDisplayName().getUnformattedText().equals("Auctions Browser")) {
            cleanUp();
            return;
        }
        ahWindowId = container.windowId;
        isInAh = true;
        new Thread(() -> {
            // do dupe checks :)
            for (Slot slot : container.inventorySlots) {
                String uuid = checkDuped(slot.slotNumber, slot.getStack());
                if (uuid != null) {
                    GuiEventListener.dupedUuids.add(uuid);
                }
            }
        }).start();

    }

    public static String checkDuped(int slotIndex, ItemStack stack) {
        String uuid = CheckHelper.getUuidFromItemStack(stack);
        if (uuid == null) {
            return null;
        }
        Integer previousSlot = uuidToIndex.putIfAbsent(uuid, slotIndex);
        if (previousSlot == null) {
            return null;
        }
        if (previousSlot != slotIndex) {
            return uuid;
        }
        return null;
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiContainer)) {
            return;
        }
        GuiContainer container = (GuiContainer) event.gui;
        Container inventorySlots = container.inventorySlots;
        for (int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
            Slot slot = inventorySlots.inventorySlots.get(i);
            ItemStack stack = slot.getStack();
            if (stack == null) {
                continue;
            }
            String uuid = CheckHelper.getUuidFromItemStack(stack);
            NBTTagCompound itemNbt = stack.serializeNBT();
            if (!dupedUuids.contains(uuid) && !CheckHelper.checkDuped(itemNbt)) {
                // Not a duped item.
                continue;
            }
            // ARGB
            highlightSlot(container, i, 0x50FF0000);
        }
    }

    public static void highlightSlot(GuiContainer container, int slotIndex, int colour) {
        // drawGradientRect obfuscated
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
        Slot slot = container.inventorySlots.getSlot(slotIndex);
        // this.guiLeft = (this.width - this.xSize) / 2;
        int j1 = slot.xDisplayPosition + (container.width - xOffset) / 2;
        // this.guiTop = (this.height - this.ySize) / 2;
        int k1 = slot.yDisplayPosition + (container.height - yOffset) / 2;
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
