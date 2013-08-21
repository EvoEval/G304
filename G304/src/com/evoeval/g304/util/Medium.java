package com.evoeval.g304.util;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.evoeval.g304.cards.CardList;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.multiplayer.G304NetworkHandler;
import com.evoeval.g304.multiplayer.G304NetworkHandler.*;
import com.evoeval.g304.singleplayer.AIBase;



public class Medium {
	
	public static final String GAME_MODE_MULTIPLAYER =  "Multiplayer";
	public static final String GAME_MODE_SINGLEPLAYER =  "Sigleplayer";	
	public static final String GAME_AI_MODE_PROFESSIONAL =  "Professional";
	public static final String GAME_AI_MODE_NOVICE =  "Novice";	
	
	private static String gameMode="";
	
	private static boolean isTrumpOpen=false; //put the card to my hand when trump is open
	private static boolean isTrumpUsed=false;
	private static boolean isTurn=false; // is it the turn of this client
	private static boolean isBidPlaced=false; // did i place a bid
	private static boolean isServer = false; // am i the server
	private static boolean isPreGameBidding=true; // is this the game bidding hand
	private static boolean isBidWinner=false; // am i the bid winner
	private static boolean isFirstPlayer=false; //do i put the first card
	private static String currentFirstCard="";
	private static String prevFirstCard="";
	private static String trumpCard = ""; // this is for the use of server
	private static int bidValue=0; //every body has the bid  saved
	private static String bidWinnerId=""; //id of the bid winner
	private static CardList playerCards = null; //my cards
	private static boolean isConnected=false; // is connection successful
	private static boolean isVibrationEnabled=true; //
	private static boolean isSoundEnabled=true; //
	private static Game gameObj = null; // very important variable which holds LibGDX Game Object
	private static int shuffler=0; // player who  shuffled
	private static int cardRoundCount=1;
	private static String roundWinner="";
	private static boolean loginStatus=false; //logged status
	private static String userID=""; //logged user's ID
	private static String name="";//logged user's name	
	private static String nickName="";//previously used nick name
	private static String serverIPAddress="";//previously used server IP address
	private static Integer clientCount=0;
	private static String gameResolution="HD";
	private static String gameTheme="Metro";
	private static String gameLanguage="English";
	private static String aiMode="Professional";
	private static boolean AILoadLocal=false;
	
	
	private static ArrayList<PutACard> currentHandCards; // cards which were used by all players for this card-round
		
	private static Preferences prefs = Gdx.app.getPreferences("Prefs");
	
	
	
	
	public static void clear(){
		
		isTrumpOpen=false; 
		isTrumpUsed=false;
		isTurn=false; 
		isBidPlaced=false; 
		isServer = false; 
		isPreGameBidding=true; 
		isBidWinner=false; 
		isFirstPlayer=false;
		currentFirstCard="";
		prevFirstCard="";
		trumpCard = "";
		bidValue=0;
		bidWinnerId="";
		playerCards = null;
		isConnected=false;
		isVibrationEnabled=true;
		isSoundEnabled=true;
		shuffler=0;
		cardRoundCount=1;
		roundWinner="";
		clientCount=0;
		currentHandCards = new ArrayList<G304NetworkHandler.PutACard>();
		
		try{
			if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
				if(G304NetworkHandler.gClient!=null){
					G304NetworkHandler.gClient.disconnect();
				}
				if(G304NetworkHandler.gServer!=null){
					G304NetworkHandler.gServer.disconnect();
				}
			}
		}catch(Exception e){
			
		}
		
		G304NetworkHandler.gClient = null;
		G304NetworkHandler.gServer = null;
		
