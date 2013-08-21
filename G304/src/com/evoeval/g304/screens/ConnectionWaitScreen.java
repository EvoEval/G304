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
import com.evoeval.g304.G304Main;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.DrawText;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;



public class ConnectionWaitScreen implements Screen {

	
	Game game;
	Stage stage=null;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	String txtStatus;
	float timer;
	int i=0;
	float touchX=0f,touchY=0f;

	public ConnectionWaitScreen (Game game) {
		timer=0.0f;
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
	}
	public void update (float deltaTime) {
		timer+=deltaTime;
		if(Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;
		}
		if(Medium.isConnected()){
			txtStatus=ResourceLoader.locale.get("Conected");
		}else{
			txtStatus=ResourceLoader.locale.get("Waiting");
		}
		if(timer>0.5f){
			if(i<5){
				i++;
			}else{
				i=0;
			}
			timer=0.0f;
		}

	}

	public void draw (float deltaTime) {
		float y=-100.0f;
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.begin();
		ScreenStandardHandler.initStandardScreen(batcher);
		DrawText.drawFontLarge(batcher, txtStatus, 0, 100);
		for(int x=0;x<i;x++){
			ResourceLoader.redDot.positionImageSpriteCenterCenter(y, 100.0f);
			y+=50;
			ResourceLoader.redDot.drawImageSprite(batcher);	
		}
		y=-100;
		for(int k=0;k<5;k++){
			ResourceLoader.dotSlot.positionImageSpriteCenterCenter(y, 100.0f);
			ResourceLoader.dotSlot.drawImageSprite(batcher);
			y+=50;
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
