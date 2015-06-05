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
package org.solace.network;

import java.nio.channels.SocketChannel;
import net.burtlebutle.bob.rand.isaac.ISAAC;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author KleptO
 */
public class RSChannelContext {

    private SocketChannel channel;
    private NIODecoder decoder;
    private Player player;
    private ISAAC encryption, decryption;

    /**
     * Creates a new channel context.
     * 
     * @param channel
     *            the channel
     * 
     * @param decoder
     *            the current incoming data decoder
     */
    public RSChannelContext(SocketChannel channel, NIODecoder decoder) {
            this.channel = channel;
            this.decoder = decoder;
    }

    /**
     * Sets the socket channel associated with this context.
     * 
     * @param channel
     *            the socket channel
     */
    public RSChannelContext channel(SocketChannel channel) {
            this.channel = channel;
            return this;
    }

    /**
     * Gets the associated socket channel
     * 
     * @return the socket channel
     */
    public SocketChannel channel() {
            return channel;
    }

    /**
     * Sets the input data decoder.
     * 
     * @param decoder
     *            the input decoder
     */
    public RSChannelContext decoder(NIODecoder decoder) {
            this.decoder = decoder;
            return this;
    }

    /**
     * Gets the input data decoder.
     * 
     * @return the input decoder
     */
    public NIODecoder decoder() {
            return decoder;
    }

    /**
     * Sets the player object for this channel.
     * 
     * @param player
     *            the player object
     */
    public RSChannelContext player(Player player) {
            this.player = player;
            return this;
    }

    /**
     * Gets the associated player object.
     * 
     * @return the associated player object
     */
    public Player player() {
            return player;
    }

    /**
     * Sets the output encryption for this channel.
     * 
     * @param encryption
     *            the output encryption
     */
    public RSChannelContext encryption(ISAAC encryption) {
            this.encryption = encryption;
            return this;
    }

    /**
     * Gets the output encryption.
     * 
     * @return the output encryption
     */
    public ISAAC encryption() {
            return encryption;
    }

    /**
     * Sets the input decryption for this channel.
     * 
     * @param encryption
     *            the input decryption
     */
    public RSChannelContext decryption(ISAAC decryption) {
            this.decryption = decryption;
            return this;
    }

    /**
     * Gets the input decryption.
     * 
     * @return the input decryption
     */
    public ISAAC decryption() {
            return decryption;
    }
}
