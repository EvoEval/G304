package com.evoeval.g304.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This calculates the screen dimensions 
 * @author EvoEval
 *
 */
public class ScreenRatioCalculator {
	public static float screenWidth; /** Screen width to be used for the camera */
	public static float screenHeight; /** Screen height to be used for the camera */
	
	/**
	 * This calculates the screen dimensions based on the device screen width and height
	 * the calculated screen dimensions are used to set up the camera
	 * @param realW width of the real screen or desktop window
	 * @param realH height of the real screen or the desktop window
	 */
	public static void calculateScreenDimensions(){
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight =Gdx.graphics.getHeight();	
		
	}
	
	/**
	 * This maps the given coordinate to screen coordinate 
	 * make sure to call calculateScreenDimensions(w,h) before calling this method
	 * @param x specify the x coordinate
	 * @return calculated x
	 */
	public static float mapX(float x){
		return -screenWidth/2 +x;
	}
	/**
	 * This maps the given coordinate to correct screen coordinate 
	 * make sure to call calculateScreenDimensions(w,h) before calling this method
	 * @param y specify the y coordinate
	 * @param height specify the height of the image
	 * @return calculated y 
	 */
	public static float mapY(float y,float height){
		return screenHeight/2 -height - y;
	}	
	/**
	 * Unmap the given coordinate(opposite of map) 
	 * @param x already mapped x
	 * @return unmapped x
	 */
	public static float unmapX(float x){
		return screenWidth/2 +x;
	}
	/**
	 * Unmap the given coordinate(opposite of map) 
	 * @param y already mapped y
	 * @param height height
	 * @return unmapped y
	 */
	public static float unmapY(float y,float height){
		return screenHeight/2 -height - y;
	}	
	/**
	 * Move actor(ui element) according to center of screen and the center of the object
	 * @param act Actor
	 * @param x x coordinate from center
	 * @param y y coordinate from center
	 */
	public static void moveActorCenterCenter(Actor act,float x,float y){
		act.setX(x - act.getWidth()/2 + screenWidth/2) ;
		act.setY(y - act.getHeight()/2 + screenHeight/2);
	}
	public static void moveActorCenterCenterX(Actor act,float x,float y){
		act.setX(x - act.getWidth()/2 + screenWidth/2) ;
		act.setY(-y - act.getHeight()/2 + screenHeight/2);
	}		
	
}
