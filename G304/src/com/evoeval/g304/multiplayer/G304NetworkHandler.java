
package com.evoeval.g304.multiplayer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;


/**
 * 
 * The Network class holds the data and classes 
 * common to both client and server
 * 
 * @author EvoEval
 * 
 * [Log]
 * 5:30 PM 5/26/2013 : comments, Bhathiya
 * 6:00 PM 5/26/2013 : modified card success, Bhathiya
 * 
 */
public class G304NetworkHandler {
	public static final int port = 54761;
	public static G304Client gClient = null;
	public static G304Server gServer = null;
	
	
	/**
	 * This registers objects that are going to be sent over the network.
	 * @param endPoint Kryonet-Client/Server Object
	 */
	public static void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(java.util.HashMap.class);	//must register hashmap or it wont work
		kryo.register(String[].class);
		kryo.register(RegisterNewClient.class);
		kryo.register(AssignID.class);
		kryo.register(BroadcastNames.class);
		kryo.register(ShuffledCards.class);
		kryo.register(AssignCards.class);
		kryo.register(PutACard.class);
		kryo.register(PlaceBid.class);
		kryo.register(FinalizeBid.class);
		kryo.register(CardSuccess.class);
		kryo.register(G304Score.class);
		
	}
	//=====================================================================================
	//...........................classes that stores info..................................
	//=====================================================================================	
	/**
	 * Register a new client
	 * unicast from all the client
	 * @author Bhathiya
	 *
	 */
	public static class RegisterNewClient {
		public String name;
	}
	

	
	/**
	 * Assign ID for all clients
	 * unicast from server
	 * @author Bhathiya
	 *
	 */
	public static class AssignID{
		public String id;
	}
	
	/**
	 * Broadcast all player details
	 * from server
	 * @author Bhathiya
	 *
	 */
	public static class BroadcastNames {
		public String p1;
		public String p2;
		public String p3;
		public String p4;
		public String shuffler; // every day i'm shuffling
	}
	
	/**
	 * Send Shuffled cards to server
	 * unicast from client
	 * @author Bhathiya
	 *
	 */
	public static class ShuffledCards{
		public String[] cards;
	}
	
	/**
	 * Assign cards for each player
	 * broadcast from server
	 * @author Bhathiya
	 *
	 */
	public static class AssignCards{
		public String[] pc1;
		public String[] pc2;
		public String[] pc3;
		public String[] pc4;
		
	}	
	
	
	/**
	 * put a card by a player
	 * unicast by client
	 * @author Bhathiya
	 *
	 */
	public static class PutACard {
		public String id;
		public String card;
		
		public String toString(){
			return card;			
		}
	}
	
	/**
	 * Place a bid
	 * unicast by client
	 * @author Bhathiya
	 *
	 */
	public static class PlaceBid {
		public String id;
		public String bid;
		public String card;		
	}	
	
	/**
	 * broadcast bid details
	 * from server
	 * @author Bhathiya
	 *
	 */
	public static class FinalizeBid {
		public String bidderId;
		public String bid;
		public String card;	
		public String firstpl; //every day i'm shufflin
	}	
	
	/**
	 * Successful Card Put
	 * Broadcasted From Server
	 * @author EvoEval
	 *
	 */
	public static class CardSuccess {
		//.handle normal card put.
		public String successId;
		public String card;
		public String nextId;	
		public String firstId;
		public String firstCard;
		//.special trump open scenario.
		public boolean isTrumpOpen;
		public String bidderId;
		public String Trump;		
	}	


	/**
	 * this class holds the final score
	 * 
	 * @author Bhathiya
	 */
	public static class G304Score{
		public String bid_winner;
		public int bid_amount;
		public int win_score;
		public boolean isTeamAWon;
		public int p1_score;
		public int p2_score;
		public int p3_score;
		public int p4_score;
		
		public String[] generateListData(){
			StringBuilder str= new StringBuilder("");
			String p1 = Medium.getPlayerNames().p1;
			String p2 = Medium.getPlayerNames().p2;
			String p3 = Medium.getPlayerNames().p3;			
			String p4 = Medium.getPlayerNames().p4;
			if(isTeamAWon){
				str.append(ResourceLoader.locale.get("WinningTeam") + " : " + p1 + "," + p3 + "\n")	;		
			}else{
				str.append(ResourceLoader.locale.get("WinningTeam") + " : " + p2 + "," + p4 + "\n")	;				
			}
			str.append(ResourceLoader.locale.get("WinningScore") + " : " + win_score  + "\n");
			str.append(ResourceLoader.locale.get("BidAmount") + " : " + bid_amount  + "\n");			
			str.append(p1 + " " + ResourceLoader.locale.get("Score") + " : " + p1_score  + "\n");
			str.append(p2 + " " + ResourceLoader.locale.get("Score") + " : " + p2_score  + "\n");
			str.append(p3 + " " + ResourceLoader.locale.get("Score") + " : " + p3_score  + "\n");
			str.append(p4 + " " + ResourceLoader.locale.get("Score") + " : " + p4_score  + "\n");
			return str.toString().split("\n");
			
		}
	}
	//=====================================================================================
	//.....................................................................................
	//=====================================================================================	
}
