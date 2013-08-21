package com.evoeval.g304.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * 
 * @author Yohan
 *
 *
 */
public class LoadingProgressBar {
	private static Sprite [] progressPortions;
	private static Texture texture;
	public static void initLoadingProgressBar(){
		texture = new Texture(Gdx.files.internal(Medium.getUIpath()+"/UI/welcome.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		progressPortions = new Sprite[447];
		float x,y;
		x = ScreenRatioCalculator.screenWidth/2-452/2;
		y = ScreenRatioCalculator.screenHeight/2-150;
		for(int i=0;i<447;i++){
			TextureRegion region = new TextureRegion(texture, i, 0, 1, 164);
			progressPortions[i] = new Sprite(region);
			positionImageSprite(progressPortions[i],x++,y,164f);
		}
 // suitable for background and welcome image
	}
	private static void positionImageSprite(Sprite sprite,float x,float y,float height){
		sprite.setPosition(ScreenRatioCalculator.mapX(x),ScreenRatioCalculator.mapY(y, height));

	}
	public static void drawProgress(SpriteBatch batcher,float progress){
		int upto;
		upto = (int)((progress*447)-1);
		for(int i=0;i<upto;i++){
			progressPortions[i].draw(batcher);
		}
	}
}
