package com.joung.vienna.note;

import com.joung.vienna.base.BasePresenter;
import com.joung.vienna.base.BaseView;
import com.joung.vienna.note.model.Note;

public interface NoteContract {

    interface View extends BaseView<Presenter> {

        void resultCheckPassword(boolean check);

        void errorCheckPassword();

        void errorAddNote();

    }

    interface Presenter extends BasePresenter {

        void checkPassword(String name, String password, boolean autoSignIn);

        void getNotes();

        void saveNotes(Note note);

        boolean checkLogin();

    }

}
