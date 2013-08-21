package com.evoeval.g304.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.evoeval.g304.util.AssetLoader;
import com.evoeval.g304.util.ScreenRatioCalculator;



/**
 * A place to hold Sprite and simplify loading them</br>
 * this also handles the asset loader and AssetErrorListener
 * @author Bhathiya Perera 
 * @version 2013.7.10;
 */
public class ImageAssetHandler implements AssetErrorListener{
	private Sprite sprite;
	private Texture texture;
	private TextureRegion region;
	private float imageWidth,imageHeight,imagePosX,imagePosY;	
	private String imagePath;
	private boolean justQueued; /* true if its just queued */
	private boolean normalDispose;

	/**
	 * Creates a new ImageAssetHandler Object (using or without using assets manager)
     * @param pathOfImage Path of the image relative to assets folder
     * @param width Image portion width
     * @param height Image portion height
     * @param x x-axis position of the sprite
     * @param y y-axis position of the sprite
     * @param loadNow if false assetLoader is used to load the image;if true load statically
	 */
	public ImageAssetHandler(String pathOfImage,float width,float height,float x,float y,boolean loadNow)
	{
		imageHeight=height;
		imageWidth=width;
		imagePath = pathOfImage;
		imagePosX=x; 
		imagePosY=y;
		if(loadNow){
			texture = new Texture(Gdx.files.internal(imagePath));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);			
			region = new TextureRegion(texture, 0, 0, (int)width, (int)height);			
			sprite = new Sprite(region);
			positionImageSprite(0,0); // suitable for background and welcome image
			normalDispose=true;
			justQueued=false;
		}else{
			normalDispose=false;
			justQueued= true;
			AssetLoader.getAssetManager().load(imagePath, Texture.class);
		}
	}
	
	public void resetSprite(){
		setRotationImageSprite(0f);
		sprite.setScale(1f);
	}
	public void minianimation(){
		if(isAssetLoaded())
			sprite.scale(+0.008f);
	}
	/**
	 * This will continue the process of sprite creation
	 * this will check if the image is loaded if so the whole ImageSprite become initialized
	 * @return true if ImageSprite is completely initialized (same as !justQueued)
	 */
	public boolean isAssetLoaded(){
		if(justQueued && AssetLoader.getAssetManager().isLoaded(imagePath)){
			texture = AssetLoader.getAssetManager().get(imagePath, Texture.class);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			region = new TextureRegion(texture, 0, 0, (int)imageWidth, (int)imageHeight);
			
			sprite = new Sprite(region);
			sprite.setPosition(ScreenRatioCalculator.mapX(imagePosX),ScreenRatioCalculator.mapY(imagePosY, imageHeight));
			justQueued=false;
			
		}
		return !justQueued;
	}
	/**
	 * check of AssetManager's Loading is complete;
	 * @return
	 */
	public static boolean isLoadingComplete(){
		return AssetLoader.getAssetManager().update();
	}
	/**
	 * Get Width
	 * @return Width
	 */
	public float getW(){
		return imageWidth;
	}
	/**
	 * get Height
	 * @return Height
	 */
	public float getH(){
		return imageHeight;
	}
	/**
	 * This changes the position of the sprite by adding the given coordinates to the current position
	 * @param x number of points the sprite must move in the direction of +x
	 * @param y number of points the sprite must move in the direction of +y
	 */
	public void moveImageSprite(float x,float y){
		if(isAssetLoaded()){
			sprite.setX(sprite.getX()+x);
			sprite.setY(sprite.getY()+y);
		}
	}
	/**
	 * Change the position of this ImageSprite
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void positionImageSprite(float x,float y){
		if(isAssetLoaded())
			sprite.setPosition(ScreenRatioCalculator.mapX(x),ScreenRatioCalculator.mapY(y, imageHeight));

	}
	/**
	 * Change the position of the ImageSprite (unmanaged)
	 * @param x new x coordinate (unmanaged)
	 * @param y new y coordinate (unmanaged)
	 */
	public void positionImageSpriteDirect(float x,float y){
		
		if(isAssetLoaded()){
			sprite.setX(x);
			sprite.setY(y);
		}
	}	
	/**
	 * position this ImageSprite's center according to screen center
	 * according to given coordinates
 	 * @param x x from center
	 * @param y y from center
	 */
	public void positionImageSpriteCenterCenter(float x,float y){

		positionImageSprite(ScreenRatioCalculator.screenWidth/2-imageWidth/2+x,ScreenRatioCalculator.screenHeight/2-imageHeight/2+y);
	}
	/**
	 * position this ImageSprite's center according to screen center
	 * according to given coordinates
	 * @param x x from center of 480w
	 * @param y y from center of 640h
	 */
	public void positionImageSpriteCenterCenterR(float x,float y){
		float newX=0f;
		float newY=0f;
		
		if(x<0f){
			newX=x-(ScreenRatioCalculator.screenWidth-640f)/2;
		}else if(x==0f){
			newX=0f;
		}else{
			newX=x+(ScreenRatioCalculator.screenWidth-640f)/2;
		}
		
		if(y<0f){
			newY=y-(ScreenRatioCalculator.screenHeight-480f)/2;
		}else if(y==0f){
			newY=0f;
		}else{
			newY=y+(ScreenRatioCalculator.screenHeight-480f)/2;
		}
				
		positionImageSpriteCenterCenter(newX,newY);
	}
	/**
	 * Draw the sprite on specified x,y 
	 * @param batch the SpriteBatch
	 */
	
	public void drawImageSprite(SpriteBatch batch){
		if(isAssetLoaded())
			sprite.draw(batch); //draw if its loaded
	}
	/**
	 * This tests if the given coordinates are inside the rectangle of the this ImageSprite
	 * this can be used to verify touch 
	 * Note:this is based on the sprite position
	 * @param x touched x
	 * @param y touched y
	 * @return if touched its true or its false
	 */	
	public boolean isTouched(float x,float y){
		if(isAssetLoaded()){
			return ((x>=getUnmappedX())&&(x<getUnmappedX()+imageWidth)&&(y>=getUnmappedY())&&(y<getUnmappedY()+imageHeight));
		}else{
			return false;
		}
	} 
	/**
	 * this is used to set the special touch rectangles for half visible cards
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param w width for the visible portion
	 * @param h height for the visible portion 
	 * @return if touched its true or its false
	 */
	public boolean isTouchedSpecial(float x,float y,float w){

		return ((x>=getUnmappedX())&&(x<getUnmappedX()+w)&&(y>=getUnmappedY())&&(y<getUnmappedY()+imageHeight));
	} 	
	/**
	 * get the unmapped x of this ImageSprite
	 * @return
	 */
	public float getUnmappedX(){
		if(isAssetLoaded()){
			return ScreenRatioCalculator.unmapX(sprite.getX());
		}else{
			return 0.0f;
		}
	}
	/**
	 * get the unmapped y of this ImageSprite
	 * @return
	 */
	public float getUnmappedY(){
		if(isAssetLoaded()){
			return ScreenRatioCalculator.unmapY(sprite.getY(),imageHeight);
		}else{
			return 0.0f;
		}
	}
	/**
	 * get the unmapped y of this ImageSprite
	 * @return
	 */
	public float getUnmappedY(float height){
		if(isAssetLoaded()){
			return sprite.getY()-ScreenRatioCalculator.screenHeight/2+height/2;
		}else{
			return 0.0f;
		}
	}	
	/**
	 * set the rotation by adding the given rotation to current rotation
	 * @param deg additional rotation
	 */
	public void rotateImageSprite(float deg){
		if(isAssetLoaded()){
			sprite.rotate(deg);
		}
	}
	/**
	 * set the rotation of the ImageSprite
	 * @param deg degrees in radian's
	 */
	public void setRotationImageSprite(float deg){
		if(isAssetLoaded()){
			sprite.setRotation(deg);
		}
	}
	/**
	 * create a micro card so it can be put in table
	 */
	public void createMicroCard(){
		if(isAssetLoaded()){
			sprite.setScale(-0.6f);
		}
	}
	/**
	 * clean up
	 */
	public void dispose(){
		if(normalDispose){
			texture.dispose();
		}else{
			AssetLoader.getAssetManager().unload(imagePath);
		}
		
	}
	public Sprite getSprite(){
		return sprite;
	}
    //---------------------------------------------------------------------------------\
    //									Scene2D Image
    //---------------------------------------------------------------------------------\
	/**
	 * get an Image object
	 * @return Image Actor
	 */
	public Image createScene2DImage(){
		if(isAssetLoaded()){
			return new Image(region);	
		}
		return null;
	}
	/**
	 * get an Image object
	 * @return Image Actor
	 */
	public ActorImage createActor(EventListener listener){
		if(isAssetLoaded()){
			ActorImage actorImage =  new ActorImage(region);
			actorImage.addListener(listener);
			return actorImage;
		}
		return null;
	}	

	/**
	 * AssetManager Error
	 */
	@SuppressWarnings("rawtypes")
	public void error(String fileName,Class type, Throwable throwable) {
		System.out.println("Error:"+fileName);		
	}

}
