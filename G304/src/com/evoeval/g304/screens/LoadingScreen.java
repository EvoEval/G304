package com.evoeval.g304.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.ui.ImageAssetHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.AssetLoader;
import com.evoeval.g304.util.BackgroundRepeater;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.LoadingProgressBar;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;

//
public class LoadingScreen implements Screen {
	
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Game game;
	float touchX=0f,touchY=0f;
	AssetManager manager;
	
	public LoadingScreen (final Game game) {		
		manager = AssetLoader.getAssetManager();
		this.game = game;
		guiCam = G304Main.camera;
		ResourceLoader.welcomeImage = new ImageAssetHandler(Medium.getUIpath()+"/UI/welcome-slot.png",454f,165f,0,0,true);
		
		ResourceLoader.welcomeImage.positionImageSpriteCenterCenter(0, -69f);
		LoadingProgressBar.initLoadingProgressBar();
		
		AudioLoader.init();
		ResourceLoader.init();
		batcher = new SpriteBatch();

	}
	public void update(float deltaTime){
		if(manager.update() && Gdx.input.justTouched()){
			
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ResourceLoader.btnDisableVibration.isTouched(touchX, touchY) & Medium.isVibrationEnabled()){
				Medium.setVibrationEnabled(false);
				Medium.saveVibrationEnabledStatus();
			}else if(ResourceLoader.btnSilent.isTouched(touchX, touchY) & Medium.isSoundEnabled()){
				Medium.setSoundEnabled(false);
				Medium.saveSoundEnabledStatus();
			}else if(ResourceLoader.btnVibration.isTouched(touchX, touchY) & !Medium.isVibrationEnabled()){
				Medium.setVibrationEnabled(true);
				Medium.saveVibrationEnabledStatus();
			}else if(ResourceLoader.btnEnableSound.isTouched(touchX, touchY) & !Medium.isSoundEnabled()){
				Medium.setSoundEnabled(true);
				Medium.saveSoundEnabledStatus();
			}else{
				if(Screens.mainMenuScreen==null) Screens.mainMenuScreen = new MainMenuScreen(game);
				game.setScreen(Screens.mainMenuScreen);
			}
		}
	}
	public void draw(float deltaTime){
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // clears screen
		batcher.setProjectionMatrix(G304Main.camera.combined); //handle coordinate system
		//all the drawings must happen between batcher.begin() and batcher.end()
		batcher.begin();
		//----------------
		BackgroundRepeater.createRepeatBackground(batcher);		
		ResourceLoader.welcomeImage.drawImageSprite(batcher);
		LoadingProgressBar.drawProgress(batcher, manager.getProgress());
		if(manager.getProgress()>=1f){
			if(Medium.isSoundEnabled()){
				ResourceLoader.btnSilent.positionImageSpriteCenterCenter(40,150);
				ResourceLoader.btnSilent.drawImageSprite(batcher);
			}else{
				ResourceLoader.btnEnableSound.positionImageSpriteCenterCenter(40,150);
				ResourceLoader.btnEnableSound.drawImageSprite(batcher);
			}if(Medium.isVibrationEnabled()){
				ResourceLoader.btnDisableVibration.positionImageSpriteCenterCenter(-40,150);
				ResourceLoader.btnDisableVibration.drawImageSprite(batcher);
			}else{
				ResourceLoader.btnVibration.positionImageSpriteCenterCenter(-40,150);
				ResourceLoader.btnVibration.drawImageSprite(batcher);
			}
		}
		//----------------------
		batcher.end();
		
	}
	public void render(float delta) {
		update(delta);		
		draw(delta);				
	}

	public void show() {}

	public void hide() {}
	
	public void dispose () {
		batcher.dispose();
	}

	public void resize(int width, int height) {}

	public void pause() {}

	public void resume() {}
}

