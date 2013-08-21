package com.evoeval.g304.util;

import com.badlogic.gdx.audio.Music;

/**
 * Use to store and handle music
 * @author Bhathiya
 * 
 * [Log]
 * 5:55 PM 6/5/2013 created , Bhathiya
 */
public class MusicClip {
	private Music music;
	private boolean justQueued;
	private String _path;
	public boolean createMusic(){
		if(justQueued && AssetLoader.getAssetManager().isLoaded(_path)){
			music = AssetLoader.getAssetManager().get(_path, Music.class);
			justQueued=false;
		}
		return !justQueued;
	}
	public void play(){
		if(createMusic() && Medium.isSoundEnabled()){
			music.play();
		}
	}
	public MusicClip(String path){
		_path=path;
		justQueued=true;
		AssetLoader.getAssetManager().load(_path, Music.class);
	}
	public void dispose(){
		AssetLoader.getAssetManager().unload(_path);
	}
}
