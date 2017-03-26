package com.sr.masharef.masharef.common;

import android.content.Context;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.utility.Log;

import java.util.HashMap;

/**Exception throws when error is returned from API
 * @author Eeshan, Zuhair
 */
//
public class JResponseError extends Exception {

	private String code = String.valueOf(Constants.kNone);
	private String type = Constants.kNoneTypeError;
	
	public static final String MSG  = "message";
	public static final String CODE = "code";
	public static final String TYPE = "type";
	
	public static JResponseError getNetConnectionError(Context context){
		return new JResponseError(context.getString(R.string.no_internet));
	}
	
	//===
	
	public JResponseError(HashMap<String, String> info){
		super((info!=null)?info.get(MSG):"Null");
		
		if(info!=null){
			
			String code = info.get(CODE);
			if(code!=null && code.length()>0){
				this.code = code;
			}
			
			String type = info.get(TYPE);
			if(type!=null && type.length()>0){
				this.type =type;  
			}
			
		}
		
	}
	
	public JResponseError(String msg, String code, String type){
		super(msg);
		this.code = code;
		this.type = type;
	}
	
	//===
	public JResponseError(String msg){
		super(msg);
	}
	
	public JResponseError(){ 
		super();
	}
	
	public int getCode() {
		
		try{
			return Integer.parseInt(code);
		}
		catch(NumberFormatException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At getCode() of JResponseError");
		}
		
		return Constants.kNone;
		
	}
	
	public String getCodeStr() {
		return code;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public String getMessage() {
		Log.e("ERROR_MSG : "+super.getMessage()+"\nERROR_CODE : "+code+"\nERROR_TYPE : "+type);
		return super.getMessage();
	}
	
	public String getServerMessage(){
		return super.getMessage();
	}
	
}
