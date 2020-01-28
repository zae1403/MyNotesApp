package com.zaelani.consumerapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zaelani.consumerapp.adapter.NoteAdapater;
import com.zaelani.consumerapp.db.DatabaseContract;
import com.zaelani.consumerapp.entity.Note;
import com.zaelani.consumerapp.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback {

    private ProgressBar progressBar;
    private RecyclerView rvNotes;
    private NoteAdapater adapater;
    private FloatingActionButton fabAdd;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notes");

            progressBar = findViewById(R.id.progressbar);
            rvNotes = findViewById(R.id.rv_notes);
            rvNotes.setLayoutManager(new LinearLayoutManager(this));
            rvNotes.setHasFixedSize(true);
            adapater = new NoteAdapater(this);
            rvNotes.setAdapter(adapater);

            fabAdd = findViewById(R.id.fab_add);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
                    startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD);
                }
            });
        }

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler,this);
        getContentResolver().registerContentObserver(DatabaseContract.NoteColumns.CONTENT_URI, true,myObserver);

        if (savedInstanceState == null) {
            //proses ambil data
            new LoadNotesAsync(this, this).execute();
        } else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapater.setListNotes(list);
            }
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapater.getListNotes());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            //akan dipanggil jika request codenya ADD
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                    adapater.addItem(note);
                    rvNotes.smoothScrollToPosition(adapater.getItemCount() - 1);
                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            }
            //update dan delete memiliki request code sama akan tetapi result codenya berbeda
            else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    adapater.updateItem(position, note);
                    rvNotes.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil di ubah");
                } else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);
                    adapater.removeItem(position);
                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0) {
            adapater.setListNotes(notes);
        } else {
            adapater.setListNotes(new ArrayList<Note>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<Note>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;

        public LoadNotesAsync(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.NoteColumns.CONTENT_URI, null, null,null,null);
            return MappingHelper.mapCursorToArraylist(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);

            weakCallback.get().postExecute(notes);
        }
    }

    public static class DataObserver extends ContentObserver{

        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNotesAsync(context, (LoadNotesCallback) context).execute();
        }
    }
}

interface LoadNotesCallback {
    void preExecute();

    void postExecute(ArrayList<Note> notes);
}
