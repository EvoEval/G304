package com.evoeval.g304.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.evoeval.g304.ui.AbstractScreen;
import com.evoeval.g304.ui.ActorImage;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.ui.SpriteAccessor;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.Screens;


/**
 * 
 * this is the main menu screen
 * @author EvoEval
 *
 */
public class MainMenuScreen extends AbstractScreen {


	ActorImage singlePlayerButton,rankButton,multiplayerButton,optionsButton,helpButton,statButton;
	/**
	 * @param game Game
	 */
	public MainMenuScreen (Game game) {

		singlePlayerButton = ResourceLoader.singlePlayerButton.createActor(singlePlayerButton_click);
		rankButton = ResourceLoader.rankButton.createActor(rankButton_click);
		multiplayerButton = ResourceLoader.multiplayerButton.createActor(multiplayerButton_click);
		optionsButton = ResourceLoader.optionsButton.createActor(optionsButton_click);
		helpButton = ResourceLoader.helpButton.createActor(helpButton_click);
		statButton = ResourceLoader.statButton.createActor(statButton_click);
		init();
	}
    //---------------------------------------------------------------------------------\
    //					    Methods that must be implemented
    //---------------------------------------------------------------------------------\
	public void init(){
		createStage();
		singlePlayerButton.positionCenterCenter(-88f-250f,-175f);
		rankButton.positionCenterCenter(-88f-250f, 0f);
		multiplayerButton.positionCenterCenter(88f+250f, -175f);
		optionsButton.positionCenterCenter(-88f-250f, 175f);
		helpButton.positionCenterCenter(88f+250f, 175f);
		statButton.positionCenterCenter(88f+250f, 0f);
		
		stage.addActor(singlePlayerButton);
		stage.addActor(rankButton);
		stage.addActor(multiplayerButton);
		stage.addActor(optionsButton);
		stage.addActor(helpButton);
		stage.addActor(statButton);
		
		Gdx.input.setInputProcessor(stage);
		Timeline.createParallel()		
			.push(Tween.to(singlePlayerButton,SpriteAccessor.CPOS_XY, 0.3f).targetRelative(+250, 0).ease(Bounce.OUT))
			.push(Tween.to(multiplayerButton,SpriteAccessor.CPOS_XY, 0.3f).targetRelative(-250, 0).ease(Bounce.OUT))
			.push(Tween.to(rankButton,SpriteAccessor.CPOS_XY, 0.4f).targetRelative(+250, 0).ease(Bounce.OUT))
			.push(Tween.to(statButton,SpriteAccessor.CPOS_XY, 0.4f).targetRelative(-250, 0).ease(Bounce.OUT))
			.push(Tween.to(optionsButton,SpriteAccessor.CPOS_XY, 0.5f).targetRelative(+250, 0).ease(Bounce.OUT))
			.push(Tween.to(helpButton,SpriteAccessor.CPOS_XY, 0.5f).targetRelative(-250, 0).ease(Bounce.OUT))
		.start(tweenManager);
		ScreenStandardHandler.setDialogVisible(false);
	}
	protected boolean handleBackButton() {
		return ScreenStandardHandler.exitButtonListner(touchX, touchY, stage);
	}
	
    //---------------------------------------------------------------------------------\
    //									InputListners
    //---------------------------------------------------------------------------------\
	
	private final InputListener singlePlayerButton_click =  new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
        	AudioLoader.play(AudioLoader.clickSound);
			if(Screens.chooseAIModeScreen==null) {
				Screens.chooseAIModeScreen = new ChooseAIModeScreen(game);
			}
			game.setScreen(Screens.chooseAIModeScreen);
			return true;	
        }
    };
	private final InputListener rankButton_click =  new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
			AudioLoader.play(AudioLoader.clickSound);				
			if(Medium.isLoggedIn()){
				if(Screens.profileScreen==null) 
					Screens.profileScreen = new ProfileScreen(game);
				else
					Screens.profileScreen.init();
				game.setScreen(Screens.profileScreen);
			}else{
				Screens.loginScreen = new LoginScreen(game,"ProfileScreen");
				game.setScreen(Screens.loginScreen);
			}
			return true;
        }
    };
	private final InputListener multiplayerButton_click =   new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
			AudioLoader.play(AudioLoader.clickSound);
			Medium.setGameMode(Medium.GAME_MODE_MULTIPLAYER);
			if(Medium.isLoggedIn()){
				if(Screens.chooseClientServerScreen==null) Screens.chooseClientServerScreen = new ChooseClientServerScreen(game);
				game.setScreen(Screens.chooseClientServerScreen);
			}else{
				Screens.loginScreen = new LoginScreen(game,"ChooseClientServerScreen");
				game.setScreen(Screens.loginScreen);
			}
			return true;
        }
    };
    private final InputListener optionsButton_click  =   new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
			AudioLoader.play(AudioLoader.clickSound);
			if(Screens.settingsScreen==null) Screens.settingsScreen = new SettingsScreen(game);
			game.setScreen(Screens.settingsScreen);		
			return true;
        }
	    
    };
    private final InputListener helpButton_click =  new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
			AudioLoader.play(AudioLoader.clickSound);
			if(Screens.helpScreen==null) 
				Screens.helpScreen = new HelpScreen(game);
			else
				Screens.helpScreen.init();
			game.setScreen(Screens.helpScreen);	
			return true;
        }
	    
    };
    private final InputListener statButton_click = new InputListener() {
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
			AudioLoader.play(AudioLoader.clickSound);
			if(Medium.isLoggedIn()){
				if(Screens.statScreen==null) 
					Screens.statScreen = new StatScreen(game);
				else
					Screens.statScreen.init();
				game.setScreen(Screens.statScreen);	
			}else{
				Screens.loginScreen = new LoginScreen(game,"StatScreen");
				game.setScreen(Screens.loginScreen);
			}	
			return true;
        }
    };
}
