package com.group1.octobots.physics;

import com.group1.octobots.World;

/**
 * Created on 5/21/2017.
 */
public class OtherPhysics extends Thread {

    private boolean running;
    private long beginTime;
    private long timeDiff;
    private long sleepTime;
    private static final int STEPS = 60;

    public OtherPhysics() {
        setName("Physics Thread");
    }

    @Override
    public void run() {
        running = true;
        System.out.println("OtherPhysics.run");
        while (running) {
            beginTime = System.currentTimeMillis();
            synchronized (World.modules) {

            }
        }
    }
}
