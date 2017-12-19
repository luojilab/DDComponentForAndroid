package com.luojilab.reader.runalone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
}
