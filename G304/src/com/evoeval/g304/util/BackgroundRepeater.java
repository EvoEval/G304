package com.evoeval.g304.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evoeval.g304.ui.ImageAssetHandler;

/**
 * 
 * @author Yohan
 *
 * create a repeated background
 */
public class BackgroundRepeater {
	
	public static ImageAssetHandler backgroundImage=null;
	public static void createRepeatBackground(SpriteBatch batcherx){
		if(backgroundImage==null){
			backgroundImage = new ImageAssetHandler(Medium.getUIpath()+"/UI/background.png", 46f, 23f,0,0,true);
		}
		for(int x=0;x<ScreenRatioCalculator.screenWidth;x+=backgroundImage.getW())
		{
			for(int y=0;y<ScreenRatioCalculator.screenHeight;y+=backgroundImage.getH())
			{
				backgroundImage.positionImageSprite(x, y);
				backgroundImage.drawImageSprite(batcherx);
			}
		}
	}

}
