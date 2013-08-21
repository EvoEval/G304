package com.evoeval.g304.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * This class is used for printing text on the game screen
 * @author Bhathiya
 *
 */
public class DrawText {
	private static BitmapFont fontTitle;	
	private static BitmapFont fontSmall;
	private static BitmapFont FontMedium;
	private static BitmapFont fontLarge;	
	/**
	 * must be called in order to load the font resources
	 */
	public static void init(){
		fontTitle = new BitmapFont(Gdx.files.internal("data/fonts/FontT.fnt"), Gdx.files.internal("data/fonts/FontT.png"), false);
		fontSmall = new BitmapFont(Gdx.files.internal("data/fonts/FontS.fnt"), Gdx.files.internal("data/fonts/FontS.png"), false);
		FontMedium = new BitmapFont(Gdx.files.internal("data/fonts/FontM.fnt"), Gdx.files.internal("data/fonts/FontM.png"), false);
		fontLarge = new BitmapFont(Gdx.files.internal("data/fonts/FontL.fnt"), Gdx.files.internal("data/fonts/FontL.png"), false);	
	}	
	
	public static void drawFontLarge(SpriteBatch batcher,String text,float x,float y){
		float w = fontLarge.getBounds(text).width;
		float h = fontLarge.getBounds(text).height;
		
		fontLarge.draw(batcher, text, x-w/2, y-h/2);
	}

	public static BitmapFont getFontTitle() {
		return fontTitle;
	}

	public static BitmapFont getFontLarge() {
		return fontLarge;
	}

	public static BitmapFont getFontSmall() {		
		return fontSmall;
	}
	
	public static BitmapFont getFontMedium() {
		return FontMedium;
	}
	
	public static void dispose(){
		fontTitle.dispose();	
		fontSmall.dispose(); 
		FontMedium.dispose();
		fontLarge.dispose();	   
	}

}
