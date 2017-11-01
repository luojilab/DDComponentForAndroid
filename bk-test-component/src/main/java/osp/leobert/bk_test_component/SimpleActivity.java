package osp.leobert.bk_test_component;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljsw.router.facade.annotation.RouteNode;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import osp.leobert.bk_test_component.unbinder.H;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

@RouteNode(path = "/demo", group = "bk")
public class SimpleActivity extends Activity {
    private static final ButterKnife.Action<View> ALPHA_FADE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setFillBefore(true);
            alphaAnimation.setDuration(500);
            alphaAnimation.setStartOffset(index * 100);
            view.startAnimation(alphaAnimation);
        }
    };

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.subtitle)
    TextView subtitle;
    @BindView(R2.id.hello)
    Button hello;
    @BindView(R2.id.list_of_things)
    ListView listOfThings;
    @BindView(R2.id.footer)
    TextView footer;
    @BindString(R2.string.app_name)
    String butterKnife;
    @BindString(R2.string.field_method)
    String fieldMethod;
    @BindString(R2.string.by_jake_wharton)
    String byJakeWharton;
    @BindString(R2.string.say_hello)
    String sayHello;

    @BindViews({R2.id.title, R2.id.subtitle, R2.id.hello})
    List<View> headerViews;

    private SimpleAdapter adapter;

    @OnClick(R2.id.hello)
    void sayHello() {
        Toast.makeText(this, "Hello, views!", LENGTH_SHORT).show();
        ButterKnife.apply(headerViews, ALPHA_FADE);
    }

    @OnLongClick(R2.id.hello)
    boolean sayGetOffMe() {
        Toast.makeText(this, "Let go of me!", LENGTH_SHORT).show();
        return true;
    }

    @OnItemClick(R2.id.list_of_things)
    void onItemClick(int position) {
        Toast.makeText(this, "You clicked: " + adapter.getItem(position), LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_activity);
        ButterKnife.bind(this);

        // Contrived code to use the bound fields.
        title.setText(butterKnife);
        subtitle.setText(fieldMethod);
        footer.setText(byJakeWharton);
        hello.setText(sayHello);

        adapter = new SimpleAdapter(this);
        listOfThings.setAdapter(adapter);

        verifyContentViewBinding();
    }


    ///////////////////////////////////////////////////////////////////////////
    // copy integration test to here, do runtime test
    ///////////////////////////////////////////////////////////////////////////
    public void verifyContentViewBinding() {
        FrameLayout frameLayout = new FrameLayout(this);
        Button button1 = new Button(this);
        button1.setId(android.R.id.button1);
        frameLayout.addView(button1);
        Button button2 = new Button(this);
        button2.setId(android.R.id.button2);
        frameLayout.addView(button2);
        Button button3 = new Button(this);
        button3.setId(android.R.id.button3);
        frameLayout.addView(button3);
        View content = new View(this);
        content.setId(android.R.id.content);
        frameLayout.addView(content);
        H h = new H(frameLayout);

        Unbinder unbinder = ButterKnife.bind(h, frameLayout);
        verifyHBound(h);
        unbinder.unbind();
        verifyHUnbound(h);
    }

    private void verifyHBound(H h) {
        throwIfNull(h.button1,"b1");
        throwIfNull(h.button2,"b2");
        throwIfNull(h.button3,"b3");
    }

    private void verifyHUnbound(H h) {
        throwIfNotNull(h.button1,"b1");
        throwIfNotNull(h.button2,"b2");
        throwIfNotNull(h.button3,"b3");
    }

    private void throwIfNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + "is null");
        }
    }

    private void throwIfNotNull(Object o, String name) {
        if (o != null) {
            throw new IllegalStateException(name + "should be null, now is leaked");
        }
    }
}

