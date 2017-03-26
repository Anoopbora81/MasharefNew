package com.sr.masharef.masharef;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sr.masharef.masharef.about.About;
import com.sr.masharef.masharef.common.dialogs.AlertDialogs;
import com.sr.masharef.masharef.common.dialogs.AlertDialogs.ActionListeners;
import com.sr.masharef.masharef.context.StaticContext;
import com.sr.masharef.masharef.dashboard.DashboardFragment;
import com.sr.masharef.masharef.event.EventFragment;
import com.sr.masharef.masharef.gallary.GalleryFragment;
import com.sr.masharef.masharef.model.user.AUserDetail;
import com.sr.masharef.masharef.phonebook.PhoneBook;
import com.sr.masharef.masharef.user.UserInfoFragment;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.voting.VotingFragment;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;


    public interface HomeActivityListeners{
        public void onNetConnectivityChanged(boolean status, boolean isAutoCall);

    }

    public enum HomeFragmentTags{
        DASHBOARD,EVENT,PHONEBOOK,CHAT,GALLARY,VOTING,QUESTION_FORM,SETTING,ABOUT,LOGOUT
    }

    private HomeActivityListeners listener;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

    TextView userName,userMail;
    ImageView userImage;

    static HomeActivity singleton;

    DialogFragment userInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashoboard);

        singleton = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        userName = (TextView)headerView.findViewById(R.id.user_name_xx);
        userMail = (TextView)headerView.findViewById(R.id.user_mail_xx);

        userImage = (ImageView) headerView.findViewById(R.id.user_image);

        setupFragment(HomeFragmentTags.DASHBOARD);

        setUserDetail();
        initializeListener();
    }

    private void initializeListener() {

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fragmentTag = "userInfo";

                userInfoDialog = new  UserInfoFragment(HomeActivity.this){
                    @Override
                    public void onBackPressed() {
                        userInfoDialog.dismiss();
                    }

                    @Override
                    public void onDonePressed() {
                        userInfoDialog.dismiss();
                    }

                };

                userInfoDialog.show(getFragmentManager(),fragmentTag);

            }
        });

    }

    public static HomeActivity getSingleton(){
        return singleton;
    }

    void setUserDetail(){

        AUserDetail userDetail = StaticContext.getLoggedInUserDetail();

        if (userDetail != null){

            String name = userDetail.firstName +" "+userDetail.lastName;
            String mail = userDetail.email;
            if (name != null  && !(name.equalsIgnoreCase("")))
            userName.setText(name);
            if (mail != null  && !(mail.equalsIgnoreCase("")))
            userMail.setText(mail);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                toolbar.setEnabled(false);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                super.onBackPressed();
//                setResult(RESULT_OK);
//                finish();
            }
        }


    public void setupFragment(HomeFragmentTags tag){

        switch (tag) {

            case DASHBOARD:{
                setActionBar("DASHBOARD");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new DashboardFragment();
                fragmentTag     = HomeFragmentTags.PHONEBOOK.toString();

                addFragment(aboutFragment, fragmentTag, false, true);

                break;

            }

            case EVENT:{

               // Utils.showIngressDialog(HomeActivity.this);
                setActionBar("EVENTS");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new EventFragment();
                fragmentTag     = HomeFragmentTags.EVENT.toString();
                addFragment(aboutFragment, fragmentTag,  false, true);

                break;
            }

            case PHONEBOOK:{
                setActionBar("PHONEBOOK");

                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new PhoneBook();
                fragmentTag     = HomeFragmentTags.PHONEBOOK.toString();

                addFragment(aboutFragment, fragmentTag,  false, true);

                break;

            }

            case CHAT:{

                Utils.showIngressDialog(HomeActivity.this);

                /*setActionBar("CHAT");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new ChatFragment();
                fragmentTag     = HomeFragmentTags.CHAT.toString();

                addFragment(aboutFragment, fragmentTag);*/

                break;

            }

            case GALLARY:{

                //Utils.showIngressDialog(HomeActivity.this);

                setActionBar("GALLERY");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new GalleryFragment();
                fragmentTag     = HomeFragmentTags.GALLARY.toString();
                MAppManager.galleryPublicImagesList=null;
                MAppManager.galleryPrivateImagesList=null;
                MAppManager.galleryPublicVideoList=null;
                MAppManager.galleryPrivateVideoList=null;
                MAppManager.galleryPublicAudioList=null;
                MAppManager.galleryPublicAudioList=null;

                addFragment(aboutFragment, fragmentTag,false,true);

                break;

            }

            case VOTING:{

                //Utils.showIngressDialog(HomeActivity.this);

                setActionBar("VOTING");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new VotingFragment();
                fragmentTag     = HomeFragmentTags.VOTING.toString();

                addFragment(aboutFragment, fragmentTag,  false, true);

                break;

            }

            case QUESTION_FORM:{

                Utils.showIngressDialog(HomeActivity.this);

                /*setActionBar("QUESTION FORM");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new QuestionFormFragment();
                fragmentTag     = HomeFragmentTags.QUESTION_FORM.toString();

                addFragment(aboutFragment, fragmentTag);*/

                break;

            }

            case SETTING:{

                Utils.showIngressDialog(HomeActivity.this);

                /*setActionBar("SETTING");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new SettingFragment();
                fragmentTag     = HomeFragmentTags.SETTING.toString();

                addFragment(aboutFragment, fragmentTag);*/

                break;

            }

            case ABOUT:{
                setActionBar("ABOUT");
                Fragment aboutFragment;
                String fragmentTag;

                aboutFragment   = new About();
                fragmentTag     = HomeFragmentTags.ABOUT.toString();

                addFragment(aboutFragment, fragmentTag);

                break;

            }

            case LOGOUT: {
                setActionBar(getString(R.string.logout));
                doLogout();
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashoboard, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            setupFragment(HomeFragmentTags.DASHBOARD);
        } else if (id == R.id.nav_event) {
            setupFragment(HomeFragmentTags.EVENT);
        } else if (id == R.id.nav_phonebook) {
            setupFragment(HomeFragmentTags.PHONEBOOK);
        } else if (id == R.id.nav_chat) {
            setupFragment(HomeFragmentTags.CHAT);
        } else if (id == R.id.nav_gallary) {
            setupFragment(HomeFragmentTags.GALLARY);
        } else if (id == R.id.nav_Voting) {
            setupFragment(HomeFragmentTags.VOTING);
        }else if (id == R.id.nav_voting_form) {
            setupFragment(HomeFragmentTags.QUESTION_FORM);
        }else if (id == R.id.nav_setting) {
            setupFragment(HomeFragmentTags.SETTING);
        }else if (id == R.id.nav_about) {
            setupFragment(HomeFragmentTags.ABOUT);
        }else if (id == R.id.nav_logout) {
            setupFragment(HomeFragmentTags.LOGOUT);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addFragment(Fragment fragment, String tag){
        addFragment(fragment, tag, false, false);
    }


    private void addFragment(Fragment fragment, String tag, boolean isAdding, boolean backStackStatus){

        try{

            if(isFinishing()){
                return;	/** nothing here as it was already finished*/
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(isAdding){
                fragmentTransaction.add(R.id.content_frame, fragment, tag);
            }
            else{
                fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            }

            if(backStackStatus){
                if (tag != null) {
                    fragmentTransaction.addToBackStack(tag);
                }
                else {
                    fragmentTransaction.addToBackStack(null);
                }
            }

            fragmentTransaction.commitAllowingStateLoss();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e("",e.getMessage()+" at addFragment(...) of HomeActivity");
        }
    }

    protected void doLogout(){

        AlertDialogs.actionAlert(this, getString(R.string.logout), getString(R.string.logout_msg), new ActionListeners() {

            @Override
            public void onPositiveClick(DialogInterface arg0, int arg1) {
                MainActivity.sendLogoutRequest(HomeActivity.this, null);
            }

            @Override
            public void onNeutralClick(DialogInterface arg0, int arg1) {

            }

            @Override
            public void onNegativeClick(DialogInterface arg0, int arg1) {

            }

        });
    }
    private void setActionBar(String title)
    {
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);

    }

}
