package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ArrayList<SongObject> songs;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_artist);

        songs= new ArrayList<SongObject>();
        songs =  getIntent().getParcelableArrayListExtra("list");

        for(int i=0;i<songs.size();i++)songs.get(i).setmCoched(false);
        SongsAdapter adapter = new SongsAdapter(this, songs);

        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(adapter);

        Button load_btn = (Button)findViewById(R.id.load_btn);
        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putParcelableArrayListExtra("list",songs);
                setResult(RESULT_OK, result);
                finish();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
