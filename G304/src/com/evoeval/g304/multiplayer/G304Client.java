
package com.evoeval.g304.multiplayer;

import com.esotericsoftware.kryonet.*;
import com.evoeval.g304.cards.CardList;
import com.evoeval.g304.multiplayer.G304NetworkHandler.*; // get subclasses inside a class 
import com.evoeval.g304.screens.*;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.Screens;


/**
 * 
 * G304Server class is responsible for communication on behalf 
 * of the client for Offline multiplayer gameplay
 * 
 * @author EvoEval
 *
 * [Log]
 * 2:09 PM 5/25/2013 : fixed a bug in cardSuccess scenario
 * 
 */
public class G304Client {
	
	private Client client;
	public  String name;
	public  String id;
	public BroadcastNames playerNames;
	private Thread clientthread;
	/**
	 * Constructor for G304Client
	 * @param _host ip address of the server
	 * @param _name name chosen by client
	 */
	public G304Client(String _host,String _name) throws Exception{
		
		client = new Client();

		client.start();
		
		name = _name;	
		final String host = _host;
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		G304NetworkHandler.register(client);
		
		
		client.addListener(new Listener() {
			
			/*
			 * connected to the server
			 */
			public void connected (Connection connection) {
				RegisterNewClient registerName = new RegisterNewClient();
				registerName.name = name;
				client.sendTCP(registerName);
			}
			
			/*
			 * object is received
			 */
			public void received (Connection connection, Object object) {
				
				/*
				 * assign an id for the client given by server
				 * if and only if object is an instance of AssignID
				 * 
				 */
				if (object instanceof AssignID) {
					AssignID assignID = (AssignID)object;
					id = assignID.id;
					return;
				}
				
				/*
				 * get all the names and id's of all players
				 * if and only if object is an instance of BroadcastNames
				 * 
				 */
				if (object instanceof BroadcastNames) {
					playerNames = (BroadcastNames)object;
					if(playerNames.shuffler.equals(id)){
						if(Screens.shuffleScreen==null) Screens.shuffleScreen = new ShuffleScreen(Medium.getGameObj());
						Medium.getGameObj().setScreen(Screens.shuffleScreen);
					}
					return;
				}		
				
				/*
				 * get personal cards
				 * if and only if object is an instance of AssignCards
				 * 
				 */	
				if (object instanceof AssignCards) {
					AssignCards assignCards = (AssignCards)object;
					
					if(id.equals("1")){
						Medium.setPlayerCards(new CardList(assignCards.pc1));
					}else if(id.equals("2")){
						Medium.setPlayerCards(new CardList(assignCards.pc2));
					}else if(id.equals("3")){
						Medium.setPlayerCards(new CardList(assignCards.pc3));
					}else{
						Medium.setPlayerCards(new CardList(assignCards.pc4));
					}
					
					
					if(Screens.cardsScreen==null) Screens.cardsScreen = new GamePlayingScreen(Medium.getGameObj());
					Screens.cardsScreen.init(true);
					Medium.getGameObj().setScreen(Screens.cardsScreen);
				}
				
				/*
				 * get the bid winner and the first player
				 */
				if(object instanceof FinalizeBid) {
					FinalizeBid finalizeBid = (FinalizeBid)object;
					Medium.setBidWinnerId(finalizeBid.bidderId);
					Medium.setBidValue(Integer.parseInt(finalizeBid.bid));
					Medium.setTrumpCard(finalizeBid.card);
					
					if(id.equals(finalizeBid.bidderId)){
						Medium.getPlayerCards().setTrumpCard(finalizeBid.card);
						Medium.getPlayerCards().setCurrentDealtCardCount(7);
						Medium.setBidWinner(true);
					}else{
						Medium.getPlayerCards().setCurrentDealtCardCount(8);
					}
					Medium.setPreGameBidding(false);
					//handle first player
					if(finalizeBid.firstpl.equals(id)){
						Medium.setFirstPlayer(true);
						Medium.setTurn(true);
					}
				}
				
				if(object instanceof CardSuccess){
					//now show the cards
					CardSuccess c = (CardSuccess)object;
					PutACard p = new PutACard();
					System.out.println("{id='" + id +" 'c.firstcard=" + c.firstCard);
					//if success id is me then remove it from CardList
					p.id = c.successId;
					p.card = c.card;
					Medium.setPrevFirstCard(Medium.getCurrentFirstCard());
					Medium.setCurrentFirstCard(c.firstCard);
					Medium.setRoundWinner(c.nextId);
					
					if(Medium.getCurrentHandCards().size()==4){
						Medium.getCurrentHandCards().clear();
						Medium.setCardRoundCount(Medium.getCardRoundCount() + 1);
					}
					
					Medium.getCurrentHandCards().add(p);
					

					if(p.id.equals(id)){
						Medium.getPlayerCards().remove(p.card);						
					}
					
					if(c.nextId.equals(id)){
						Medium.setTurn(true);
					}else{
						Medium.setTurn(false);
					}
					//TODO Optimize
					if(c.isTrumpOpen){
						
						//M.setTrumpCard(c.Trump);
						if(id.equals(c.bidderId)){
							Medium.getPlayerCards().setTrumpOpen();
						}
						
						
						
						if(!Medium.isTrumpOpen()){
							Screens.cardsScreen.showTrumpOpenWindow();
						}
						
						Medium.setTrumpOpen(true);
					}
				}
				
				if (object instanceof G304Score) {
					Screens.gameOverScreen = new GameOverScreen(Medium.getGameObj(),((G304Score)object));
					Medium.getGameObj().setScreen(Screens.gameOverScreen);
					
				}
			}

			/*
			 * server disconnected
			 */
			public void disconnected (Connection connection) {

			}
		});

		
		//connect to server	
		clientthread = new Thread("Connect") {
			public void run () {
				try{
					client.connect(5000, host, G304NetworkHandler.port);
				}catch(Exception e){
					System.out.println("Client-Creation-Faild");
				}
			}
		};
		
		clientthread.start();
	}
	public void disconnect(){
		try {		
			client.stop();
			clientthread.join();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * send all cards
	 * @param _cards cards array
	 */
	public void sendCardsToServer(String[] _cards){
		ShuffledCards shuffledCards = new ShuffledCards();
		shuffledCards.cards = _cards;
		client.sendTCP(shuffledCards);
	}
	
	/**
	 * place a bid 
	 * @param _bid amount
	 * @param _card chosen card
	 */
	public void sendBid(String _bid,String _card){
		PlaceBid placeBid = new PlaceBid();
		placeBid.card = _card;
		placeBid.bid = _bid;
		placeBid.id = this.id;
		client.sendTCP(placeBid);
	}
	
	/**
	 * this allows the client to put a card
	 * @param card card to be put
	 */
	public void putACard(String card){
		PutACard put = new PutACard();
		put.card = card;
		put.id = this.id;
		client.sendTCP(put);
	}
}
