package com.mrzhang.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ljsw.router.facade.annotation.Autowired;
import com.mrzhang.component.componentlib.router.ui.UIRouter;

/**
 * Created by mrzhang on 2017/6/20.
 */

public class ShareActivity extends AppCompatActivity {

    @Autowired
    String bookName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity_share);
//        UIRouter.getInstance().inject

        TextView textView = (TextView) findViewById(R.id.share_tv_tag);
        textView.setText("share: " + bookName);

    }

}
