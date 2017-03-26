package com.sr.masharef.masharef;

import android.app.Activity;

import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.model.gallery.AGallery;

import java.util.ArrayList;

/**
 * Created by ZUHAIR on 18/01/17.
 */

public class MAppManager extends ApplicationManager {
    public static ArrayList<AGallery> galleryPrivateImagesList;
    public static ArrayList<AGallery> galleryPublicImagesList;
    public static ArrayList<AGallery> galleryPrivateVideoList;
    public static ArrayList<AGallery> galleryPublicVideoList;
    public static ArrayList<AGallery> galleryPrivateAudioList;
    public static ArrayList<AGallery> galleryPublicAudioList;
    @Override
    protected void updateAppRequest(Activity activity) {
        // Show app update dialog
    }

    @Override
    public String getStaticAppName() {
        return "Masmaref";
    }
}
