package com.luojilab.share;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.luojilab.component.componentlib.service.AutowiredService;
import com.luojilab.componentservice.share.bean.Author;
import com.luojilab.router.facade.annotation.Autowired;
import com.luojilab.router.facade.annotation.RouteNode;
import com.luojilab.share.databinding.ShareActivityShareBinding;

/**
 * Created by mrzhang on 2017/6/20.
 */
@RouteNode(path = "/shareBook")
public class ShareActivity extends AppCompatActivity {

    @Autowired
    String bookName;

    @Autowired
    Author author;

    private AutowiredService autowiredService = AutowiredService.Factory.getInstance().create();

    ShareActivityShareBinding binding;

    private final static int RESULT_CODE = 8888;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.share_activity_share);
        autowiredService.autowire(this);

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
