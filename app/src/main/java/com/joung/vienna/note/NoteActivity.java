package com.joung.vienna.note;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.joung.vienna.R;
import com.joung.vienna.note.model.Note;
import com.marcoscg.materialtoast.MaterialToast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteActivity extends AppCompatActivity implements NoteContract.View {

    private NoteContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        new NotePresenter(this, this);
        mPresenter.getNotes();
    }

    @OnClick(R.id.button_add_note)
    public void onAddNote() {
        if (!mPresenter.checkLogin()) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_sign_in, null);
            EditText editNameTextView = view.findViewById(R.id.edit_name);
            EditText editPasswordTextView = view.findViewById(R.id.edit_password);
            CheckBox checkAutoSignIn = view.findViewById(R.id.check_auto_sign_in);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setTitle(R.string.title_sign_in)
                    .setCancelable(false)
                    .setNegativeButton(R.string.button_sign_in, (dialog12, which) -> {
                        String name = editNameTextView.getText().toString().trim();
                        String password = editPasswordTextView.getText().toString().trim();
                        boolean auto = checkAutoSignIn.isChecked();

                        mPresenter.checkPassword(name, password, auto);
                    })
                    .setPositiveButton(R.string.button_cancel, (dialog1, which) -> dialog1.dismiss())
                    .create();

            dialog.show();
        } else {
            mPresenter.checkPassword(null, null, true);
        }
    }

    @Override
    public void setPresenter(NoteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void resultCheckPassword(boolean check) {
        if (check) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null);
            EditText editTitleTextView = view.findViewById(R.id.edit_title);
            EditText editContentTextView = view.findViewById(R.id.edit_content);
            EditText editDateTextView = view.findViewById(R.id.edit_date);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setTitle(R.string.title_add_note)
                    .setCancelable(false)
                    .setNegativeButton(R.string.button_save, (dialog12, which) -> {
                        String title = editTitleTextView.getText().toString().trim();
                        String content = editContentTextView.getText().toString().trim();
                        String date = editDateTextView.getText().toString().trim();

                        Note note = new Note(title, content, date);
                        mPresenter.saveNotes(note);
                    })
                    .setPositiveButton(R.string.button_cancel, (dialog1, which) -> dialog1.dismiss())
                    .create();

            dialog.show();
        } else {
            new MaterialToast(this)
                    .setMessage(getString(R.string.error_text_sign_in))
                    .setIcon(R.mipmap.ic_launcher)
                    .setDuration(Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void errorCheckPassword() {
        new MaterialToast(this)
                .setMessage(getString(R.string.error_text_database))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void addNote(Note note) {
        Log.v(NoteActivity.class.getSimpleName(), "Add note - " + note.getTitle() + " / " + note.getContent() + " / " + note.getDate() + " / " + note.getAuthor());
    }

    @Override
    public void errorAddNote() {
        new MaterialToast(this)
                .setMessage(getString(R.string.error_text_add_note))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }
}
