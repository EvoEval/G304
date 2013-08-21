
package com.evoeval.g304.multiplayer;

import java.util.*;
import com.evoeval.g304.multiplayer.G304NetworkHandler.*;

/**
 * 
 * Stores GameData (For Server)
 * @author EvoEval
 * 
 * [Log]
 * 4:05 PM 5/25/2013 : comments , Bhathiya
 * 
  */

public class G304ServerGameData {
	
	private String trumpCard = "";
	private int bidValue=0; 
	private String bidWinnerId=""; //id of the bid winner	
	private ArrayList<ArrayList<PutACard>> allCardRounds=null;	
	private ArrayList<PutACard> cardRound=null;
	private BroadcastNames playerNames=null;
	//private int cardRoundCount=0;
	
	/**
	 * init GameData
	 */
	public G304ServerGameData(){
		cardRound = new ArrayList<PutACard>(); //create new object
		allCardRounds = new ArrayList<ArrayList<PutACard>>(); //create new object
	}
	
	/**
	 * put a card to ArrayList
	 * @param put put a card object
	 */
	public void putACard(PutACard put){	
		cardRound.add(put);
		if(cardRound.size()==4){
			allCardRounds.add(cardRound);
			cardRound = new ArrayList<PutACard>();
		}		
	}
	/**
	 * get the first card of card round
	 * if card round is null it returns empty
	 * @return first card
	 */
	public String getFirstCard(){
		debugPrint();
		if(cardRound.size()==0){
			return "";
		}else{
			System.out.println("game data card : "+cardRound.get(0).card);
			return cardRound.get(0).card;
			
		}
	}
	
	public void debugPrint(){		
		System.out.println("allCardRounds="+Arrays.toString(allCardRounds.toArray())+
				" cardRound="+Arrays.toString(cardRound.toArray(new PutACard[0])));
	}
	/**
	 * get the id of player of the first card
	 * @return first id
	 */
	public String getFirstId(){
		if(cardRound.isEmpty()){
			return "";
		}else{
			return cardRound.get(0).id;
		}
	}
	
	/**
	 * @return the playerNames
	 */
	public BroadcastNames getPlayerNames() {
		return playerNames;
	}

	/**
	 * @param playerNames the playerNames to set
	 */
	public void setPlayerNames(BroadcastNames playerNames) {
		this.playerNames = playerNames;
	}

	/**
	 * @return the trumpCard
	 */
	public String getTrumpCard() {
		return trumpCard;
	}

	/**
	 * @param trumpCard the trumpCard to set
	 */
	public void setTrumpCard(String trumpCard) {
		this.trumpCard = trumpCard;
	}

	/**
	 * @return the bidValue
	 */
	public int getBidValue() {
		return bidValue;
	}

	/**
	 * @param bidValue the bidValue to set
	 */
	public void setBidValue(int bidValue) {
		this.bidValue = bidValue;
	}

	/**
	 * @return the bidWinnerId
	 */
	public String getBidWinnerId() {
		return bidWinnerId;
	}

	/**
	 * @param bidWinnerId the bidWinnerId to set
	 */
	public void setBidWinnerId(String bidWinnerId) {
		this.bidWinnerId = bidWinnerId;
	}

	/**
	 * @return the cardRoundCount
	 */
	public int getCardRoundCount() {
		return allCardRounds.size();
	}
	
	/**
	 * 
	 * @return is end of a card round
	 */
	public boolean isEndOfCardRound(){
		return ((allCardRounds.size()>0) && (cardRound.size()==0));
	}
	/**
	 * @return get previous card round
	 */
	public ArrayList<PutACard> getCardRound(){
		if(allCardRounds!=null && allCardRounds.size()>0){
			return allCardRounds.get(allCardRounds.size()-1); //last one added
		}
		return null;
	}
	
	public ArrayList<ArrayList<PutACard>> getAllCardRounds(){
		return allCardRounds;
	}
}
