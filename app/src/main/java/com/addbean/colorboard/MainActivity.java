package com.addbean.colorboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.addbean.color_selector.ColorBoard;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ColorBoard mColorBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        mColorBoard = (ColorBoard) findViewById(R.id.color_board);
        bindEvent();
        mColorBoard.show();
    }

    private void bindEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.show();
            }
        });
    }
}
