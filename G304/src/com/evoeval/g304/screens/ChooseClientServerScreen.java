package com.evoeval.g304.screens;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Server;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * This is the screen that chooses the client/server mode for current user
 * User may choose either client / server / exit
 * @author EvoEval
 *
 */
public class ChooseClientServerScreen implements Screen {

	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	/**
	 * this will initialize the position of client and server buttons
	 * @param game Game
	 */
	public ChooseClientServerScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();
		ResourceLoader.serverButton.positionImageSpriteCenterCenter(0f, -50f);
		ResourceLoader.clientButton.positionImageSpriteCenterCenter(0f, +50f);
	}
	/**
	 * check if the user touched the screen if so then check if the client or server button is touched
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,false,null)) return;
			if(ResourceLoader.serverButton.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				G304Server.startServer();
				if(Screens.serverWaitForClientsScreen==null){ 
					Screens.serverWaitForClientsScreen = new ServerWaitForClientsScreen(game);
				}else{
					Screens.serverWaitForClientsScreen.init();
				}
				game.setScreen(Screens.serverWaitForClientsScreen);
			}else if(ResourceLoader.clientButton.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				if(Screens.clientRequestScreen==null){
					Screens.clientRequestScreen = new ClientRequestScreen(game);
				}else{
					Screens.clientRequestScreen.init();
				}
				game.setScreen(Screens.clientRequestScreen);
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
		ResourceLoader.serverButton.drawImageSprite(batcher);
		ResourceLoader.clientButton.drawImageSprite(batcher);
		//DrawText.drawText24(batcher, "x="+touchX+" y="+touchY, 0, 0);
		batcher.end();
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
