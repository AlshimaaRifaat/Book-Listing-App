package com.example.shosho.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shosho on 2/5/2018.
 */

public class BookCustomListAdapter extends ArrayAdapter<Book> {
    Context context;
    ArrayList<Book> books;

    public BookCustomListAdapter(Context context, int resource, ArrayList<Book> objects)
    {

        super(context, resource, objects);
        this.context = context;
        this.books=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.one_book, null);
        }
        Book book=getItem(position);
        TextView BookName=(TextView) v.findViewById(R.id.book_title);
        ListView authorList=(ListView) v.findViewById(R.id.authorList);
        BookName.setText(book.getTitle());
        AuthorCustomListAdapter authorCustomListAdapter=new AuthorCustomListAdapter(context,R.layout.one_author,book.author);
        authorList.setAdapter(authorCustomListAdapter);
        return v;

    }
}
