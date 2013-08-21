
package com.evoeval.g304.gamerule;

import java.util.ArrayList;
import com.evoeval.g304.cards.CardList;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.multiplayer.G304NetworkHandler.*;

/**
 * 
 * The GameRule Class which handles testing of game rules
 * 
 * @author Bhathiya
 * 
 * [Log]
 * 6:00 PM 5/21/2013 : getRoundWinner() coded , Bhathiya
 * 2:14 PM 5/25/2013 : comments , Bhathiya
 * 5:40 PM 5/25/2013 : getScoreofARound(),getGameWinner() , Bhathiya
 * 4:00 PM 5/26/2013 : data classes ==> G304Score,RoundWinner Bhathiya
 * 5:00 PM 5/26/2013 : getGameWinner() removed a huge bug, Bhathiya
 * 5:30 PM 5/26/2013 : getRoundWinner() now returns trump open boolean, Bhathiya
 * 
 */

public class GameRule {	
	/**
	 * can a card in question be shown for given user
	 * 
	 * @param trumpOpen is trump open
	 * @param playerId id of the player
	 * @param cardPlayerId id of the player of the card in question
	 * @param firstCard first card of card round
	 * @param card card in question
	 * 
	 * @return can card be shown
	 * 
	 * TODO let player see their own card on playingScreen when tapped
	 */
	public static boolean isCardShowable(boolean trumpOpen,String playerId,
			String cardPlayerId,String firstCard,String card){
			
		if ((trumpOpen || 
				CardsHandler.getCardType(card).equals(CardsHandler.getCardType(firstCard)) || 
					playerId.equals(cardPlayerId))) {
			return true;
		}else{
			return false;
		}
			
		//return true;
	}	
	/**
	 * can the card in question be used by current user::
	 * 
	 * split the card type of the first card and match it with this hand : done
	 * if is the trump holder and trump is already open enable all the cards
	 * else enable trump and all other cards except other trump type cards in hand 
	 * if it is the trump holder
	 * 
	 * @param firstCard first card of game round
	 * @param Trump trump card
	 * @param isBidWinner is the player bid winner
	 * @param isFirstPlayer is the player first player of the game
	 * @param trumpOpen is trump open
	 * @param playerCard card in question
	 * @param cardList list of cards of player
	 * 
	 * @return can card be used
	 */
	public static boolean validateCard(String firstCard,String Trump,boolean isBidWinner,
			boolean isFirstPlayer,boolean trumpOpen, String playerCard,CardList cardList){

		
		if(firstCard.isEmpty()){ //major
			if(isFirstPlayer && 
					isBidWinner && CardsHandler.getCardType(playerCard).equals(CardsHandler.getCardType(Trump))){
				return false;
			}else{
				return true;			
			}
		}else{
			if(CardsHandler.getCardType(playerCard).equals(CardsHandler.getCardType(firstCard))){
				return true;				
			}else if(!checkHandForType(cardList,CardsHandler.getCardType(firstCard))){
				if(isBidWinner && !trumpOpen && !Trump.equals(playerCard) && 
						CardsHandler.getCardType(playerCard).equals(CardsHandler.getCardType(Trump))){
					return false;
				}else{
					return true;					
				}
			}else{
				return false;
			}
		}
	}
	/**
	 * check if the card list has card(s) of given type
	 * 
	 * @param cardList card list
	 * @param type type of card
	 * 
	 * @return if the card list has card(s) of given type
	 */
	public static boolean checkHandForType(CardList cardList,String type){
		for(String card:cardList.getCards()){
			if(CardsHandler.getCardType(card).equals(type)){
				return true;
			}
		}
		return false;
	}
	/**
	 * check if the trump card can be used 
	 * 
	 * @param cardList card list of the player
	 * @param firstCard first card of card round
	 * @param cardRoundCount number of card rounds
	 * @param isFirstPlayer is first player putting first card of whole game
	 * 
	 * @return if the trump card can be used 
	 */
	public static boolean checkTrumpUserbility(CardList cardList,String firstCard,int cardRoundCount,boolean isFirstPlayer,String trumpCard){
		
		if((firstCard.isEmpty()) || isFirstPlayer) 
		{
			//not first player putting first card of whole game
			return false;
		}else if( cardRoundCount==8 || (!checkHandForType(cardList,CardsHandler.getCardType(firstCard)) && !CardsHandler.getCardType(trumpCard).equals(CardsHandler.getCardType(firstCard)))){
			//same type cards as first card's type not available in card set
			//is last round (no other cards)
			return true;
		}
		return false;
	}
	
