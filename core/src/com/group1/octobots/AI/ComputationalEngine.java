package com.group1.octobots.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.group1.octobots.Module;
import com.group1.octobots.World;
import com.group1.octobots.physics.Movement;
import com.group1.octobots.physics.PhysicsEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Created on 4/21/2017.
 */
public class ComputationalEngine implements Runnable {

    private final State state;
    private List<Agent> agents = new ArrayList<>(World.modCount);
    private boolean converged;
    private int agentsNotMoved;

    private Thread thread;
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public ComputationalEngine(State state) {
        this.state = state;
        Movement mover = new Movement(state);
        for (Module module : World.modules) {
            agents.add(new MDPAgent(module, mover, state));
        }
        thread = new Thread(this);
        thread.setName("INTELLIGENCE Thread");
    }

    private void doStep() {
        for (Agent agent : agents) {
            Move move = agent.act();
            if (MathUtils.isZero(move.getMaxMagnitude()))
                agentsNotMoved++;
            else
                move.apply(state);
        }
        state.assertValidity();
        converged = (agentsNotMoved == agents.size());
        agentsNotMoved = 0;
        World.simulationTimeSteps++;
    }

    public void step() {
//        if (Thread.currentThread().equals(thread)) {
            doStep();
//        } else {
//            if (paused || !thread.isAlive())
//                new Thread(this::doStep).start();
//        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " says HELLO!");
        while (!converged) {
            synchronized (pauseLock) {
                // End thread if !running ( e.g. stop() was called from a different thread )
                if (!running) break;
                // Pause thread if paused ( e.g. pause() was called from a different thread )
                if (paused) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is starting it's break");
                        pauseLock.wait();
                        System.out.println(Thread.currentThread().getName() + " is ending it's break");
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
                // ENDOF Thread techincalities

                // Engine stuff:
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doStep();
            }
        }
        System.out.println(thread.getName() + " says buh-bye!");
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            System.out.println("timeSteps = " + World.simulationTimeSteps);
            return;
        }
        running = false;

        resume();
    }

    public void pause() {
        paused = true;
    }
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public boolean converged() {
        return converged;
    }
}
