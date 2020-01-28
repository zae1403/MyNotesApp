package com.zaelani.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaelani.mynotesapp.CustomOnItemClickListener;
import com.zaelani.mynotesapp.NoteAddUpdateActivity;
import com.zaelani.mynotesapp.R;
import com.zaelani.mynotesapp.entity.Note;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapater extends RecyclerView.Adapter<NoteAdapater.NoteViewHolder> {
    private ArrayList<Note> listNotes = new ArrayList<>();
    private Activity activity;

    public NoteAdapater(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {
        if (listNotes.size()>0){
            this.listNotes.clear();
        }
        this.listNotes = listNotes;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapater.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapater.NoteViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.tvDate.setText(note.getDate());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE,listNotes.get(position));
                activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);

            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public void addItem(Note note){
        this.listNotes.add(note);
        notifyItemInserted(listNotes.size()-1);
    }

    public void updateItem(int position, Note note){
        this.listNotes.set(position,note);
        notifyItemChanged(position,note);
    }

    public void removeItem(int position){
        this.listNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listNotes.size());
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle,tvDescription,tvDate;
        final CardView cvNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cardView);
        }
    }
}
