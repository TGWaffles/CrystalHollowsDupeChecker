package club.thom.crystalhollowsdupechecker;

import club.thom.crystalhollowsdupechecker.commands.DupeResetCommand;
import club.thom.crystalhollowsdupechecker.listeners.GuiEventListener;
import club.thom.crystalhollowsdupechecker.listeners.ItemTooltipListener;
import club.thom.crystalhollowsdupechecker.packets.ContainerSetItemListener;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = CrystalHollowsDupeChecker.MOD_ID, version = CrystalHollowsDupeChecker.VERSION, certificateFingerprint = CrystalHollowsDupeChecker.SIGNATURE)
public class CrystalHollowsDupeChecker {
    public static final String MOD_ID = "CRYSTALHOLLOWSDUPECHECKER";
    // This is replaced by build.gradle with the real version name
    public static final String VERSION = "@@VERSION@@";
    // Signature to compare to, so you know this is an official release of TEM.
    public static final String SIGNATURE = "32d142d222d0a18c9d19d5b88917c7477af1cd28";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ItemTooltipListener());
        MinecraftForge.EVENT_BUS.register(new GuiEventListener());
        MinecraftForge.EVENT_BUS.register(new ContainerSetItemListener());

        ClientCommandHandler.instance.registerCommand(new DupeResetCommand());
    }
}
