package com.xiaoming.widgetrecyclerview.twoitemsrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.Toast;

import com.xiaoming.widgetrecyclerview.R;

public class TwoItemsRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView twoItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_items_recycler_view);

        twoItemsRecyclerView = findViewById(R.id.two_items_rv);

        //设置线性布局管理器
        twoItemsRecyclerView.setLayoutManager(new LinearLayoutManager(TwoItemsRecyclerViewActivity.this));
        //设置Adapter
        twoItemsRecyclerView.setAdapter(new TwoItemsRecyclerViewAdapter(TwoItemsRecyclerViewActivity.this, new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        }));
    }
}
