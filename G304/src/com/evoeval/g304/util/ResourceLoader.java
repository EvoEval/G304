package com.evoeval.g304.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.evoeval.g304.cards.CardsHandler;
import com.evoeval.g304.multiplayer.G304NetworkHandler;
import com.evoeval.g304.ui.ImageAssetHandler;

/**
 * This class is used to load the graphics that are needed by the game
 * @author Bhathiya Perera
 *
 */
public class ResourceLoader { 
	
	public static ImageAssetHandler btnProfessional;
	public static ImageAssetHandler btnNovice;
	public static ImageAssetHandler clientButton;
	public static ImageAssetHandler serverButton;
	public static ImageAssetHandler okButton;
	public static ImageAssetHandler continueButton;
	public static ImageAssetHandler btnClose;
	public static ImageAssetHandler connectButton;
	
	public static ImageAssetHandler handButton;	
	public static ImageAssetHandler bidButton;
	
	public static ImageAssetHandler bid60Button; 
	public static ImageAssetHandler bid70Button;
	public static ImageAssetHandler bid80Button;
	public static ImageAssetHandler bid90Button;
	public static ImageAssetHandler bid100Button;
	public static ImageAssetHandler bid110Button;
	public static ImageAssetHandler bid120Button;
	public static ImageAssetHandler bid130Button; 
	
	public static ImageAssetHandler welcomeImage;
	public static ImageAssetHandler emptyCardSlot;
	public static ImageAssetHandler outerGlow;
	public static ImageAssetHandler outerGlowRed;
	public static ImageAssetHandler outerGlowYellow;
	public static ImageAssetHandler btnSilent;
	public static ImageAssetHandler btnEnableSound;
	public static ImageAssetHandler btnVibration;
	public static ImageAssetHandler btnDisableVibration;
	public static ImageAssetHandler btnUpdate;
	public static ImageAssetHandler btnLoginEnabled;
	public static ImageAssetHandler btnLoginDisabled;
	public static ImageAssetHandler redDot;
	public static ImageAssetHandler dotSlot;
	public static ImageAssetHandler tableImage;
	
	public static ImageAssetHandler singlePlayerButton;
	public static ImageAssetHandler rankButton;
	public static ImageAssetHandler multiplayerButton;
	public static ImageAssetHandler optionsButton;
	public static ImageAssetHandler helpButton;
	public static ImageAssetHandler statButton;
	
	public static ImageAssetHandler loginButton;
	

	public static ImageAssetHandler ratingStarSlot;
	public static ImageAssetHandler ratingStarGold;
	public static ImageAssetHandler ratingStarSilver;
	public static ImageAssetHandler ratingStarBronze;
	
	public static ImageAssetHandler rankIron;
	public static ImageAssetHandler rankBronz;
	public static ImageAssetHandler rankSilver;
	public static ImageAssetHandler rankGold;
	
	public static ImageAssetHandler gameOverVictory;
	public static ImageAssetHandler gameOverDefeat;	
	
	public static ImageAssetHandler btnLanguageEnglish;
	public static ImageAssetHandler btnLanguageEnglishDisabled;
	public static ImageAssetHandler btnLanguageSinhala;
	public static ImageAssetHandler btnLanguageSinhalaDisabled;
	
	public static ImageAssetHandler btnMetroTheme;
	public static ImageAssetHandler btnMetroThemeDisabled;
	public static ImageAssetHandler btnMetalTheme;
	public static ImageAssetHandler btnMetalThemeDisabled;
	
	public static ImageAssetHandler btnHelpHelp;
	public static ImageAssetHandler btnHelpAbout;
	
	private static ImageAssetHandler hlpPoints;
	private static ImageAssetHandler hlpTitle;
		
	public static Skin skin;
	public static Skin largeSkin;
	
