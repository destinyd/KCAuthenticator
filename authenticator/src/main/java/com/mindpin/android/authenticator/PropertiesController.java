package com.mindpin.android.authenticator;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by dd on 14-6-11.
 */
public class PropertiesController {
    public static void writeConfiguration(Context context, Object c) throws IOException {
        com.mindpin.android.authenticator.common.PropertiesUtil.writeConfiguration(context, "mindpin", "authenticator", c);
    }

    public static <T> T readConfiguration(Context context, Type clazz) throws FileNotFoundException {
        return com.mindpin.android.authenticator.common.PropertiesUtil.readConfiguration(context, "mindpin", "authenticator", clazz);
    }
}
