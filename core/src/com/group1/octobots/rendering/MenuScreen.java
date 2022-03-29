package com.group1.octobots.rendering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.group1.octobots.World;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * Created by Jun on 2017-05-14.
 */
public class MenuScreen extends ScreenAdapter {

    private Stage stage;
    private JFileChooser fileChooser = new JFileChooser("/input/");

    public MenuScreen(){
        init();
    }

    private void init() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton startBtn = new TextButton("Start", skin);
        startBtn.setPosition(300,350);
        startBtn.setSize(200,60);
        startBtn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                startBtnClicked();
            }

        });
        stage.addActor(startBtn);

        TextButton exitBtn = new TextButton("Exit",skin);
        exitBtn.setPosition(300,150);
        exitBtn.setSize(200,60);
        exitBtn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                exitBtnClicked();
            }
        });
        stage.addActor(exitBtn);

        TextButton fileBtn = new TextButton(" Agent ",skin);
        fileBtn.setPosition(50,350);
        fileBtn.setSize(200,60);
        fileBtn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                fileBtnClicked();
            }
        });
        stage.addActor(fileBtn);

        TextButton file2Btn = new TextButton(" Obstacle ",skin);
        file2Btn.setPosition(50,250);
        file2Btn.setSize(200,60);
        file2Btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                fileBtn2Clicked();
            }
        });
        stage.addActor(file2Btn);

        TextButton file3Btn = new TextButton(" Target ",skin);
        file3Btn.setPosition(50,150);
        file3Btn.setSize(200,60);
        file3Btn.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                fileBtn3Clicked();
            }
        });
        stage.addActor(file3Btn);
    }

    @Override
    public void show() {
        Models.init();  //fixme See how all this should be ordered: Models is package-private, when do we Initialize {@link World}?
        World.init();
    }

    private void startBtnClicked(){
        World.application.setScreen(new SimulationScreen()); //change to simulation screen
    }

    private void exitBtnClicked(){
        Gdx.app.exit();
    }

    private void fileBtnClicked(){
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
        }
    }

    private void fileBtn2Clicked(){
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file2 = fileChooser.getSelectedFile();
        }
    }

    private void fileBtn3Clicked(){
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file3 = fileChooser.getSelectedFile();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
}
