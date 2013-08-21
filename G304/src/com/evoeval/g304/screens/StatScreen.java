package com.evoeval.g304.screens;

import java.util.Vector;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Service;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.DrawText;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * This is the stat screen 
 * 
 * @author Bhathiya Perera
 *
 */
public class StatScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	Thread statThread;
	Vector<Vector<String>> statList;
	/**
	 * this will initialize the position buttons and the list
	 * @param game Game
	 */
	public StatScreen (Game game) {
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
		
		statThread = new Thread("Connect") {
			public void run () {
				try {
					statList = G304Service.getStatInfo();
				} catch (Exception ex) {

				}
			}
		};
		
		statThread.start();
		try{
			statThread.join();
		}catch(Exception ex){
			
		}
				
		if(statList != null){
			Table profileInfoTable=new Table();	
			
			LabelStyle gothicSmallRed = new LabelStyle(DrawText.getFontSmall(),Color.RED);
			LabelStyle gothicSmallGold = new LabelStyle(DrawText.getFontSmall(),Color.WHITE);	
			
			profileInfoTable.add(new Label(ResourceLoader.locale.get("ID"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);
			profileInfoTable.add(new Label(ResourceLoader.locale.get("BidStatus"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);			
			profileInfoTable.add(new Label(ResourceLoader.locale.get("GameStatus"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);
			profileInfoTable.add(new Label(ResourceLoader.locale.get("Score"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);
			profileInfoTable.add(new Label(ResourceLoader.locale.get("WiningScore"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);
			profileInfoTable.add(new Label(ResourceLoader.locale.get("ExpPoints"), gothicSmallRed)).center().spaceLeft(20f).spaceRight(20f);
			profileInfoTable.pack();
						
			for(Vector<String> statDetail : statList){
				for(String stat: statDetail){
					Label lblName = new Label(stat, gothicSmallGold);
					profileInfoTable.add(lblName).left().spaceLeft(10f).spaceRight(10f);				
				}
				profileInfoTable.pack();
			}
			ScreenRatioCalculator.moveActorCenterCenter(profileInfoTable, 0f, 0f); //position scroll pane+list
				
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
