package com.evoeval.g304.util;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.support.igd.PortMappingListener;
import org.teleal.cling.support.model.PortMapping;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;


/**
 *
 * @author Chamath Mc
 */
public class UPnP {
    
    private static UpnpService upnpService;
    
    public static void ForwardPort(PortMapping.Protocol protocol, int portNumber, String description) throws UnknownHostException{
    	if(Gdx.app.getType()==ApplicationType.Desktop){
	        String InternalIP;
			try {
				InternalIP = getLocalAddress();
		        PortMapping desiredMapping = new PortMapping(portNumber, InternalIP, protocol,description);
		        upnpService = new UpnpServiceImpl(new PortMappingListener(desiredMapping));
		        upnpService.getControlPoint().search();
			} catch (Exception e) {
	
			}    
    	}
    }
    
    public static void ShutDown(){
    	if(Gdx.app.getType()==ApplicationType.Desktop){
    		upnpService.shutdown();
    	}
    }
    
	/**
	 * get the ip address of current device
	 * @return ip address as string
	 * @throws Exception IOEsception/ScocketException
	 */
	public static String getLocalAddress() throws Exception{
    	if(Gdx.app.getType()==ApplicationType.Android){
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostName();
                    }
                }
            }
    	}else{
    		return InetAddress.getLocalHost().getHostAddress();
    	}

        return null;
    }
}
