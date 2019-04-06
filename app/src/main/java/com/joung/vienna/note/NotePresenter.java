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
import com.google.gson.JsonObject;
import com.joung.vienna.R;
import com.joung.vienna.admin.model.User;
import com.joung.vienna.note.model.Note;
import com.joung.vienna.note.model.NoteDataModel;
import com.joung.vienna.retrofit.APIClient;
import com.joung.vienna.retrofit.BaseUrl;
import com.joung.vienna.retrofit.FCMAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class NotePresenter implements NoteContract.Presenter {

    private static final String TAG = NotePresenter.class.getSimpleName();
    private static final String PREF_NAME = TAG + "_user.pref";

    private static final String PREF_COLUMN_USER = TAG + "_user";

    private final Context mContext;
    private final NoteContract.View mView;
    private final NoteDataModel mNoteDataModel;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private User mUser;
    private String mFCMKey = null;

    NotePresenter(Context context, NoteContract.View view, NoteDataModel noteDataModel) {
        mView = view;
        mView.setPresenter(this);

        mContext = context;

        mNoteDataModel = noteDataModel;

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
        mView.showProgressBar();

        databaseReference.child("note").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Note note = dataSnapshot.getValue(Note.class);
                if (dataSnapshot.getKey() != null && note != null) {
                    long key = Long.valueOf(dataSnapshot.getKey().split("-")[1]);
                    note.setKey(key);
                }
                mNoteDataModel.addNote(note);
                mView.hideProgressBar();
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
        if (note.isEmpty() && mFCMKey != null) {
            mView.errorAddNote();
            return;
        }

        if (mUser != null) {
            note.setAuthor(mUser.getName());
        }

        String dateFormat = mContext.getString(R.string.format_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.KOREA);

        Date currentTime = Calendar.getInstance().getTime();
        try {
            Date dayDate = simpleDateFormat.parse(note.getDate());
            String key = dayDate.getTime() + "-" + currentTime.getTime();
            sendFCM(note);
            databaseReference.child("note").child(key).setValue(note);
        } catch (ParseException e) {
            Log.e(TAG, "e - " + e.toString());
            mView.errorAddNote();
        }
    }

    @Override
    public boolean checkLogin() {
        return mUser != null;
    }

    @Override
    public void getFCMKey() {
        databaseReference.child("fcm_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFCMKey = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "FCM Cancelled - " + databaseError.getMessage());
            }
        });
    }

    private void sendFCM(Note note) {
        Retrofit retrofit = APIClient.getClient();
        FCMAPI fcmAPI = retrofit.create(FCMAPI.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(BaseUrl.PARAM_TO, "/topics/"
                + mContext.getString(R.string.vienna_notification_channel_topic));

        JsonObject notificationJsonObject = new JsonObject();
        notificationJsonObject.addProperty(BaseUrl.PARAM_TITLE, note.getTitle());
        notificationJsonObject.addProperty(BaseUrl.PARAM_BODY, note.getContent());

        jsonObject.add(BaseUrl.PARAM_NOTIFICATION, notificationJsonObject);

        fcmAPI.sendFCM("key=" + mFCMKey, jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "response - " + response.toString());
                    mView.errorAddNote();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "response - " + t.toString());
                mView.errorAddNote();
            }
        });
    }
}
