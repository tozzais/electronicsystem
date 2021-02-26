package com.gaocheng.rainsystem;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by 32672 on 2019/2/26.
 */

public class TestActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gis);
        map = findViewById(R.id.map);


    }


}
