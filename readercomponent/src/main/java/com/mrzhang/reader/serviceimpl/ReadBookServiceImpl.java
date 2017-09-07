package com.mrzhang.reader.serviceimpl;

import android.support.v4.app.Fragment;

import com.mrzhang.componentservice.readerbook.ReadBookService;
import com.mrzhang.reader.ReaderFragment;
import com.mrzhang.componentservice.readerbook.ReadBookService;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReadBookServiceImpl implements ReadBookService {
    @Override
    public Fragment getReadBookFragment() {
        return new ReaderFragment();
    }
}
