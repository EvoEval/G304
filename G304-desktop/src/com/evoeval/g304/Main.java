package com.evoeval.g304;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable=false;
		config.width = 480;
		config.height = 690;
		config.title = "EvoEval:G304";
//		new Thread() { 
//			public void run() {
//				new Tester().setVisible(true);
//				}
//		}.start();
		new LwjglApplication(new G304Main(), config);
		
	}
}
