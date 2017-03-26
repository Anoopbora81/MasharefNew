package com.sr.masharef.masharef.verification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sr.masharef.masharef.MainActivity;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.manager.FragmentActivityManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.verification.AVBaseFragment.AVBaseFragmentListener;
import com.sr.masharef.masharef.verification.accountreview.AccountReviewFragment;
import com.sr.masharef.masharef.verification.addsubaccount.AddSubAccountMainFragment;
import com.sr.masharef.masharef.verification.memberinformation.MemberInformationFragment;
import com.sr.masharef.masharef.verification.mobile.MobileVerificationFragment;
import com.sr.masharef.masharef.verification.otherinformation.OtherInformationFragment;
import com.sr.masharef.masharef.verification.termsncondition.TermsConditionsFragment;

public class AccountVerificationActivity extends FragmentActivityManager implements AVBaseFragmentListener{

	
	public interface AccountVerificationActivityListener{
		public void onActivityResult(int requestCode, int resultCode, Intent result);
	}
	
	public AccountVerificationActivity() {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_verification);
		
		if(savedInstanceState == null){
			checkAndTakeDecision();
		}
		else{
			restartActivity();
		}
		
	}
	
	private void checkAndTakeDecision(){

		if(StaticContext.getLoggedInUserDetail()!=null){
			
			boolean verifiedStatus = isAcccountVerified(this);
			
			if(verifiedStatus){
				Intent i = new Intent();
				i.putExtra(Constants.ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_OK);
				setResult(RESULT_OK, i);
				finish();
			}
		}
		else{
			restartActivity();
		}
			
	}
	
	private void restartActivity(){
		Intent i = new Intent();
		i.putExtra(Constants.ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_RESTART);
		setResult(RESULT_OK, i);
		finish();
	}
	
	/*
     * Provide null in parameter if just checking the status
     */
    
	public static boolean isAcccountVerified(AccountVerificationActivity activity){ 

		Log.e("isAcccountVerified IN........");
		Log.e("isAcccountVerified IN........");
		System.out.println("isAcccountVerified IN........");
		try{
			AUserDetail userInfo = StaticContext.getLoggedInUserDetail();

			Log.e("userInfo "+(userInfo == null));

			//=====Verify User Information
			if(userInfo == null){
				return false;
			}
			
			//=====Verify Mobile Information

			Log.e("userInfo.userActions.assignMobile "+userInfo.userActions.assignMobile);
			if(userInfo.userActions.assignMobile){
				if(activity!=null){
					MobileVerificationFragment fragment = new MobileVerificationFragment();
					addVerificationFragment(activity, fragment);
				}	
				return false;
			}




			//=====Verify Member Information
			Log.e("userInfo.userActions.memberInformation "+userInfo.userActions.memberInformation);
			if(userInfo.userActions.memberInformation){
				if(activity!=null){
					MemberInformationFragment fragment = new MemberInformationFragment();
					addVerificationFragment(activity, fragment);
				}
				return false;
			}

			//=====Verify Other Information
			Log.e("userInfo.userActions.otherInformation "+userInfo.userActions.otherInformation);
			if(userInfo.userActions.otherInformation){
				if(activity!=null){
					OtherInformationFragment fragment = new OtherInformationFragment();
					addVerificationFragment(activity, fragment);
				}
				return false;
			}

			//=====Verify Terms & Conditions Info
			Log.e("userInfo.userActions.openTermsAndConditions "+userInfo.userActions.openTermsAndConditions);
			if(userInfo.userActions.openTermsAndConditions){
				if(activity!=null){
					TermsConditionsFragment fragment = new TermsConditionsFragment();
					addVerificationFragment(activity, fragment);
				}
				return false;
			}

			//=====Verify addSubAccount
			Log.e("userInfo.userActions.addSubAccount "+userInfo.userActions.subAccount);
			if(userInfo.userActions.subAccount){
				if(activity!=null){
					AddSubAccountMainFragment fragment = new AddSubAccountMainFragment();
					addVerificationFragment(activity, fragment);
				}
				return false;

			}

			//=====Verify Account Review
			Log.e("userInfo.userActions.openAccountReview "+userInfo.userActions.openAccountReview);
			if(userInfo.userActions.openAccountReview){
				if(activity!=null){
					AccountReviewFragment fragment = new AccountReviewFragment();
					addVerificationFragment(activity, fragment);
				}
				return false;
			}

			
			return true;
		}
		catch(NullPointerException e){
			e.printStackTrace();
			Log.e(e.getMessage()+" At isAcccountVerified(...) of AccountVerificationActivity");
		}
		
		return false;
	}
	
	
	private static void addVerificationFragment(AccountVerificationActivity activity, AVBaseFragment fragment){

		Log.d("Add Before fragment Manager Count ===>> "+activity.getSupportFragmentManager().getBackStackEntryCount());
		
		fragment.listener = activity;
		activity.getSupportFragmentManager().beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.replace(R.id.contents, fragment)
		.addToBackStack(null)
		.commit();
		
		Log.d("Add After fragment Manager Count ===>> "+activity.getSupportFragmentManager().getBackStackEntryCount());

	}


	@Override
	public void onLogoutEvent(final Exception error) {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				boolean status = MainActivity.sendLogoutRequest(AccountVerificationActivity.this, error);
				
				if(status){
					try{
						removeAllChildFragments();
					}
					catch(IllegalStateException e){
						e.printStackTrace();
						Log.e(e.getMessage()+" at onLogoutEvent(...) of AccountVerificationActivity");
					}
					catch (Exception e) {
						e.printStackTrace();
						Log.e(e.getMessage()+" at onLogoutEvent(...) of AccountVerificationActivity");
					}
				}
				
			}
		});
	}

	@Override
	public void onCloseEvent() {
		
	}

	@Override
	public void onSuccessEvent() {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				try{
					removeAllChildFragments();
					checkAndTakeDecision();
				}
				catch(IllegalStateException e){
					e.printStackTrace();
					Log.e(e.getMessage()+" at onSuccessEvent() of AccountVerificationActivity");
				}
				catch (Exception e) {
					e.printStackTrace();
					Log.e(e.getMessage()+" at onSuccessEvent() of AccountVerificationActivity");
				}
			}
		});
	}

	
	@Override
	public void onBackPressed() {
		
		Log.d("On Back Pressed fragment Manager Count ===>> "+getSupportFragmentManager().getBackStackEntryCount());
		
		if(getSupportFragmentManager().getBackStackEntryCount() == 1){
			Intent i = new Intent();
			i.putExtra(Constants.ResultKeys.ACTIVITY_RESULT.toString(), Constants.RESULT_QUIT);
			setResult(RESULT_OK, i);
			finish();
		}	
		else{
			getSupportFragmentManager().popBackStack();
		}
	}
	
	
	private void removeAllChildFragments() throws IllegalStateException {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		Log.d("Remove Before fragment Manager Count ===>> "+fragmentManager.getBackStackEntryCount());
		for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
			fragmentManager.popBackStack();
		}
		Log.d("Remove After fragment Manager Count ===>> "+fragmentManager.getBackStackEntryCount());
		
	}


}
