package org.solace.network.packet;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.solace.network.NIOSelector;
import org.solace.network.RSChannelContext;
import org.solace.world.World;

/**
 * Single data packet implementation.
 * Represents a data packet which is used to decode incoming or encode outgoing
 * data with RuneScape protocol standards.
 * @author Faris
 */
public class Packet {

	private ByteBuffer buffer = ByteBuffer.allocate(5096);
	private int opcode, length;

	/**
	 * Reads a signed byte.
	 * 
	 * @return the signed byte value
	 */
	public int getByte() {
		return buffer.get();
	}

	/**
	 * Reads negated signed byte.
	 * 
	 * @return the signed byte value
	 */
	public int getByteC() {
		return -(buffer.get());
	}

	/**
	 * Reads the unsigned subtrahend from the buffer.
	 * 
	 * @return the unsigned byte value
	 */
	public int getUByteS() {
		return 128 - buffer.get() & 0xff;
	}

	/**
	 * Reads signed big-endian short.
	 * 
	 * @return the short value
	 */
	public int getShort() {
		return buffer.getShort();
	}

	/**
	 * Reads unsigned big-endian short.
	 * 
	 * @return the short value
	 */
	public int getUShort() {
		int i = buffer.get(), j = buffer.get();
		return ((i & 0xff) << 8) + (j & 0xff);
	}

	/**
	 * Reads unsigned little-endian short.
	 * 
	 * @return the short value
	 */
	public int getULEShort() {
		int i = buffer.get(), j = buffer.get();
		return ((j & 0xff) << 8) + (i & 0xff);
	}

	/**
	 * Reads unsigned big-endian short with addition.
	 * 
	 * @return the short value
	 */
	public int getUShortA() {
		int i = buffer.get(), j = buffer.get();
		return ((i & 0xff) << 8) + (j - 128 & 0xff);
	}

	/**
	 * Reads unsigned little-endian short with addition.
	 * 
	 * @return the short value
	 */
	public int getULEShortA() {
		int i = buffer.get(), j = buffer.get();
		return ((j & 0xff) << 8) + (i - 128 & 0xff);
	}

	/**
	 * Reads little-endian short.
	 * 
	 * @return the short value
	 */
	public int getLEShort() {
		int i = buffer.get(), j = buffer.get();
		int value = ((j & 0xff) << 8) + (i & 0xff);
		return value > 32767 ? value - 0x10000 : value;
	}

	/**
	 * Reads little-endian short.
	 * 
	 * @return the short value
	 */
	public int getLEShortA() {
		int i = buffer.get(), j = buffer.get();
		int value = ((j & 0xff) << 8) + (i - 128 & 0xff);
		return value > 32767 ? value - 0x10000 : value;
	}

	/**
	 * Reads big-endian long.
	 * 
	 * @return the long value
	 */
	public long getLong() {
		return buffer.getLong();
	}


	/**
	 * Reads given amount of bytes to the array along with addition to the
	 * values.
	 * 
	 * @param amount
	 *            the amount of bytes
	 * 
	 * @return the bytes array
	 */
	public byte[] getBytesA(int amount) {
		byte[] bytes = new byte[amount];
		for (int i = 0; i < amount; i++) {
			bytes[i] = (byte) (buffer.get() + 128);
		}
		return bytes;
	}
	
	/**
	 * Reads the amount of bytes into a byte array, starting at the current
	 * position.
	 * 
	 * @param amount
	 *            the amount of bytes
	 * @param type
	 *            the value type of each byte
	 * @return a buffer filled with the data
	 */
	public byte[] readBytes(int amount) {
		byte[] data = new byte[amount];
		for (int i = 0; i < amount; i++) {
			data[i] = (byte) getByte();
		}
		return data;
	}

	/**
	 * Reads given amount of bytes to the array in reverse order along with
	 * addition to the values.
	 * 
	 * @param amount
	 *            the amount of bytes
	 * 
	 * @return the bytes array
	 */
	public byte[] getReversedBytesA(int amount) {
		byte[] bytes = new byte[amount];
		int position = amount - 1;
		for (; position >= 0; position--) {
			bytes[position] = (byte) (buffer.get() + 128);
		}
		return bytes;
	}

	/**
	 * Sets associated buffer for this packet
	 * 
	 * @param buffer
	 *            the byte buffer
	 */
	public Packet buffer(ByteBuffer buffer) {
		this.buffer = buffer;
		return this;
	}

	/**
	 * Gets the associated byte buffer.
	 * 
	 * @return the byte buffer
	 */
	public ByteBuffer buffer() {
		return buffer;
	}

	/**
	 * Sets the associated opcode.
	 * 
	 * @param opcode
	 *            the packet opcode
	 */
	public Packet opcode(int opcode) {
		this.opcode = opcode;
		return this;
	}

	/**
	 * Gets the associated opcode.
	 * 
	 * @return the packet opcode
	 */
	public int opcode() {
		return opcode;
	}

	/**
	 * Sets the packet payload length.
	 * 
	 * @param length
	 *            the payload length
	 */
	public Packet length(int length) {
		this.length = length;
		return this;
	}

	/**
	 * Gets the packet payload length.
	 * 
	 * @return the payload length
	 */
	public int length() {
		return length;
	}

	/**
	 * Sends the packet data to the socket channel.
	 * 
	 * @param channel
	 *            the socket channel
	 * 
	 * @see SocketChannel #write(ByteBuffer)
	 */
	public Packet sendTo(SocketChannel channel) {
		try {
			buffer.flip();
			channel.write(buffer);
		} catch (Exception e) {
			World.getSingleton().deregister(((RSChannelContext) channel.keyFor(
					selector.selector()).attachment()).player());
		}
		return this;
	}

	/**
	 * Builds the incoming packet for packet processing.
	 * 
	 * @param source
	 *            the data source
	 * 
	 * @param opcode
	 *            the packet opcode
	 * 
	 * @param length
	 *            the payload length
	 * 
	 * @return new packet ready to be processed
	 */
	public static Packet buildPacket(ByteBuffer source, int opcode, int length) {
		Packet packet = new Packet().buffer(ByteBuffer.allocateDirect(length))
				.opcode(opcode).length(length);
		for (; length > 0; length--) {
			packet.buffer().put(source.get());
		}
		packet.buffer().flip();
		return packet;
	}
        
        NIOSelector selector = new NIOSelector();

}