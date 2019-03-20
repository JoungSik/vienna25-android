package com.joung.vienna.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.joung.vienna.admin.model.User;
import com.joung.vienna.note.model.Note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class NotePresenter implements NoteContract.Presenter {

    private static final String TAG = NotePresenter.class.getSimpleName();
    private static final String PREF_NAME = TAG + "_user.pref";

    private static final String PREF_COLUMN_USER = TAG + "_user";

    private final Context mContext;
    private final NoteContract.View mView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private User mUser;

    NotePresenter(Context context, NoteContract.View view) {
        mContext = context;

        mView = view;
        mView.setPresenter(this);

        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String stringUser = pref.getString(PREF_COLUMN_USER, null);
        mUser = stringUser != null ? new Gson().fromJson(stringUser, User.class) : null;
    }

    @Override
    public void checkPassword(String name, String password, boolean autoSignIn) {
        if (mUser != null) {
            mView.resultCheckPassword(true);
        } else {

            if (name.isEmpty() || password.isEmpty()) {
                mView.resultCheckPassword(false);
                return;
            }

            databaseReference.child("user").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.checkPassword(password)) {
                        mUser = user;

                        if (autoSignIn) {
                            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(PREF_COLUMN_USER, new Gson().toJson(mUser));
                            editor.apply();
                        }

                        mView.resultCheckPassword(true);
                    } else {
                        mView.resultCheckPassword(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                    mView.errorCheckPassword();
                }
            });
        }
    }

    @Override
    public void getNotes() {
        databaseReference.child("note").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Note note = dataSnapshot.getValue(Note.class);
                mView.addNote(note);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveNotes(Note note) {
        if (note.isEmpty()) {
            mView.errorAddNote();
            return;
        }

        if (mUser != null) {
            note.setAuthor(mUser.getName());
        }
        databaseReference.child("note").push().setValue(note);
    }

    @Override
    public boolean checkLogin() {
        return mUser != null;
    }
}
