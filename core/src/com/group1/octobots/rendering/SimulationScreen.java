package com.group1.octobots.rendering;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.group1.octobots.Module;
import com.group1.octobots.ModuleController;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;
import com.group1.octobots.World;
import com.group1.octobots.physics.PhysicsEngine;
import com.group1.octobots.physics.V3;
import com.group1.octobots.utility.Constants;

import static com.group1.octobots.rendering.Models.*;
import static com.group1.octobots.utility.Constants.Rendering.X_OFFSET;
import static com.group1.octobots.utility.Constants.Rendering.Y_OFFSET;

/**
 * Class responsible for the rendering of the Simulation.
 */
public class SimulationScreen implements Screen {

    private ModuleController modController;

    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
//    private HUD hud;

    private ModelBatch modelBatch;

    private Array<ModelInstance> initialConfigInstances   = new Array<>(World.modCount);
    private Array<ModelInstance> obstacleInstances        = new Array<>(World.obstacles.size());
    private Array<ModelInstance> targetInstances          = new Array<>(World.targets.size());

    private ModelInstance floorInstance;
    private ModelInstance lGridInstance;

    public SimulationScreen() {

        // Set up the Environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // Set up the Camera TODO Make thisClass implement InputProcessor to handle camera movement better
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(-13f * 2, 9f * 2, -5f * 2);  //TODO make it a function of World.SIZE: scalable and clear code for bonus points, no magic numbers
        cam.lookAt(Vector3.Zero);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        camController.target = Vector3.Zero;
        Gdx.input.setInputProcessor(camController);

        // Create ModelBatch
        modelBatch = new ModelBatch();

        // Set up the floor
        floorInstance = new ModelInstance(floorModel);
        floorInstance.transform.setToTranslation(0f, -Constants.Physics.OBJECT_SIZE / 2, 0f);
        floorInstance.transform.rotate(Vector3.X, 90);

        // Set up the grid
        lGridInstance = new ModelInstance(lineGridModel);
        lGridInstance.transform.setToTranslation(0f, -Constants.Physics.OBJECT_SIZE / 2 + 0.05f, 0f);

        // Create the HUD
//        hud = new HUD();

        // Create instances of the drawable objects
        makeModelInstances();

        // Make rendering available only on request.
        Gdx.graphics.setContinuousRendering(true);

        // For showcasing Physics, be able to control the modules by direct input
        modController = new ModuleController();

//		World.computationalModule.run();
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Listen for user input
        modController.listen();

        // Run the simulation
        World.physicsEngine.update(delta);

        // Render the simulation
        draw();

        Gdx.graphics.setTitle("OctoBots | " + Gdx.graphics.getFramesPerSecond()
                + " | TIMESTEP " + World.simulationTimeSteps);
    }

    private void draw() {
        // Update the camera
        camController.update();

        // Clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Start rendering
        modelBatch.begin(cam);
        modelBatch.render(floorInstance, environment);
        modelBatch.render(lGridInstance, environment);

        for (Module module : World.modules) {
            module.gfxComponent().render(modelBatch);
        }

        modelBatch.render(targetInstances, environment);
        modelBatch.render(initialConfigInstances, environment);
        modelBatch.render(obstacleInstances, environment);
        modelBatch.end();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        // Dispose of all resources
        modelBatch.dispose();
        Models.dispose();
//        botInstances.clear();
        obstacleInstances.clear();
        targetInstances.clear();
    }

    /*private void updateBots() {
        for (ObjectMap.Entry<Module, ModelInstance> entry : botInstances) {
            ModelInstance botInstance = entry.value;
            Module mod = entry.key;

            botInstance.transform.setToTranslation(
                    renderingVector(mod.pos())
            );
        }
    }*/

    private void makeModelInstances() {
        for (Obstacle o : World.obstacles) {
            obstacleInstances.add(
                    new ModelInstance(
                            obstacleModel,
                            renderingVector(o.pos())
                    )
            );
        }
        for (Target t : World.targets) {
            targetInstances.add(
                    new ModelInstance(
                            targetModel,
                            renderingVector(t.pos())
                    )
            );
        }
    }

    private Vector3 renderingVector(V3 simulationVector) {
        return new Vector3(
                simulationVector.x - X_OFFSET,
                simulationVector.z,
                simulationVector.y - Y_OFFSET);
    }
}
