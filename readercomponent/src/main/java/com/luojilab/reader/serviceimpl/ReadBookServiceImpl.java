package com.luojilab.reader.serviceimpl;

import android.support.v4.app.Fragment;

import com.luojilab.reader.ReaderFragment;
import com.luojilab.reader.readerbook.ReadBookService;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReadBookServiceImpl implements ReadBookService {
    @Override
    public Fragment getReadBookFragment() {
        return new ReaderFragment();
    }
}
