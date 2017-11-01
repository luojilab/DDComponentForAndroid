package osp.leobert.bk_test_component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleAdapter extends BaseAdapter {
  private static final String[] CONTENTS = "The quick brown fox jumps over the lazy dog".split(" ");

  private final LayoutInflater inflater;

  public SimpleAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {
    return CONTENTS.length;
  }

  @Override public String getItem(int position) {
    return CONTENTS[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = inflater.inflate(R.layout.simple_list_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    String word = getItem(position);
    holder.word.setText(String.format(Locale.getDefault(), "Word: %s", word));
    holder.length.setText(String.format(Locale.getDefault(), "Length: %d", word.length()));
    holder.position.setText(String.format(Locale.getDefault(), "Position: %d", position));
    // Note: don't actually do string concatenation like this in an adapter's getView.

    return view;
  }

  static final class ViewHolder {
    @BindView(R2.id.word) TextView word;
    @BindView(R2.id.length) TextView length;
    @BindView(R2.id.position) TextView position;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
