package com.mindpin.android.authenticator.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;


public class PropertiesUtil {
    static final String TAG = "PropertiesUtil";

    public static String readInStream(FileInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

    public static void writeConfiguration(Context context, String path, String file_name, Object c) throws IOException {
        File file;
        if (SDCard.hasSdcard()) {
            String sd_path = Environment
                    .getExternalStorageDirectory() + "/" + path;

            file = new File(sd_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(sd_path, file_name);
        } else {
            file = new File(context.getDatabasePath(file_name).getAbsolutePath());
        }


        if (!file.exists()) {
            file.createNewFile();
        }

        String str_json = new Gson().toJson(c);
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(str_json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readConfiguration(Context context, String path, String file_name, Type clazz) throws FileNotFoundException {
        File file;
        if (SDCard.hasSdcard()) {
            String sd_path = Environment
                    .getExternalStorageDirectory() + "/" + path;

            file = new File(sd_path, file_name);
        } else {
            file = new File(context.getDatabasePath(file_name).getAbsolutePath());
        }

        String str_json = null;
        FileInputStream fi;
        fi = new FileInputStream(file);
        str_json = readInStream(fi);

        if (str_json.length() > 0) {
            return new Gson().fromJson(str_json, clazz);
        }

        return null;

    }
}
