
package com.sr.masharef.masharef.utility;

import java.io.File;

//import com.crashlytics.android.Crashlytics;


/**
 * @author Zuhair
 */

//

public final class Log {
    
    private static boolean _logEnabled = true;
    public final static String tag = "zhr";
    public final static String ExceptionTag = "zhr_exp";
    
    //==========================
    public static boolean enabled(){ return _logEnabled;} 
    
    //==========================
    
    public static void content(String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.i(tag, message);
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At content(...) of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" At content(...) of Log");
        }
    }
    
    //==========================
    
    public static void v(String message){
    	v(tag, message);
    }

    public static void v(String tag, String message){
    	v(null, tag, message);
    }
    
    public static void v(File file, String tag, String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.v(tag, message);
            	//if(file!=null){
            	//	Utils.logPartialData(file, tag+" : "+message);
            	//}
                //Crashlytics.log(android.util.Log.VERBOSE, tag,message);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At v() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" At v() of Log");
        }
    }
    
    //==========================
    
    public static void d(String message){
        d(tag, message);
    }
   
    public static void d(String tag, String message){
    	d(null, tag, message);
    }
    
    public static void d(File file, String tag, String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.d(tag, message);
            	//if(file!=null){
            	//	Utils.logPartialData(file, tag+" : "+message);
            	//}
                //Crashlytics.log(android.util.Log.DEBUG, tag, message);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At d() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" At d() of Log");
        }
    }
    
    
    //==========================
    
    public static void e(String message){
    	e(ExceptionTag, message);
    }
    
    public static void e(File file, String message){
    	e(file, ExceptionTag, message);
    }
    
    public static void e(String tag, String message){
    	e(null, tag, message);
    }
    
    public static void e(File file, String tag, String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.e(tag, message);
            	//if(file!=null){
            	//	Utils.logPartialData(file, tag+" : "+message);
            	//}
                //Crashlytics.log(android.util.Log.ERROR, tag,message);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At e() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At e() of Log");
        }
    }
    
    //==========================
    
    public static void i(String message){
        i(tag, message);
    }
    
    public static void i(String tag, String message){
    	i(null, tag, message);
    }
    
    public static void i(File file, String tag, String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.i(tag, message);
            	//if(file!=null){
            	//	Utils.logPartialData(file, tag+" : "+message);
            	//}
            }    
            
        } 
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At i() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At i() of Log");
        }
    }
    
    public static void i(Exception e){
        
        if(_logEnabled)
            e.printStackTrace();
    }
    
    //==========================
    public static void syncMsg(String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.e("zhr_sync", message);
                //Crashlytics.log(android.util.Log.ERROR, "zhr_sync",message);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At syncMsg() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At syncMsg() of Log");
        }
    }
    
    //==========================
    public static void apiServiceMsg(String message){
        
        try{
            
            if(_logEnabled){
                android.util.Log.e("zhr_sync", message);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At apiServiceMsg() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At apiServiceMsg() of Log");
        }
    }
    
    //==========================
    public static void e(String tag, String message, Throwable t){
        
        try{
            
            if(_logEnabled){
                android.util.Log.e(tag, message, t);
            }    
            
        }
        catch(OutOfMemoryError e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At e() of Log");
        }
        catch(Exception e){
        	e.printStackTrace();
        	Log.e(tag, e.getMessage()+" OOME At e() of Log");
        }
    }
    
    //==========================
    public static void ex(Exception e){
        
        if(_logEnabled)
            e.printStackTrace();
    }
}
