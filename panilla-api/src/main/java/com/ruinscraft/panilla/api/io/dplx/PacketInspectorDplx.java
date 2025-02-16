package com.ruinscraft.panilla.api.io.dplx;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.PacketException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketInspectorDplx extends ChannelDuplexHandler {

    private final IPanilla panilla;
    private final IPanillaPlayer player;

    public PacketInspectorDplx(IPanilla panilla, IPanillaPlayer player) {
        this.panilla = panilla;
        this.player = player;
    }

    // player -> server
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            panilla.getPacketInspector().checkPlayIn(panilla, player, msg);
        } catch (PacketException e) {
            if (handlePacketException(player, e)) {
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    // server -> player
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            panilla.getPacketInspector().checkPlayOut(panilla, msg);
        } catch (PacketException e) {
            if (handlePacketException(player, e)) {
                return;
            }
        }

        try {
            super.write(ctx, msg, promise);
        } catch (IllegalArgumentException e) {
            // java.lang.IllegalArgumentException: Packet too big (is 10606067, should be less than 8388608): net.minecraft.network.protocol.game.PacketPlayOutWindowItems@20e05249
            panilla.getPanillaLogger().info("Dropped packet to " + player.getName() + " :: " + e.getMessage(), false);
        }
    }

    private boolean handlePacketException(IPanillaPlayer player, PacketException e) {
        if (!player.canBypassChecks(panilla, e)) {
            panilla.getInventoryCleaner().clean(player);

            IPanillaLogger panillaLogger = panilla.getPanillaLogger();
            PTranslations pTranslations = panilla.getPTranslations();

            String nmsClass = e.getNmsClass();
            String username = player.getName();
            String tag;

            if (FailedNbt.failsThreshold(e.getFailedNbt())) {
                tag = "key size threshold";
            } else {
                tag = e.getFailedNbt().key;
            }

            final String message;

            if (e.isFrom()) {
                message = pTranslations.getTranslation("packetFromDropped", nmsClass, username, tag);
            } else {
                message = pTranslations.getTranslation("packetToDropped", nmsClass, username, tag);
            }

            panillaLogger.log(message, true);

            return true;
        }

        return false;
    }

}
