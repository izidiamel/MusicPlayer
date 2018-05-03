package com.example.android.musicplayer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amel on 25/03/2018.
 */

public class SongsAdapter extends ArrayAdapter<SongObject> {
    private SongObject song;
    private boolean[] selected;
    public SongsAdapter(Activity context, ArrayList<SongObject> word_list) {

        super(context, 0, word_list);

        selected = new boolean[word_list.size()];

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;


        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        song = getItem(position);

        TextView artistName = (TextView) listItemView.findViewById(R.id.nom_artist);
        artistName.setText(song.getArtistName());


        TextView songName = (TextView) listItemView.findViewById(R.id.nom_chanssant);
        songName.setText(song.getSongName());


        ImageView artistImage = (ImageView)listItemView.findViewById(R.id.profile_image);
        artistImage.setImageResource(song.getImageRessourceId());

        CheckBox check = (CheckBox)listItemView.findViewById(R.id.check);
        check.setTag(position);

        check.setChecked(selected[position]);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selected[(Integer) v.getTag()] = true;
                    getItem((Integer) v.getTag()).setmCoched(true);
                }
                else {
                    selected[(Integer) v.getTag()] = false;
                    getItem((Integer) v.getTag()).setmCoched(false);
                }
            }
        });
        return listItemView;
    }
}
