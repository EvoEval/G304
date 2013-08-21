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
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.ui.ImageAssetHandler;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;

public class BiddingScreen implements Screen{

	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	ImageAssetHandler trumpCard;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	
	public BiddingScreen (Game game) {
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
		
		trumpCard=CardsHandler.getCardImageSprite(Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));

		ResourceLoader.bid60Button.positionImageSpriteCenterCenter(1f, -173f);		
		ResourceLoader.bid70Button.positionImageSpriteCenterCenter(-150f, -150f);
		ResourceLoader.bid80Button.positionImageSpriteCenterCenter(-150f, -20f);
		ResourceLoader.bid90Button.positionImageSpriteCenterCenter(-150f, 110f);		
		ResourceLoader.bid100Button.positionImageSpriteCenterCenter(150f, -150f);
		ResourceLoader.bid110Button.positionImageSpriteCenterCenter(150f, -20f);
		ResourceLoader.bid120Button.positionImageSpriteCenterCenter(150f, 110f);		
		ResourceLoader.bid130Button.positionImageSpriteCenterCenter(0f, 134f);
	}
	public void update (float deltaTime) {
		
		if (Gdx.input.justTouched()) {
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;			
			if(ResourceLoader.bid60Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("60", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid70Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("70", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid80Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("80", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid90Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("90", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid100Button.isTouched(touchX,touchY)){		
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("100", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid110Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("110", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid120Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("120", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.bid130Button.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				Medium.createBid("130", Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
				Medium.setBidPlaced(true);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
			}else if(ResourceLoader.handButton.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(game);
				Screens.cardsScreen.init(true);
				game.setScreen(Screens.cardsScreen);
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
		ResourceLoader.handButton.positionImageSpriteCenterCenterR(-241.0f,216.0f);
		ResourceLoader.handButton.drawImageSprite(batcher);
		
		trumpCard.positionImageSpriteCenterCenter(0f, -20f);
		trumpCard.drawImageSprite(batcher);
		
		ResourceLoader.bid60Button.drawImageSprite(batcher);		
		ResourceLoader.bid70Button.drawImageSprite(batcher);
		ResourceLoader.bid80Button.drawImageSprite(batcher);
		ResourceLoader.bid90Button.drawImageSprite(batcher);
		ResourceLoader.bid100Button.drawImageSprite(batcher);
		ResourceLoader.bid110Button.drawImageSprite(batcher);
		ResourceLoader.bid120Button.drawImageSprite(batcher);
		ResourceLoader.bid130Button.drawImageSprite(batcher);
		
		batcher.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();		
	}
	
	@Override
	public void render (float delta) {
		
		trumpCard=CardsHandler.getCardImageSprite(Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
		draw(delta);
		update(delta);
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
