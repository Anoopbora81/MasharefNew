
//FragmentManager.java

//Created By Zuhair on Oct 29, 2014


package com.sr.masharef.masharef.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.sr.masharef.masharef.manager.net.NetConnectivityManager;
import com.sr.masharef.masharef.utility.Log;


public class FragmentX extends Fragment implements NetConnectivityManager.NetConnectivityListener {

	private static final String TAG	= "ash_fragx";
	private OnKeyListener keyListener;
	private OnBackStackChangedListener backStackListener;
	
	public FragmentX() {
		super();
		initializeEvents();
	}
	
	/*****Overriding Delegates********/
	
	@Override
	public void onResume() {
		super.onResume();
		requestFocus();
		registerEvent();
	};
	
	@Override
	public void onPause(){
		super.onPause();
		releaseFocus();
		unregisterEvent();	
	}
	
	/***** Private Modules********/
	
	private void initializeEvents(){
		
		keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
					onBackButtonPressed();
					return true;
				}
				return false;
			}
		};
		
		backStackListener = new OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				
				
				Log.i(TAG, "Class Name ==>> "+FragmentX.this.getClass().getSimpleName());
				
				Fragment parent = FragmentX.this.getParentFragment();
				if(parent!=null){
					Log.i(TAG, "Parent Class Name ==>> "+parent.getClass().getSimpleName());
				}	
				
				Log.i(TAG, "OnBack Stack Changed Count ===>>>"+getChildFragmentManager().getBackStackEntryCount());
				
				if(getChildFragmentManager().getBackStackEntryCount() == 0){
					requestFocus();
				}
				
			}
		};
		
		
	}
	
	public void requestFocus(){
		
		try{
			View vu  = this.getView();
			vu.setFocusableInTouchMode(true);
			vu.requestFocus();
		}	
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At requestFocus() of FragmentX");
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At requestFocus() of FragmentX");
		}
	}
	
	public void releaseFocus(){
		try{
			View vu = this.getView();
			vu.setFocusableInTouchMode(false);
			vu.clearFocus();
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At releaseFocus() of FragmentX");
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At releaseFocus() of FragmentX");
		}
		
	}
	
	private void registerEvent(){
		
		try{
			NetConnectivityManager.getInstance(getActivity()).registerNetConnectionListener(this);
			getChildFragmentManager().addOnBackStackChangedListener(backStackListener);
			getView().setOnKeyListener(keyListener);
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At registerEvent() of FragmentX");
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At registerEvent() of FragmentX");
		}
	}
	
	private void unregisterEvent(){
		try{
			NetConnectivityManager.getInstance(getActivity()).removeNetConnectionListener(this);
			getChildFragmentManager().removeOnBackStackChangedListener(backStackListener);
			getView().setOnKeyListener(null);
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At unregisterEvent() of FragmentX");
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At unregisterEvent() of FragmentX");
		}
	}
	
	private void requestRemoveAllChildFragment(){
		
		FragmentManager manager = getChildFragmentManager();
		
		for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
			manager.popBackStack();
		}		
		
	}
	
	private static boolean addOrReplaceChildFragment(Fragment requestFragment, int layoutID, Fragment fragment, boolean isAdd){
		
		boolean status = false;
		
		try{
		
			FragmentTransaction transaction = requestFragment.getChildFragmentManager().beginTransaction();
			
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			
			if(isAdd)
				transaction.add(layoutID, fragment);
			else
				transaction.replace(layoutID, fragment);
			
			transaction.addToBackStack(null);
	
			transaction.commitAllowingStateLoss();
			
			status = true;
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At addChildFragment(...) of FragmentX");
		}
		
		return status;
	}

	/***** Public Modules********/
	
	public boolean requestReplaceChildFragment(int layoutID, Fragment fragment){
		return addOrReplaceChildFragment(this, layoutID, fragment, false);
	}
	
	public static boolean requestAddChildFragment(Fragment requestFragment, int layoutID, Fragment fragment){
		return addOrReplaceChildFragment(requestFragment, layoutID, fragment, true);
	}
	
	public boolean requestAddChildFragment(int layoutID, Fragment fragment){
		return addOrReplaceChildFragment(this, layoutID, fragment, true);
	}
	
	public boolean requestRemoveFragment(){
		
		boolean status = false;
		
		try{
			
			/***Remove all child fragment First***/
			requestRemoveAllChildFragment();
			
			/***Remove the main fragment***/
			
			Fragment parent = getParentFragment();
			
			Log.i(TAG, "Parent Fragment ==>>> "+parent);
			
			if(parent == null){
				
				if(getActivity()!=null){
					
					FragmentManager manager = getActivity().getSupportFragmentManager();
					
					if(manager.getFragments().contains(this)){
						Log.i(TAG, "Fragment Found in Activity ==>>> "+parent);
						manager.beginTransaction().remove(this).commit();
						status = true;
					}
					
				}				
			}
			else{
				getParentFragment().getChildFragmentManager().
				popBackStack(this.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
				status = true;
			}	
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At requestRemoveFragment(...) of FragmentX");
		}
		
		return status;
		
	}
	
	public void requestRemoveChildFragment(){
		try{
			getChildFragmentManager().popBackStack();
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At requestRemoveChildFragment() of FragmentX");
		}
	}
	
	public void requestActivityBackEvent(){
		if(getActivity()!=null){
			getActivity().onBackPressed();
		}	
	}
	
	public void onBackButtonPressed(){
		
		Log.i(TAG, "On Back Button Pressed On Handled On FragmentX Class >> "+getClass().getSimpleName());
		
		try{

			if(requestRemoveFragment()){
				//TODO Nothing here del_right now
			}
			else{
				requestActivityBackEvent();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage()+" At onBackButtonPressed() of FragmentX");
		}
		
	}

	/*
	 * Internet Connectivity Callback Listeners.
	 */

	@Override
	public void onNetConnected(boolean isRegistrationCall) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetDisconnected(boolean isRegistrationCall) {
		// TODO Auto-generated method stub
		
	};

}
