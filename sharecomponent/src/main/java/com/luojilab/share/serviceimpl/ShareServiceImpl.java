package com.luojilab.share.serviceimpl;

import android.app.Activity;
import android.content.Intent;
import com.luojilab.share.Share2Activity;
import com.luojilab.share.ShareService;

/**
 *  实现原生方式跳转Activity
 * </p>
 * created by OuyangPeng at 2018/1/15 上午 11:40
 */
public class ShareServiceImpl implements ShareService {
    @Override
    public void startShare2Activity(Activity context, String bookName, String author) {
        Intent intent = new Intent(context, Share2Activity.class);
        intent.putExtra("bookName", bookName);
        intent.putExtra("author", author);
        context.startActivity(intent);
    }

    @Override
    public void startShare2ActivityForResult(Activity context, String bookName, String author, int requestCode) {
        Intent intent = new Intent(context, Share2Activity.class);
        intent.putExtra("bookName", bookName);
        intent.putExtra("author", author);
        context.startActivityForResult(intent,requestCode);
    }
}
