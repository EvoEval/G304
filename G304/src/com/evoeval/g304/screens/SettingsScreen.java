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
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;
import com.evoeval.g304.util.SystemUpdater;


public class SettingsScreen implements Screen{
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	/**
	 * this will initialize the position of client and server buttons
	 * @param game Game
	 */
	public SettingsScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();
		
		
		
		ResourceLoader.btnEnableSound.positionImageSpriteCenterCenter(-111.0f, -74f);
		ResourceLoader.btnVibration.positionImageSpriteCenterCenter(-37.0f, -74f);
		ResourceLoader.btnSilent.positionImageSpriteCenterCenter(-111.0f, -74f);
		ResourceLoader.btnDisableVibration.positionImageSpriteCenterCenter(-37.0f, -74f);
		ResourceLoader.btnLoginEnabled.positionImageSpriteCenterCenter(37.0f, -74f);
		ResourceLoader.btnLoginDisabled.positionImageSpriteCenterCenter(37.0f, -74f);
		ResourceLoader.btnUpdate.positionImageSpriteCenterCenter(111.0f, -74f);
		ResourceLoader.btnLanguageEnglish.positionImageSpriteCenterCenter(-74.0f, 0f);
		ResourceLoader.btnLanguageEnglishDisabled.positionImageSpriteCenterCenter(-74.0f, 0f);
		ResourceLoader.btnLanguageSinhala.positionImageSpriteCenterCenter(74.0f, 0f);
		ResourceLoader.btnLanguageSinhalaDisabled.positionImageSpriteCenterCenter(74.0f, 0f);
		ResourceLoader.btnMetalTheme.positionImageSpriteCenterCenter(-74.0f, 74f);
		ResourceLoader.btnMetalThemeDisabled.positionImageSpriteCenterCenter(-74.0f, 74f);
		ResourceLoader.btnMetroTheme.positionImageSpriteCenterCenter(74.0f, 74f);
		ResourceLoader.btnMetroThemeDisabled.positionImageSpriteCenterCenter(74.0f, 74f);
	}
	
	public void init(){
		
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
			if(ResourceLoader.btnDisableVibration.isTouched(touchX, touchY) & Medium.isVibrationEnabled()){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setVibrationEnabled(false);
				Medium.saveVibrationEnabledStatus();
			}else if(ResourceLoader.btnSilent.isTouched(touchX, touchY) & Medium.isSoundEnabled()){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setSoundEnabled(false);
				Medium.saveSoundEnabledStatus();
			}else if(ResourceLoader.btnVibration.isTouched(touchX, touchY) & !Medium.isVibrationEnabled()){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setVibrationEnabled(true);
				Medium.saveVibrationEnabledStatus();
			}else if(ResourceLoader.btnEnableSound.isTouched(touchX, touchY) & !Medium.isSoundEnabled()){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setSoundEnabled(true);
				Medium.saveSoundEnabledStatus();
			}else if(ResourceLoader.btnLoginEnabled.isTouched(touchX, touchY) & Medium.isLoggedIn()){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setLoginStatus(false);
				Medium.removeLoginInfotmation();
			}else if(ResourceLoader.btnLoginDisabled.isTouched(touchX, touchY) & !Medium.isLoggedIn()){
				AudioLoader.play(AudioLoader.clickSound);	
				Screens.loginScreen = new LoginScreen(game,"SettingsScreen");
				game.setScreen(Screens.loginScreen);
			}else if(ResourceLoader.btnMetalThemeDisabled.isTouched(touchX, touchY) & Medium.getGameTheme().equals("Metro")){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setGameTheme("Metal");
				Medium.saveTheme();
			}else if(ResourceLoader.btnMetroThemeDisabled.isTouched(touchX, touchY) & Medium.getGameTheme().equals("Metal")){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setGameTheme("Metro");
				Medium.saveTheme();
			}else if(ResourceLoader.btnLanguageEnglishDisabled.isTouched(touchX, touchY) & Medium.getGameLanguage().equals("Sinhala")){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setGameLanguage("English");
				Medium.saveLanguage();
			}else if(ResourceLoader.btnLanguageSinhalaDisabled.isTouched(touchX, touchY) & Medium.getGameLanguage().equals("English")){
				AudioLoader.play(AudioLoader.clickSound);	
				Medium.setGameLanguage("Sinhala");
				Medium.saveLanguage();
			}else if(ResourceLoader.btnUpdate.isTouched(touchX, touchY)){
				AudioLoader.play(AudioLoader.clickSound);	
				new Thread(new Runnable() {					
					@Override
					public void run() {
						SystemUpdater.updateAI();						
					}
				}).start();				
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
		if(Medium.isSoundEnabled()){
			ResourceLoader.btnSilent.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnEnableSound.drawImageSprite(batcher);
		}if(Medium.isVibrationEnabled()){
			ResourceLoader.btnDisableVibration.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnVibration.drawImageSprite(batcher);
		}if(Medium.isLoggedIn()){
			ResourceLoader.btnLoginEnabled.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnLoginDisabled.drawImageSprite(batcher);
		}if(Medium.getGameLanguage().equals("English")){
			ResourceLoader.btnLanguageEnglish.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnLanguageEnglishDisabled.drawImageSprite(batcher);
		}if(Medium.getGameTheme().equals("Metro")){
			ResourceLoader.btnMetroTheme.drawImageSprite(batcher);
			ResourceLoader.btnMetalThemeDisabled.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnMetalTheme.drawImageSprite(batcher);
			ResourceLoader.btnMetroThemeDisabled.drawImageSprite(batcher);
		}if(Medium.getGameLanguage().equals("English")){
			ResourceLoader.btnLanguageEnglish.drawImageSprite(batcher);
			ResourceLoader.btnLanguageSinhalaDisabled.drawImageSprite(batcher);
		}else{
			ResourceLoader.btnLanguageSinhala.drawImageSprite(batcher);
			ResourceLoader.btnLanguageEnglishDisabled.drawImageSprite(batcher);			
		}
		ResourceLoader.btnUpdate.drawImageSprite(batcher);
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
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		batcher.dispose();
	}
}
