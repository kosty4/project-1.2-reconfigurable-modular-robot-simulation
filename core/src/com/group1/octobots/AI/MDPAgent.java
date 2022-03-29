package com.group1.octobots.AI;

import com.group1.octobots.Module;
import com.group1.octobots.World;
import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.Movement;
import com.group1.octobots.physics.V3;
import java.util.Comparator;

import static com.group1.octobots.utility.Constants.Physics.OBJECT_SIZE;

/**
 * Created on 6/22/2017.
 */
public class MDPAgent implements Agent {

    private final State  state;
    private final Module subject;
    private final Movement mover;

    MDPAgent(Module subject, Movement mover, State state) {
        this.state = state;
        this.subject = subject;
        this.mover = mover;
    }

    private float evaluate(Move m) {
        V3 sPrime = subject.pos().cpy().add(m.getResultingVector());
        AABB box = new AABB(sPrime, sPrime.cpy().add(OBJECT_SIZE));
        return (float) World.targets.stream().mapToDouble(tar -> tar.aabb().collide(box).euclidianDistance()).sum() / World.targets.size();
//        float reward = 0;
//        List<Target> targets = state.getTargsAt(sPrime);
//        if (targets.size() == 1) {
//            //height of the target we can reach
//            float tz = targets.get(0).pos().z;
//            //height of the highest target
//            float maxZ = (float) World.targets.stream().mapToDouble(target -> target.pos().z).max().orElse(0);
//            //height of the lowest target
//            float minZ = (float) World.targets.stream().mapToDouble(target -> target.pos().z).min().orElse(0);
//            float dz = maxZ - minZ;
//            float dToMax = Math.abs(maxZ - tz);
//            float dToMin = Math.abs(tz - minZ);
//
//            float negativeReward = 1/dToMax;        // decreases the farther away u are from the highest target
//            float negativeRewardV2 = 1/dToMin;      // decreases the farther away u are from the bottom-most target
//
//            return ThreadLocalRandom.current().nextFloat();
//        }
//
//        return ThreadLocalRandom.current().nextFloat();
    }

    @Override
    public Move act() {
        return mover.getAvailableMoves(subject)
                .stream().min(Comparator.comparingDouble(this::evaluate))
                    .orElseThrow(NullPointerException::new);
//        return null;
    }
}
