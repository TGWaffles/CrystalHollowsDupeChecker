package club.thom.crystalhollowsdupechecker.utils;

import net.minecraft.nbt.NBTTagCompound;

public class CheckHelper {
    // List of items that the dupe check does not work on (to my knowledge).
    String[] BLACKLISTED_ITEM_IDS = new String[]{
        "WISHING_COMPASS", "PICKONIMBUS", "PREHISTORIC_EGG", "JUNGLE_HEART", "ASCENSION_ROPE"};

    public static boolean checkDuped(NBTTagCompound itemNbt) {
        if (!itemNbt.hasKey("tag") || itemNbt.getCompoundTag("tag").hasKey("ExtraAttributes")) {
            // not a skyblock item!
            return false;
        }

        // TODO: change this :)
        return true;
    }

}
