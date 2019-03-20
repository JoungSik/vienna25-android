package com.joung.vienna.note.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joung.vienna.R;
import com.joung.vienna.note.model.Note;
import com.joung.vienna.note.model.NoteDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>
        implements NoteDataModel {

    private Context mContext;
    private ArrayList<Note> mList = new ArrayList<>();

    public NoteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void addNote(Note note) {
        mList.add(note);

        Descending descending = new Descending(mContext);
        Collections.sort(mList, descending);

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_note_title)
        TextView mTextTitle;

        @BindView(R.id.text_note_content)
        TextView mTextContent;

        @BindView(R.id.text_note_date)
        TextView mTextDate;

        @BindView(R.id.text_note_author)
        TextView mTextAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            Note note = mList.get(position);
            if (note != null) {
                mTextTitle.setText(note.getTitle());
                mTextContent.setText(note.getContent());
                mTextDate.setText(note.getDate());
                mTextAuthor.setText(note.getAuthor());
            }
        }
    }

    class Descending implements Comparator<Note> {

        private Context mContext;

        Descending(Context context) {
            mContext = context;
        }

        @Override
        public int compare(Note o1, Note o2) {
            String dateTimeFormat = mContext.getString(R.string.format_date);
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat, Locale.KOREA);

            try {
                Date o1DayDate = dateFormat.parse(o1.getDate());
                Date o2DayDate = dateFormat.parse(o2.getDate());

                return o1DayDate.compareTo(o2DayDate);
            } catch (ParseException e) {
                Log.e(NoteAdapter.class.getSimpleName(), "e - " + e.toString());
            }

            return 0;
        }
    }
}
