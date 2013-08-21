package com.evoeval.g304.singleplayer;
// > == gt
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.esotericsoftware.jsonbeans.Json;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.gamerule.GameRule;
import com.evoeval.g304.gamerule.GameRule.G304RoundWinner;
import com.evoeval.g304.multiplayer.G304NetworkHandler.PutACard;
import com.evoeval.g304.util.Medium;

public class InterpreterTools {
	
	public void println(String text){
		System.out.println(text);
	}
	
	public String tojson(Object object){
		Json json = new Json();
		return json.prettyPrint(json.toJson(object));
	}
	
	public Integer getCardValue(String card){
		return CardsHandler.getCardValue(card);
	}
	
	public Integer getCardValueCmp(String card){
		return CardsHandler.getCardValueForComparision(card);
	}
	
	public Integer getCardValueCmp(String card,String trump){
		return CardsHandler.getCardValueForComparision(card,trump);
	}		
	public String getCardType(String card){
		return CardsHandler.getCardType(card);
	}	
	
	public String getMaxRemainingCard(String[] usedCards,String cardType){
		
		ArrayList<String> arr = new ArrayList<String>(Arrays.asList(cardType+"J",cardType+"9",
				cardType+"A",cardType+"0",cardType+"K",cardType+"Q",cardType+"8",cardType+"7"));
		for(String card : usedCards){
			arr.remove(card);
		}
		if(arr.size() > 0){
			return arr.get(0);
		}
		return "";
	}
	
	public String[] filterCards(String[] cards,String type){
		ArrayList<String> arr = new ArrayList<String>();
		for(String card : cards){
			if(getCardType(card).equals(type)){
				arr.add(card);
			}
		}
		return arr.toArray(new String[0]);
	}
	//return tools.tojson(tools.getMaxUsableCards(''.split(' '),''.split(' ')));
	public String[] getMaxUsableCards(String[] myCards,String[] cards){
		
		ArrayList<String> arr = new ArrayList<String>();
		
		String clubs = getMaxRemainingCard(filterCards(cards,CardsHandler.CLUBS),"C");		
		if(!clubs.isEmpty() && hasCard(myCards, clubs).equals("true")){
			arr.add(clubs);
		}

		String hearts = getMaxRemainingCard(filterCards(cards,CardsHandler.HEART),"H");		
		if(!hearts.isEmpty() && hasCard(myCards, hearts).equals("true")){
			arr.add(hearts);
		}
		
		String diamond = getMaxRemainingCard(filterCards(cards,CardsHandler.DIAMOND),"D");		
		if(!diamond.isEmpty() && hasCard(myCards, diamond).equals("true")){
			arr.add(diamond);
		}
		
		String spades = getMaxRemainingCard(filterCards(cards,CardsHandler.SPADES),"S");		
		if(!spades.isEmpty() && hasCard(myCards, spades).equals("true")){
			arr.add(spades);
		}
		
		return arr.toArray(new String[0]);
		
	}
	public String getAtIndex(String[] arr,Integer index){
		try{
			return arr[index];
		}catch(Exception e){
			
		}
		return "";
	}
	public Integer getCardCountOfType(String[] cards,String type){
		int i = 0;
		for(String card : cards){
			if(getCardType(card).equals(type)){
				i++;
			}
		}
		return i;
	}

	public String getMaxCard(String[] cards,String type,String useType){
		
		String retCard = "";
		for(String card : cards){
			if( (useType.equals("true")) 
					? (getCardType(card).equals(type)) : (true) ){
				retCard = ( getCardValueCmp(card) > getCardValueCmp(retCard) ) ? card : retCard ;
				
			}
		}
		return retCard;
	}
	
	public String getMinCard(String[] cards,String type,String useType){
		
		String retCard = "";
		for(String card : cards){
			if( (useType.equals("true")) 
					? (getCardType(card).equals(type)) : (true) ){
				
				if(retCard.isEmpty()){ 
					retCard = card; 
				}
				
				retCard = ( getCardValueCmp(card) < getCardValueCmp(retCard) ) ? card : retCard ;
				
			}
		}
		return retCard;
	}
	
