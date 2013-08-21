import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class ServiceHandler {
	private static String SOAP_ACTION;    
    private static String OPERATION_NAME;    
    private static final String WSDL_TARGET_NAMESPACE = "http://xxx.xxxx.com/";    
    private static final String SOAP_ADDRESS = "http://xxx.xxxx.com/WebService.asmx";
    
    /**
     * call WebMethod
     * 
     * @param param1 
     * @param param2 
     * 
     * @return true if login successful
     */
    public static String callWebMethod(String param1,String param2){
    	    	
    	OPERATION_NAME = "myFunc";
    	SOAP_ACTION = "http://xxx.xxxx.com/myFunc";
    	
    	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
		
        request.addProperty("param1",param1);
        request.addProperty("param2",param2);
                
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        
        envelope.setOutputSoapObject(request);
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        
        try{                  
	        httpTransport.call(SOAP_ACTION, envelope);      
	        SoapObject object = (SoapObject) envelope.getResponse();
	        
        }catch(Exception exception){
        	System.out.println(exception.toString());
        	return "";        	
        }
    }
    
    
}
