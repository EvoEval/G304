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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Service;
import com.evoeval.g304.multiplayer.G304NetworkHandler.G304Score;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * This is the game over screen 
 * 
 * @author Bhathiya Perera
 *
 */
public class GameOverScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	
	G304Score score;
	int myScore;
	boolean isWon=false;

	public GameOverScreen (Game game,G304Score object) {
		
		score = object;		
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
		isWon = ((score.isTeamAWon && ( Medium.getId().equals("1") || Medium.getId().equals("3")))
				|| (!score.isTeamAWon && ( Medium.getId().equals("2") || Medium.getId().equals("4"))));
		
		if(isWon){
			AudioLoader.play(AudioLoader.winSound);
		}else{
			AudioLoader.play(AudioLoader.loseSound);
		}
		
	}
	public void update (float deltaTime) {
		if(Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			
			if(Medium.isLoggedIn() && Medium.getGameMode().equals(Medium.GAME_MODE_MULTIPLAYER)){	
				if(Integer.parseInt(Medium.getId()) == 1){
					myScore = score.p1_score;
				}else if(Integer.parseInt(Medium.getId()) == 2){
					myScore = score.p2_score;
				}else if(Integer.parseInt(Medium.getId()) == 3){
					myScore = score.p3_score;
				}else if(Integer.parseInt(Medium.getId()) == 4){
					myScore = score.p4_score;
				}
				G304Service.fillGameInfo((score.bid_winner.equals(Medium.getId())) ? "Won":"Lose",
						Integer.toString(score.bid_amount),Integer.toString(myScore),
						(isWon) ? "Won":"Lose",
						Integer.toString(score.win_score));
			}
			Medium.clear();
			Screens.mainMenuScreen.init();
			game.setScreen(Screens.mainMenuScreen);
			return;

		}
		
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		Table tblGameOvr=new Table();	
		stage.clear();
		if(isWon){
			tblGameOvr.add(ResourceLoader.gameOverVictory.createScene2DImage());
			tblGameOvr.pack();
		}else{
			tblGameOvr.add(ResourceLoader.gameOverDefeat.createScene2DImage());
			tblGameOvr.pack();
		}
				
		String [] scoreArr= score.generateListData();		
		for(int i=0;i<scoreArr.length;i++){
			Label lbl = new Label(scoreArr[i], ResourceLoader.skin);
			tblGameOvr.add(lbl);
			tblGameOvr.pack();
		}	
		
		ScreenRatioCalculator.moveActorCenterCenter(tblGameOvr, 0f, 0f);
		stage.addActor(tblGameOvr);
		batcher.begin();
		ScreenStandardHandler.initStandardScreenNoExit(batcher);

		batcher.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
	}

	@Override
	public void render (float delta) {
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
