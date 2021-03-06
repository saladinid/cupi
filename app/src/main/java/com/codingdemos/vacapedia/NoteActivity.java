package com.codingdemos.vacapedia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingdemos.flowers.R;
import com.codingdemos.vacapedia.rest.RestApis;
import com.codingdemos.vacapedia.widgets.BusyDialog;

public class NoteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mDescription, mTitle;
    private ImageView edit_iv, delete_iv;
    private String id = null;
    private String title = null;
    private String content = null;
    private BusyDialog progressDialog;
    private boolean isProgressVisible;

    private void getIntentData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("_id");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getIntentData();
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mDescription = findViewById(R.id.tvDescription);
        mTitle = findViewById(R.id.tvTitle);
        mToolbar.setTitle("Note");
        edit_iv = (ImageView) findViewById(R.id.edit_iv);
        delete_iv = (ImageView) findViewById(R.id.delete_iv);
        edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(NoteActivity.this, EditNoteActivity.class);
                mIntent.putExtra("_id", id);
                mIntent.putExtra("title", title);
                mIntent.putExtra("content", content);
                NoteActivity.this.startActivity(mIntent);
            }
        });
        delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new BusyDialog(NoteActivity.this, false, "");
                progressDialog.show();
                final RequestQueue mRequestQueue;
                // Setup instance
                mRequestQueue = Volley.newRequestQueue(NoteActivity.this);
                String url = RestApis.KarmaGroups.vacapediaNotes + "/" + id;
                StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener < String > () {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                // response
                                Intent in = new Intent(NoteActivity.this, NotesActivity.class);
                                NoteActivity.this.startActivity( in );
                                NoteActivity.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error.
                            }
                        }
                );
                mRequestQueue.add(dr);
            }
        });
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mTitle.setText(title);
            mDescription.setText(content);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_account_box_black_24dp);
            mToolbar.setOverflowIcon(drawable);
        }
    }

}
