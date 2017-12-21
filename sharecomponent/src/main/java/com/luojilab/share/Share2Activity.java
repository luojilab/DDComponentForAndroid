package com.luojilab.share;

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
 * Created by mrzhang on 2017/12/19.
 */
@RouteNode(path = "/shareMagazine", desc = "分享杂志页面")
public class Share2Activity extends AppCompatActivity {
    static final boolean enablethrow  = true;

    @Autowired(name = "bookName", required = true, throwOnNull = AutowiredService.THROW_CONFIG)
    String magazineName;

    @Autowired
    Author author;

    private AutowiredService autowiredService = AutowiredService.Factory.getInstance().create();

    ShareActivityShareBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.share_activity_share);
        autowiredService.autowire(this);

        binding.shareTitle.setText("Magazine");

        if (magazineName != null) {
            binding.shareTvTag.setText(magazineName);
        }

        if (author != null) {
            binding.shareTvAuthor.setText(author.getName());
            binding.shareTvCounty.setText(author.getCounty());
        }
    }
}
