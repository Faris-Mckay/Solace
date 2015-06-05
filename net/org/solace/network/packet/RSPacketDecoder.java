/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.network.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.solace.network.NIODecoder;
import org.solace.network.RSChannelContext;


/**
 * RuneScape protocol packets decoder.
 * @author KleptO
 */
public class RSPacketDecoder implements NIODecoder {

	private int opcode = -1, length = -1;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(126);

	@Override
	public void decode(RSChannelContext channelContext) throws IOException {
		/*
		 * Read the incoming data to our local buffer.
		 */
		channelContext.channel().read(buffer);
		buffer.flip();

		/*
		 * If buffer has no readable data, we skip packet decoding.
		 */
		if (!buffer.hasRemaining()) {
			buffer.clear();
			return;
		}

		while (buffer.hasRemaining()) {
			if (opcode == -1) {
				opcode = buffer.get() & 0xFF;
				opcode = opcode - channelContext.decryption().getNextKey()
						& 0xff;
				length = PACKET_SIZES[opcode];
			}

			/*
			 * Length -1 indicates that this packet is variable sized. There for
			 * we need to read next byte to figure out packet's length.
			 */
			if (length == -1) {
				if (!buffer.hasRemaining()) {
					buffer.clear();
					return;
				}
				length = buffer.get() & 0xFF;
			}

			if (buffer.remaining() < length) {
				buffer.compact();
				return;
			}

			Packet packet = Packet.buildPacket(buffer, opcode, length);
			PacketType.handlePacket(channelContext, packet);

			/*
			 * After handling the packet we reset the variables in order to
			 * continue decoding other packets.
			 */
			opcode = length = -1;
			buffer.compact().flip();
		}

		/*
		 * After all packets are handled we need to clear the buffer to be ready
		 * to accept new incoming data.
		 */
		buffer.clear();
	}

	/**
	 * Incoming packet sizes. Some of the packets have fixed or no size at all,
	 * for those packets we don't need to read length byte. Yet all the packets
	 * which have size of -1 have length indicator which we have to read in
	 * order to figure out the size.
	 */
	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, 0, 2, 0, 6, 0, 12, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 8, 4, 0, 0, 2, 2, 6, 0, 6, 0, -1, 0, 0, 0, 0, 0, 0, 0, 12,
			0, 0, 0, 0, 8, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 2, 2, 8, 6,
			0, -1, 0, 6, 0, 0, 0, 0, 0, 1, 4, 6, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0,
			-1, 0, 0, 13, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0,
			0, 1, 0, 6, 0, 0, 0, -1, 0, 2, 6, 0, 4, 6, 8, 0, 6, 0, 0, 0, 2, 0,
			0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 1, 2, 0, 2, 6, 0, 0, 0, 0, 0, 0,
			0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 3, 0,
			2, 0, 0, 8, 1, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
			0, 4, 0, 4, 0, 0, 0, 7, 8, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, -1, 0, 6,
			0, 1, 0, 0, 0, 6, 0, 6, 8, 1, 0, 0, 4, 0, 0, 0, 0, -1, 0, -1, 4, 0,
			0, 6, 6, 0, 0, 0 };

}