	public String hasCardType(String[] cards,String type){
		String ret = "false";
		for(String card : cards){
			if(getCardType(card).equals(type)){
				return "true";
			}
		}
		return ret ;
	}
		
	public String hasCard(String[] cards,String theCard){
		String ret = "false";
		for(String card : cards){
			if(card.equals(theCard)){
				return "true";
			}
		}
		return ret ;
	}	
	
	public String hasFirstType(String[] cards){
		
		try{
			return hasCardType(cards,getCardType(Medium.getCurrentHandCards().get(0).card));
		}catch(Exception e){
			
		}
		
		return "false";
	}
	
	public String isEnemySoFarWinning(String botId){
		String ret = "false";		
		if(Medium.getCurrentHandCards().size()>0 && Medium.getCurrentHandCards().size()<4){
			G304RoundWinner round = GameRule.getRoundWinner(Medium.getCurrentHandCards(), Medium.getTrumpCard());
			if((botId.equals("1") && round.winner_id.equals("3")) || (botId.equals("2") && round.winner_id.equals("4")) || 
					(botId.equals("3") && round.winner_id.equals("1")) || (botId.equals("4") && round.winner_id.equals("2"))){
				ret = "true";
			}
		}
		return ret ;
	}
	
	//tools.getMinAvailableCardType('HJ H9 HK HQ S9 SJ'.split(' '));
	public String getMinAvailableCardType(String[] cards ){
		
		HashMap<String,Integer> hashm = new HashMap<String,Integer>();
		
		String type = "";
		
		Integer clubCnt = getCardCountOfType(cards,CardsHandler.CLUBS);
		Integer spadeCnt = getCardCountOfType(cards,CardsHandler.SPADES);
		Integer diamondCnt = getCardCountOfType(cards,CardsHandler.DIAMOND);
		Integer heartCnt = getCardCountOfType(cards,CardsHandler.HEART);
		
		if(clubCnt>0){
			hashm.put(CardsHandler.CLUBS,clubCnt );
		}
		
		if(spadeCnt>0){
			hashm.put(CardsHandler.SPADES,spadeCnt );
		}
		
		if(diamondCnt>0){
			hashm.put(CardsHandler.DIAMOND,diamondCnt );
		}
		
		if(heartCnt>0){
			hashm.put(CardsHandler.HEART,heartCnt );
		}
		
		Integer[] values = hashm.values().toArray(new Integer[0]);
		if(values.length > 0){
			Integer small = values[0];
			int ind = 0;
			for(int i =0;i<values.length;i++ )
			{
				if(values[i]<small){
					small = values[i];
					ind = i;
				}
			}
			
			type = hashm.keySet().toArray(new String[0])[ind];
		}
		return type;
	}
	
	public String isTrumpOpen(){
		
		if(Medium.isTrumpOpen()){
			return "true" ;					
		}
		
		return "false";
				
	}
	public String hasTrumpCards(String[] cards){	
		 
		return hasCardType(cards, getCardType(Medium.getTrumpCard()));
				
	}
	
	public String getTrumpCard(){
		return Medium.getTrumpCard();
	}
	
	public String getWinnableCard(String[] myCards,String botId){
		String max = "";
		
		if( Medium.getCurrentHandCards().size()>0 && Medium.getCurrentHandCards().size()<4){
			
			int largeEnemy=0;
			for(PutACard p : Medium.getCurrentHandCards()){
				if(getCardValueCmp(p.card,Medium.getTrumpCard())>largeEnemy){
					largeEnemy = getCardValueCmp(p.card,Medium.getTrumpCard());
				}
			}

			int largeMe=0;
			String lartgeMeCard="";
			for(String p : myCards){
				if(getCardValueCmp(p,Medium.getTrumpCard())>largeMe){
					largeMe = getCardValueCmp(p,Medium.getTrumpCard());
					lartgeMeCard = p;
				}
			}
			
			if(largeMe>largeEnemy){
				max = lartgeMeCard;
			}
		}
		return max;
	}
}
