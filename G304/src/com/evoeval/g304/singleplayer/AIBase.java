package com.evoeval.g304.singleplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import com.evoeval.g304.cards.CardList;
import com.evoeval.g304.gamerule.GameRule;
import com.evoeval.g304.gamerule.GameRule.G304RoundWinner;
import com.evoeval.g304.multiplayer.G304NetworkHandler.BroadcastNames;
import com.evoeval.g304.multiplayer.G304NetworkHandler.G304Score;
import com.evoeval.g304.multiplayer.G304NetworkHandler.PutACard;
import com.evoeval.g304.screens.GameOverScreen;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.Screens;
import com.evoeval.g304.util.AIActionIntepreter;


public class AIBase{

	private static HashMap<Integer,CardList> allPlayerCards=null;
	private static BroadcastNames names=null;
	private static Integer next=1;	
	private static boolean playerCardPlayed=false;
	private static ArrayList<ArrayList<PutACard>> allCardRounds=null;
	private final static InterpreterTools toolsObj = new InterpreterTools();
	private final static ArrayList<String> arrayObj = new ArrayList<String>();
	public static JexlContext currentJexl=null;	
	
	public static void initAI(){
		Medium.setGameMode(Medium.GAME_MODE_SINGLEPLAYER);
		if(names==null){
			initNames();
		}
		allCardRounds=new ArrayList<ArrayList<PutACard>>();
		allPlayerCards=null;          
		next=1;	      
		playerCardPlayed=false;
	}
	
	private static void initNames(){
		names = new BroadcastNames();
		names.p1 = (Medium.getName().isEmpty())?"Player":Medium.getName();
		names.p2 = "Bot Bhathiya";
		names.p3 = "Bot Yohan";
		names.p4 = "Bot Ralph";
	}
	private static void chechIfWon(){	
		if(allCardRounds.size()==8){
			G304Score sc = GameRule.getGameWinner(allCardRounds, Medium.getTrumpCard(), Medium.getBidValue(), "1");
			Screens.gameOverScreen = new GameOverScreen(Medium.getGameObj(),sc);
			Medium.getGameObj().setScreen(Screens.gameOverScreen);
			return;
		}
	}
	private static void addCard(PutACard p){
		
		System.out.println("PutACard card="+p.card+" id="+p.id);		
		
		if(Medium.getCurrentHandCards().size()==4){	
			allCardRounds.add(Medium.getCurrentHandCards());
			Medium.setCurrentHandCards(new ArrayList<PutACard>());
			Medium.setCardRoundCount(Medium.getCardRoundCount() + 1);			
		}	
				
		Medium.getCurrentHandCards().add(p);
		
		next = (((next+1)%4) == 0)?4:(((next)+1)%4);
		
		if(Medium.getCurrentHandCards().size()==1){
			Medium.setCurrentFirstCard(p.card);
		}else if(Medium.getCurrentHandCards().size()==4){			
			G304RoundWinner r=GameRule.getRoundWinner(Medium.getCurrentHandCards(), Medium.getTrumpCard());
			next=Integer.parseInt(r.winner_id);	
			if(next==1){
				Medium.setTurn(true);
			}
			Medium.setRoundWinner(next.toString());
			if(r.isTrumpOpen && !Medium.isTrumpOpen()){				
				Medium.getPlayerCards().setTrumpOpen();
				Medium.getPlayerCards().remove(p.card);
				Medium.setTrumpOpen(true);
				Screens.cardsScreen.showTrumpOpenWindow();
			}
			if(Medium.getCardRoundCount()==8){
				allCardRounds.add(Medium.getCurrentHandCards());
				chechIfWon();
			}
		}else if(Medium.getCurrentHandCards().size()==3){
			Medium.setPrevFirstCard(Medium.getCurrentFirstCard());
			Medium.setCurrentFirstCard("");
			
		}

	}
	
