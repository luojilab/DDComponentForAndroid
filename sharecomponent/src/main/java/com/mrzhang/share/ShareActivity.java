package com.mrzhang.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ljsw.component.di.AutowiredService;
import com.ljsw.component.json.JsonService;
import com.ljsw.router.facade.annotation.Autowired;
import com.ljsw.router.facade.annotation.RouteNode;


/**
 * 正确的测试url：会打开本页面
 * //shareComponent/share?bookName=testBookName&testDto={'s':'sss'}&b1=true&b2=true&testChar=a
 * <p>
 * 错误的测试url：会打开主页
 * //shareComponent/dummy?bookName=testBookName&testDto={'s':'sss'}&b1=true&b2=true&testChar=a" target="_blank">app</a></li>
 */
@RouteNode(path = "/share", group = "share")
public class ShareActivity extends AppCompatActivity {

    private static final String TAG = ShareActivity.class.getSimpleName();

    static class TestDto {
        String s;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }

    @Autowired
    String bookName;

    @Autowired
    TestDto testDto;

    @Autowired
    boolean b1;

    @Autowired
    Boolean b2 = Boolean.valueOf(false);

    @Autowired
    char testChar;

    private JsonService jsonService = JsonService.Factory.getInstance().create();
    private AutowiredService autowiredService = AutowiredService.Factory.getInstance().create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity_share);
        autowiredService.autowire(this);


        TextView textView = (TextView) findViewById(R.id.share_tv_tag);

        String debug = "share: " + bookName + "\r\n" +
                "dto:\r" + (jsonService.toJsonString(testDto)) + "\r\n"
                + "b1:" + b1 + "\r\n"
                + "b2:" + b2 + "\r\n"
                + "testChar:" + testChar;
        textView.setText(debug);

        Log.d(TAG, debug);

    }

}
