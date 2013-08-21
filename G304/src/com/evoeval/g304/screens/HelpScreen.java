package com.evoeval.g304.screens;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.XmlReader;
import com.evoeval.g304.G304Main;
import com.evoeval.g304.ui.ScreenStandardHandler;
import com.evoeval.g304.util.DrawText;
import com.evoeval.g304.util.Medium;
import com.evoeval.g304.util.ResourceLoader;
import com.evoeval.g304.util.ScreenRatioCalculator;


/**
 * This is the help screen 
 * 
 * @author Bhathiya Perera
 *
 */
public class HelpScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float touchX=0f,touchY=0f;
	Stage stage=null;
	/**
	 * this will initialize the position buttons and the list
	 * @param game Game
	 */
	public HelpScreen (Game game) {
		this.game = game;
		guiCam = G304Main.camera;
		batcher = new SpriteBatch();	
		ResourceLoader.btnHelpHelp.positionImageSpriteCenterCenter(-42f, 275f);
		ResourceLoader.btnHelpAbout.positionImageSpriteCenterCenter(42f, 275f);
		init();
	}
	@SuppressWarnings("rawtypes")
	private void loadHelp(Table tblHelp){
		XmlReader xml = new XmlReader();
		XmlReader.Element xml_element;
		Iterator iterator_level;
		LabelStyle title = new LabelStyle(DrawText.getFontTitle(), Color.WHITE);
		try {
			if(Medium.getGameLanguage().equals("English")){
				xml_element = xml.parse(Gdx.files.internal("data/help/Help.xml"));
			}else{
				xml_element = xml.parse(new String(Gdx.files.internal("data/help/HelpSinhala.xml").readBytes(),"UTF-8"));
			}
			iterator_level = xml_element.getChildrenByName("line").iterator();
			while(iterator_level.hasNext()){
			     XmlReader.Element level_element = (XmlReader.Element)iterator_level.next();
			     String type = level_element.getAttribute("type");
			     String data = level_element.getAttribute("data");
			     if(type.equals("normal")){
			    	Label lbl = new Label(data, ResourceLoader.skin);
					tblHelp.add(lbl);
			     }else if(type.equals("title")){
			    	Label lbl = new Label(data, title);
					tblHelp.add(lbl);
			     }else if(type.equals("image")){
			    	tblHelp.add(ResourceLoader.helpImages.get(data).createScene2DImage());
			     }else if(type.equals("empty")){
			    	Label lbl = new Label("\n", ResourceLoader.skin);
					tblHelp.add(lbl);						
			     }
			     tblHelp.pack();
			     
			 }
			
		} catch (Exception e) {
			//System.out.println(" " + e.getMessage());
		}
	}
	public void init(){
		
		Table table=new Table();

		ScrollPane scrollPaneStat = new ScrollPane(table, ResourceLoader.skin); //manually add a scroll pane
		scrollPaneStat.setBounds(0f, 0f, 440f, 500f);//set scroll pane+list size
		ScreenRatioCalculator.moveActorCenterCenter(scrollPaneStat, 0f, 25f); //position scroll pane+list
		loadHelp(table);	
		if(stage==null){
			stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);		
		}else{
			stage.clear();
		}
		Gdx.input.setInputProcessor(stage);
		ScreenStandardHandler.setDialogVisible(false);
		stage.addActor(scrollPaneStat);
		
	}
	/**
	 * 
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if(Gdx.input.justTouched()){
			Vector3 v3 = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			guiCam.unproject(v3);
			touchX=ScreenRatioCalculator.unmapX(v3.x);
			touchY=ScreenRatioCalculator.unmapY(v3.y,0);
			if(ScreenStandardHandler.backButtonListner(touchX, touchY,game,false,null)) return;
			if(ResourceLoader.btnHelpHelp.isTouched(touchX,touchY)){
				
			}else if(ResourceLoader.btnHelpHelp.isTouched(touchX,touchY)){
				
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
		ResourceLoader.btnHelpHelp.drawImageSprite(batcher);
		ResourceLoader.btnHelpAbout.drawImageSprite(batcher);
		batcher.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
	}

	@Override
	public void render (float delta) {

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
