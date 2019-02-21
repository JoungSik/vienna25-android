package com.joung.vienna.base;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
