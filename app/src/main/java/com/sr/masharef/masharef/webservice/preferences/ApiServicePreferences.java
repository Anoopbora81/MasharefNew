package com.sr.masharef.masharef.webservice.preferences;

import android.content.Context;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.JResponseError;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.preferences.Preferences;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;


public class ApiServicePreferences {

	public enum ServerUrlProtocolType{
		http, https
	}
	
	public enum ServerUrlCertificateAuthority{
		CertifiedAuthority, SelfSigned
	}
	
	public static ServerUrlCertificateAuthority getCertificateAuthority(Context ctx){
		
		String certAuthStr 						= new Preferences(ctx).get(Constants.kApiCA);
    	ServerUrlCertificateAuthority certAuth 	= ServerUrlCertificateAuthority.CertifiedAuthority;
    	
    	try{
			certAuth = ServerUrlCertificateAuthority.valueOf(certAuthStr);
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At getCertificateAuthority(...) of ApiServicePreferences");
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At getCertificateAuthority(...) of ApiServicePreferences");
		}
		
    	return certAuth;
		
	}
	
	public static ServerUrlProtocolType getServerProtocol(Context ctx){
    	
		String protocolStr 				= new Preferences(ctx).get(Constants.kApiProtocol);
    	//ServerUrlProtocolType protocol 	= ServerUrlProtocolType.https;
		ServerUrlProtocolType protocol 	= ServerUrlProtocolType.http;
    	
    	try{
    		if(protocolStr.isEmpty()){
    			//TODO nothing here as no protocol found.
    		}
    		else{
    			protocol = ServerUrlProtocolType.valueOf(protocolStr);
    		}	
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At getServerProtocol(...) of ApiServicePreferences");
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At getServerProtocol(...) of ApiServicePreferences");
		}
		
    	return protocol;
    }
	
	    
    public static String getServerUrl(Context ctx){
    	return getServerUrl(ctx, true);
    }

    public static boolean isDefaultServer(Context ctx){
        return getServerUrl(ctx, true).equals(getDefaultUrl(ctx));
    }
    
    public static String getServerUrl(Context ctx, boolean returnDefault){
    	String serverUrl = new Preferences(ctx).get(Constants.kApiUrl);
    	if(serverUrl.length() == 0 && returnDefault){
    		serverUrl = getDefaultUrl(ctx); 
    	}
    	return serverUrl;
    }
    
    public static String getGroupID(Context ctx){
    	String groupID = new Preferences(ctx).get(Constants.kGroupID);
    	return groupID;
    }
    
    public static String getForgetPassUrl(Context ctx){
    	return getForgetPassUrl(ctx, true);
    }
    
    public static String getForgetPassUrl(Context ctx, boolean returnDefault){
    	
    	Preferences pref = new Preferences(ctx);
    	String fpURL = pref.get(Constants.kForgetPassURL);
    	//if (pref.contains(Constants.kForgetPassURL) && pref.get(Constants.kForgetPassURL) != null && !(pref.get(Constants.kForgetPassURL).equals("")) && returnDefault) {
    	if (fpURL.length() == 0 && returnDefault) {
    		return getDefaultForgetPasswordUrl(ctx);
		}
    	
    	return fpURL;
    	
    }
    
    public static Exception savePreferences(Context ctx, ServerUrlProtocolType protocol, ServerUrlCertificateAuthority certAuth, String url, String groupID, String fpUrl){
    	
    	Exception error = validatePreferences(ctx, protocol, certAuth, url,fpUrl);
    	
    	if(error == null){
	    	Preferences pref = new Preferences(ctx);
	    	pref.set(Constants.kApiUrl, url);
	    	pref.set(Constants.kApiProtocol, (protocol == null)?null:protocol.toString());
	    	pref.set(Constants.kApiCA, certAuth.toString());
	    	pref.set(Constants.kGroupID, groupID);
	    	pref.set(Constants.kForgetPassURL, fpUrl);
	    	pref.commit();
    	}
    	else{
    		//TODO nothing here as validation failed.
    	}
    	
    	return error;
    }

    private static String getDefaultUrl(Context ctx){
    	return ctx.getString(R.string.api_server);
    }
    
    private static String getDefaultForgetPasswordUrl(Context ctx){
    	return ctx.getString(R.string.default_forget_password_url);
    }
    
    
    
    private static Exception validatePreferences(Context ctx, ServerUrlProtocolType protocol,
												 ServerUrlCertificateAuthority certAuth, String url, String fpUrl){
    	
    	JResponseError error = null;
    	
    	if(!Utils.isDebug(ctx) && (validateUrl(url, getDefaultUrl(ctx)) /*|| validateUrl(fpUrl, getDefaultForgetPasswordUrl(ctx))*/)){
    			
			if(protocol!= ServerUrlProtocolType.https){
				error = new JResponseError(ctx.getString(R.string.invalid_protocol));
			}
			else if(certAuth != ServerUrlCertificateAuthority.CertifiedAuthority){
				error = new JResponseError(ctx.getString(R.string.invalid_certificate_msg));
			}

    	}
    	
    	return error;
    	
    }
    
    private static boolean validateUrl(String url, String defaultUrl){
    	return url == null || url.isEmpty() || url.equalsIgnoreCase(defaultUrl);
    }

}
