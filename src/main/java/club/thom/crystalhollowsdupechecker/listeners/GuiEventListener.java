package club.thom.crystalhollowsdupechecker.listeners;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventListener {
    public static boolean hasRanInThisGui = false;
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        // GUI Closed
        if (event.gui == null) {
            hasRanInThisGui = false;
        }

    }

}
