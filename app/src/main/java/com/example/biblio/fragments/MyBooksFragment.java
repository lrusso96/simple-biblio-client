package com.example.biblio.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.biblio.BuildConfig;
import com.example.biblio.R;
import com.example.biblio.adapters.MyBooksAdapter;
import com.example.biblio.models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.ArrayList;

public class MyBooksFragment extends Fragment {
    private ListView mListView;
    private SharedPreferences sharedPreferences;
    private MyBooksAdapter mAdapter;
    private ImageView mImageTemplate;
    private TextView mTextViewTemplate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_books_fragment, container, false);

        mListView = v.findViewById(R.id.listView);
        mImageTemplate = v.findViewById(R.id.iv_template);
        mTextViewTemplate = v.findViewById(R.id.tv_template);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String response = sharedPreferences.getString("mybooks", null);

        if(response != null) {
            mImageTemplate.setVisibility(View.INVISIBLE);
            mTextViewTemplate.setVisibility(View.INVISIBLE);

            final ArrayList<Book> myBooks = new Gson().fromJson(response, new TypeToken<ArrayList<Book>>() {}.getType());
            Log.d("SharedPrefs", myBooks.toString());

            if(myBooks.isEmpty()) {
                mImageTemplate.setVisibility(View.VISIBLE);
                mTextViewTemplate.setVisibility(View.VISIBLE);
            }

            mAdapter = new MyBooksAdapter(getContext(), myBooks);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(), ((Book) adapterView.getItemAtPosition(i)).toString(), Toast.LENGTH_LONG).show();
                    openFile(myBooks.get(i).getTitle() +"_" + myBooks.get(i).getAuthor() + "_" + myBooks.get(i).getPublication_year() + ".epub");
                }
            });
        } else {
            mImageTemplate.setVisibility(View.VISIBLE);
            mTextViewTemplate.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void openFile(String filename) {
        File path = new File(Environment.getExternalStorageDirectory() + "/biblioData/" + filename);
        Log.d("openFile", path.toString());

        Intent in = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                path);
        in.setDataAndType(uri, "application/epub+zip");
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(in);
    }
}
