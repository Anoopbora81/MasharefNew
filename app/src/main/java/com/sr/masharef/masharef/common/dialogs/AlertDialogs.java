
//AlertDialog.java

//Created By Zuhair on Mar 10, 2017

package com.sr.masharef.masharef.common.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sr.masharef.masharef.R;

public class AlertDialogs {
	
	public interface ActionListeners{
		public void onPositiveClick(DialogInterface dialog, int which);
		public void onNegativeClick(DialogInterface dialog, int which);
		public void onNeutralClick(DialogInterface dialog, int which);
	}

	public static void actionAlert(final Context ctx, String title, String message, final ActionListeners listener)
  	{
  		
  		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setCancelable(false);
		
		if(title!=null){
			dialog.setTitle(title);
		}
		
		if(message!=null){
			dialog.setMessage(message);
		}	
		dialog.setInverseBackgroundForced(true);
		dialog.setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(listener!=null)
					listener.onPositiveClick(dialog, which);				
			}
		});
		dialog.setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(listener!=null)
					listener.onNegativeClick(dialog, which);
			}
		});
		dialog.show();
	}

}
