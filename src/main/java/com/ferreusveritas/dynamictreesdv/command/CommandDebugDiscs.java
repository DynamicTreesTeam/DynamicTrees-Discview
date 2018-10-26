package com.ferreusveritas.dynamictreesdv.command;

import java.util.ArrayList;
import java.util.List;

import com.ferreusveritas.dynamictreesdv.DynamicTreesDV;
import com.ferreusveritas.dynamictreesdv.packets.PacketOpenGui;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDebugDiscs implements ICommand {
	
	@Override
	public int compareTo(ICommand o) {
		return getName().compareTo(o.getName());
	}
	
	@Override
	public String getName() {
		return "debugdiscs";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "debugdiscs";
	}
	
	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		System.out.println("I'm sending a packet to the command sender");
		PacketOpenGui packet = new PacketOpenGui();
		DynamicTreesDV.network.sendTo(packet, (EntityPlayerMP) sender.getCommandSenderEntity());
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;//anyone can run this command
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	
}
