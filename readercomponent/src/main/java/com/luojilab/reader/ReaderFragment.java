package com.luojilab.reader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.component.componentlib.service.JsonService;
import com.luojilab.componentservice.readerbook.ReadBookService;
import com.luojilab.componentservice.share.ShareService;
import com.luojilab.componentservice.share.bean.Author;


/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReaderFragment extends Fragment {

    private View rootView;

    private final static int REQUEST_CODE = 7777;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.readerbook_fragment_reader, container,
                    false);

            rootView.findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToShareActivityWithBundle();
                }
            });
            rootView.findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToShareActivityWithUri();
                }
            });
            rootView.findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToShareActivityForResult();
                }
            });
            rootView.findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToShareActivityByNative();
                }
            });
            rootView.findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToShareActivityForResultByNative();
                }
            });
        }
        return rootView;
    }

    // UI transfer with Bundle
    private void goToShareActivityWithBundle() {
        Author author = new Author();
        author.setName("Margaret Mitchell");
        author.setCounty("USA");
        Bundle bundle = new Bundle();
        bundle.putString("bookName", "Gone with the Wind");
        bundle.putString("author", JsonService.Factory.getInstance().create().toJsonString(author));
        UIRouter.getInstance().openUri(getActivity(), "DDComp://share/shareBook", bundle);
    }

    // UI transfer with URI
    private void goToShareActivityWithUri() {
        Author author = new Author();
        author.setName("Barack Obama");
        author.setCounty("New York");
        UIRouter.getInstance().openUri(getActivity(),
                "DDComp://kotlin/shareMagazine?bookName=NYTIME&author="
                        + JsonService.Factory.getInstance().create().toJsonString(author), null);
    }

    //startActivityForResult
    private void goToShareActivityForResult() {
        Author author = new Author();
        author.setName("Margaret Mitchell");
        author.setCounty("USA");
        UIRouter.getInstance().openUri(getActivity(),
                "DDComp://share/shareBook?bookName=Gone with the Wind&author="
                        + JsonService.Factory.getInstance().create().toJsonString(author), null, REQUEST_CODE);
    }
    /**
     * 使用原生的方法来进行跳转
     */
    private void goToShareActivityByNative() {
        Author author = new Author();
        author.setName("OuyangPeng");
        author.setCounty("China");
        String authorString = JsonService.Factory.getInstance().create().toJsonString(author);

        String bookName = "goToShareActivityByNative";

        Router router = Router.getInstance();
        ShareService service = (ShareService) router.getService(ShareService.class.getSimpleName());
        if (service != null) {
            service.startShare2Activity(getActivity(), bookName, authorString);
        }
    }

    /**
     * 使用原生的方法来进行跳转，并且处理返回结果
     */
    private void goToShareActivityForResultByNative() {
        Author author = new Author();
        author.setName("OuyangPeng");
        author.setCounty("China");
        String authorString = JsonService.Factory.getInstance().create().toJsonString(author);

        String bookName = "goToShareActivityForResultByNative";

        Router router = Router.getInstance();

        ShareService service = (ShareService) router.getService(ShareService.class.getSimpleName());
        if (service != null) {
            service.startShare2ActivityForResult(getActivity(), bookName, authorString, REQUEST_CODE);
        }
    }



}
