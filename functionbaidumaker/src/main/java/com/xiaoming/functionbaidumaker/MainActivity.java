package com.xiaoming.functionbaidumaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaoming.functionbaidumaker.custommapview.MyMapViewActivity;
import com.xiaoming.functionbaidumaker.marker.MarkerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnMaker;
    private Button btnCustomMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnMaker = findViewById(R.id.btn_maker);
        btnCustomMapView = findViewById(R.id.btn_custom_map_view);
        btnMaker.setOnClickListener(this);
        btnCustomMapView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_maker:
                startActivity(new Intent(MainActivity.this, MarkerActivity.class));
                break;
            case R.id.btn_custom_map_view:
                startActivity(new Intent(MainActivity.this, MyMapViewActivity.class));
                break;
        }
    }
}
