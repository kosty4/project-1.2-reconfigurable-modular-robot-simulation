package com.group1.octobots.rendering;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.group1.octobots.physics.V3;

import static com.group1.octobots.utility.Constants.Rendering.X_OFFSET;
import static com.group1.octobots.utility.Constants.Rendering.Y_OFFSET;

/**
 * Created on 4/23/2017.
 */
public interface GraphicsComponent {

    void updateModel(V3 vector);
    void render(ModelBatch mb);

    default void setToDrawVector(V3 vector, Vector3 drawVector) {
        drawVector.set(
                vector.x - X_OFFSET,
                vector.z,
                vector.y - Y_OFFSET
        );
    }
}
