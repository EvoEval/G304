package com.evoeval.g304.util;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.evoeval.g304.ui.ImageAssetHandler;

/**
 * 
 * Use reflection api to save data to  a xml file
 * 
 * also load data from  a xml file
 * 
 * @author Bhathiya
 *
 */
public class XMLReflector {
	
	public static void GenerateXMLFromObject(Object object){
		 StringWriter writer = new StringWriter();
		 @SuppressWarnings("resource")
		 XmlWriter xml = new XmlWriter(writer);
		 Class<?> c = object.getClass();
		 Field fs[] = c.getFields();					
		 try {			 
			xml = xml.element("GraphicsLoader");			
			for(Field f:fs){
				if(f.getType().equals(ImageAssetHandler.class)){
					boolean isNormal = !ReadMemberBoolean(f.get(object),"normalDispose");
					if(isNormal){
						xml = xml.element("ImageSprite")
								.attribute("name", f.getName())
								.attribute("x", ReadMemberFloatAsString(f.get(object),"_x"))
								.attribute("y",ReadMemberFloatAsString(f.get(object),"_y"))
								.attribute("w",ReadMemberFloatAsString(f.get(object),"_width"))
								.attribute("h",ReadMemberFloatAsString(f.get(object),"_height"))
								.attribute("image",ReadMemberString(f.get(object),"_imagePath"))
									.pop();
					}
								
				}
			}
			xml.pop();
			System.out.println(writer);
			xml.close();
		} catch (Exception e) {
		}		 
	}
	
	
	public static boolean ReadMemberBoolean(Object object,String fieldName){
		
		if(object==null) return false;
		Class<?> c = object.getClass();
		boolean bool=false;
		try {
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			bool=f.getBoolean(object);
		} catch (Exception e) {
		}
		
		return bool;
	}
	
	public static String ReadMemberFloatAsString(Object object,String fieldName){
		
		if(object==null) return "-";
		Class<?> c = object.getClass();
		String str="-";
		try {
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			str=(new Float(f.getFloat(object))).toString();
		} catch (Exception e) {
		}
		
		return str+"f";
	}
	
	public static String ReadMemberString(Object object,String fieldName){
		
		if(object==null) return "";
		Class<?> c = object.getClass();
		String str="";
		try {
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			str=(String) f.get(object);
		} catch (Exception e) {
		}
		
		return str;
	}	
	
	@SuppressWarnings("rawtypes")
	public static void loadGraphics(Object object,String xmlPath){
		XmlReader xml = new XmlReader();
		XmlReader.Element xml_element;
		Iterator iterator_level;
		Class<?> c = object.getClass();
		try {
			xml_element = xml.parse(Gdx.files.internal(xmlPath));
			iterator_level = xml_element.getChildrenByName("ImageSprite").iterator();
			while(iterator_level.hasNext()){
			     XmlReader.Element level_element = (XmlReader.Element)iterator_level.next();
			     String x = level_element.getAttribute("x");
			     String y = level_element.getAttribute("y");
			     String w = level_element.getAttribute("w");
			     String h = level_element.getAttribute("h");
			     String img = level_element.getAttribute("image");
			     String nam = level_element.getAttribute("name");
			     Field f = c.getDeclaredField(nam);
			     f.set(object, new ImageAssetHandler(
			    		 img,
			    		 Float.parseFloat(w),
			    		 Float.parseFloat(h),
			    		 Float.parseFloat(x),
			    		 Float.parseFloat(y),
			    		 false
			    		 ));
			 }
			
		} catch (Exception e) {
			System.out.println(" " + e.getMessage());
		}
	}
	
}
