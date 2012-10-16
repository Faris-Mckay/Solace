package org.solace.network.packet;

import java.nio.ByteBuffer;
import org.solace.util.ISAAC;

/**
 * Data packet builder.
 * @author Faris
 */
public class PacketBuilder extends Packet {

	private int[] frameSizes = new int[10];
	private int frameSizePointer = 0;

	private int bitPosition;
	private boolean bitAccess = false;

	/**
	 * Writes a boolean bit flag.
	 * 
	 * @param flag
	 *            the flag
	 */
	public PacketBuilder putBit(boolean flag) {
		putBits(1, flag ? 1 : 0);
		return this;
	}

	/**
	 * Appends the bytes buffer to this packet.
	 * 
	 * @param buffer
	 *            the bytes buffer
	 */
	public PacketBuilder put(ByteBuffer buffer) {
		buffer().put((ByteBuffer) buffer.flip());
		return this;
	}

	/**
	 * Appends the bytes array to this packet.
	 * 
	 * @param buffer
	 *            the bytes array
	 */
	public PacketBuilder put(byte[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			buffer().put(buffer[i]);
		}
		return this;
	}

	/**
	 * Puts the signed byte to the buffer.
	 * 
	 * @param value
	 *            the signed byte
	 */
	public PacketBuilder putByte(int value) {
		buffer().put((byte) value);
		return this;
	}

	/**
	 * Puts the signed byte with addition.
	 * 
	 * @param value
	 *            the signed byte
	 */
	public PacketBuilder putByteA(int value) {
		putByte(value + 128);
		return this;
	}

	/**
	 * Puts the signed byte as subtrahend.
	 * 
	 * @param value
	 *            the signed byte
	 */
	public PacketBuilder putByteS(int value) {
		putByte(128 - value);
		return this;
	}

	/**
	 * Puts the negated signed byte.
	 * 
	 * @param value
	 *            the signed byte
	 */
	public PacketBuilder putByteC(int value) {
		putByte(-value);
		return this;
	}

	/**
	 * Puts the signed short.
	 * 
	 * @param value
	 *            the signed short
	 */
	public PacketBuilder putShort(int value) {
		buffer().putShort((short) value);
		return this;
	}

	/**
	 * Puts the big-endian short with addition.
	 * 
	 * @param value
	 *            the signed short
	 */
	public PacketBuilder putShortA(int value) {
		putByte(value >> 8).putByte(value + 128);
		return this;
	}

	/**
	 * Puts the signed little-endian short.
	 * 
	 * @param value
	 *            the signed short
	 */
	public PacketBuilder putLEShort(int value) {
		putByte(value).putByte(value >> 8);
		return this;
	}

	/**
	 * Puts the signed little-endian short with addition.
	 * 
	 * @param value
	 *            the signed short
	 */
	public PacketBuilder putLEShortA(int value) {
		putByte(value + 128).putByte(value >> 8);
		return this;
	}

	/**
	 * Puts the mixed-endian small integer.
	 * 
	 * @param value
	 *            the signed integer
	 */
	public PacketBuilder putMESmallInt(int value) {
		putByte((byte) (value >> 8)).putByte((byte) value)
				.putByte((byte) (value >> 24)).putByte((byte) (value >> 16));
		return this;
	}

	/**
	 * Puts an integer value
	 * 
	 * @param value
	 *            The signed integer
	 * @return The packet builder
	 */
	public PacketBuilder putInt(int value) {
		putByte(value >> 8).putByte(value >> 16).putByte(value)
				.putByte(value >> 24);
		return this;
	}

	public PacketBuilder putByte3(int value) {
		putByte((byte) value).putByte((byte) value >> 8).putByte(
				(byte) value >> 16);
		return this;
	}

	/**
	 * Puts the given amount of bytes with the same specified value.
	 * 
	 * @param value
	 *            the value of the bytes
	 * 
	 * @param amount
	 *            the amount of bytes
	 */
	public PacketBuilder putBytes(int value, int amount) {
		for (; amount > 0; amount--) {
			putByte(value);
		}
		return this;
	}

	/**
	 * Writes the bytes into this buffer.
	 * 
	 * @param from
	 */
	public void writeBytes(byte[] from, int size) {
		buffer().put(from, 0, size);
	}

	/**
	 * Puts the signed long to the buffer.
	 * 
	 * @param value
	 *            the signed long
	 */
	public PacketBuilder putLong(long value) {
		buffer().putLong(value);
		return this;
	}

