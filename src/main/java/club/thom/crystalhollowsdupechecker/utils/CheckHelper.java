package club.thom.crystalhollowsdupechecker.utils;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;

public class CheckHelper {
    // List of items that the dupe check does not work on (to my knowledge).
    // Enchanted books can go into stash from experimentation table
    static List<String> BLACKLISTED_ITEM_IDS = Arrays.asList("WISHING_COMPASS", "PICKONIMBUS", "PREHISTORIC_EGG",
            "JUNGLE_HEART", "ASCENSION_ROPE", "ENCHANTED_BOOK");

    public static boolean checkDuped(NBTTagCompound itemNbt) {
        if (!itemNbt.hasKey("tag") || !itemNbt.getCompoundTag("tag").hasKey("ExtraAttributes")) {
            // not a skyblock item!
            return false;
        }

        NBTTagCompound extraAttributes = itemNbt.getCompoundTag("tag").getCompoundTag("ExtraAttributes");
        // No item ID = possibly not a skyblock item??
        if (!extraAttributes.hasKey("id")) {
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

}
