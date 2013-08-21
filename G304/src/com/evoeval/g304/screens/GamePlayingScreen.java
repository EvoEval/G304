package com.evoeval.g304.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.gamerule.GameRule;
import com.evoeval.g304.multiplayer.G304NetworkHandler.PutACard;
import com.evoeval.g304.singleplayer.AIBase;
import com.evoeval.g304.ui.ImageAssetHandler;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.AudioLoader;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ScreenRatioCalculator;
import com.evoeval.g304.util.Screens;


/**
 * show the cards the the player has
 * 
 * @author Bhathiya
 *
 * [Log]
 * 
 */

public class GamePlayingScreen implements Screen {

	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	ImageAssetHandler backCard=null,outerGlowCard,outerGlowRedCard,outerGlowYlCard,trumpCard=null;
	boolean firstScreen=false;
	float touchX=0f,touchY=0f;
	final static float space=30.0f;
	Stage stage = null;
	Vector3 v3;
	int animation=0,mov=0;
	float dt;
	private boolean showtrump=false;
	
	Label lblP1,lblP2,lblP3,lblP4;
	ImageAssetHandler card1=null,card2=null,card3=null,card4=null;
	
	
	/**
	 * Start the cards screen
	 * very important screen of g304
	 * @param game
	 */
	public GamePlayingScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();
		init(true);
		//---------------------
	}
	/**
	 * 
	 */
	public void init(boolean setCards){
		
		firstScreen=false;
		touchX=0f;
		touchY=0f;
		animation=0;		
		mov=0;	
		showtrump=false;
		
		if(stage==null){
			stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);		
		}else{
			stage.clear();
		}
			
		Gdx.input.setInputProcessor(stage);	
		
		ScreenStandardHandler.setDialogVisible(false);
		if(backCard!=null){
			backCard.resetSprite();
		}
		createLabels();	
		
		if(setCards){
			firstScreen=true;
			Medium.getPlayerCards().setSelectedCardIndex(-1);		
			if(Medium.isPreGameBidding()){
				Medium.getPlayerCards().setCurrentDealtCardCount(4);
			}else if(Medium.getPlayerCards().hasTrump()){
				Medium.getPlayerCards().setCurrentDealtCardCount(7);
			}else{
				Medium.getPlayerCards().setCurrentDealtCardCount(8);
			}
		}
	}
	public void showTrumpOpenWindow()
	{
		this.showtrump=true;
	}
	/**
	 * get display name
	 * @param _id user id
	 * @return display name
	 */
	private CharSequence getDisplayName(Integer _id){
		String disp = "";
		
		if(_id==1){
			disp += Medium.getPlayerNames().p1;
		}else if(_id==2){
			disp += Medium.getPlayerNames().p2;
		}else if(_id==3){
			disp += Medium.getPlayerNames().p3;
		}else{
			disp += Medium.getPlayerNames().p4;
		}
		
		if(Medium.getBidWinnerId().equals(_id.toString())){
			disp += " : " + Medium.getBidValue();
		}
		return disp;
	}
	
	/**
	 * create smart labels
	 */
	private void createLabels(){
		if(Medium.getId().equals("1")){
			lblP1 = new Label(getDisplayName(1),ResourceLoader.skin);
			lblP2 = new Label(getDisplayName(2),ResourceLoader.skin);
			lblP3 = new Label(getDisplayName(3),ResourceLoader.skin);
			lblP4 = new Label(getDisplayName(4),ResourceLoader.skin);
			
		}else if(Medium.getId().equals("2")){
			lblP1 = new Label(getDisplayName(2),ResourceLoader.skin);
			lblP2 = new Label(getDisplayName(3),ResourceLoader.skin);
			lblP3 = new Label(getDisplayName(4),ResourceLoader.skin);
			lblP4 = new Label(getDisplayName(1),ResourceLoader.skin);
		}else if(Medium.getId().equals("3")){
			lblP1 = new Label(getDisplayName(3),ResourceLoader.skin);
			lblP2 = new Label(getDisplayName(4),ResourceLoader.skin);
			lblP3 = new Label(getDisplayName(1),ResourceLoader.skin);
			lblP4 = new Label(getDisplayName(2),ResourceLoader.skin);
		}else if(Medium.getId().equals("4")){
			lblP1 = new Label(getDisplayName(4),ResourceLoader.skin);
			lblP2 = new Label(getDisplayName(1),ResourceLoader.skin);
			lblP3 = new Label(getDisplayName(2),ResourceLoader.skin);
			lblP4 = new Label(getDisplayName(3),ResourceLoader.skin);
		}
		stage.addActor(lblP1);
		stage.addActor(lblP2);
		stage.addActor(lblP3);
		stage.addActor(lblP4);	
	}
	/**
	 * 
	 * @return
	 */
	private boolean isCardShowable(PutACard p){
		if(Medium.getGameMode().equals(Medium.GAME_MODE_MULTIPLAYER)){
			return GameRule.isCardShowable(Medium.isTrumpOpen(),Medium.getId(),p.id,
					(Medium.getCurrentHandCards().size()==4)?Medium.getPrevFirstCard():Medium.getCurrentFirstCard(),p.card);
		}else{
			return GameRule.isCardShowable(Medium.isTrumpOpen(),Medium.getId(),p.id,
					(Medium.getCurrentHandCards().size()>=3)?Medium.getPrevFirstCard():Medium.getCurrentFirstCard(),p.card);		
		}
	}
	/**
	 * draw imageSprites for cards
	 */
	private void drawCardImageSprites(){
		card1=null;
		card2=null;
		card3=null;
		card4=null;
		for(PutACard p:Medium.getCurrentHandCards().toArray(new PutACard[0])){
			if(p.id.equals("1")){
				if(isCardShowable(p)){
					card1=CardsHandler.getCardImageSprite(p.card);					
				}else{
					card1=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);
				}
				positionCard1();
				if(card1 != null)
					card1.drawImageSprite(batcher);
			}else if(p.id.equals("2")){
				if(isCardShowable(p)){
					card2=CardsHandler.getCardImageSprite(p.card);
				}else{
					card2=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);
				}
				positionCard2();
				if(card2 != null)
					card2.drawImageSprite(batcher);
			}else if(p.id.equals("3")){
				if(isCardShowable(p)){
					card3=CardsHandler.getCardImageSprite(p.card);
				}else{
					card3=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);
				}
				positionCard3();
				if(card3 != null)
					card3.drawImageSprite(batcher);
			}else{
				if(isCardShowable(p)){
					card4=CardsHandler.getCardImageSprite(p.card);
				}else{
					card4=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);
				}
				positionCard4();
				if(card4 != null)
					card4.drawImageSprite(batcher);
				
			}
		}
	}
	
	/**
	 * position all cards smartly
	 */
	private void positionCard1(){
		if(Medium.getId().equals("1")){
			if(card1!=null){
				card1.positionImageSpriteCenterCenter(0f,-100f+CardsHandler.CARD_HEIGHT*0.2f);//b
				card1.setRotationImageSprite(0f);
				card1.createMicroCard();
			}
		}else if(Medium.getId().equals("2")){
			if(card1!=null){
				card1.positionImageSpriteCenterCenter(-CardsHandler.CARD_HEIGHT*0.2f, -100f);//l
				card1.setRotationImageSprite(-90f);
				card1.createMicroCard();
			}
			
		}else if(Medium.getId().equals("3")){
			if(card1!=null){
				card1.positionImageSpriteCenterCenter(0f, -100-CardsHandler.CARD_HEIGHT*0.2f);//t
				card1.setRotationImageSprite(0f);
				card1.createMicroCard();
			}
		}else if(Medium.getId().equals("4")){
			if(card1!=null){
				card1.positionImageSpriteCenterCenter(CardsHandler.CARD_HEIGHT*0.2f, -100f);//r
				card1.setRotationImageSprite(+90f);
				card1.createMicroCard();
			}			
	
		}
	}
	private void positionCard2(){
		if(Medium.getId().equals("1")){
			if(card2!=null){
				card2.positionImageSpriteCenterCenter(CardsHandler.CARD_HEIGHT*0.2f, -100f);//r
				card2.setRotationImageSprite(+90f);
				card2.createMicroCard();
			}
		}else if(Medium.getId().equals("2")){
			if(card2!=null){
				card2.positionImageSpriteCenterCenter(0f,-100f+CardsHandler.CARD_HEIGHT*0.2f);//b
				card2.setRotationImageSprite(0f);
				card2.createMicroCard();
			}
		}else if(Medium.getId().equals("3")){
			if(card2!=null){
				card2.positionImageSpriteCenterCenter(-CardsHandler.CARD_HEIGHT*0.2f, -100f);//l
				card2.setRotationImageSprite(-90f);
				card2.createMicroCard();
			}
		}else if(Medium.getId().equals("4")){
			if(card2!=null){
				card2.positionImageSpriteCenterCenter(0f, -100-CardsHandler.CARD_HEIGHT*0.2f);//t
				card2.setRotationImageSprite(0f);
				card2.createMicroCard();
			}
		}
	}
	private void positionCard3(){
		if(Medium.getId().equals("1")){
			if(card3!=null){
				card3.positionImageSpriteCenterCenter(0f, -100-CardsHandler.CARD_HEIGHT*0.2f);//t
				card3.setRotationImageSprite(0f);
				card3.createMicroCard();
			}
		}else if(Medium.getId().equals("2")){
			if(card3!=null){
				card3.positionImageSpriteCenterCenter(CardsHandler.CARD_HEIGHT*0.2f, -100f);//r
				card3.setRotationImageSprite(+90f);
				card3.createMicroCard();
			}	
		}else if(Medium.getId().equals("3")){
			if(card3!=null){
				card3.positionImageSpriteCenterCenter(0f,-100f+CardsHandler.CARD_HEIGHT*0.2f);//b
				card3.setRotationImageSprite(0f);
				card3.createMicroCard();
			}
		}else if(Medium.getId().equals("4")){
			if(card3!=null){
				card3.positionImageSpriteCenterCenter(-CardsHandler.CARD_HEIGHT*0.2f, -100f);//l
				card3.setRotationImageSprite(-90f);
				card3.createMicroCard();
			}			
		}
	}
	private void positionCard4(){
		if(Medium.getId().equals("1")){
			if(card4!=null){
				card4.positionImageSpriteCenterCenter(-CardsHandler.CARD_HEIGHT*0.2f, -100f);//l
				card4.setRotationImageSprite(-90f);
				card4.createMicroCard();
			}
		}else if(Medium.getId().equals("2")){
			if(card4!=null){
				card4.positionImageSpriteCenterCenter(0f, -100-CardsHandler.CARD_HEIGHT*0.2f);//t
				card4.setRotationImageSprite(0f);
				card4.createMicroCard();
			}		
		}else if(Medium.getId().equals("3")){
			if(card4!=null){
				card4.positionImageSpriteCenterCenter(CardsHandler.CARD_HEIGHT*0.2f, -100f);//r
				card4.setRotationImageSprite(+90f);
				card4.createMicroCard();
			}
		}else if(Medium.getId().equals("4")){
			if(card4!=null){
				card4.positionImageSpriteCenterCenter(0f,-100f+CardsHandler.CARD_HEIGHT*0.2f);//b
				card4.setRotationImageSprite(0f);
				card4.createMicroCard();
			}
		}
	}
	private Vector2 getLocation(String _from, String _to)
	{
		int from=-1,to=-1;
		
		try{
			from=Integer.parseInt(_from);
			to  =Integer.parseInt(_to  );
		}catch(Exception e){}
		
		final Vector2 Left=new Vector2(-1f,0f);
		final Vector2 Right=new Vector2(1f,0f);
		final Vector2 Bottom=new Vector2(0f,-1f);
		final Vector2 Top=new Vector2(0f,1f);
		
		Vector2 Ret=new Vector2(0f,0f);
		

		if(from == 1){
			if(to == 1){
				Ret = Bottom;
			}else if(to == 2){
				Ret = Right;
			}else if(to == 3){
				Ret = Top;
			}else if(to == 4){
				Ret = Left;
			}
		}else if(from == 2){
			if(to == 1){
				Ret = Left;
			}else if(to == 2){
				Ret = Bottom;
			}else if(to == 3){
				Ret = Right;
			}else if(to == 4){
				Ret = Top;
			}			
		}else if(from == 3){
			if(to == 1){
				Ret = Top;
			}else if(to == 2){
				Ret = Left;
			}else if(to == 3){
				Ret = Bottom;
			}else if(to == 4){
				Ret = Right;
			}			
		}else if (from == 4){
			if(to == 1){
				Ret = Right;
			}else if(to == 2){
				Ret = Top;
			}else if(to == 3){
				Ret = Left;
			}else if(to == 4){
				Ret = Bottom;
			}		
		}
		 
		return Ret;
	}

	/**
	 * return if a card is valid 
	 * @return isValid
	 */
	private boolean isValidCard(){
		boolean ret;
		if(Medium.getGameMode().equals(Medium.GAME_MODE_MULTIPLAYER)){
			ret =  GameRule.validateCard(Medium.getCurrentFirstCard(), Medium.getTrumpCard(), Medium.isBidWinner() , Medium.isFirstPlayer(), 
					Medium.isTrumpOpen(),Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()),
					Medium.getPlayerCards());
		}else{
			ret =  GameRule.validateCard((Medium.getCurrentHandCards().size()!=3)?Medium.getCurrentFirstCard():Medium.getPrevFirstCard(),
					Medium.getTrumpCard(), Medium.isBidWinner() , Medium.isFirstPlayer(), 
					Medium.isTrumpOpen(),Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()),
					Medium.getPlayerCards());
		}
		
		if(ret && Medium.isFirstPlayer()){
			Medium.setFirstPlayer(false); 
		}
		
		return ret;
		
	}
	private boolean isTrumpUsable(){
		boolean ret;
		if(Medium.getGameMode().equals(Medium.GAME_MODE_MULTIPLAYER)){
			ret = GameRule.checkTrumpUserbility(Medium.getPlayerCards(),
					(Medium.getCurrentHandCards().size()==4)?Medium.getPrevFirstCard():Medium.getCurrentFirstCard(), 
					Medium.getCardRoundCount(), Medium.isFirstPlayer(),Medium.getTrumpCard());
		}else{
			ret = GameRule.checkTrumpUserbility(Medium.getPlayerCards(),
					(Medium.getCurrentHandCards().size()==3)?Medium.getPrevFirstCard():Medium.getCurrentFirstCard(), 
					Medium.getCardRoundCount(), Medium.isFirstPlayer(),Medium.getTrumpCard());
		}
		return ret;
	}
	public void update (float deltaTime) {
		
		if (Gdx.input.justTouched()){
			if(ScreenStandardHandler.isDialogVisible()) return;
			firstScreen=false;
			v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);			
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,true,stage)) return;
			if(ResourceLoader.bidButton.isTouched(touchX,touchY) && Medium.isPreGameBidding() && Medium.getPlayerCards().getSelectedCardIndex()>-1){
				AudioLoader.play(AudioLoader.clickSound);
				if(Screens.biddingScreen==null) Screens.biddingScreen = new BiddingScreen(game);
				Screens.biddingScreen.init();
				game.setScreen(Screens.biddingScreen);
			}else if(trumpCard!=null && trumpCard.isTouched(touchX, touchY) && Medium.isBidWinner() && isTrumpUsable()
					 && Medium.isTurn()){
				//touch trump card
				//put this card now
				Medium.putACard(Medium.getPlayerCards().getTrumpCard());
				Medium.getPlayerCards().useTrump();
				//goto playing screen
				Medium.setTrumpUsed(true);
				//
			}else{ //check the cards if they are selected
				ImageAssetHandler[] playerCardsSprites = Medium.getPlayerCards().getImageSprites();
				for(int i=0;i<Medium.getPlayerCards().getCurrentDealtCardCount();i++){
					if(playerCardsSprites[i].isTouchedSpecial(touchX, touchY,space) && i<Medium.getPlayerCards().getCurrentDealtCardCount()-1){ 
						AudioLoader.play(AudioLoader.clickSound);
						if(!Medium.isPreGameBidding() &&  i==Medium.getPlayerCards().getSelectedCardIndex() && Medium.isTurn()){
							if(isValidCard()){
								Medium.putACard(Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));								
							}
						}
						Medium.getPlayerCards().setSelectedCardIndex(i);
					}else if(playerCardsSprites[i].isTouched(touchX, touchY) && i==Medium.getPlayerCards().getCurrentDealtCardCount()-1){
						AudioLoader.play(AudioLoader.clickSound);
						if(!Medium.isPreGameBidding() &&  i==Medium.getPlayerCards().getSelectedCardIndex() && Medium.isTurn()){
							if(isValidCard()){
								Medium.putACard(Medium.getPlayerCards().getCardByIndex(Medium.getPlayerCards().getSelectedCardIndex()));
							}
						}
						Medium.getPlayerCards().setSelectedCardIndex(i);
					}			
				}
			}
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		
		batcher.begin();	
		ScreenStandardHandler.initStandardScreen(batcher);
		
		ResourceLoader.tableImage.positionImageSpriteCenterCenter(0f, -100f);		
		ResourceLoader.tableImage.drawImageSprite(batcher);
		
		float x=-((Medium.getPlayerCards().getCardSetLength(space)/2.0f)-CardsHandler.CARD_WIDTH/2.0f);
		float y= ((ScreenRatioCalculator.screenHeight/2f)-(CardsHandler.CARD_HEIGHT/2f)-70f);
		
		if(firstScreen){
			for(int i=0;i<Medium.getPlayerCards().getCurrentDealtCardCount();i++){
				backCard.positionImageSpriteCenterCenter(x,y);
				backCard.drawImageSprite(batcher);
				x+=space;
			}
		}else{
			
			if(Medium.isPreGameBidding()){
				if(Medium.getPlayerCards().getSelectedCardIndex()>-1 && !Medium.isBidPlaced()){
					ResourceLoader.bidButton.drawImageSprite(batcher);
				}
			}
			ImageAssetHandler[] playerCardsSprites = Medium.getPlayerCards().getImageSprites();
			
			if(Medium.isBidWinner() && !Medium.isTrumpOpen() &&!Medium.isTrumpUsed()){
				x-=CardsHandler.CARD_WIDTH-35f;
			}
			
			for(int i=0;i<Medium.getPlayerCards().getCurrentDealtCardCount();i++){
				if(i==Medium.getPlayerCards().getSelectedCardIndex()){
					playerCardsSprites[i].positionImageSpriteCenterCenter(x,y-20.0f); //--
					if(!Medium.isTurn() && !Medium.isPreGameBidding()){
						outerGlowRedCard.positionImageSpriteCenterCenter(x+2,y-18.0f);
						outerGlowRedCard.drawImageSprite(batcher);
					}else{
						outerGlowCard.positionImageSpriteCenterCenter(x+2,y-18.0f);
						outerGlowCard.drawImageSprite(batcher);						
					}
				}else{
					playerCardsSprites[i].positionImageSpriteCenterCenter(x,y);
				}
				playerCardsSprites[i].drawImageSprite(batcher);
				x+=space;
			}
			
			if(Medium.isBidWinner()){
				x+=CardsHandler.CARD_WIDTH;
				trumpCard=CardsHandler.getCardImageSprite(Medium.getPlayerCards().getTrumpCard());	
				if(trumpCard!=null){
					trumpCard.positionImageSpriteCenterCenter(x, y);
					outerGlowYlCard.positionImageSpriteCenterCenter(x+2, y+2);
					trumpCard.drawImageSprite(batcher);
					outerGlowYlCard.drawImageSprite(batcher);
				}
			}
		}
		
		if(Medium.getCurrentHandCards().size()==4){
			
			if(animation==0){
				
				dt=1f;
				animation=1;
				
			}else if(animation==1){
				
				dt-=deltaTime;
				if(dt<=0f){
					dt=1.5f;
					animation=2;
				}
				drawCardImageSprites();	
				
			}else if(animation==2){
				
				dt-=deltaTime;				
				Vector2 location= getLocation(Medium.getId(),Medium.getRoundWinner());
				
				
				mov++;
				
				positionCard1();
				if(card1!=null){
					card1.moveImageSprite(location.x*mov,location.y*mov);
					card1.rotateImageSprite(1f*mov);
					card1.drawImageSprite(batcher);
				}
				positionCard2();
				if(card2!=null){
					card2.moveImageSprite(location.x*mov,location.y*mov);
					card2.rotateImageSprite(1f*mov);
					card2.drawImageSprite(batcher);
				}
				positionCard3();
				if(card3!=null){
					card3.moveImageSprite(location.x*mov,location.y*mov);
					card3.rotateImageSprite(1f*mov);
					card3.drawImageSprite(batcher);
				}
				positionCard4();
				if(card4!=null){
					card4.moveImageSprite(location.x*mov,location.y*mov);
					card4.rotateImageSprite(1f*mov);
					card4.drawImageSprite(batcher);
				}
				
				if(dt<=0f){
					mov=0;
					animation=3;
				}
				
			}
			
		}else{
			
			animation=0;
			drawCardImageSprites();
			
		}	
		
		ScreenRatioCalculator.moveActorCenterCenter(lblP1, 0f, -50f);
		ScreenRatioCalculator.moveActorCenterCenter(lblP4, -160f, +100f);
		ScreenRatioCalculator.moveActorCenterCenter(lblP2,+160f, +100f);
		ScreenRatioCalculator.moveActorCenterCenter(lblP3,0f, +250f);
		

		batcher.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();		
		
		if(Medium.getGameMode().equals(Medium.GAME_MODE_SINGLEPLAYER) && (animation==0||animation==3)){
			AIBase.getAIcards();
		}
	}
	

	@Override
	public void render (float delta) {
		ResourceLoader.bidButton.positionImageSpriteCenterCenterR(-241.0f,216.0f);
		backCard=CardsHandler.getCardImageSprite(CardsHandler.CARD_BACK);
		outerGlowCard=ResourceLoader.outerGlow;
		outerGlowRedCard=ResourceLoader.outerGlowRed;
		outerGlowYlCard=ResourceLoader.outerGlowYellow;
		try{
			if(this.showtrump){			
				showtrump=false;
				AudioLoader.vibrate();
				ScreenStandardHandler.setDialogVisible(true);
				Dialog error=new Dialog("G304", ResourceLoader.largeSkin, "dialog") {
		    		protected void result (Object object) {
		    			AudioLoader.play(AudioLoader.clickSound);
		    			ScreenStandardHandler.setDialogVisible(false);
		    			this.hide();    			    			
		    		}
				};
				error.text(ResourceLoader.locale.get("TrumpOpen")).pack();
				error.add(CardsHandler.getCardImageSprite(Medium.getTrumpCard()).createScene2DImage());
				error.pack();
				
				error.button(ResourceLoader.locale.get("Ok"), true).key(Keys.ENTER, true)
		    		.key(Keys.ESCAPE, false).pad(20f).pack();
		    	error.show(stage);
			}
		}catch(Exception e){}
		update(delta);
		draw(delta);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		batcher.dispose();
	}

}
