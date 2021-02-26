package com.gaocheng.baselibrary.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jumpbox on 16/5/4.
 */
public class PartMapUtils {

    public static RequestBody getTextRequestBody(String body) {
        return RequestBody.create(MediaType.parse("text/plan"), body);
    }

    public static RequestBody getImageRequestBody(String filePath) {
        return RequestBody.create(MediaType.parse("image/png"), new File(filePath));
    }


}
