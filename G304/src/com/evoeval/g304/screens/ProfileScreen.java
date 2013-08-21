package com.evoeval.g304.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Service;
import com.evoeval.g304.multiplayer.G304Service.ProfileInfo;
import com.evoeval.g304.ui.ImageAssetHandler;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.DrawText;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;

public class ProfileScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	Thread profileThread;
	ProfileInfo profInfo;
	/**
	 * this will initialize the position buttons and the list
	 * @param game Game
	 */
	public ProfileScreen(Game game) {
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
		
		Table profileInfoTable=new Table();

		Table rankTable=new Table();
		
		int rate=0;	
		
		profileThread = new Thread("Connect") {
			public void run () {
				try {
					profInfo=G304Service.getProfileInfo();
				} catch (Exception ex) {
				}
			}
		};
		
		profileThread.start();
		try{
			profileThread.join();
		}
		catch(Exception ex){
			
		}
		if(profInfo != null){
			Label lblName = new Label(Medium.getName(), new LabelStyle(DrawText.getFontMedium(),Color.WHITE));
			profileInfoTable.add(lblName).colspan(3).spaceBottom(10f);
			profileInfoTable.pack();
			LabelStyle lableStyle = new LabelStyle(DrawText.getFontSmall(),Color.WHITE);
			Label lblNoOfGamesWon = new Label(ResourceLoader.locale.get("GamesWon"),lableStyle );
			profileInfoTable.add(lblNoOfGamesWon).spaceLeft(25f).spaceRight(25f);			
			Label lblNoOfBidWon = new Label(ResourceLoader.locale.get("BidWon"),lableStyle);
			profileInfoTable.add(lblNoOfBidWon).spaceLeft(25f).spaceRight(25f);				
			Label lblTotScore = new Label(ResourceLoader.locale.get("TotalScore"),lableStyle);
			profileInfoTable.add(lblTotScore).spaceLeft(25f).spaceRight(25f);		
			profileInfoTable.pack();
				
			ImageAssetHandler rankImg = ResourceLoader.rankGold;
			
			if(Integer.parseInt(profInfo.getTotGamesWon())<5){
				rankImg = ResourceLoader.rankIron;
			}else if(Integer.parseInt(profInfo.getTotGamesWon())<15){
				rankImg = ResourceLoader.rankBronz;
			}else if(Integer.parseInt(profInfo.getTotGamesWon())<50){
				rankImg = ResourceLoader.rankSilver;
			}else{
				rankImg = ResourceLoader.rankGold;
			}
			
			profileInfoTable.add(rankImg.createScene2DImage()).spaceLeft(25f).spaceRight(25f);	
			
			if(Integer.parseInt(profInfo.getTotBidWon())<5){
				rankImg = ResourceLoader.rankIron;
			}else if(Integer.parseInt(profInfo.getTotBidWon())<15){
				rankImg = ResourceLoader.rankBronz;
			}else if(Integer.parseInt(profInfo.getTotBidWon())<50){
				rankImg = ResourceLoader.rankSilver;
			}else{
				rankImg = ResourceLoader.rankGold;
			}
			
			profileInfoTable.add(rankImg.createScene2DImage()).spaceLeft(25f).spaceRight(25f);	
			
			if(Integer.parseInt(profInfo.getTotScore())<1000){
				rankImg = ResourceLoader.rankIron;
			}else if(Integer.parseInt(profInfo.getTotScore())<5000){
				rankImg = ResourceLoader.rankBronz;
			}else if(Integer.parseInt(profInfo.getTotScore())<10000){
				rankImg = ResourceLoader.rankSilver;
			}else{
				rankImg = ResourceLoader.rankGold;
			}
			
			profileInfoTable.add(rankImg.createScene2DImage()).spaceLeft(25f).spaceRight(25f);		
			profileInfoTable.pack();
						
			Label lblNoOfGamesWon1 = new Label(profInfo.getTotGamesWon(), lableStyle);
			profileInfoTable.add(lblNoOfGamesWon1).spaceLeft(25f).spaceRight(25f);		
			Label lblNoOfBidWon1 = new Label(profInfo.getTotBidWon(), lableStyle);
			profileInfoTable.add(lblNoOfBidWon1).spaceLeft(25f).spaceRight(25f);		
			Label lblTotScore1 = new Label(profInfo.getTotScore(), lableStyle);
			profileInfoTable.add(lblTotScore1).spaceLeft(25f).spaceRight(25f);
			profileInfoTable.pack();
			
			Label lblNoOfGamesPlayed = new Label(ResourceLoader.locale.get("GamesPlayed")+" : "+profInfo.getTotGamesPlayed(), lableStyle);
			profileInfoTable.add(lblNoOfGamesPlayed).colspan(3);
			profileInfoTable.pack();
	
			Label lblHighestBid = new Label(ResourceLoader.locale.get("HighestBid") + " : "+profInfo.getHighestBid(), lableStyle);
			profileInfoTable.add(lblHighestBid).colspan(3);
			profileInfoTable.pack();
						
			Label lblHighestScore = new Label(ResourceLoader.locale.get("HighestScore")+" : "+profInfo.getHighScore(), lableStyle);
			profileInfoTable.add(lblHighestScore).colspan(3);
			profileInfoTable.pack();
	
			Label lblAvgScore = new Label(ResourceLoader.locale.get("AverageScore")+" : "+profInfo.getAvgScore(), lableStyle);
			profileInfoTable.add(lblAvgScore).colspan(3);
			profileInfoTable.pack();
			
			Label lblwinPer = new Label(ResourceLoader.locale.get("WinPercentage")+" : "+profInfo.getWinPer(), lableStyle);
			profileInfoTable.add(lblwinPer).colspan(3);
			profileInfoTable.pack();
			
			Label lbltotExpPoints = new Label(ResourceLoader.locale.get("TotExpPoints")+" : "+profInfo.getTotExpPoints(), lableStyle);
			profileInfoTable.add(lbltotExpPoints).colspan(3);
			profileInfoTable.pack();
			
			rate=Integer.parseInt(profInfo.getRank());
			
			Label rank = new Label(ResourceLoader.locale.get("Rank"), lableStyle);
			profileInfoTable.add(rank).colspan(3);
			profileInfoTable.pack();
			
			ImageAssetHandler rankStar = ResourceLoader.ratingStarGold;
			
			if(rate<4){
				rankStar = ResourceLoader.ratingStarBronze;
			}else if(rate<7){
				rankStar = ResourceLoader.ratingStarSilver;
			}
			
			
			for(int i=0;i<rate;i++){
				rankTable.add(rankStar.createScene2DImage()).width(20f);
			}
			for(int i=0;i<10-rate;i++){
				rankTable.add(ResourceLoader.ratingStarSlot.createScene2DImage()).width(20f);
			}		
			rankTable.pack();
						
			profileInfoTable.add(rankTable).colspan(3);
			profileInfoTable.pack();
		
			ScreenRatioCalculator.moveActorCenterCenter(profileInfoTable, 0f, 0f);
		
			stage.addActor(profileInfoTable);
		}else{
			Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
				protected void result (Object object) {
					AudioLoader.play(AudioLoader.clickSound);
					this.hide();
					if(object.equals(true)){	        				
						Screens.mainMenuScreen.init();
        				game.setScreen(Screens.mainMenuScreen);
					}					
				}
    		};
    		error.text(ResourceLoader.locale.get("NoInternetLine1")).pack();
    		error.text(ResourceLoader.locale.get("NoInternetLine2")).pack();
    		error.button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true).pad(20f);		    	
	    	error.show(stage);
		}
	}
	/**
	 * 
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if(Gdx.input.justTouched()){
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,false,null)) return;

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