	public static HashMap<String, String> locale=null;
	public static HashMap<String, ImageAssetHandler> helpImages=null;
	/**
	 * load images,including cards
	 */
	public static void init(){
		
		DrawText.init();
					
		CardsHandler.loadCardImages(); 
		CardsHandler.initializeDeck();
		Medium.setCurrentHandCards(new ArrayList<G304NetworkHandler.PutACard>());
		skin = new Skin(Gdx.files.internal("data/gothskin.json"));
		largeSkin = new Skin(Gdx.files.internal("data/largeskin.json"));
		XMLReflector.loadGraphics(new ResourceLoader(),"data/xml/GraphicsLoader_" + Medium.getXMLPostfix() + ".xml");
		helpImages = new HashMap<String, ImageAssetHandler>();
		
		hlpPoints = new ImageAssetHandler("data/help/values.png", 272f, 120f, 0f, 0f, false);
		if(Medium.getGameTheme().equals("Metro")){
			hlpTitle = new ImageAssetHandler("data/help/helpTitleMetro.png", 272f, 86f, 0f, 0f, false);
		}else{
			hlpTitle = new ImageAssetHandler("data/help/helpTitleMetal.png", 272f, 86f, 0f, 0f, false);
		}
		
		
		helpImages.put("Points", hlpPoints);
		helpImages.put("Welcome", hlpTitle);
		
		locale = loadLanguage(Medium.getGameLanguage());
		AIActionIntepreter.init();
		
		
	}
	
	
	private static HashMap<String,String> loadLanguage(String Lang){
        HashMap<String,String> hash = new HashMap<String, String>();
        XmlReader x = new XmlReader();        
        Element el;
		try {
			el = x.parse(new String(Gdx.files.internal("data/xml/Local.xml").readBytes(),"UTF-8"));
		    @SuppressWarnings("rawtypes")
			Iterator iterator_level = el.getChildrenByName("Text").iterator();
	        while(iterator_level.hasNext()){
	             
	             Element level_element = (XmlReader.Element)iterator_level.next();
	             
	             String slang = level_element.getAttribute("lang", "");
	             String stext = level_element.getAttribute("txt" , "");            
	             String stag  = level_element.getAttribute("tag" , ""); 
	             
	             if(slang.equals(Lang)){
	                 hash.put(stag, stext.replace("$", "\n"));	
	             }
	         }
	        return hash;
		} catch (Exception e) {
			
		} 
		return null;
    }

	/**
	 * dispose all images
	 */
	public static void dispose(){
		
		btnProfessional.dispose();
		btnNovice.dispose();
		clientButton.dispose();
		serverButton.dispose();
		okButton.dispose();
		continueButton.dispose();
		connectButton.dispose();
		handButton.dispose();
		bidButton.dispose();
		
		bid60Button.dispose();
		bid70Button.dispose();
		bid80Button.dispose();
		bid90Button.dispose();
		bid100Button.dispose();
		bid110Button.dispose();
		bid120Button.dispose();
		bid130Button.dispose();	
		

		welcomeImage.dispose();
		emptyCardSlot.dispose();
		outerGlow.dispose();
		outerGlowRed.dispose();
		outerGlowYellow.dispose();
		
		btnSilent.dispose();
		btnEnableSound.dispose();
		btnVibration.dispose();
		btnDisableVibration.dispose();
		btnUpdate.dispose();
		btnLoginEnabled.dispose();
		btnLoginDisabled.dispose();
		
		redDot.dispose();
		dotSlot.dispose();
		tableImage.dispose();
		
		singlePlayerButton.dispose();
		rankButton.dispose();
		multiplayerButton.dispose();
		optionsButton.dispose();
		helpButton.dispose();
		statButton.dispose();
		
		loginButton.dispose();
		
		ratingStarSlot.dispose();
		ratingStarGold.dispose();
		ratingStarSilver.dispose();
		ratingStarBronze.dispose();
		
		rankIron.dispose();
		rankBronz.dispose();
		rankSilver.dispose();
		rankGold.dispose();
		
		skin.dispose();		
        
		gameOverVictory.dispose();
		gameOverDefeat.dispose();
		
		btnLanguageEnglish.dispose();
		btnLanguageEnglishDisabled.dispose();
		btnLanguageSinhala.dispose();
		btnLanguageSinhalaDisabled.dispose();
		
		btnMetroTheme.dispose();           
		btnMetroThemeDisabled.dispose();   
		btnMetalTheme.dispose();           
		btnMetalThemeDisabled.dispose();   
		
		DrawText.dispose();
		CardsHandler.dispose(); 
		
		btnHelpAbout.dispose();
		btnHelpHelp.dispose();
		
		hlpPoints.dispose();
		hlpTitle.dispose();
		
	}
}
