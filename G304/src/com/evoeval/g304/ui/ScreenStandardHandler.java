package com.evoeval.g304.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.BackgroundRepeater;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Screens;

public class ScreenStandardHandler {
	
	private static boolean isDialogVisible=false;

	public static void initStandardScreen(SpriteBatch batcher){
		BackgroundRepeater.createRepeatBackground(batcher);	
		ResourceLoader.btnClose.positionImageSpriteCenterCenterR(+296f, -216f);
		ResourceLoader.btnClose.drawImageSprite(batcher);
	}
	public static void initStandardScreenNoExit(SpriteBatch batcher){
		BackgroundRepeater.createRepeatBackground(batcher);	
	}	
	private static void exitScreen(){
	//	Screens.dispose();
		ResourceLoader.dispose();
		AudioLoader.dispose();
		Gdx.app.exit();
		if(Gdx.app.getType()==ApplicationType.Android){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			System.exit(0);
		}
		
	}
	public static boolean exitButtonListner(float touchX,float touchY,Stage stage){
		if(ResourceLoader.btnClose.isTouched(touchX,touchY)){
			AudioLoader.play(AudioLoader.clickSound);
	     	Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
				protected void result (Object object) {
					AudioLoader.play(AudioLoader.clickSound);
					isDialogVisible=false;
					this.hide();
					if(object.equals(true)){	
						Medium.clear();
						exitScreen();
					}					
				}
    		};
    		error.text(ResourceLoader.locale.get("ExitLine1")).pack();
    		error.text(ResourceLoader.locale.get("ExitLine2")).pack();
    		error.button(ResourceLoader.locale.get("Yes"), true).button(ResourceLoader.locale.get("No"), false).key(Keys.ENTER, true)
    			.key(Keys.ESCAPE, false).pad(20f);		    	
	    	error.show(stage);
	    	isDialogVisible=true;	
			return true;
		}
		return false;
	}
	public static boolean backButtonListner(float touchX,float touchY,final Game game,boolean clearData,Stage stage){
		if(ResourceLoader.btnClose.isTouched(touchX,touchY)){
			AudioLoader.play(AudioLoader.clickSound);
			
			if(clearData){
	        	Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
	        		protected void result (Object object) {
	        			AudioLoader.play(AudioLoader.clickSound);
	        			isDialogVisible=false;
	        			this.hide();
	        			if(object.equals(true)){	        				
	        				Medium.clear();
	        				Screens.mainMenuScreen.init();
	        				game.setScreen(Screens.mainMenuScreen);
	        			}
	        			
	        		}
	    		};
	    		error.text(ResourceLoader.locale.get("ExitToMainLine1")).pack();
	    		error.text(ResourceLoader.locale.get("ExitToMainLine2")).pack();
	    		error.button(ResourceLoader.locale.get("Yes"), true).button(ResourceLoader.locale.get("No"), false).key(Keys.ENTER, true)
		    		.key(Keys.ESCAPE, false).pad(20f);		
		    	
		    	error.show(stage);
		    	isDialogVisible=true;
			}else{
				Screens.mainMenuScreen.init();
				game.setScreen(Screens.mainMenuScreen);
			}
			
			
			
			return true;
		}
		return false;
		
		
	}
	/**
	 * @return the isDialogVisible
	 */
	public static boolean isDialogVisible() {
		return isDialogVisible;
	}
	/**
	 * @param isDialogVisible the isDialogVisible
	 */
	public static void setDialogVisible(boolean isDialogVisible) {
		ScreenStandardHandler.isDialogVisible = isDialogVisible;
	}
}
