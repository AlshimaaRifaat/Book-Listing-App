package com.example.shosho.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shosho on 2/5/2018.
 */

public class AuthorCustomListAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> Author;
    public AuthorCustomListAdapter(Context context, int resource, ArrayList<String> objects)
    {

        super(context, resource, objects);
        this.context = context;
        this.Author=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.one_author, null);
        }
        String Author=getItem(position);
        TextView AuthorName=(TextView) v.findViewById(R.id.authorName);
        AuthorName.setText(Author);
        return v;

    }

}
