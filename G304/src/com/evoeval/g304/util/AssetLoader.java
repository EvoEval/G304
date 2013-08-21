package com.evoeval.g304.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

/**
 * 
 * used to load music and textures
 * @author Bhathiya
 *
 */
public class AssetLoader {
	private static AssetManager manager=null;
	/**
	 * initialize the AssetManager must be called before creating ImageSprite Objects
	 */
	public static AssetManager getAssetManager(){
		if(manager==null){
			manager   = new AssetManager();
			manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
			manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));
		}
		return manager;
	}
}
