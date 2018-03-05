/**
 * This file is part of Zap Framework.
 *
 * Zap is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Zap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Zap. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solace.network;

import org.jboss.netty.channel.Channel;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class Session {

    private final Channel channel;
    private Player player;

    public Session(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setClient(Player player) {
        this.player = player;
    }
}
