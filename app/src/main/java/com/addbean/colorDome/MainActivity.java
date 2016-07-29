package com.addbean.colorDome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.colorboard.ColorBoard;
import com.addbean.colorboard.IItemClickListener;
import com.addbean.colorboard.items.ItemMate;

public class MainActivity extends AppCompatActivity {

    private TextView mButton;
    private ColorBoard mColorBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (TextView) findViewById(R.id.button);
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
        mColorBoard.setIItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClick(ItemMate mate) {
                Toast.makeText(getBaseContext(),""+mate.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
