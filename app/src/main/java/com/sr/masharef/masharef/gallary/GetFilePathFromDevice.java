package com.sr.masharef.masharef.gallary;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/26/2017.
 */


    public final class GetFilePathFromDevice {

        /**
         * Get file path from URI
         *
         * @param context context of Activity
         * @param uri     uri of file
         * @return path of given URI
         */
        public static String getPath(final Context context, final Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];


                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    else {
                        File[] externalDirs = context.getExternalFilesDirs(null);
                        List<String> results = new ArrayList<>();
                        String imPath = null;

                        for (File file : externalDirs) {
                            String path = file.getPath().split("/Android")[0];

                            boolean addPath = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                addPath = Environment.isExternalStorageRemovable(file);
                            } else {
                                addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                            }

                            if (addPath) {
                                results.add(path);
                            }

//                            String[] temp = rv.toArray(new String[rv.size()]);
                            for (int i = 0; i < results.size(); i++) {
                                File tempf = new File(results.get(i) + "/" + split[1]);
                                if (tempf.exists()) {
                                    imPath= results.get(i) + "/" + split[1];
                                }
                            }
                        }
                        return imPath;
                    }
                    }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getContentData(context,uri);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        public static String getContentData(Context context,Uri uri){
             ContentResolver cr = context.getContentResolver();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cur = cr.query(uri, projection, null, null, null);
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        String filePath = cur.getString(0);
                        return filePath;
                    } else {
                        // Uri was ok but no entry found.
                    }
                    cur.close();
                } else {
                    // content Uri was invalid or some other error occurred
                }
            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }

