package club.thom.crystalhollowsdupechecker.packets;

import club.thom.crystalhollowsdupechecker.listeners.GuiEventListener;
import io.netty.channel.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@SuppressWarnings("rawtypes")
@ChannelHandler.Sharable
public class ContainerSetItemListener extends SimpleChannelInboundHandler<Packet> {
    @SubscribeEvent
    public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ChannelPipeline pipeline = event.manager.channel().pipeline();
        pipeline.addBefore("packet_handler", this.getClass().getName(), this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
        // only listening for S2FPacketSetSlot packets!
        ctx.fireChannelRead(msg);
        if (!(msg instanceof S2FPacketSetSlot)) {
            return;
        }
        if (!GuiEventListener.isInAh) {
            return;
        }
        S2FPacketSetSlot packet = (S2FPacketSetSlot) msg;
        if (packet.func_149174_e() == null) {
            return;
        }
        if (packet.func_149175_c() != GuiEventListener.ahWindowId) {
            return;
        }
        new Thread(() -> {
            String uuid = GuiEventListener.checkDuped(packet.func_149173_d(), packet.func_149174_e());
            if (uuid == null) {
                return;
            }
            GuiEventListener.dupedUuids.add(uuid);
        }).start();
    }
}
