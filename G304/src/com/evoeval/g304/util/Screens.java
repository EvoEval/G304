package com.evoeval.g304.util;
import com.evoeval.g304.screens.*;
public class Screens {
	
	public static GamePlayingScreen cardsScreen;
	public static BiddingScreen biddingScreen;
	public static ChooseClientServerScreen chooseClientServerScreen;
	public static ClientRequestScreen clientRequestScreen;
	public static ConnectionWaitScreen connectionWaitScreen;
	public static ServerWaitForClientsScreen serverWaitForClientsScreen;
	public static ShuffleScreen shuffleScreen;	
	public static LoadingScreen loadingScreen;
	public static MainMenuScreen mainMenuScreen;
	public static LoginScreen loginScreen;
	public static SettingsScreen settingsScreen; 
	public static StatScreen statScreen;
	public static HelpScreen helpScreen;
	public static GameOverScreen gameOverScreen;
	public static ProfileScreen profileScreen;
	public static ChooseAIModeScreen chooseAIModeScreen;
	public static void init(){
		cardsScreen = null;
		biddingScreen = null;
		chooseClientServerScreen = null;
		clientRequestScreen = null;
		serverWaitForClientsScreen = null;
		shuffleScreen= null;
		loadingScreen= null;
		mainMenuScreen = null;
		loginScreen=null;
		settingsScreen=null;
		statScreen=null;
		helpScreen=null;
		gameOverScreen=null;
		profileScreen=null;
		chooseAIModeScreen=null;
	}
	public static void dispose(){
		if(cardsScreen!=null) cardsScreen.dispose();
		if(biddingScreen!=null) biddingScreen.dispose();
		if(chooseClientServerScreen!=null) chooseClientServerScreen.dispose();
		if(clientRequestScreen!=null) clientRequestScreen.dispose();
		if(connectionWaitScreen!=null) connectionWaitScreen.dispose(); 
		if(serverWaitForClientsScreen!=null) serverWaitForClientsScreen.dispose();
		if(shuffleScreen!=null) shuffleScreen.dispose();
		if(loadingScreen!=null) loadingScreen.dispose();
		if(mainMenuScreen!=null) mainMenuScreen.dispose();
		if(loginScreen!=null) loginScreen.dispose();
		if(settingsScreen!=null) settingsScreen.dispose();
		if(statScreen!=null) statScreen.dispose();
		if(helpScreen!=null) helpScreen.dispose();
		if(gameOverScreen!=null) gameOverScreen.dispose();
		if(profileScreen!=null) profileScreen.dispose();
		if(chooseAIModeScreen!=null) chooseAIModeScreen.dispose();		
	}
}
