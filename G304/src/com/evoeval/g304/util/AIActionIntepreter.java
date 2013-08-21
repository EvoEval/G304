package com.evoeval.g304.util;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.*;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;

/**
 *
 * @author Bhathiya
 */
public class AIActionIntepreter {
    
    private static String last="";
	public static HashMap<String, String> jexlCodes=null;
	
	
	
	public static void init(){
		jexlCodes = loadScripts();
	}	
	
    //---------------------------------------------------------------------------------\
    //									AI XML
    //---------------------------------------------------------------------------------\
    public static String getAIXMLDecision(HashMap<String,String> hash){
        XmlReader x = new XmlReader();        
        Element el;
		try {
			
			if(Medium.getAILoadLocal() && Gdx.files.external("G304update/AIBasic.xml").exists()){
				el = x.parse(Gdx.files.external("G304update/AIBasic.xml"));
			}else{
				el = x.parse(Gdx.files.internal("data/xml/AIBasic.xml"));
			}
			
			return recursiveGet(hash, el);
		} catch (Exception e) {
			e.printStackTrace();
		}        
       return null;
    }  
    
    private static String recursiveGet(HashMap<String,String> hash,Element el){
        int count = el.getChildCount();
        
        for(int i=0;i<count;i++){             
             Element level_element = el.getChild(i);             
             String stype = level_element.getName();
             
             if(stype.equals("check")){
                 last = hash.get(level_element.get("variable"));
                 return recursiveGet(hash,level_element);
             }else if(stype.equals("validate") ){
                 String value = level_element.get("value");
                 if(value.equals(last)){
                     return recursiveGet(hash,level_element);
                 }
             }else if(stype.equals("return")){
                 return level_element.get("out");
             }             
         }
        
        return "";
    }

    public static class TagValidate{
        String var;
        String type;
    } 
    
    //---------------------------------------------------------------------------------\
    //									JEXL
    //---------------------------------------------------------------------------------\
    private static HashMap<String,String> loadScripts(){
        HashMap<String,String> hash = new HashMap<String, String>();
        XmlReader x = new XmlReader();        
        Element el;
		try {
			
			if(Medium.getAILoadLocal() && Gdx.files.external("G304update/JexlScripts.xml").exists()){
				el = x.parse(Gdx.files.external("G304update/JexlScripts.xml"));
			}else{
				el = x.parse(Gdx.files.internal("data/xml/JexlScripts.xml"));
			}
			
		    @SuppressWarnings("rawtypes")
			Iterator iterator_level = el.getChildrenByName("function").iterator();
	        while(iterator_level.hasNext()){
	             
	             Element level_element = (XmlReader.Element)iterator_level.next();
	             
	             String name = level_element.getAttribute("name", "");
	             hash.put(name, level_element.getText());
	             
	         }
	        return hash;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		return null;
    }
    public static String runJexlScript(JexlContext jc,String script){
    	JexlEngine jexl = new JexlEngine();
    	Script scr = jexl.createScript(script);
    	return (String)(scr.execute(jc));
    	
    }
    public static String generateHashMapforXML(HashMap<String,String> hash,JexlContext jc){    	
    	return runJexlScript(jc,jexlCodes.get("startup"));
    }
    public static String runScriptFunction(JexlContext jc,String functionName){
    	return runJexlScript(jc,jexlCodes.get(functionName));
    }
}
