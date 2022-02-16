package club.thom.crystalhollowsdupechecker.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerUtils {
    // Lock to create a condition from.
    private final Lock playerAlertLock = new ReentrantLock();

    // To avoid busy-waiting in many threads.
    private final Condition playerNotNullCondition = playerAlertLock.newCondition();


    Thread waitForNotNullPlayer = new Thread(() -> {
        // thePlayer
        while (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) {
            try {
                // This is busy-waiting
                //noinspection BusyWait
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        playerAlertLock.lock();
        try {
            // player is now not null, tell the getPlayer calls!
            playerNotNullCondition.signalAll();
        } finally {
            playerAlertLock.unlock();
        }
    });

    public EntityPlayerSP getPlayer() {
        playerAlertLock.lock();
        try {
            if (Minecraft.getMinecraft().thePlayer == null) {
                if (!waitForNotNullPlayer.isAlive()) {
                    waitForNotNullPlayer.start();
                }
                try {
                    playerNotNullCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            playerAlertLock.unlock();
        }
        return Minecraft.getMinecraft().thePlayer;
    }

}
