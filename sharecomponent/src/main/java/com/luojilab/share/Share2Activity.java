package com.luojilab.share;

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
 * Created by mrzhang on 2017/12/19.
 */
@Route(path = "/share/shareMagazine")
public class Share2Activity extends AppCompatActivity {

    @Autowired(name = "bookName")
    String magazineName;

    @Autowired
    Author author;

    ShareActivityShareBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.share_activity_share);

        ARouter.getInstance().inject(this);

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
