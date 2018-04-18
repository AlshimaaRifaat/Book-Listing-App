package com.example.shosho.myapplication2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String BookBaseURL="https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    ArrayList<Book> books1;
    ListView listView;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null)
        {
            books1=new ArrayList<>();
            books1=savedInstanceState.getParcelableArrayList("myBooks");
            if(books1!=null) {
                Parcelable state=savedInstanceState.getParcelable("myList");
                BookCustomListAdapter bookCustomListAdapter = new BookCustomListAdapter(MainActivity.this, R.layout.one_book, books1);
                listView.setAdapter(bookCustomListAdapter);
                listView.onRestoreInstanceState(state);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText=(EditText) findViewById(R.id.search_edittext);
        Button button=(Button) findViewById(R.id.searchButton);
        listView=(ListView) findViewById(R.id.BookList);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Query = editText.getText().toString();
                if(!Query.equals(""))
                {
                    String Search=BookBaseURL + Query;

                    //check if there is internet or not
                    ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if(activeNetworkInfo != null &&activeNetworkInfo.isConnected())
                    {
                        BookDataTask bookDataTask=new BookDataTask();
                        bookDataTask.execute(Search);

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    class BookDataTask extends AsyncTask<String,Void,ArrayList<Book>>
    {

        @Override
        protected ArrayList<Book> doInBackground(String... strings)
        {

            HttpURLConnection urlConnection = null;
            BufferedReader reader=null ;
            String JsonObj ;

            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                {
                    JsonObj = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0)
                {
                    JsonObj = null;
                }
                JsonObj = buffer.toString();

            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
                JsonObj=null;
            }
            catch (MalformedURLException e)
            {

                e.printStackTrace();
                JsonObj=null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                JsonObj=null;
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (reader != null)
                {
                    try {
                        reader.close();
                    }
                    catch (final IOException e) {}
                }
            }
            //convert jeson Arraylist
            ArrayList<Book> books = new ArrayList<>();
            try
            {
                if(JsonObj!=null) {
                    JSONObject itemsObj = new JSONObject(JsonObj);
                    if (!itemsObj.isNull("items")) {
                        JSONArray jsonArray = itemsObj.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject items = jsonArray.getJSONObject(i);
                            JSONObject volumeInfo = items.getJSONObject("volumeInfo");
                            String title = volumeInfo.getString("title");
                            ArrayList<String> authors = new ArrayList<>();
                            if (!volumeInfo.isNull("authors")) {
                                JSONArray Authors = volumeInfo.getJSONArray("authors");

                                for (int j = 0; j < Authors.length(); j++) {
                                    authors.add(Authors.getString(j));
                                }
                            }
                            Book book = new Book();
                            book.setTitle(title);
                            book.setAuthor(authors);
                            books.add(book);
                        }

                    }
                }
                else {
                    books=null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            listView=(ListView) findViewById(R.id.BookList);
            books1=new ArrayList<>();
            books1=books;
            BookCustomListAdapter bookCustomListAdapter=new BookCustomListAdapter(MainActivity.this,R.layout.one_book,books);
            listView.setAdapter(bookCustomListAdapter);

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("myBooks",books1);
        outState.putParcelable("myList",listView.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
