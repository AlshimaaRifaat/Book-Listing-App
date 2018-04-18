package com.example.shosho.myapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shosho on 2/5/2018.
 */

public class Book implements Parcelable {
    public String title;
    public ArrayList<String> author;

    protected Book(Parcel in) {
        title = in.readString();
        author = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book() {

    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeStringList(author);
    }

}
