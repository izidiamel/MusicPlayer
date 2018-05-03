package com.example.android.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Home on 04/03/2018.
 */

public  class SongObject implements Parcelable {

    private int mImageRessourceId;
    private String mArtistName;
    private String mSongName;
    private int mAudioResourceId;
    private int  mCoched;

     public  SongObject( String artistName , String songName ,int audioId,int imageId,int iscoched){
        mImageRessourceId=imageId;
        mArtistName=artistName;
        mSongName=songName;
        mAudioResourceId=audioId;
        mCoched=iscoched;

    }


    public boolean isCoched(){
        boolean t=false;
        if(mCoched==0)t= false ;
       if(mCoched==1)t= true;
       return t;
    }
    public void   setmCoched(boolean state){
        if(state)mCoched=1;
        if(!state)mCoched=0;
    }

    public int    getAudioRessourceId(){
        return mAudioResourceId;
    }
    public int    getImageRessourceId(){
        return mImageRessourceId;
    }
    public int    getmCoched(){
        return mCoched;
    }
    public String getArtistName(){
        return mArtistName ;
    }
    public String getSongName(){
        return  mSongName;
    }


    public  SongObject (Parcel in){
        this.mArtistName = in.readString();
        this.mSongName= in.readString();
        this.mAudioResourceId=in.readInt();
        this.mImageRessourceId=in.readInt();
        this.mCoched=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mArtistName);
        dest.writeString(mSongName);
        dest.writeInt(mAudioResourceId);
        dest.writeInt(mImageRessourceId);
        dest.writeInt(mCoched);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SongObject createFromParcel(Parcel in) {
            return new SongObject(in);
        }

        public SongObject[] newArray(int size) {
            return new SongObject[size];
        }
    };

}
