package com.robertx22.network;

import com.robertx22.mmorpg.Main;
import com.robertx22.saveclasses.Unit;
import com.robertx22.uncommon.datasaving.UnitSaving;
import com.robertx22.uncommon.datasaving.bases.Saving;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerPackage implements IMessage {

	public String toSend;

	public PlayerPackage() {

	}

	public PlayerPackage(String str) {
		this.toSend = str;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		toSend = ByteBufUtils.readUTF8String(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, toSend);

	}

	public static class Handler implements IMessageHandler<PlayerPackage, IMessage> {

		@Override
		public IMessage onMessage(PlayerPackage message, MessageContext ctx) {

			try {

				final EntityPlayer player = Main.proxy.getPlayerEntityFromContext(ctx);

				Unit unit = Saving.Load(message.toSend, Unit.class);

				if (unit != null) {
					UnitSaving.Save(player, unit);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

	}
}
