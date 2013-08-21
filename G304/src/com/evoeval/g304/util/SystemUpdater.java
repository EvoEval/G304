package com.evoeval.g304.util;

import java.io.InputStream;
import java.net.URL;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * update ai
 * @author Bhathiya
 *
 */
public class SystemUpdater {

	private static void saveFileToLocal(String fileURL) {
		try {
			String downloadedFileName = fileURL.substring(fileURL.lastIndexOf("/")+1);
			
	        URL url = new URL(fileURL);
	        InputStream is = url.openStream();
	        FileHandle fhandle = Gdx.files.external("G304update/" + downloadedFileName);
	        
	        byte[] buffer = new byte[4096];
	        int bytesRead = 0;
	        
	        
	        while ((bytesRead = is.read(buffer)) != -1) {
	        	System.out.println("Downloading..");
	        	fhandle.writeBytes(buffer, 0, bytesRead, true);
	        }
       
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void updateAI(){
		System.out.println("External Storage :" + Gdx.files.getExternalStoragePath() + " " + Gdx.files.isExternalStorageAvailable());
		if(Gdx.files.isExternalStorageAvailable()){
			saveFileToLocal("http://g304.evoeval.com/g304/update/AIBasic.xml");
			saveFileToLocal("http://g304.evoeval.com/g304/update/JexlScripts.xml");
			Medium.setAIAsUpdated();
		}
	}
}