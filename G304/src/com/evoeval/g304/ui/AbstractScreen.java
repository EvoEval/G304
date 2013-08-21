package com.evoeval.g304.ui;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;

/**
 * Abstract class for Screens to simplify Screens' code
 * @author Bhathiya Perera
 */
public abstract class AbstractScreen implements Screen {
		
	protected float touchX=0f,touchY=0f;	
	protected Stage stage=null;
	
	protected final SpriteBatch batcher = new SpriteBatch();
	protected final TweenManager tweenManager = new TweenManager();
	protected final OrthographicCamera guiCam = G304Main.camera;
	protected final Game game = Medium.getGameObj();	
		
	/**
	 * must call create stage</br>
	 * 
	 * <pre>
	 * init(){
	 * 	createStage();
	 * 	//do something
	 * }
	 * </pre>
	 */
	public abstract void init(); 
	
	/**
	 * handle back button
	 * @return
	 */
	protected abstract boolean handleBackButton();
	
	/**
	 * default dispose command
	 */
	public void dispose(){
		batcher.dispose();
	}
	
	/**
	 * create stage object if its null
	 */
	protected void createStage(){
		if(stage==null){
			stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);		
		}else{
			stage.clear();
		}
	}
	

	/**
	 * this functions calculates the suitable values for touchX,touchY
	 * and call handleBackButton()
	 * @return                                     
	 */
	protected boolean defaultTouchEvents(){
		if (Gdx.input.justTouched()) {
			if(ScreenStandardHandler.isDialogVisible()) return false;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(handleBackButton()) return false;
			return true;
		}
		return false;
	}
	
	/**
	 * clear screen and batcher.begin()
	 * Note: for usage on draw()
	 * @param deltaTime
	 */
	protected void preDraw(float deltaTime){
		tweenManager.update(deltaTime);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);		
		batcher.begin();
	}
	
	/**
	 * batcher end and stage.draw()</br>
	 * Note: for usage on draw()
	 * @param deltaTime
	 */
	protected void postDraw(float deltaTime){
		batcher.end();
		stage.act(deltaTime);		
		stage.draw();
	}
	/**
	 * default update
	 * <pre>
	 *  if(defaultTouchEvents()){
	 *  	//handle other touch events
	 *  }
	 * </pre>
	 * @param deltaTime automatically passed by libgdx
	 */
	public void update(float deltaTime){
		defaultTouchEvents();
	}
	
	/**
	 * default draw
	 * <pre>
	 *  preDraw(deltaTime);                               
	 *  ScreenStandardHandler.initStandardScreen(batcher);
	 *  //handle other drawing
	 *  postDraw(deltaTime);                              
	 * </pre>
	 * @param deltaTime automatically passed by libgdx
	 */
	public void draw(float deltaTime){

		preDraw(deltaTime);
		ScreenStandardHandler.initStandardScreen(batcher);
		postDraw(deltaTime);

	}	
	
	/**
	 * default render</br>
	 * <pre>
	 * update(deltatime);
	 * draw(deltatime);
	 * </pre>
	 */
	public void render(float deltatime){
		update(deltatime);
		draw(deltatime);
	}
    //---------------------------------------------------------------------------------\
    //									Screen
    //---------------------------------------------------------------------------------\
	public void resize(int w, int h){}
	public void resume() {}
	public void show() {}
	
	/**
	 * default hide</br>
	 * prevents input listeners from working in other screens
	 * <pre>
	 *  stage.dispose();
	 *  stage = null;   
	 * </pre>
	 */
	public void hide() {		
		stage.dispose();
		stage = null;
	}
	
	public void pause() {}
}
