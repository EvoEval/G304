package com.evoeval.g304;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.evoeval.g304.screens.*;
import com.evoeval.g304.ui.ActorAccessor;
import com.evoeval.g304.ui.ActorImage;
import com.evoeval.g304.ui.SpriteAccessor;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;

/**
 * This is the main class of the G304 game
 * @author EvoEval
 *
 */
public class G304Main extends Game{
	public static OrthographicCamera camera ; 

	/**
	 * create the game
	 * <ol>
	 * <li>calculate screen dimensions</li>
	 * <li>loads the graphics</li>
	 * <li>sets starting screen</li>
	 * </ol>
	 */
	public void create () {		
		Gdx.input.setCatchBackKey(true);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(ActorImage.class, new ActorAccessor());
		
		ScreenRatioCalculator.calculateScreenDimensions();		
		camera = new OrthographicCamera(ScreenRatioCalculator.screenWidth,ScreenRatioCalculator.screenHeight);	
		
		Medium.setGameObj(this);
		Medium.loadSavedDetails();
		
		if(Screens.loadingScreen==null) Screens.loadingScreen = new LoadingScreen(this);
		setScreen(Screens.loadingScreen);
		
	}

	public void pause(){
		super.pause();
	}
	public void dispose () {
		super.dispose();
	}
	public void killme(){
		pause();
		dispose();
	}
}