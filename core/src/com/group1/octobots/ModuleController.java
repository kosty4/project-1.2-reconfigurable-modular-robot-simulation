package com.group1.octobots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.group1.octobots.AI.ComputationalEngine;
import com.group1.octobots.AI.Direction;
import com.group1.octobots.AI.Move;
import com.group1.octobots.AI.State;
import com.group1.octobots.AI.ArrayState;
import com.group1.octobots.physics.Movement;
import com.group1.octobots.physics.PhysicsEngine;
import com.group1.octobots.utility.CyclicIntIterator;
import java.security.Key;
import java.util.List;

/**
 * Created on 5/16/2017.
 */
public class ModuleController {

    private CyclicIntIterator modSelector;
    private Module selectedMod;
    private Movement mover;
    private State state;

    public ModuleController() {
        modSelector = new CyclicIntIterator(World.modCount - 1);
        selectedMod = World.modules.get(modSelector.current());
        selectedMod.gfxComponent().toggleMoved();
        System.out.println("Selected " + selectedMod);
        state = World.state;
        mover = new Movement(state);
    }


    public void listen() {
        if (Gdx.input.isKeyJustPressed(Keys.COMMA)) {

        }
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            PhysicsEngine.SIMULATION_PAUSED =
                    !PhysicsEngine.SIMULATION_PAUSED;
        }
        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            selectedMod.gfxComponent().toggleMoved();
        }
        if (Gdx.input.isKeyJustPressed(Keys.PERIOD)) {

        }
        if (Gdx.input.isKeyJustPressed(Keys.F)) {
            World.computationalEngine.step();
        }
        if (Gdx.input.isKeyJustPressed(Keys.T)) {
            state.assertValidity();
        }

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
                selectedMod = World.modules.get(modSelector.previous());
                selectedMod.gfxComponent().toggleMoved();
                World.modules.get(modSelector.peekNext())
                        .gfxComponent()
                        .toggleMoved();
                List<Move> availableMoves = mover.getAvailableMoves(selectedMod);
                System.out.println("Selected " + selectedMod + ((selectedMod.body().isConverged())?" is converged" : " not converged"));
                System.out.println(availableMoves.size()+" moves: " + availableMoves);
                selectedMod.body().COOLOID_METH();
                return;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
            selectedMod = World.modules.get(modSelector.next());
            selectedMod.gfxComponent().toggleMoved();
            World.modules.get(modSelector.peekPrevious())
                    .gfxComponent()
                    .toggleMoved();
            List<Move> availableMoves = mover.getAvailableMoves(selectedMod);
            System.out.println("Selected " + selectedMod + ((selectedMod.body().isConverged())?" is converged" : " not converged"));
            System.out.println(availableMoves.size()+" moves: " + availableMoves);
            selectedMod.body().COOLOID_METH();
            return;
        }

        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            for (Move m : mover.getAvailableMoves(selectedMod)) {
                if (m.getDirection() == Direction.Front) {
                    if (MathUtils.isZero(m.getMaxMagnitude())) continue;
                    System.out.println(m.toStringVerbose());
                    m.apply(state);
                    break;
                }
            }
        } else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            for (Move m : mover.getAvailableMoves(selectedMod)) {
                if (m.getDirection() == Direction.Back) {
                    if (MathUtils.isZero(m.getMaxMagnitude())) continue;
                    System.out.println(m.toStringVerbose());
                    m.apply(state);
                    break;
                }
            }
        } else if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            for (Move m : mover.getAvailableMoves(selectedMod)) {
                if (m.getDirection() == Direction.Left) {
                    if (MathUtils.isZero(m.getMaxMagnitude())) continue;
                    System.out.println(m.toStringVerbose());
                    m.apply(state);
                    break;
                }
            }
        } else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            for (Move m : mover.getAvailableMoves(selectedMod)) {
                if (m.getDirection() == Direction.Right) {
                    if (MathUtils.isZero(m.getMaxMagnitude())) continue;
                    System.out.println(m.toStringVerbose());
                    m.apply(state);
                    break;
                }
            }
        }
    }

}
