package com.sr.masharef.masharef.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.constants.Constants;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Zuhair on 06/01/17.
 */

public class Utils {

    private static AlertDialog alert;
    private static AlertDialog decisionAlert;
    private static int FLIP_VERTICAL = 1;
    private static int FLIP_HORIZONTAL = 2;

    //================================================================

    public static void alert(Context c, int titleRes, String message) {
        alert(c, R.drawable.del_aa_common_error, titleRes, message, true, null);
    }

    public static void alert(Context c, int iconRes, int titleRes, String message) {
        alert(c, iconRes, titleRes, message, true, null);
    }

    public static void alert(Context c, int iconRes, int titleRes, String message, boolean foreground, DialogInterface.OnCancelListener cancelListener) {

        if (foreground) {

            try {
                if (alert != null && alert.isShowing()) {
                    //======Do nothing
                } else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(c);

                    try {
                        alert.setIcon(iconRes);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                        Log.i(e.getMessage() + " at alert(...) of Utils");
                    }

                    try {
                        alert.setTitle(titleRes);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                        Log.i(e.getMessage() + " at alert(...) of Utils");
                    }

                    alert.setMessage(message);
                    alert.setOnCancelListener(cancelListener);

                    Utils.alert = alert.create();
                    Utils.alert.setCanceledOnTouchOutside(true);
                    Utils.alert.show();

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e(e.getMessage() + " at alert(...) of Utils");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(e.getMessage() + " at decisionAlert(...) of Utils");
            }

        }

    }

    //================================================================
    public static void alert(Context c, String message) {
        alert(c, R.string.error, message);
    }


    //================================================================
    public static void alert(Context c, String message, DialogInterface.OnCancelListener cancelListener) {
        alert(c, R.drawable.del_aa_common_error, R.string.error, message, true, cancelListener);
    }

    public static void alert(Context c, int title, String message, DialogInterface.OnCancelListener cancelListener) {
        alert(c, R.drawable.del_aa_common_error, title, message, true, cancelListener);
    }

    //======================================================================
    public static void showAlertOnMainThread(final Context ctx, final int titleRes, final String text) {

        try {
            if (ctx != null) {
                ((Activity) ctx).runOnUiThread(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        alert(ctx, titleRes, text);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At showAlertOnMainThread(...) module of Utils class");
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At showAlertOnMainThread(...) module of Utils class");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At showAlertOnMainThread(...) module of Utils class");
        }

    }

    //======================================================================
    public static void showAlertOnMainThread(final Context ctx, final String text) {
        showAlertOnMainThread(ctx, text, null);
    }

    //======================================================================
    public static void showAlertOnMainThread(final Context ctx, final String text, final DialogInterface.OnCancelListener listener) {

        try {
            ((Activity) ctx).runOnUiThread(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    alert(ctx, text, listener);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At showAlertOnMainThread(...) of Utils");
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At showAlertOnMainThread(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At showAlertOnMainThread(...) of Utils");
        }

    }

    //======================================================================
    public static boolean isNetworkConnected(Context ctx, boolean showAlert) {

        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnectedOrConnecting()) {
                status = true;
            } else {
                if (showAlert)
                    Utils.showAlertOnMainThread(ctx, R.string.error, ctx.getResources().getString(R.string.no_internet));
                status = false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At isNetworkConnected(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At isNetworkConnected(...) of Utils");
        }

        return status;

    }


    //=======================================================

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At calculateInSampleSize(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At calculateInSampleSize(...) of Utils");
        }
        return 1;
    }

    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {

        int inSampleSize = 1;

        if ((height > reqHeight || width > reqWidth) && (reqWidth != -1 && reqHeight != -1)) {
            if (width < height) {

                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        } else {

            if (reqHeight == -1)
                inSampleSize = Math.round((float) width / (float) reqWidth);
            else
                inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        if (reqWidth != -1 && reqHeight != -1)
            while ((inSampleSize * reqWidth) < width || (inSampleSize * reqHeight) < height)
                inSampleSize++;

        return inSampleSize;

    }

    //================================================================

    public static int getImageRotationAngle(String filePath) {

        int rotationAngle = 0;

        try {
            ExifInterface exif = new ExifInterface(filePath);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getImageRotationAngle(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getImageRotationAngle(...) of Utils");
        }

        Log.d("Rotation Angle == " + rotationAngle);

        return rotationAngle;
    }

    //================================================================
    public static Bitmap getPortraitViewBitmap(String filePath) {

        Bitmap mainBitmap = null;
        Bitmap tempBitmap = BitmapFactory.decodeFile(filePath);

        if (tempBitmap != null) {

            mainBitmap = getPortraitViewBitmap(getImageRotationAngle(filePath), tempBitmap);

            tempBitmap.recycle();
            System.gc();

        }

        return mainBitmap;

    }

    //================================================================

    public static Bitmap getPortraitViewBitmap(int rotationAngle, Bitmap mBitmap) {

        Bitmap rotatedBitmap = null;
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) mBitmap.getWidth() / 2, (float) mBitmap.getHeight() / 2);
            rotatedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getPortraitViewBitmap(...) of Utils");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getPortraitViewBitmap(...) of Utils");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getPortraitViewBitmap(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getPortraitViewBitmap(...) of Utils");
        }

        return rotatedBitmap;

    }

    //======================================================================

    public static String getAppVersionName(Context context) {

        String versionName = new String();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getAppVersionName(...) of Utils");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getAppVersionName(...) of Utils");
        }
        return versionName;
    }

