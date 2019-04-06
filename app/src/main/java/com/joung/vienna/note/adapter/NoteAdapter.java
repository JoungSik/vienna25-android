package com.joung.vienna.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joung.vienna.R;
import com.joung.vienna.note.model.Note;
import com.joung.vienna.note.model.NoteDataModel;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>
        implements NoteDataModel {

    private ArrayList<Note> mList = new ArrayList<>();

    public NoteAdapter() {
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

        Collections.sort(mList, (o1, o2) -> Long.compare(o2.getKey(), o1.getKey()));

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
}
