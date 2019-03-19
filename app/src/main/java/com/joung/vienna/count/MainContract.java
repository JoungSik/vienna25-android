package com.joung.vienna.count;

import com.joung.vienna.base.BasePresenter;
import com.joung.vienna.base.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void updateCountView(Long dateTime);

        void updateCountViewError();

    }

    interface Presenter extends BasePresenter {

        void getCurrentDateTime();

    }

}
