package com.evoeval.g304.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.ui.ImageAssetHandler;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.DrawText;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.Screens;


/**
 * This is the screen that handle shuffling
 * @author EvoEval
 *
 */
public class ShuffleScreen implements Screen {

	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	ImageAssetHandler backCard;
	Rectangle nextBounds;
	boolean animateFakeShuffle=false;
	float shuffleTimeRemining;
	final static float card_deck_len = (23.0f*10.0f)+(144.0f*0.75f);
	int k=5;
	
	/**
	 * 
	 * @param game Game
	 */
	public ShuffleScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();
		backCard=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);		
		init();
	}
	
	public void init(){
		shuffleTimeRemining=5.0f;
	}
	/**
	 * check if the user touched the screen 
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		shuffleTimeRemining-=deltaTime;
		if(shuffleTimeRemining<=0f){
			//send cards to server
			Medium.sendCardsToServer(CardsHandler.cardDeck);
			if(Medium.getGameMode().equals(Medium.GAME_MODE_MULTIPLAYER)){
				if(Screens.connectionWaitScreen==null) 
					Screens.connectionWaitScreen = new ConnectionWaitScreen(game);
				else
					Screens.connectionWaitScreen.init();
				game.setScreen(Screens.connectionWaitScreen);		
			}else{
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(Medium.getGameObj());
				Screens.cardsScreen.init(true);
				Medium.getGameObj().setScreen(Screens.cardsScreen);
			}
		}

		if (Gdx.input.justTouched() && !animateFakeShuffle) {
			AudioLoader.play(AudioLoader.shuffleSound);
			animateFakeShuffle=true;
			AudioLoader.vibrate();
			CardsHandler.shuffle();
		}
	}
	/**
	 * draw the graphics 
	 * warning : equal to a loop
	 * @param deltaTime -passed by libgdx
	 */
	public void draw (float deltaTime) {
		//todo : nice animation
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.begin();		
		ScreenStandardHandler.initStandardScreenNoExit(batcher);
		if(animateFakeShuffle){
			float x=-((card_deck_len/2)-backCard.getW()/2);
			for(int i=0;i<24;i++){
				backCard.positionImageSpriteCenterCenter(x, 0);
				backCard.drawImageSprite(batcher);
				x+=k;
			}
			if(!Gdx.input.isTouched()){
				k++;
			}
			if(k>20){
				animateFakeShuffle=false;
				k=5;
			}
		}else{
			
			float x=-((card_deck_len/2)-backCard.getW()/2);
			for(int i=0;i<24;i++)
			{
				backCard.positionImageSpriteCenterCenter(x, 0);
				backCard.drawImageSprite(batcher);
				x+=10;
			}
		}
		DrawText.drawFontLarge(batcher, ""+(int)shuffleTimeRemining, 0f, 200f);
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
