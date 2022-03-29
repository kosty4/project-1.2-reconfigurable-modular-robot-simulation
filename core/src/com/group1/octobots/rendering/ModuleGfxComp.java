package com.group1.octobots.rendering;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.group1.octobots.Module;
import com.group1.octobots.physics.V3;

/**
 * Created on 4/23/2017.
 */
public class ModuleGfxComp implements GraphicsComponent {

    private Vector3 renderingPosition;

    private ModelInstance instance;
    private ModelInstance secondary;

    public ModuleGfxComp(V3 position) {
        renderingPosition = new Vector3();

        instance = new ModelInstance(
                Models.robotModel,
                renderingPosition
        );
        secondary = new ModelInstance(
                Models.movedRobotModel,
                renderingPosition
        );

        updateModel(position);
    }

    @Override
    public void render(ModelBatch mb) {
        mb.render(instance);
    }

    @Override
    public void updateModel(V3 vector) {
        setToDrawVector(vector, renderingPosition);
        instance.transform.setToTranslation(renderingPosition);
        secondary.transform.setToTranslation(renderingPosition);
    }

    public void toggleMoved() {
        ModelInstance swappidySwap = secondary;
        secondary = instance;
        instance = swappidySwap;
    }
}
