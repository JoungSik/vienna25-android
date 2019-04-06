package com.joung.vienna.note;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joung.vienna.R;
import com.joung.vienna.note.adapter.NoteAdapter;
import com.joung.vienna.note.model.Note;
import com.marcoscg.materialtoast.MaterialToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteActivity extends AppCompatActivity implements NoteContract.View {

    @BindView(R.id.list_note)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_note)
    ProgressBar mProgressBar;

    @BindView(R.id.button_add_note)
    FloatingActionButton mFab;

    private NoteContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));

        NoteAdapter adapter = new NoteAdapter();
        mRecyclerView.setAdapter(adapter);

        new NotePresenter(this, this, adapter);
        mPresenter.getFCMKey();
        mPresenter.getNotes();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void resultCheckPassword(boolean check) {
        if (check) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null);

            EditText editTitleTextView = view.findViewById(R.id.edit_title);
            EditText editContentTextView = view.findViewById(R.id.edit_content);
            EditText editDateTextView = view.findViewById(R.id.edit_date);

            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            String currentDateString = sdf.format(currentDate);
            editDateTextView.setText(currentDateString);

            editDateTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editDateTextView.removeTextChangedListener(this);

                    String origin = s.toString();
                    String format = origin.replace(".", "");

                    if (format.length() > 6) {
                        format = format.substring(0, 4) + "." + format.substring(4, 6) + "." + format.substring(6, format.length());
                    } else if (format.length() > 4) {
                        format = format.substring(0, 4) + "." + format.substring(4, format.length());
                    }

                    editDateTextView.setText(format);
                    editDateTextView.setSelection(editDateTextView.getText().length());

                    editDateTextView.addTextChangedListener(this);
                }
            });

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
    public void errorDateFormat() {
        new MaterialToast(this)
                .setMessage(getString(R.string.error_text_date_format))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void errorAddNote() {
        new MaterialToast(this)
                .setMessage(getString(R.string.error_text_add_note))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
