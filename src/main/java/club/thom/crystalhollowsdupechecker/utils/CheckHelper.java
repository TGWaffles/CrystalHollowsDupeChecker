package club.thom.crystalhollowsdupechecker.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;

public class CheckHelper {
    // List of items that the dupe check does not work on (to my knowledge).
    // Enchanted books can go into stash from experimentation table
    static List<String> BLACKLISTED_ITEM_IDS = Arrays.asList("WISHING_COMPASS", "PICKONIMBUS", "PREHISTORIC_EGG",
            "JUNGLE_HEART", "ASCENSION_ROPE", "ENCHANTED_BOOK");

    public static NBTTagCompound getExtraAttributes(NBTTagCompound itemNbt) {
        if (!itemNbt.hasKey("tag") || !itemNbt.getCompoundTag("tag").hasKey("ExtraAttributes")) {
            // not a skyblock item!
            return null;
        }

        NBTTagCompound extraAttributes = itemNbt.getCompoundTag("tag").getCompoundTag("ExtraAttributes");
        // No item ID = possibly not a skyblock item??
        if (!extraAttributes.hasKey("id")) {
            return null;
        }
        return extraAttributes;
    }

    public static boolean checkDuped(NBTTagCompound itemNbt) {
        NBTTagCompound extraAttributes = getExtraAttributes(itemNbt);
        if (extraAttributes == null) {
            return false;
        }
        String itemId = extraAttributes.getString("id");
        // Can't check if it's duped.
        if (BLACKLISTED_ITEM_IDS.contains(itemId)) {
            return false;
        }

        // Where the item is from!
        if (!extraAttributes.hasKey("originTag")) {
            return false;
        }
        String originTag = extraAttributes.getString("originTag");


        return originTag.equals("ITEM_STASH");
    }

    public static String getUuidFromItemStack(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NBTTagCompound nbt = stack.serializeNBT();
        NBTTagCompound extraAttributes = getExtraAttributes(nbt);
        if (extraAttributes == null || !extraAttributes.hasKey("uuid")) {
            return null;
        }
        return extraAttributes.getString("uuid");
    }

}
