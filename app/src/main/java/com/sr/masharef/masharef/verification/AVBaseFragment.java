package com.sr.masharef.masharef.verification;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.sr.masharef.masharef.common.dialogs.AlertDialogs;
import com.sr.masharef.masharef.R;


public class AVBaseFragment extends Fragment {
	
	public interface AVBaseFragmentListener{
		void onLogoutEvent(Exception error);
		void onCloseEvent();
		void onSuccessEvent();
	}

	
	public AVBaseFragmentListener listener;
	
	public AVBaseFragment() {
		super();
	}
	
	protected void finishFragment(){
		getActivity().onBackPressed();
	}
	
	protected void doLogout(){
		
		AlertDialogs.actionAlert(getActivity(), getString(R.string.logout), getString(R.string.logout_msg), new AlertDialogs.ActionListeners() {
			
			@Override
			public void onPositiveClick(DialogInterface arg0, int arg1) {
				if(listener!=null){
					listener.onLogoutEvent(null);
				}
			}
			
			@Override
			public void onNeutralClick(DialogInterface arg0, int arg1) {
				
			}
			
			@Override
			public void onNegativeClick(DialogInterface arg0, int arg1) {
				
			}
			
		});
		
	}

}
