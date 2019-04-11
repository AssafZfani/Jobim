package zfani.assaf.jobim.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JsonHelper {

    @Nullable
    public static String loadJSONFromAsset(@NonNull InputStream is) {
        String json = null;
        int num = 0;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            num = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return num > 0 ? json : null;
    }
}
