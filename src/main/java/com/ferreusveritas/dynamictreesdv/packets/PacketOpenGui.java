package com.ferreusveritas.dynamictreesdv.packets;

import com.ferreusveritas.dynamictreesdv.view.DiscView;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGui implements IMessage, IMessageHandler<PacketOpenGui, IMessage> {

	@Override
	public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
		System.out.println("Packet to open gui received");
		new DiscView();

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

}
