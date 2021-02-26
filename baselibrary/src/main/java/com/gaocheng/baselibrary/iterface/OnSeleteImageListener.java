package com.gaocheng.baselibrary.iterface;

import android.widget.ImageView;

/**
 * Created by xumingming on 2017/7/25.
 * 雨污的信息更新 现场评估
 * 河道的信息更新 现场评估
 */

public interface OnSeleteImageListener {

    void selete(ImageView imageView, int position);
    void delete(String path);

}
