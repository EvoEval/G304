package com.evoeval.g304.screens;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
import com.evoeval.g304.util.UPnP;




public class ServerWaitForClientsScreen implements Screen {

	
	Game game;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Stage stage=null;
	float touchX=0f,touchY=0f;
	Label txtIP,txtClients;
	TextField txtServerName;
	



	public ServerWaitForClientsScreen (Game game) {
		
		this.game = game;
		guiCam = G304Main.camera;

		batcher = new SpriteBatch();	
		
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
		
		String serverIp="";
		try {
			serverIp = ResourceLoader.locale.get("Server")+" : "+UPnP.getLocalAddress();
		} catch (Exception e) {
			//TODO
		}
		
		txtServerName = new TextField("", ResourceLoader.skin);
		txtIP = new Label(serverIp, ResourceLoader.skin);
		txtClients = new Label(ResourceLoader.locale.get("Client")+" : "+Medium.getClientCount(), ResourceLoader.skin);
		
		stage.addActor(txtServerName);
		stage.addActor(txtIP);
		stage.addActor(txtClients);
		
		ScreenRatioCalculator.moveActorCenterCenter(txtServerName, 0, 50.0f);		
		ScreenRatioCalculator.moveActorCenterCenter(txtClients, 0, -50.0f);		
		ScreenRatioCalculator.moveActorCenterCenter(txtIP, 0, 0.0f);
		
		Random r = new Random();
				
		txtServerName.setText("sv" + r.nextInt(1000) );
		
		txtServerName.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField txtIP, char key) {
				if (key == '\n') txtIP.getOnscreenKeyboard().show(false);
			}
		});
	}

	public void update (float deltaTime) {
		
		txtClients.setText(ResourceLoader.locale.get("Client")+" : "+Medium.getClientCount());
		if(Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;
			if(ResourceLoader.okButton.isTouched(touchX,touchY) && Medium.getClientCount()==3){
				AudioLoader.play(AudioLoader.clickSound);
				if(txtServerName.getText().isEmpty()){
					//todo
				}else{
					new Thread("FakeClient") {
						public void run () {
							try {
								G304NetworkHandler.gClient = new G304Client("localhost",txtServerName.getText());
							} catch (Exception ex) {
								System.out.println("fake client creation failed");
							}
						}
					}.start();
					if(Screens.connectionWaitScreen==null) 
						Screens.connectionWaitScreen = new ConnectionWaitScreen(game);
					else
						Screens.connectionWaitScreen.init();
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
		if(Medium.getClientCount()==3){
			ResourceLoader.okButton.positionImageSpriteCenterCenter(0f, 120.0f);
			ResourceLoader.okButton.drawImageSprite(batcher);
		}
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
		//helpImage.dispose();
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		batcher.dispose();
	}

}
