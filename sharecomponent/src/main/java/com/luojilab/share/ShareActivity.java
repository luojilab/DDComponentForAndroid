package com.luojilab.share;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.luojilab.componentservice.share.bean.Author;
import com.luojilab.share.databinding.ShareActivityShareBinding;

/**
 * Created by mrzhang on 2017/6/20.
 */
//@RouteNode(path = "/shareBook", desc = "分享书籍页面")
@Route(path = "/share/shareBook")
public class ShareActivity extends AppCompatActivity {

    @Autowired
    String bookName;

    @Autowired
    Author author;

    ShareActivityShareBinding binding;

    private final static int RESULT_CODE = 8888;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.share_activity_share);

        ARouter.getInstance().inject(this);

        binding.shareTitle.setText("Book");

        if (bookName != null) {
            binding.shareTvTag.setText(bookName);
        }

        if (author != null) {
            binding.shareTvAuthor.setText(author.getName());
            binding.shareTvCounty.setText(author.getCounty());
        }

        Intent intent = new Intent();
        intent.putExtra("result", "Share Success");
        setResult(RESULT_CODE, intent);

    }
}