	/**
	 * Puts the string to the buffer.
	 * 
	 * @param value
	 *            the string
	 */
	public PacketBuilder putString(String value) {
		put(value.getBytes()).putByte(10);
		return this;
	}

	/**
	 * Creates packet frame with given opcode.
	 * 
	 * @param opcode
	 *            the packet opcode
	 * 
	 * @param encryption
	 *            the ISAAC encryption
	 */
	public PacketBuilder createFrame(int opcode, ISAAC encryption) {
		putByte(opcode + encryption.getNextKey()).opcode(opcode);
		return this;
	}

	/**
	 * Creates sized packet frame with specified opcode.
	 * 
	 * @param opcode
	 *            the packet opcode
	 * 
	 * @param encryption
	 *            the ISAAC encryption
	 */
	public PacketBuilder createSizedFrame(int opcode, ISAAC encryption) {
		createFrame(opcode, encryption).putByte(0);
		frameSizes[++frameSizePointer] = buffer().position();
		return this;
	}

	/**
	 * Creates short sized packet frame with specified opcode.
	 * 
	 * @param opcode
	 *            the packet opcode
	 * 
	 * @param encryption
	 *            the ISAAC encryption
	 */
	public PacketBuilder createShortSizedFrame(int opcode, ISAAC encryption) {
		createFrame(opcode, encryption).putShort(0);
		frameSizes[++frameSizePointer] = buffer().position();
		return this;
	}

	/**
	 * Finishes sized packet frame.
	 */
	public PacketBuilder finishSizedFrame() {
		int position = buffer().position();
		int length = position - frameSizes[frameSizePointer--];
		buffer().put(position - length - 1, (byte) length);
		return this;
	}

	/**
	 * Finishes short sized packet frame.
	 */
	public PacketBuilder finishShortSizedFrame() {
		int position = buffer().position();
		int length = position - frameSizes[frameSizePointer--];
		buffer().put(position - length - 2, (byte) (length >> 8));
		buffer().put(position - length - 1, (byte) length);
		return this;
	}

	/**
	 * Initializes bit access to the buffer.
	 */
	public PacketBuilder bitAccess() {
		if (!bitAccess) {
			bitPosition = buffer().position() * 8;
			bitAccess = true;
		}
		return this;
	}

	/**
	 * Initializes byte access to the buffer only if bit access is currently
	 * active.
	 */
	public PacketBuilder byteAccess() {
		if (bitAccess) {
			buffer().position((bitPosition + 7) / 8);
			bitAccess = false;
		}
		return this;
	}

	/**
	 * Puts boolean as a bit into byte buffer.
	 * 
	 * @param amount
	 *            the number of bits
	 * 
	 * @param value
	 *            the boolean value
	 */
	public PacketBuilder putBits(int amount, boolean value) {
		putBits(amount, value ? 1 : 0);
		return this;
	}

	/**
	 * Puts bits into byte buffer.
	 * 
	 * @param amount
	 *            the number of bits
	 * 
	 * @param value
	 *            the bits value
	 */
	public PacketBuilder putBits(int amount, int value) {
		int i = 0, bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += amount;
		for (; amount > bitOffset; bitOffset = 8) {
			i = buffer().get(bytePos) & ~BIT_MASK[bitOffset];
			buffer().put(bytePos, (byte) i);
			i = buffer().get(bytePos) | (value >> (amount - bitOffset))
					& BIT_MASK[bitOffset];
			buffer().put(bytePos++, (byte) i);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			i = buffer().get(bytePos) & ~BIT_MASK[bitOffset];
			buffer().put(bytePos, (byte) i);
			i = buffer().get(bytePos) | value & BIT_MASK[bitOffset];
			buffer().put(bytePos, (byte) i);
		} else {
			i = buffer().get(bytePos)
					& ~(BIT_MASK[amount] << (bitOffset - amount));
			buffer().put(bytePos, (byte) i);
			i = buffer().get(bytePos)
					| (value & BIT_MASK[amount]) << (bitOffset - amount);
			buffer().put(bytePos, (byte) i);
		}
		return this;
	}

	/**
	 * Allocates packet builder with given capacity.
	 * 
	 * @param capacity
	 *            the packet capacity
	 * 
	 * @return the newly allocated packet builder
	 */
	public static PacketBuilder allocate(int capacity) {
		return (PacketBuilder) new PacketBuilder().buffer(ByteBuffer
				.allocateDirect(capacity));
	}

	public static int BIT_MASK[] = new int[32];

	/**
	 * Initializes the bit masks.
	 */
	static {
		for (int i = 0; i < 32; i++)
			BIT_MASK[i] = (1 << i) - 1;
	}

}