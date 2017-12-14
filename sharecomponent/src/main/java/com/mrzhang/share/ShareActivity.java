package com.mrzhang.share;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljsw.component.service.AutowiredService;
import com.ljsw.router.facade.annotation.Autowired;
import com.ljsw.router.facade.annotation.RouteNode;
import com.mrzhang.componentservice.share.bean.Author;
import com.mrzhang.share.databinding.ShareActivityShareBinding;

/**
 * Created by mrzhang on 2017/6/20.
 */
@RouteNode(host = "share", path = "/shareBook")
public class ShareActivity extends AppCompatActivity {

    @Autowired
    String bookName;

    @Autowired
    Author testDto;

    private AutowiredService autowiredService = AutowiredService.Factory.getInstance().create();

    ShareActivityShareBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.share_activity_share);
        autowiredService.autowire(this);

        if (bookName != null) {
            binding.shareTvTag.setText(" " + bookName);
        }

        if (testDto != null) {
            binding.shareTvAuthor.setText(testDto.getName());
            binding.shareTvCounty.setText(testDto.getCounty());
        }
    }
}
