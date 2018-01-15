package com.luojilab.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.luojilab.component.componentlib.service.AutowiredService;
import com.luojilab.component.componentlib.service.JsonService;
import com.luojilab.componentservice.share.bean.Author;

/**
 *  测试使用原生的是否可以
 * </p>
 * created by OuyangPeng at 2018/1/15 上午 11:29
 */
public class Share2Activity extends AppCompatActivity {

    String bookName;

    Author author;

    private TextView tvShareTitle;
    private TextView tvShareBook;
    private TextView tvAuthor;
    private TextView tvCounty;

    private final static int RESULT_CODE = 8888;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutowiredService.Factory.getInstance().create().autowire(this);
        setContentView(R.layout.share_activity_share);

        tvShareTitle =  (TextView)findViewById(R.id.share_title);
        tvShareBook =  (TextView)findViewById(R.id.share_tv_tag);
        tvAuthor =  (TextView)findViewById(R.id.share_tv_author);
        tvCounty =  (TextView)findViewById(R.id.share_tv_county);

        tvShareTitle.setText("Book2");


        bookName = getIntent().getStringExtra("bookName");
        String authorString = getIntent().getStringExtra("author");
        System.out.println("获取到的参数为：bookName = " + bookName + " author = " + authorString);
        author = JsonService.Factory.getInstance().create().parseObject(authorString,Author.class);

        if (bookName != null) {
            tvShareBook.setText(bookName);
        }

        if (author != null) {
            tvAuthor.setText(author.getName());
            tvCounty.setText(author.getCounty());
        }

        Intent intent = new Intent();
        intent.putExtra("result", "Share Success! OuyangPeng's method!");
        setResult(RESULT_CODE, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
