package com.joung.vienna.count;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mView;

    MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

}
