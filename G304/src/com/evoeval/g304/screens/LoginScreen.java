package com.evoeval.g304.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.multiplayer.G304Service;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * This is the client request screen 
 * once the ip of the server is given client can connect
 * 
 * @author EvoEval
 *
 */
public class LoginScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	TextField txtEmail,txtPass;
	String nextScreen;
	String status;
	Thread loginThread;
	Label lblSkip,lblAbout,space;

	/**
	 * this will initialize the position of client and server buttons
	 * @param game Game
	 */
	public LoginScreen (Game game,String nextScreen) {
		
		this.nextScreen = nextScreen;
		
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();			
		ResourceLoader.loginButton.positionImageSpriteCenterCenterR(220f, 180f);
		
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
		
		lblSkip = new Label(ResourceLoader.locale.get("Skip"), ResourceLoader.skin);
		lblAbout = new Label(ResourceLoader.locale.get("About"), ResourceLoader.skin);
		space = new Label("|", ResourceLoader.skin);
		
		stage.addActor(lblSkip);
		stage.addActor(lblAbout);
		stage.addActor(space);

		
		ScreenRatioCalculator.moveActorCenterCenter(lblSkip, 45.0f, -50.0f);
		ScreenRatioCalculator.moveActorCenterCenter(lblAbout, -45.0f, -50.0f);
		ScreenRatioCalculator.moveActorCenterCenter(space, 0.0f, -50.0f);
		
		lblSkip.addListener(new InputListener() {			
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        	AudioLoader.play(AudioLoader.clickSound);
	        	loadNextScreen();
	            return true;
	        }
		});
		
		lblAbout.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        	AudioLoader.play(AudioLoader.clickSound);
	        	Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
    				protected void result (Object object) {
    					AudioLoader.play(AudioLoader.clickSound);
    					this.hide();
    					if(object.equals(true)){	        				
    						txtEmail.setText("");
    						txtPass.setText("");
    					}					
    				}
        		};
        		error.text(ResourceLoader.locale.get("RegisterLine1")).pack();
        		error.text(ResourceLoader.locale.get("RegisterLine2")).pack();
        		error.button("Ok", true).key(Keys.ENTER, true).pad(20f);		    	
    	    	error.show(stage); 
	            return true;
	        }
		});
		
		txtEmail = new TextField("jadogg.coder@gmail.com",  ResourceLoader.skin);
		txtEmail.setWidth(200.0f);
		txtEmail.setMessageText("Username");
		txtPass = new TextField("123",  ResourceLoader.skin);
		txtPass.setWidth(200.0f);
		txtPass.setMessageText("Password");
		txtPass.setPasswordCharacter('*');
		txtPass.setPasswordMode(true);
		ScreenRatioCalculator.moveActorCenterCenter(txtPass, 0, 0);
		ScreenRatioCalculator.moveActorCenterCenter(txtEmail, 0, 50);
		
		stage.addActor(txtPass);
		stage.addActor(txtEmail);
		
		
		txtEmail.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField txtIP, char key) {

				if (key == '\n') txtIP.getOnscreenKeyboard().show(false);
			}
		});
		
		txtPass.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField txtIP, char key) {

				if (key == '\n') txtIP.getOnscreenKeyboard().show(false);
			}
		});
	}
	/**
	 * 
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if(Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;
			if(ResourceLoader.loginButton.isTouched(touchX,touchY)){
				AudioLoader.play(AudioLoader.clickSound);
				if(txtEmail.getText().isEmpty()){
					Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
        				protected void result (Object object) {
        					AudioLoader.play(AudioLoader.clickSound);
        					this.hide();
        					if(object.equals(true)){	        				
        						txtEmail.setText("");
        						txtPass.setText("");
        					}					
        				}
            		};
            		error.text(ResourceLoader.locale.get("EmailID")).button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true).pad(20f);		    	
        	    	error.show(stage); 
				}else if(txtPass.getText().isEmpty()){
					Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
        				protected void result (Object object) {
        					AudioLoader.play(AudioLoader.clickSound);
        					this.hide();
        					if(object.equals(true)){	        				
        						txtEmail.setText("");
        						txtPass.setText("");
        					}					
        				}
            		};
            		error.text(ResourceLoader.locale.get("Password")).button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true).pad(20f);		    	
        	    	error.show(stage); 
				}else{	
					loginThread = new Thread("Login") {
						public void run () {
							try {
								status=G304Service.userAuthenticate(txtEmail.getText(), txtPass.getText());
							} catch (Exception ex) {

							}
						}
					};
					
					loginThread.start();
					try{
						loginThread.join();
					}catch(Exception ex){
						
					}
					
                    if(status.equals(G304Service.STATE_AUTHENTICATED)){
                    	Medium.saveLogin();
                    	loadNextScreen();
                    }else if(status.equals(G304Service.STATE_UNAUTHENTICATED)){
                    	Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
            				protected void result (Object object) {
            					AudioLoader.play(AudioLoader.clickSound);
            					this.hide();
            					if(object.equals(true)){	        				
            						txtEmail.setText("");
            						txtPass.setText("");
            					}					
            				}
                		};
                		error.text(ResourceLoader.locale.get("InvalidLogin")).button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true).pad(20f);		    	
            	    	error.show(stage);                    		
                    }else{
                    	Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
            				protected void result (Object object) {
            					AudioLoader.play(AudioLoader.clickSound);
            					this.hide();
            					if(object.equals(true)){	        				
            						txtEmail.setText("");
            						txtPass.setText("");
            					}					
            				}
                		};
                		error.text(ResourceLoader.locale.get("NoInternetLine1")).pack();
                		error.text(ResourceLoader.locale.get("NoInternetLine2")).pack();
                		error.button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true).pad(20f);		    	
            	    	error.show(stage); 
                    }
				}
			}
		}
		
	}
	private void loadNextScreen(){
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		
		if(nextScreen.equals("ChooseClientServerScreen")){
            if(Screens.chooseClientServerScreen==null) Screens.chooseClientServerScreen = new ChooseClientServerScreen(game);
			game.setScreen(Screens.chooseClientServerScreen);
    	}else if(nextScreen.equals("ProfileScreen")){
    		if(Screens.profileScreen==null){ 
    			Screens.profileScreen = new ProfileScreen(game);
    		}else{
    			Screens.profileScreen.init();
    		}
			game.setScreen(Screens.profileScreen);
    	}else if(nextScreen.equals("StatScreen")){
    		if(Screens.statScreen==null) {
    			Screens.statScreen = new StatScreen(game);
    		}else{
    			Screens.statScreen.init();
    		}
			game.setScreen(Screens.statScreen);	
    	}else if(nextScreen.equals("SettingsScreen")){
    		if(Screens.settingsScreen==null) {
    			Screens.settingsScreen = new SettingsScreen(game);
    		}else{
    			Screens.settingsScreen.init();
    		}
			game.setScreen(Screens.settingsScreen);	
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
		ResourceLoader.loginButton.drawImageSprite(batcher);
		
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
