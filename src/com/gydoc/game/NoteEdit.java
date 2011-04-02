package com.gydoc.game;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 */
public class NoteEdit extends Activity {

    private EditText titleText;
    private EditText bodyText;
    private Long rowId;
    private NotesDbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);
        titleText = (EditText) findViewById(R.id.title);
        bodyText = (EditText) findViewById(R.id.body);
        dbHelper = new NotesDbAdapter(this);
        dbHelper.open();

        rowId = savedInstanceState == null ? null : (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (rowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                rowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
            }
        }
        populateFields();

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(NotesDbAdapter.KEY_TITLE, titleText.getText().toString());
//                bundle.putString(NotesDbAdapter.KEY_BODY, bodyText.getText().toString());
//                if (rowId != null) {
//                    bundle.putLong(NotesDbAdapter.KEY_ROWID, rowId);
//                }
//                Intent intent = new Intent();
//                intent.putExtras(bundle);
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    private void populateFields() {
        if (rowId != null) {
            Cursor cursor = dbHelper.fetchNote(rowId);
            startManagingCursor(cursor);
            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            bodyText.setText(cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, rowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();
        if (rowId == null) {
            long id = dbHelper.createNote(title, body);
            if (id > 0) {
                rowId = id;
            }
        } else {
            dbHelper.updateNote(rowId, title, body);
        }
    }
    
}
