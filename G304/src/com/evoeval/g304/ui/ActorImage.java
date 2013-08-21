package com.evoeval.g304.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ActorImage extends Image{
	
	private final static float screenWidth = Gdx.graphics.getWidth();
	private final static float screenHeight = Gdx.graphics.getHeight();

	
	public ActorImage(TextureRegion region){
		super(region);
	}
	
	public static void positionActorCenterCenter(Actor act,float x,float y){
		act.setX(x - act.getWidth()/2 + screenWidth/2) ;
		act.setY(-y - act.getHeight()/2 + screenHeight/2);
	}	 
	
	public void positionCenterCenter(float x,float y){
		setX(x - getWidth()/2 + screenWidth/2) ;
		setY(-y - getHeight()/2 + screenHeight/2);
	}	
	

}
