package com.example.moreclickedlistener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.moreclick.MoreClickedProxy;

public class MainActivity extends AppCompatActivity implements MoreClickedProxy.ClickedListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MoreClickedProxy proxy = new MoreClickedProxy();
        Button button = findViewById(R.id.btn_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                proxy.setClickedListener(MainActivity.this);
                proxy.onTouch(view, motionEvent);

                return false;
            }
        });
    }

    @Override
    public void onSingleClicked(View view) {
        Log.e(TAG, "onSingleClicked: ");
    }

    @Override
    public void onDoubleClicked(View view) {
        Log.e(TAG, "onDoubleClicked: ");
    }

    @Override
    public void onLongClicked(View view) {
        Log.e(TAG, "onLongClicked: ");
    }
}
