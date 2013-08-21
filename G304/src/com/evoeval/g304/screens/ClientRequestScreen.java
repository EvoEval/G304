package com.evoeval.g304.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Client;
import com.evoeval.g304.multiplayer.G304NetworkHandler;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * This is the client request screen 
 * once the ip of the server and name are given client can connect
 * 
 * @author EvoEval
 *
 */
public class ClientRequestScreen implements Screen {

	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	TextField txtIP,txtnickname;
	
	/**
	 * this will initialize the position of client and server buttons
	 * @param game Game
	 */
	public ClientRequestScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();


		ResourceLoader.connectButton.positionImageSpriteCenterCenter(0f, 80f);
		init();
		
	}
	public void init(){		
		if(stage==null){
			stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);		
		}else{
			stage.clear();
		}
		Gdx.input.setInputProcessor(stage);
		ScreenStandardHandler.setDialogVisible(false);
		
		txtIP = new TextField(Medium.getServerIPAddress(),  ResourceLoader.skin);
		txtIP.setMessageText("IP Address");
		txtnickname = new TextField(Medium.getNickName(), ResourceLoader.skin);
		txtnickname.setMessageText("Nickname");
		ScreenRatioCalculator.moveActorCenterCenter(txtnickname, 0f, 80f);
		ScreenRatioCalculator.moveActorCenterCenter(txtIP, 0f, 30f);
		
		stage.addActor(txtnickname);
		stage.addActor(txtIP);
		
		txtIP.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField txtIP, char key) {
				if (key == '\n') txtIP.getOnscreenKeyboard().show(false);
			}
		});
	}
	/**
	 * 
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if(Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;
			if(ResourceLoader.connectButton.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				if(txtnickname.getText().isEmpty()){
					//todo
				}else if(txtIP.getText().isEmpty()){
					//todo
				}else{
					try {
						G304NetworkHandler.gClient = new G304Client(txtIP.getText().trim(), txtnickname.getText().trim());
					} catch (Exception e){
						System.out.println("create client bug->" + e.getMessage());
					}
					
					if(Screens.connectionWaitScreen==null){ 						
						Screens.connectionWaitScreen = new ConnectionWaitScreen(game);
					}else{
						Screens.connectionWaitScreen.init();
					}
					Medium.setNickName(txtnickname.getText());
					Medium.setServerIPAddress(txtIP.getText());
					Medium.saveClientInformation();
					game.setScreen(Screens.connectionWaitScreen);
				}
			}
		}
		
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.begin();
		ScreenStandardHandler.initStandardScreen(batcher);
		ResourceLoader.connectButton.drawImageSprite(batcher);
		batcher.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
	}

	@Override
	public void render (float delta) {

		update(delta);
		draw(delta);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		batcher.dispose();
	}

}
