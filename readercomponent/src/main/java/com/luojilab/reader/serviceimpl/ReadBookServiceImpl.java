package com.luojilab.reader.serviceimpl;

import android.support.v4.app.Fragment;

import com.luojilab.componentservice.readerbook.ReadBookService;
import com.luojilab.reader.ReaderFragment;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReadBookServiceImpl implements ReadBookService {
    @Override
    public Fragment getReadBookFragment() {
        return new ReaderFragment();
    }
}