	private static String[] getUsableCards(int id,CardList cards){
		ArrayList<String> usable = new ArrayList<String>();
		int bidwinner=Integer.parseInt(Medium.getBidWinnerId());
		for(String card:cards.getCards()){
			if(GameRule.validateCard((Medium.getCurrentHandCards().size()!=3)?Medium.getCurrentFirstCard():Medium.getPrevFirstCard(),
					Medium.getTrumpCard(),id==bidwinner, 
					Medium.isFirstPlayer(), Medium.isTrumpOpen(), card, cards)){
				usable.add(card);
			}
		}
		if(id==bidwinner && GameRule.checkTrumpUserbility(cards,
				(Medium.getCurrentHandCards().size()==3)?Medium.getPrevFirstCard():Medium.getCurrentFirstCard(), 
				Medium.getCardRoundCount(), Medium.isFirstPlayer(),Medium.getTrumpCard())){
			usable.add(cards.getTrumpCard());
		}
		if(usable.isEmpty()){
			return cards.getCards();
		}else{
			return usable.toArray(new String[0]);
		}
		
	}
	private static String[] getAllCards(){
		ArrayList<String> str=new ArrayList<String>();
		for(ArrayList<PutACard> cards : allCardRounds){
			for(PutACard putACard : cards){
				str.add(putACard.card);
			}
		}
		return (String[]) str.toArray(new String[str.size()]);
	}
	private static JexlContext getDetails(String[] cards,String id){
		JexlContext jc = new MapContext();
		jc.set("M", new Medium());
		jc.set("usable", cards);
		jc.set("allCards", getAllCards());
		jc.set("botId", id);
		jc.set("tools",toolsObj );
		jc.set("temp",arrayObj );
		return jc;
	}
	private static String getProAICard(int id){
		
		try{
			String[] cards = getUsableCards(id,allPlayerCards.get(id));
			System.out.print(Arrays.toString(cards));
			int r = (cards.length-1);
			if(r>0){
					HashMap<String, String> hash = new HashMap<String, String>();
					JexlContext jc = getDetails(cards,new Integer(id).toString());
					currentJexl = jc;
					jc.set("hash", hash);
					String ret = AIActionIntepreter.generateHashMapforXML(hash,jc);							
					if(ret.equals("success")){
						String functionName = AIActionIntepreter.getAIXMLDecision(hash);
						System.out.println(functionName);
						String choice = AIActionIntepreter.runScriptFunction(jc, functionName);
						if(choice.isEmpty()){
							return cards[0];
						}else{
							return choice;
						}
					}				
			}else if(r==0){
				return cards[0];
			}	
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return "";
		
	}	
	
	private static String getRandomAICard(int id){
		
		String[] cards = getUsableCards(id,allPlayerCards.get(id));
	
		Random rn = new Random();
		int r = (cards.length-1);
		
		if(r>0){				
				int randomNum =  rn.nextInt(r);
				return cards[randomNum];
				
		}else if(r==0){
			return cards[0];
		}	
		
		return "";
		
	}

	public static void putPlayerCard(String card){
		if(Medium.isTurn()){
			Medium.getPlayerCards().remove(card);
			PutACard p = new PutACard();
			p.id = "1";
			p.card = card;
			addCard(p);
			if(card.equals(Medium.getTrumpCard())){
				Medium.getPlayerCards().useTrump();
			}
			Medium.setTurn(false);
			playerCardPlayed=true;
		}
	}
	
	public static void getAIcards(){		
		if(next==1){
			Medium.setTurn(true);
		}else if(next!=-1 && !Medium.isTurn() && playerCardPlayed){
			String card;
			if(Medium.getAiMode().equals(Medium.GAME_AI_MODE_PROFESSIONAL)){
				card = getProAICard(next);
			}else{
				card = getRandomAICard(next);
			}
			PutACard p = new PutACard();
			p.card = card;
			p.id = next.toString();
			allPlayerCards.get(next).remove(card);				
			addCard(p);			
		} 
	}
	
	public static void setShuflledCards(String[] cards){

		ArrayList<String> A1 = new ArrayList<String>();
		ArrayList<String> A2 = new ArrayList<String>();
		ArrayList<String> A3 = new ArrayList<String>();
		ArrayList<String> A4 = new ArrayList<String>();
		
		for(int i = 0;i<cards.length;i+=4){
			A1.add(cards[i]);
			A2.add(cards[i+1]);
			A3.add(cards[i+2]);
			A4.add(cards[i+3]);
		}	
		
		allPlayerCards = new HashMap<Integer, CardList>();
		
		allPlayerCards.put(1,new CardList(A1.toArray(new String[0])));		
		allPlayerCards.put(2, new CardList(A2.toArray(new String[0])));
		allPlayerCards.put(3, new CardList(A3.toArray(new String[0])));
		allPlayerCards.put(4, new CardList(A4.toArray(new String[0])));
		
		Medium.setPlayerCards(allPlayerCards.get(1));
	}
	
	public static BroadcastNames getNames() {
		return names;
	}

	public static void setPlayerBid(String bid,String card){
		
		
		Medium.setBidWinnerId("1");
		Medium.setBidValue(Integer.parseInt(bid));
		Medium.setTrumpCard(card);
		
		//i'm bid winner
		Medium.getPlayerCards().setTrumpCard(card);
		Medium.setBidWinner(true);	

		Medium.setPreGameBidding(false);
		
		//i'm first player
		Medium.setFirstPlayer(true);
		Medium.setTurn(true);
	}
}
