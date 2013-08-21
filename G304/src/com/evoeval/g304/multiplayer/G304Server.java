
package com.evoeval.g304.multiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import com.esotericsoftware.kryonet.*;
import com.evoeval.g304.gamerule.GameRule;
import com.evoeval.g304.gamerule.GameRule.G304RoundWinner;
import com.evoeval.g304.multiplayer.G304NetworkHandler.*; // get subclasses inside a class 
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.UPnP;

import java.net.UnknownHostException;

import org.teleal.cling.support.model.PortMapping;
/**
 * G304Server class is responsible for communication on behalf 
 * of the server for Offline multiplayer gameplay
 * 
 * @author EvoEval
 * 
 * [Log]
 * 2:00 PM 5/25/2013 : now server calculates the card round winner , Bhathiya
 */

public class G304Server {
	
	private Server server; // Kryonet-server object
	
	private int bidCnt=0;
	private PlaceBid preBid=null;
	private G304ServerGameData gameData;
	
	/**
	 * Default constructor of the G304Server
	 * @throws IOException 
	 */
	private G304Server() throws IOException {
		
		gameData = new G304ServerGameData();
		
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new G304Connection();
			}
		};
		
	
		try{				
            UPnP.ForwardPort(PortMapping.Protocol.TCP, G304NetworkHandler.port, "G304");
        }	        
        catch(UnknownHostException e){
            //error getting the internal IP address (eg: 192.168.1.1), thus, port forwarding is not done.
        }

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		G304NetworkHandler.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				
				// connections for this server are actually G304Connections.
				
				G304Connection connection = (G304Connection)c;
				
				/*
				 * register a new client
				 * if and only if object is an instance of RegisterNewClient
				 * 
				 */
				if (object instanceof RegisterNewClient) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (connection.name != null) return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterNewClient)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					Medium.setClientCount(Medium.getClientCount()+1);
					connection.id=(Medium.getClientCount()).toString();
					//check if clientCount is 4
					if(Medium.getClientCount()<=4){
						AssignID assignID = new AssignID();
						assignID.id=connection.id;
						server.sendToTCP(connection.getID(), assignID); // send assigned ID to client
						if(Medium.getClientCount()==4){
							broadcastNames(); // send all names to all clients
						}
					}
					return;
				}
				
				/*
				 * get shuffled cards -> broadcast shuffled cards to all
				 * if and only if object is an instance of ShuffledCards
				 * 
				 */
				if (object instanceof ShuffledCards) {				
					
					ArrayList<String> A1 = new ArrayList<String>();
					ArrayList<String> A2 = new ArrayList<String>();
					ArrayList<String> A3 = new ArrayList<String>();
					ArrayList<String> A4 = new ArrayList<String>();
					ShuffledCards shuffledCards = (ShuffledCards)object;
					//send it to all
					for(int i = 0;i<shuffledCards.cards.length;i+=4){
						A1.add(shuffledCards.cards[i]);
						A2.add(shuffledCards.cards[i+1]);
						A3.add(shuffledCards.cards[i+2]);
						A4.add(shuffledCards.cards[i+3]);
					}
				
					AssignCards assignCards = new AssignCards();
					
					assignCards.pc1 = A1.toArray(new String[0]);
					assignCards.pc2 = A2.toArray(new String[0]);
					assignCards.pc3 = A3.toArray(new String[0]);
					assignCards.pc4 = A4.toArray(new String[0]);
					
					server.sendToAllTCP(assignCards);
					
				}
				
				/*
				 * track client bids
				 * broadcast the bid winner and the first player
				 */
				if (object instanceof PlaceBid) {
					PlaceBid placeBid = (PlaceBid)object;
					if(++bidCnt==4){
						if(Integer.parseInt(preBid.bid)<Integer.parseInt(placeBid.bid)){
							preBid=placeBid;
						}
						FinalizeBid finalizeBid = new FinalizeBid();
						finalizeBid.bid = preBid.bid;
						finalizeBid.bidderId = preBid.id;
						finalizeBid.card = preBid.card;
						Integer firstpl = (((Medium.getShuffler()+1)%4) == 0)?4:(((Medium.getShuffler())+1)%4); //select init bidder :p
						finalizeBid.firstpl = firstpl.toString();
						//------------------------------------
						//save data
						gameData.setTrumpCard(finalizeBid.card);
						gameData.setBidWinnerId(finalizeBid.bidderId);
						gameData.setBidValue(Integer.parseInt(finalizeBid.bid));
						//------------------------------------
						server.sendToAllTCP(finalizeBid); // broadcast the bid winner and the first player
					}else if(preBid==null||Integer.parseInt(preBid.bid)<Integer.parseInt(placeBid.bid)){
						preBid=placeBid;
					}
				}
				
				if(object instanceof PutACard){
					PutACard put = (PutACard)object;
					gameData.putACard(put); //save data
					
					CardSuccess cs = new CardSuccess();
					cs.card=put.card;
					cs.successId=put.id;					
					cs.firstCard=gameData.getFirstCard();
					cs.firstId=gameData.getFirstId();
					cs.isTrumpOpen=false;
					cs.bidderId="";
					cs.Trump="";		
					//
					if(gameData.isEndOfCardRound()){
						G304RoundWinner roundw = GameRule.getRoundWinner(gameData.getCardRound(),gameData.getTrumpCard());
						cs.nextId=roundw.winner_id;
						if(roundw.isTrumpOpen){
							cs.isTrumpOpen=true;
							cs.bidderId = gameData.getBidWinnerId();
							cs.Trump = gameData.getTrumpCard();
						}						
					}else{
						int successId = Integer.parseInt(put.id);
						Integer nextId = (((successId+1)%4) == 0)?4:(((successId)+1)%4);
						cs.nextId=nextId.toString();
					}
					
					server.sendToAllTCP(cs);
					
					if(gameData.getCardRoundCount()==8){
						//game over
						G304Score scor = GameRule.getGameWinner(gameData.getAllCardRounds(), 
								gameData.getTrumpCard(), gameData.getBidValue(), gameData.getBidWinnerId());
						server.sendToAllTCP(scor);
					}
				}
			}

			public void disconnected (Connection c) {
				G304Connection connection = (G304Connection)c;
				if (connection.name != null) {
					//one of them disconnected
				}
			}
		});
		server.bind(G304NetworkHandler.port);
		server.start();
		

		
	}
	
	public void disconnect(){
		try{
			server.stop();
			server.close();
			UPnP.ShutDown();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * broadcast player names and id's
	 */
	private void broadcastNames () {
		// Collect the names/id's for each connection .
		Connection[] connections = server.getConnections();
		HashMap<String,String> broadcastData = new HashMap<String,String>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			G304Connection connection = (G304Connection)connections[i];
			broadcastData.put(connection.id,connection.name); 
		}
		
		// Send the names and id's to everyone.
		BroadcastNames broadcastNames = new BroadcastNames();
		broadcastNames.p1 = broadcastData.get("1");
		broadcastNames.p2 = broadcastData.get("2");
		broadcastNames.p3 = broadcastData.get("3");
		broadcastNames.p4 = broadcastData.get("4");
		
		/*
		 * select a random shuffler
		 */
		Random rnd = new Random();
		Integer shuffler = rnd.nextInt(3)+1;
		Medium.setShuffler(shuffler);
		broadcastNames.shuffler = shuffler.toString();
		
		server.sendToAllTCP(broadcastNames);
	}
	
	
	/**
	 * Get Kryonet-connection by player id
	 * @param id player id
	 * @return Kryonet-connection
	 */
	@SuppressWarnings("unused")
	private G304Connection getConectionbyId(String id){
		Connection[] connections = server.getConnections();
		for (int i = connections.length - 1; i >= 0; i--) {
			G304Connection connection = (G304Connection)connections[i];
			if(connection.id.equals(id)){
				return connection;
			}
		}
		return null;
	}
	
	
	/**
	 * This holds per connection state
	 * @author EvoEval
	 */
	static class G304Connection extends Connection {
		public String id; //unique identifier for a player, can be 1-4 , play order
		public String name;

	}
	/**
	 * start the server 
	 * singleton <b>;)</b>
	 */
	public static void startServer(){
		try {
			G304NetworkHandler.gServer = new G304Server();
		} catch (IOException e) {
			System.out.println("Server creation failed->" + e.getMessage());
		}
	}
}
