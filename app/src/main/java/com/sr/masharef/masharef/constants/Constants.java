package com.sr.masharef.masharef.constants;

/**
 * Created by Zuhair on 09/01/17.
 */

public class Constants {

    public static final int kNone				=	200;// it was -1
    public static final int kZero				=	 0;


    public static final int RESULT_NONE 		= 1101;
    public static final int RESULT_LOGIN 		= 1102;
    public static final int RESULT_UPDATE 		= 1103;
    public static final int RESULT_QUIT 		= 1104;
    public static final int RESULT_RESTART 		= 1105;
    public static final int RESULT_OK 			= 1106;
    public static final int LANGUAGE_CHANGED	= 1107;

    public static final String kNoneTypeError	=	"ErrorType Not Found!!!";

    public static final String kApiError		=	"Error in Api";
    public static final String kApiProtocol		=	"api_protocol";
    public static final String kApiUrl			=	"api_url";
    public static final String kGroupID			=	"group_id";
    public static final String kForgetPassURL	=	"forget_pass_url";
    public static final String kApiCA			=	"api_ca";
    public static final String kResCode			=	"res_code";
    public static final String kResMsg			=	"res_msg";

    public static final String CIPHER_KEY       =   "Masharef_Key";
    public static final String APP_LANGUAGE		=	"app_lang";

    public static final String LAN_ENGLISH		=	"en";
    public static final String LAN_ARABIC		=	"ar";
    
    public enum ResultKeys{
        LOGIN_ERROR_MSG, ACTIVITY_RESULT
    }
}
