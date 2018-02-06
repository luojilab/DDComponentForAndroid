package com.luojilab.reader.runalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.luojilab.component.basiclib.ToastManager;
import com.luojilab.component.basicres.BaseApplication;
import com.luojilab.reader.R;
import com.luojilab.reader.ReaderFragment;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReaderTestActivity extends AppCompatActivity {

    ReaderFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readerbook_activity_test);
        fragment = new ReaderFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.tab_content, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分享组件 的 RESULT_CODE
        if (resultCode == 8888) {
            if (data != null) {
                ToastManager.show(BaseApplication.getAppContext(), data.getStringExtra("result"));
            }
        }
    }

}
