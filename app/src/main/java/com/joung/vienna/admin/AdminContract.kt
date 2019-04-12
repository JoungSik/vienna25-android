package com.joung.vienna.admin

import com.joung.vienna.base.BasePresenter
import com.joung.vienna.base.BaseView

interface AdminContract {

    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter

}