package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.world.game.entity.UpdateFlags.UpdateFlag;
import org.solace.world.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.world.game.entity.mobile.MobilityManager;
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public class WalkingUpdatePacket implements PacketHandler {
    
    public static final int COMMAND_MOVEMENT_OPCODE = 98;
    public static final int GAME_MOVEMENT_OPCODE = 164;
    public static final int MINIMAP_MOVEMENT_OPCODE = 248;

    @Override
    public void handlePacket(Player player, Packet packet) {
        if (packet.opcode() == 248) {
                packet.length(packet.length() - 14);
        }
        if (player.getStatus() == WelfareStatus.DEAD)
            return;
        player.setInteractingEntityIndex(-1);
        player.setInteractingEntity(null);
        player.getUpdateFlags().flag(UpdateFlag.FACE_ENTITY);
        //test something nop
        MobilityManager queue = player.getMobilityManager();
        queue.prepare();
        int steps = (packet.length() - 5) / 2;
        int[][] path = new int[steps][2];
        int firstStepX = packet.getLEShortA();
        for (int i = 0; i < steps; i++) {
                path[i][0] = packet.getByte();
                path[i][1] = packet.getByte();
        }
        int firstStepY = packet.getLEShort();
        final boolean runSteps = packet.getByteC() == 1;
        queue.setRunQueue(runSteps);
        queue.queueDestination(new Location(firstStepX, firstStepY));
        for (int i = 0; i < steps; i++) {
                path[i][0] += firstStepX;
                path[i][1] += firstStepY;
                queue.queueDestination(new Location(path[i][0], path[i][1]));
        }
        queue.finish();

        /*
         * Reset the walk to action task.
         */
        if (player.walkToAction() != null) {
                player.walkToAction().stop();
                player.walkToAction(null);
        }

    }

}
