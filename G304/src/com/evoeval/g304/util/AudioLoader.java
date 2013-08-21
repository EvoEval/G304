package com.evoeval.g304.util;

import com.badlogic.gdx.Gdx;

/**
 * 
 * This class is used to load the audio that are needed by the game
 * @author Bhathiya
 *
 * [Log]
 * 5:55 PM 6/5/2013 created , Bhathiya
 */
public class AudioLoader {
	
	private static final String _dealCardsSound = "dealCardsSound.mp3";
	private static final String _putCardSound = "putCardSound.mp3";
	private static final String _shuffleSound = "shuffleSound.mp3";
	private static final String _clickSound = "click.mp3";
	private static final String _win = "win.mp3";
	private static final String _loose = "lose.mp3";	
	public static MusicClip dealCardsSound,putCardSound,shuffleSound,clickSound,winSound,loseSound;
	
	
	/**
	 * 
	 * this is used to load the audio to the memory must be called by the main class
	 * must be called by both server and client
	 * 
	 */
	public static void init(){
		dealCardsSound = new MusicClip("data/sounds/"+_dealCardsSound);
		putCardSound = new MusicClip("data/sounds/"+_putCardSound);
		shuffleSound = new MusicClip("data/sounds/"+_shuffleSound);
		clickSound = new MusicClip("data/sounds/"+_clickSound);
		winSound = new MusicClip("data/sounds/"+_win);
		loseSound = new MusicClip("data/sounds/"+_loose);		
	}
	
	
	/**
	 * play given music
	 * example=> 
	 * AL.play(AL.dealCardsSound); 
	 * @param music music object
	 */
	public static void play(MusicClip music){
		music.play();
	}
	
	public static void vibrate(){
		if(Medium.isVibrationEnabled()){
			Gdx.input.vibrate(400);
		}
	}
	
	public static void dispose(){
		dealCardsSound.dispose();
		putCardSound.dispose();
		shuffleSound.dispose();
		clickSound.dispose();
		winSound.dispose();
		loseSound.dispose();
	}
	
	
}
