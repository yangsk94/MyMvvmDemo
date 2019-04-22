package com.network.helper;


import com.utils.Logger;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private static String TAG = HttpLogger.class.getSimpleName();

    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(String message) {
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
        }
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = Logger.INSTANCE.formatJson(message);
        }
        mMessage.append(message.concat("\n"));
        if (message.startsWith("<-- END HTTP")) {
            Logger.INSTANCE.e(TAG, mMessage.toString());
        }
    }
}