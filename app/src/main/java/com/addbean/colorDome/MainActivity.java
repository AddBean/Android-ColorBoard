package com.addbean.colorDome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.colorboard.ColorBoard;
import com.addbean.colorboard.IItemClickListener;
import com.addbean.colorboard.items.ItemMate;

public class MainActivity extends AppCompatActivity {
    private TextView mButton0;
    private TextView mButton1;
    private TextView mButton2;
    private TextView mButton3;
    private TextView mButton4;
    private TextView mButton5;
    private TextView mButton6;
    private TextView mButton7;
    private TextView mButton8;
    private ColorBoard mColorBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton0 = (TextView) findViewById(R.id.button_0);
        mButton1 = (TextView) findViewById(R.id.button_1);
        mButton2 = (TextView) findViewById(R.id.button_2);
        mButton3 = (TextView) findViewById(R.id.button_3);
        mButton4 = (TextView) findViewById(R.id.button_4);
        mButton5 = (TextView) findViewById(R.id.button_5);
        mButton6 = (TextView) findViewById(R.id.button_6);
        mButton7 = (TextView) findViewById(R.id.button_7);
        mButton8 = (TextView) findViewById(R.id.button_8);
        mColorBoard = (ColorBoard) findViewById(R.id.color_board);
        bindEvent();
        mColorBoard.show();
    }

    private void bindEvent() {
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.CENTER);
                mColorBoard.show();
            }
        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.TOP);
                mColorBoard.show();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.DOWN);
                mColorBoard.show();
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.LEFT);
                mColorBoard.show();
            }
        });
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.RIGHT);
                mColorBoard.show();
            }
        });
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.TOP | ColorBoard.LEFT);
                mColorBoard.show();
            }
        });
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.TOP | ColorBoard.RIGHT);
                mColorBoard.show();
            }
        });
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.DOWN | ColorBoard.LEFT);
                mColorBoard.show();
            }
        });

        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorBoard.setPosition(ColorBoard.DOWN | ColorBoard.RIGHT);
                mColorBoard.show();
            }
        });
        mColorBoard.setIItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClick(ItemMate mate) {
                Toast.makeText(getBaseContext(), "" + mate.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
