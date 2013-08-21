package com.evoeval.g304.cards;

import java.util.*;

import com.evoeval.g304.ui.ImageAssetHandler;
/**
 * this is a util class for storing a card list
 * @author EvoEval
 *
 */
public class CardList {
	private Vector<String> cardList;
	private int selectedCard;
	private int currentDealtCardCount;
	private String trumpCard;
	/**
	 * create an empty card list 
	 */
	public CardList(){
		cardList = new Vector<String>();
		trumpCard="";
		selectedCard=-1;
		currentDealtCardCount=0;
	}
	/**
	 * Add set of cards using an array
	 * @param cards
	 */
	public CardList(String[] cards){
		cardList = new Vector<String>();
		for(String card:cards){
			cardList.add(card);
		}
	}
	/**
	 * Add set of cards using a space separated String
	 * @param cards
	 */
	public CardList(String cards){
		cardList = new Vector<String>();
		for(String card:cards.split(" ")){
			cardList.add(card);
		}
	}
	/**
	 * Add a card
	 * @param card new card
	 */
	public void add(String card){
		cardList.add(card);
	}
	/**
	 * Remove a card (this will also reduce the currentDealtCardCount)
	 * @param card remove a card
	 */
	public void remove(String card){
		if(cardList.remove(card)){
			currentDealtCardCount--;
		}
		
	}	
	/**
	 * get card by given index
	 * @param cardIndex card index
	 * @return card identifier string
	 */
	public String getCardByIndex(int cardIndex){
		String[] cards=getCards();
		return cards[cardIndex];
	}
	/**
	 * Get cards
	 * @return cards String array
	 */
	public String[] getCards(){
		String[] cards =  cardList.toArray(new String[0]);
		sortCards(cards);
		return cards;
	}	
	/**
	 * get set of cards string that can be used to send as a message
	 * @param cards array of cards
	 * @return suitable message
	 */
	public static String getCardsMessage(String[] cards){
		String s="";
		s=cards[0];
		for(int i=1;i<cards.length;i++){
			s+=" "+cards[i];
		}
		return s;
	}
	/**
	 * get set of ImageSprites for the current list of cards stored in the array
	 * @return ImageSprite list
	 */
	public ImageAssetHandler[] getImageSprites(){
		ImageAssetHandler[] imgSp = new ImageAssetHandler[this.getCount()];
		int i =0;
		for(String card:getCards()){
			imgSp[i++] = CardsHandler.getCardImageSprite(card);
		}
		return imgSp;
	} 
	/**
	 * get number of cards
	 * @return number of cards
	 */
	public int getCount(){
		return cardList.size();
	}
	/**
	 * get the card set length (used to place the cards at the center of the screen)
	 * @param spaceBetweenCards space between cards
	 * @return card set length
	 */
	public float getCardSetLength(float spaceBetweenCards){
		return ((getCurrentDealtCardCount()-1)*spaceBetweenCards+CardsHandler.CARD_WIDTH);
		
	}
	/**
	 * sort the string array according to cardOrder
	 * @param cardArray sorted card array
	 */
	private static void sortCards(String[] cardArray){
		Arrays.sort(cardArray,new Comparator<String>(){
			public int compare(String card1,String card2){
				int c1Index=0,c2Index=0;
				for(int i=0;i<CardsHandler.cardOrder.length;i++){
					if(CardsHandler.cardOrder[i].equals(card1))
						c1Index=i;
					if(CardsHandler.cardOrder[i].equals(card2))
						c2Index=i;				
				}
				if(c1Index==c2Index)
					return 0;
				else if(c1Index<c2Index)
					return -1;
				else
					return 1;
		
			}
		});
	}
	/**
	 * @return the selectedCard
	 */
	public int getSelectedCardIndex() {
		return selectedCard;
	}
	/**
	 * @param selectedCard the selectedCard to set
	 */
	public void setSelectedCardIndex(int selectedCard) {
		this.selectedCard = selectedCard;
	}
	/**
	 * get the number of cards dealt to the user
	 * @return the currentDealtCardCount
	 */
	public int getCurrentDealtCardCount() {
		return currentDealtCardCount;
	}
	/**
	 * set the number of cards dealt to the user
	 * @param currentDealtCardCount the currentDealtCardCount to set
	 */
	public void setCurrentDealtCardCount(int currentDealtCardCount) {
		this.currentDealtCardCount = currentDealtCardCount;
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
		remove(trumpCard);
		this.trumpCard = trumpCard;
	}
	/**
	 * set trump open
	 */
	public void setTrumpOpen(){
		if(!trumpCard.isEmpty()){
			currentDealtCardCount++;
			cardList.add(trumpCard);
			trumpCard="";
		}
	}
	public boolean hasTrump(){
		return (trumpCard!=null && !trumpCard.equals(""));
	}
	/**
	 * use trump card so its not there
	 */
	public void useTrump(){
		this.trumpCard = "";
	}
}
