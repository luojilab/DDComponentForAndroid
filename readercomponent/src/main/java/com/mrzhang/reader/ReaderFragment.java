package com.mrzhang.reader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljsw.component.service.JsonService;
import com.mrzhang.component.componentlib.router.ui.UIRouter;
import com.mrzhang.componentservice.share.bean.Author;


/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReaderFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.readerbook_fragment_reader, container,
                    false);

            rootView.findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Author author = new Author();
                    author.setName("Margaret Mitchell");
                    author.setCounty("USA");

//                    Bundle bundle = new Bundle();
//                    bundle.putString("bookName", "Gone with the Wind");
//                    bundle.putString("testDto", JsonService.Factory.getInstance().create().toJsonString(author));
//                    UIRouter.getInstance().openUri(getActivity(), "dunb://share/shareBook", bundle);

                    UIRouter.getInstance().openUri(getActivity(),
                            "dunb://share/shareBook?bookName=Gone with the Wind&testDto="
                                    + JsonService.Factory.getInstance().create().toJsonString(author), null);
                }
            });


        }
        return rootView;
    }
}
