package com.ruinscraft.panilla.paper.v1_21_4.io.dplx;

import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class PacketSerializer implements IPacketSerializer {

  private final FriendlyByteBuf handle;

  public PacketSerializer(FriendlyByteBuf byteBuf) {
    this.handle = byteBuf;
  }

  @Override
  public int readableBytes() {
    return handle.readableBytes();
  }

  @Override
  public int readVarInt() {
    return handle.readVarInt();
  }

  @Override
  public ByteBuf readBytes(int i) {
    return handle.readBytes(i);
  }

  @Override
  public ByteBuf readBytes(byte[] buffer) {
    return handle.readBytes(buffer);
  }

}