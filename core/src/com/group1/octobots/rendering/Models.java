package com.group1.octobots.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.group1.octobots.utility.Constants;

import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;
import static com.group1.octobots.utility.Constants.Physics.OBJECT_SIZE;

/**
 * Container class for Models of drawable objects.
 */
public class Models {


    static Model robotModel;
    static Model movedRobotModel;
    static Model obstacleModel;
    static Model initialConfigModel;
    static Model targetModel;

    static Model floorModel;
    static Model lineGridModel;

    static void init() {
        ModelBuilder mb = new ModelBuilder();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        robotModel = mb.createBox(OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE,
                new Material(TextureAttribute.createDiffuse(new Texture("textures/BotTexture.png"))),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        movedRobotModel = mb.createBox(OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE,
                new Material(TextureAttribute.createDiffuse(new Texture("textures/BotTextureMoved.png"))),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        obstacleModel = mb.createBox(OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE,
                new Material(TextureAttribute.createDiffuse(new Texture("textures/ObstacleTexture.png"))),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        initialConfigModel = mb.createBox(OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE,
                new Material(TextureAttribute.createDiffuse(new Texture("textures/InitialConfigTexture.png"))),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        initialConfigModel.materials.first()
                .set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                );

        targetModel = mb.createBox(OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE,
                new Material(TextureAttribute.createDiffuse(new Texture("textures/TargetTexture.png"))),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        targetModel.materials.first()
                .set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                );

        floorModel = mb.createBox(MAP_SIZE.x, MAP_SIZE.y, 0.01f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                Usage.Position | Usage.Normal);

        lineGridModel = mb.createLineGrid(MAP_SIZE.x, MAP_SIZE.y, OBJECT_SIZE, OBJECT_SIZE,
                new Material(ColorAttribute.createDiffuse(Color.BLACK)),
                Usage.Position | Usage.Normal);
    }

    static void dispose() {
        robotModel.dispose();
        obstacleModel.dispose();
        targetModel.dispose();
        floorModel.dispose();
        lineGridModel.dispose();
    }

}
