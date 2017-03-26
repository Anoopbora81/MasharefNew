package com.sr.masharef.masharef.webservice;


import com.sr.masharef.masharef.utility.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsURLUnsafeConnection {
	
	public static void setup(HttpsURLConnection connection){
		
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, getCertificates(), null);
	        connection.setSSLSocketFactory(sc.getSocketFactory());
	        connection.setHostnameVerifier(getHostnameVerifier());
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Log.e(ApiServiceBase.TAG, e.getMessage()+" at setup(...) of HttpsURLUnsafeConnection");
		} 
		catch (KeyManagementException e) {
			e.printStackTrace();
			Log.e(ApiServiceBase.TAG, e.getMessage()+" at setup(...) of HttpsURLUnsafeConnection");
		}
       
	}
	
	public static TrustManager[] getCertificates(){
		
		TrustManager[] trustAllCerts = new TrustManager[]{
	        new X509TrustManager() {
	        	
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            	return null;
	            }
	            
	            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){
	            	
	            }
	            
	            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){
	            	
	            }
	            
	        }
	    };
		
		return trustAllCerts;
		
	}
	
	public static HostnameVerifier getHostnameVerifier(){
		
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		    	return true;
		    }
		};
		
		return hostnameVerifier;
		
	}
	
	/**
	 * This module is added for future reference if there is any requirement for adding any specific 
	 * certificate for url verification. The code is not accurate yet but can be used as an reference
	 * to make it functional.
	 */
	
	private void referenceModule(){
		
		/*InputStream caInput = null;
    	
		try {
			
			caInput = new BufferedInputStream(ctx.getAssets().open("Mashraf.cer"));
		    Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(caInput);
		    Log.i(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());

			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("CN", ca);

			String algorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf= TrustManagerFactory.getInstance(algorithm);
			tmf.init(keyStore);
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);
			
			connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			
		} 
		 catch (CertificateException e) {
			e.printStackTrace();
			if(caInput!=null) caInput.close();
			Log.e(TAG, e.getMessage()+" At getConnection(...) of ApiServiceBase");
		} catch (KeyStoreException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At getConnection(...) of ApiServiceBase");
		}*/

	}

}