    //================================================================

    public static String getAppName(Context pContext) {

        String appName = new String();

        try {
            PackageManager lPackageManager = pContext.getPackageManager();
            ApplicationInfo lApplicationInfo = null;
            lApplicationInfo = lPackageManager.getApplicationInfo(pContext.getApplicationInfo().packageName, 0);
            appName = (lApplicationInfo != null ? lPackageManager.getApplicationLabel(lApplicationInfo).toString() : "Unknown");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getAppName(...) of Utils");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getAppName(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getAppName(...) of Utils");
        }

        return appName;
    }

    //======================================================================

    public static void decisionAlertOnMainThread(final Context c, final int titleRes,
                                                 final CharSequence message, final String positiveTitle, final DialogInterface.OnClickListener onPositiveClick,
                                                 final String negativeTitle, final DialogInterface.OnClickListener onNegativeClick) {
        decisionAlertOnMainThread(c, R.drawable.del_aa_common_error, titleRes, message, positiveTitle, onPositiveClick, negativeTitle, onNegativeClick);
    }

    public static void decisionAlertOnMainThread(final Context c, final int iconRes, final int titleRes,
                                                 final CharSequence message, final String positiveTitle, final DialogInterface.OnClickListener onPositiveClick,
                                                 final String negativeTitle, final DialogInterface.OnClickListener onNegativeClick) {

        try {
            ((Activity) c).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    decisionAlert(c, iconRes, titleRes, message, positiveTitle, onPositiveClick, negativeTitle, onNegativeClick);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At decisionAlertOnMainThread(...) of Utils");
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At decisionAlertOnMainThread(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At decisionAlertOnMainThread(...) of Utils");
        }

    }

    public static void decisionAlert(Context c, int titleRes, CharSequence message, String positiveTitle, DialogInterface.OnClickListener onPositiveClick, String negativeTitle, DialogInterface.OnClickListener onNegativeClick) {
        decisionAlert(c, R.drawable.del_aa_common_error, titleRes, message, positiveTitle, onPositiveClick, negativeTitle, onNegativeClick);
    }

    public static void decisionAlert(Context c, int iconRes, int titleRes, CharSequence message, String positiveTitle, DialogInterface.OnClickListener onPositiveClick, String negativeTitle, DialogInterface.OnClickListener onNegativeClick) {

        try {
            if (decisionAlert != null && decisionAlert.isShowing()) {
                //======Do nothing
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(c);

                try {
                    builder.setIcon(iconRes);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Log.e(e.getMessage() + " At decisionAlert(...) of Utils");
                }

                try {
                    builder.setTitle(titleRes);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Log.e(e.getMessage() + " At decisionAlert(...) of Utils");
                }

                builder.setMessage(message);
                builder.setCancelable(!(onPositiveClick != null || onNegativeClick != null));

                if (onPositiveClick != null) {
                    builder.setPositiveButton(positiveTitle, onPositiveClick);
                }

                if (onNegativeClick != null) {
                    builder.setNegativeButton(negativeTitle, onNegativeClick);
                }

                decisionAlert = builder.create();
                decisionAlert.show();

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " at decisionAlert(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " at decisionAlert(...) of Utils");
        }

    }

    //======================================================================

    public static boolean isLocationEnabled(Context context) {
        return (gpsLocEnabled(context) || networkLocEnabled(context));
    }

    public static boolean isLocationEnabled(Context context, String provider) {
        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            return networkLocEnabled(context);
        } else if (provider.equals(LocationManager.GPS_PROVIDER)) {
            return gpsLocEnabled(context);
        } else {
            return false;
        }
    }

    public static boolean gpsLocEnabled(Context context) {

        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Return a boolean
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At gpsLocEnabled(...) of Utility");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At gpsLocEnabled(...) of Utility");
        }

        return false;

    }

    public static boolean networkLocEnabled(Context context) {

        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // Return a boolean
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At networkLocEnabled(...) of Utility");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At networkLocEnabled(...) of Utility");
        }

        return false;

    }


    //================================================================

    public static boolean isDebug(Context context) {

        Object obj = null;
        try {
            //Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Class<?> clazz = Class.forName("com.sr.masharef.masharef.BuildConfig");

            Field field = clazz.getField("DEBUG");
            obj = field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (obj != null || obj instanceof Boolean) {
            return ((Boolean) obj);
        } else {
            return false;
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + "(" + model + ")";
        }
    }

    public static String getDeviceVersion() {
        return "Android " + Build.VERSION.RELEASE;
    }


    public static ProgressDialog getProgress(Context ctx, String title, String message) {
        return ProgressDialog.show(ctx, title, message, true);
    }

    public static ProgressDialog getProgress(Context ctx, String title) {
        return getProgress(ctx, title, ctx.getString(R.string.crop__wait));
    }

    public static ProgressDialog getProgress(Context ctx) {
        return getProgress(ctx, ctx.getString(R.string.loading), ctx.getString(R.string.crop__wait));
    }


    //======================================================================
    public static void hideSoftKeyboard(Activity activity) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At hideSoftKeyboard(...) of Utility");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At hideSoftKeyboard(...) of Utility");
        }

    }

    //======================================================================
    public static void SoftKeyBoard(Context c, boolean state) {
        try {
            if (!state) {
                InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (((Activity) c).getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(((Activity) c).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At SoftKeyBoard(...) of Utility");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At SoftKeyBoard(...) of Utility");
        }
    }


    //======================================================================
    private static void postSoftKeyBoard(Context c, View v, boolean state) {

        try {
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (state) {
                //imm.showSoftInput(v, InputMethod.SHOW_FORCED);
                imm.toggleSoftInputFromWindow(v.getApplicationWindowToken(), 0, 0);
                v.requestFocus();
            } else {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.clearFocus();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At postSoftKeyBoard(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At postSoftKeyBoard(...) of Utils");
        }

    }

    public static void SoftKeyboard(final Context c, final View v, final boolean state) {

        try {
            v.post(new Runnable() {

                @Override
                public void run() {
                    postSoftKeyBoard(c, v, state);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At SoftKeyboard(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At SoftKeyboard(...) of Utils");
        }

    }

    public static SpannableString getUnderLineString(String text) {

        SpannableString content = new SpannableString(new String());

        try {
            content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getUnderLineString(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + " At getUnderLineString(...) of Utils");
        }

        return content;

    }


    //======================================================================

    public static HashMap<String, String> getMapFromJsonObject(JsonObject json) {

        HashMap<String, String> result = new HashMap<String, String>();

        try {
            for (Iterator<Map.Entry<String, JsonElement>> it = json.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) it.next();
                //CelUtils.log(Constants.kApiTag, "Key == "+entry.getKey()+" Value == "+entry.getValue().getAsString());
                result.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        }

        return result;

    }

//======================================================================

    public static HashMap<String, String> getMapFromJsonObject(JSONObject json) {

        HashMap<String, String> result = new HashMap<String, String>();
        try {
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String key = it.next();
                result.put(key, json.optString(key));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage() + "At getMapFromJsonObject(...) of Utils");
        }

        return result;

    }


    public static String getSelectedLanguage(Context context) {

        String lang = Constants.LAN_ENGLISH;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            lang = settings.getString(Constants.APP_LANGUAGE, lang);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lang;


    }

    public static void showIngressDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.inprogress_dialog_layout);
        dialog.setCancelable(false);

        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static String calculateDuration(Date startDate, Date currentDate) {
        String output = "";

        long diff = startDate.getTime() - currentDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 1) {

            output = new SimpleDateFormat("dd-MM-yyyy").format(startDate);

        } else {

            if (hours > 24 && hours < 48) {
                output = output + days + " Yesterday ";
            } else if (hours >0  && minutes >= 0) {
                output = output + hours + ":" + minutes + "Hours ";
            } else if (minutes > 0 && seconds >= 0) {
                output = output + minutes + "Minutes ";
            } else if (seconds > 0) {
                output = output + seconds + "Seconds ";
            }
            output = output + " ago";
        }
        return output;
    }

}