		CardsHandler.setDefault();
	}
	/**
	 * save login details
	 */
	public static void saveLogin(){		
		prefs.putString("userID", userID);
		prefs.putString("name", name);		
		prefs.putBoolean("loginStatus", loginStatus);
		prefs.flush();
	}
	/**
	 * load login details
	 */
	public static void loadSavedDetails(){		
		userID = prefs.getString("userID", "");
		name = prefs.getString("name", ""); 
		loginStatus = prefs.getBoolean("loginStatus", false); 
		nickName = prefs.getString("nickName", "");
		serverIPAddress = prefs.getString("serverIPAddress", "");
		isVibrationEnabled = prefs.getBoolean("vibration", true);
		isSoundEnabled = prefs.getBoolean("sound", true);
		gameTheme = prefs.getString("theme", "Metro");
		gameLanguage = prefs.getString("language", "English");
		AILoadLocal = prefs.getBoolean("AILoadLocal", false);		
	}
	/**
	 * Save Client nick name and IP address previously used
	 */
	public static void saveClientInformation(){
		prefs.putString("nickName", nickName);
		prefs.putString("serverIPAddress", serverIPAddress);		
		prefs.flush();
	}
	
	public static void saveVibrationEnabledStatus(){
		prefs.putBoolean("vibration", isVibrationEnabled);	
		prefs.flush();
	}
	
	public static void saveSoundEnabledStatus(){
		prefs.putBoolean("sound", isSoundEnabled);		
		prefs.flush();
	}
	
	public static void saveTheme(){
		prefs.putString("theme", gameTheme);		
		prefs.flush();
	}
	
	public static void saveLanguage(){
		prefs.putString("language", gameLanguage);		
		prefs.flush();
	}
	
	public static void removeLoginInfotmation(){
		prefs.putString("userID", "");
		prefs.putString("name", "");		
		prefs.putBoolean("loginStatus", loginStatus);
		prefs.flush();
	}
	
	public static void setAIAsUpdated(){
		prefs.putBoolean("AILoadLocal", true);	
		prefs.flush();
	}
	/**
	 * @return the gameMode
	 */
	public static String getGameMode() {
		return gameMode;
	}

	/**
	 * @param gameMode the gameMode to set
	 */
	public static void setGameMode(String _gameMode) {
		gameMode = _gameMode;
	}
	
	public static void createBid(String bid,String card){
		if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
			G304NetworkHandler.gClient.sendBid(bid, card);
		}else{
			AIBase.setPlayerBid(bid, card);
		}
	}
	public static BroadcastNames getPlayerNames(){
		if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
			return G304NetworkHandler.gClient.playerNames;
		}else{
			return AIBase.getNames();
		}
	}	
	public static String getId(){
		if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
			return G304NetworkHandler.gClient.id;
		}else{
			return "1";
		}
	}	
	public static void putACard(String card){
		if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
			G304NetworkHandler.gClient.putACard(card);
		}else{
			AIBase.putPlayerCard(card);
		}
	}		
	public static void sendCardsToServer(String[] cards){
		if(gameMode.equals(GAME_MODE_MULTIPLAYER)){
			G304NetworkHandler.gClient.sendCardsToServer(cards);
		}else{
			AIBase.setShuflledCards(cards);
		}
	}

	/**
	 * @return the currentHandCards
	 */
	public static ArrayList<PutACard> getCurrentHandCards() {
		return currentHandCards;
	}

	/**
	 * @param currentHandCards the currentHandCards to set
	 */
	public static void setCurrentHandCards(ArrayList<PutACard> currentHandCards) {
		Medium.currentHandCards = currentHandCards;
	}

	/**
	 * @return the isTrumpOpen
	 */
	public static boolean isTrumpOpen() {
		return isTrumpOpen;
	}

	/**
	 * @param isTrumpOpen the isTrumpOpen to set
	 */
	public static void setTrumpOpen(boolean isTrumpOpen) {
		Medium.isTrumpOpen = isTrumpOpen;
	}

	/**
	 * @return the isTrumpUsed
	 */
	public static boolean isTrumpUsed() {
		return isTrumpUsed;
	}

	/**
	 * @param isTrumpUsed the isTrumpUsed to set
	 */
	public static void setTrumpUsed(boolean isTrumpUsed) {
		Medium.isTrumpUsed = isTrumpUsed;
	}

	/**
	 * @return the isTurn
	 */
	public static boolean isTurn() {
		return isTurn;
	}

	/**
	 * @param isTurn the isTurn to set
	 */
	public static void setTurn(boolean isTurn) {
		Medium.isTurn = isTurn;
	}

	/**
	 * @return the isBidPlaced
	 */
	public static boolean isBidPlaced() {
		return isBidPlaced;
	}

	/**
	 * @param isBidPlaced the isBidPlaced to set
	 */
	public static void setBidPlaced(boolean isBidPlaced) {
		Medium.isBidPlaced = isBidPlaced;
	}

	/**
	 * @return the isServer
	 */
	public static boolean isServer() {
		return isServer;
	}

	/**
	 * @param isServer the isServer to set
	 */
	public static void setServer(boolean isServer) {
		Medium.isServer = isServer;
	}

	/**
	 * @return the isPreGameBidding
	 */
	public static boolean isPreGameBidding() {
		return isPreGameBidding;
	}

	/**
	 * @param isPreGameBidding the isPreGameBidding to set
	 */
	public static void setPreGameBidding(boolean isPreGameBidding) {
		Medium.isPreGameBidding = isPreGameBidding;
	}

	/**
	 * @return the isBidWinner
	 */
	public static boolean isBidWinner() {
		return isBidWinner;
	}

	/**
	 * @param isBidWinner the isBidWinner to set
	 */
	public static void setBidWinner(boolean isBidWinner) {
		Medium.isBidWinner = isBidWinner;
	}

	/**
	 * @return the isFirstPlayer
	 */
	public static boolean isFirstPlayer() {
		return isFirstPlayer;
	}

	/**
	 * @param isFirstPlayer the isFirstPlayer to set
	 */
	public static void setFirstPlayer(boolean isFirstPlayer) {
		Medium.isFirstPlayer = isFirstPlayer;
	}

	/**
	 * @return the currentFirstCard
	 */
	public static String getCurrentFirstCard() {
		return currentFirstCard;
	}

	/**
	 * @param currentFirstCard the currentFirstCard to set
	 */
	public static void setCurrentFirstCard(String currentFirstCard) {
		Medium.currentFirstCard = currentFirstCard;
	}

	/**
	 * @return the prevFirstCard
	 */
	public static String getPrevFirstCard() {
		return prevFirstCard;
	}

	/**
	 * @param prevFirstCard the prevFirstCard to set
	 */
	public static void setPrevFirstCard(String prevFirstCard) {
		Medium.prevFirstCard = prevFirstCard;
	}

	/**
	 * @return the trumpCard
	 */
	public static String getTrumpCard() {
		return trumpCard;
	}

	/**
	 * @param trumpCard the trumpCard to set
	 */
	public static void setTrumpCard(String trumpCard) {
		Medium.trumpCard = trumpCard;
	}

	/**
	 * @return the bidWinnerId
	 */
	public static String getBidWinnerId() {
		return bidWinnerId;
	}

	/**
	 * @param bidWinnerId the bidWinnerId to set
	 */
	public static void setBidWinnerId(String bidWinnerId) {
		Medium.bidWinnerId = bidWinnerId;
	}

	/**
	 * @return the bidValue
	 */
	public static int getBidValue() {
		return bidValue;
	}

	/**
	 * @param bidValue the bidValue to set
	 */
	public static void setBidValue(int bidValue) {
		Medium.bidValue = bidValue;
	}

	/**
	 * @return the playerCards
	 */
	public static CardList getPlayerCards() {
		return playerCards;
	}

	/**
	 * @param playerCards the playerCards to set
	 */
	public static void setPlayerCards(CardList playerCards) {
		Medium.playerCards = playerCards;
	}

	/**
	 * @return the isConnected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	/**
	 * @param isConnected the isConnected to set
	 */
	public static void setConnected(boolean isConnected) {
		Medium.isConnected = isConnected;
	}

	/**
	 * @return the isVibrationEnabled
	 */
	public static boolean isVibrationEnabled() {
		return isVibrationEnabled;
	}

	/**
	 * @param isVibrationEnabled the isVibrationEnabled to set
	 */
	public static void setVibrationEnabled(boolean isVibrationEnabled) {
		Medium.isVibrationEnabled = isVibrationEnabled;
	}

	/**
	 * @return the isSoundEnabled
	 */
	public static boolean isSoundEnabled() {
		return isSoundEnabled;
	}

	/**
	 * @param isSoundEnabled the isSoundEnabled to set
	 */
	public static void setSoundEnabled(boolean isSoundEnabled) {
		Medium.isSoundEnabled = isSoundEnabled;
	}

	/**
	 * @return the gameObj
	 */
	public static Game getGameObj() {
		return gameObj;
	}

	/**
	 * @param gameObj the gameObj to set
	 */
	public static void setGameObj(Game gameObj) {
		Medium.gameObj = gameObj;
	}

	/**
	 * @return the shuffler
	 */
	public static int getShuffler() {
		return shuffler;
	}

	/**
	 * @param shuffler the shuffler to set
	 */
	public static void setShuffler(int shuffler) {
		Medium.shuffler = shuffler;
	}

	/**
	 * @return the cardRoundCount
	 */
	public static int getCardRoundCount() {
		return cardRoundCount;
	}

	/**
	 * @param cardRoundCount the cardRoundCount to set
	 */
	public static void setCardRoundCount(int cardRoundCount) {
		Medium.cardRoundCount = cardRoundCount;
	}

	/**
	 * @return the loginStatus
	 */
	public static boolean isLoggedIn() {
		return loginStatus;
	}

	/**
	 * @param loginStatus the loginStatus to set
	 */
	public static void setLoginStatus(boolean loginStatus) {
		Medium.loginStatus = loginStatus;
	}

	/**
	 * @return the roundWinner
	 */
	public static String getRoundWinner() {
		return roundWinner;
	}

	/**
	 * @param roundWinner the roundWinner to set
	 */
	public static void setRoundWinner(String roundWinner) {
		Medium.roundWinner = roundWinner;
	}

	/**
	 * @return the userID
	 */
	public static String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public static void setUserID(String userID) {
		Medium.userID = userID;
	}

	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public static void setName(String name) {
		Medium.name = name;
	}
	/**
	 * @return the clientCount
	 */
	public static Integer getClientCount() {
		return clientCount;
	}
	/**
	 * @param clientCount the clientCount to set
	 */
	public static void setClientCount(Integer clientCount) {
		Medium.clientCount = clientCount;
	}
	/**
	 * @return the serverIPAddress1
	 */
	public static String getServerIPAddress() {
		return serverIPAddress;
	}
	/**
	 * @param serverIPAddress1 the serverIPAddress1 to set
	 */
	public static void setServerIPAddress(String serverIPAddress) {
		Medium.serverIPAddress = serverIPAddress;
	}
	/**
	 * @return the nickName1
	 */
	public static String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName1 the nickName1 to set
	 */
	public static void setNickName(String nickName) {
		Medium.nickName = nickName;
	}
	/**
	 * @return the gameResolution
	 */
	public static String getGameResolution() {
		return gameResolution;
	}
	/**
	 * @param gameResolution the gameResolution to set
	 */
	public static void setGameResolution(String gameResolution) {
		Medium.gameResolution = gameResolution;
	}
	/**
	 * @return the gameTheme
	 */
	public static String getGameTheme() {
		return gameTheme;
	}
	/**
	 * @param gameTheme the gameTheme to set
	 */
	public static void setGameTheme(String gameTheme) {
		Medium.gameTheme = gameTheme;
	}
	/**
	 * @return the gameLanguage
	 */
	public static String getGameLanguage() {
		return gameLanguage;
	}
	/**
	 * @param gameLanguage the gameLanguage to set
	 */
	public static void setGameLanguage(String gameLanguage) {
		Medium.gameLanguage = gameLanguage;
	}
	/**
	 * @return ui path
	 */
	public static String getUIpath(){
		return Medium.gameResolution + "/" + Medium.gameTheme + "/" + Medium.gameLanguage;
	}
	/**
	 * @return xml postfix
	 */
	public static String getXMLPostfix(){
		return Medium.gameResolution + "_" + Medium.gameTheme + "_" + Medium.gameLanguage;
	}
	/**
	 * @return the gameLanguage
	 */
	/**
	 * @return the aiMode
	 */
	public static String getAiMode() {
		return aiMode;
	}
	/**
	 * @param aiMode the aiMode to set
	 */
	public static void setAiMode(String aiMode) {
		Medium.aiMode = aiMode;
	}
	/**
	 * @return the aILoadLocal
	 */
	public static boolean getAILoadLocal() {
		return AILoadLocal;
	}
}