	/**
	 * get round winner
	 * 
	 * @param currentHandCards card list of current game round
	 * @param Trump trump card
	 * 
	 * @return id of the round winner and if its trump open
	 */
	public static G304RoundWinner getRoundWinner(ArrayList<PutACard> currentHandCards,String Trump){
		PutACard first,current,winCard;
		current = currentHandCards.get(0);
		first = currentHandCards.get(0);
		winCard = currentHandCards.get(0);
		G304RoundWinner roundWinner = new G304RoundWinner();
		int size = currentHandCards.size();
		for(int i=1;i<size;i++){
			current = currentHandCards.get(i);
			if((CardsHandler.getCardType(current.card).equals(CardsHandler.getCardType(first.card)) || 
					CardsHandler.getCardType(current.card).equals(CardsHandler.getCardType(Trump))) && 
						CardsHandler.getCardValueForComparision(current.card, Trump)>CardsHandler.getCardValueForComparision(winCard.card, Trump)){
				winCard = current;
			}
		}
		roundWinner.winner_id = winCard.id;
		roundWinner.isTrumpOpen = false;
		
		//win-card's-type equals trump-card's-type and the first-card's-type not equals trump-card's-type
		if(CardsHandler.getCardType(winCard.card).equals(CardsHandler.getCardType(Trump)) && 
				!CardsHandler.getCardType(first.card).equals(CardsHandler.getCardType(Trump))){
			roundWinner.isTrumpOpen = true;
		}
		return roundWinner;
	}
	/**
	 * get score of a card round
	 * 
	 * @param currentHandCards current hands
	 * 
	 * @return score of given card round
	 */
	public static int getScoreofARound(ArrayList<PutACard> currentHandCards){
		int score=0;
		for(int i=0;i<4;i++){
			score+=CardsHandler.getCardValue(currentHandCards.get(i).card);
		}
		return score;
	}
	
	/**
	 * get the game winner
	 * 
	 * @param allCardRounds all cards
	 * @param Trump trump
	 * 
	 * @return G304Score object containing information
	 */
	public static G304Score getGameWinner(ArrayList<ArrayList<PutACard>> allCardRounds,String Trump,int bid_amount,String bid_winner){
		//1,3 Team A
		//2,4 Team B
		G304Score score = new G304Score();
		String winner;
		ArrayList<PutACard> currentHandCards; 
		int scoreA=0,scoreB=0,p1=0,p2=0,p3=0,p4=0,roundScore;
		for(int i=0;i<8;i++){
			currentHandCards = allCardRounds.get(i);
			winner = getRoundWinner(currentHandCards,Trump).winner_id;
			roundScore = getScoreofARound(currentHandCards);
			
			//-----------------------------------------
			if(winner.equals("1") || winner.equals("3")){
				scoreA+=roundScore;
			}else{
				scoreB+=roundScore;
			}
			//-----------------------------------------
			if(winner.equals("1")){
				p1+=roundScore;
			}else if(winner.equals("2")){
				p2+=roundScore;
			}else if(winner.equals("3")){
				p3+=roundScore;
			}else{
				p4+=roundScore;
			}
			//-----------------------------------------
		}
		
		if(bid_winner.equals("1") || bid_winner.equals("3")){
			if(scoreB>=304-bid_amount-100){
				score.isTeamAWon=false; //team A bids and looses game
				score.win_score=scoreB;
			}else{
				score.isTeamAWon=true; //team A bids and wins game
				score.win_score=scoreA;
			}
		}else{
			if(scoreA>=304-bid_amount-100){
				score.isTeamAWon=true; //team B bids and looses game
				score.win_score=scoreA;
			}else{
				score.isTeamAWon=false; //team B bids and wins game 
				score.win_score=scoreB;
			}			
		}
		score.p1_score = p1;
		score.p2_score = p2;
		score.p3_score = p3;
		score.p4_score = p4;
		
		score.bid_winner = bid_winner;
		score.bid_amount = bid_amount;
		
		return score;
	}
	
	//=====================================================================================
	//...........................additional classes that stores info.......................
	//=====================================================================================	
		
	/**
	 * this class stores the winner id and if its trump open
	 * 
	 * @author Bhathiya
	 */
	public static class G304RoundWinner{
		public String winner_id;
		public boolean isTrumpOpen;
	}
	
	//=====================================================================================
	//.....................................................................................
	//=====================================================================================	
	
}
