package osp.leobert.bk_test_component.unbinder;

import android.support.annotation.ColorInt;
import android.view.View;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class H extends G {

  @BindColor(android.R.color.primary_text_dark) @ColorInt
  int grayColor;
  @BindView(android.R.id.button3)
  public View button3;

  public H(View view) {
    super(view);
    ButterKnife.bind(this, view);
  }
}
