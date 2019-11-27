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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.BuildConfig;
import com.example.biblio.R;
import com.example.biblio.adapters.MyEbooksAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import lrusso96.simplebiblio.core.Ebook;

import static com.example.biblio.helpers.SharedPreferencesHelper.MY_EBOOKS_TAG;

//todo: handle duplicates!
public class MyEbooksFragment extends Fragment implements MyEbooksAdapter.OnItemListener {

    private ArrayList<Ebook> mEbooks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_ebooks_fragment, container, false);

        RecyclerView mRecyclerView = v.findViewById(R.id.my_ebooks_rv);
        ImageView mImageTemplate = v.findViewById(R.id.iv_template);
        TextView mTextViewTemplate = v.findViewById(R.id.tv_template);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
        String response = sharedPreferences.getString(MY_EBOOKS_TAG, null);

        if (response != null) {
            mImageTemplate.setVisibility(View.INVISIBLE);
            mTextViewTemplate.setVisibility(View.INVISIBLE);

            mEbooks = new Gson().fromJson(response, new TypeToken<ArrayList<Ebook>>() {
            }.getType());
            Log.d("SharedPrefs", mEbooks.toString());

            if (mEbooks.isEmpty()) {
                mImageTemplate.setVisibility(View.VISIBLE);
                mTextViewTemplate.setVisibility(View.VISIBLE);
            } else {


                MyEbooksAdapter.OnItemListener mMyEbooksListener = this;

                MyEbooksAdapter mAdapter = new MyEbooksAdapter(mEbooks, mMyEbooksListener, getContext());
                mRecyclerView.setAdapter(mAdapter);

            }
        } else {
            mImageTemplate.setVisibility(View.VISIBLE);
            mTextViewTemplate.setVisibility(View.VISIBLE);
        }
        return v;
    }


    /**
     * Launches an ebook reader to open the file.
     *
     * @param filename  name of the file, already downloaded
     * @param extension file format (e.g. pdf)
     */
    private void openFile(@NotNull String filename, @NotNull String extension) {
        filename = String.format("%s.%s", filename, extension);
        File path = new File(String.format("%s/Biblio/%s", Environment.getExternalStorageDirectory(), filename));
        Log.d("openFile", path.toString());

        Intent in = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                BuildConfig.APPLICATION_ID + ".provider",
                path);

        //todo: what about other extensions supported by Mu?
        if (extension.equals("epub"))
            in.setDataAndType(uri, "application/epub+zip");
        else if (extension.equals("pdf"))
            in.setDataAndType(uri, "application/pdf");

        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(in);
    }

    @Override
    public void onItemClick(int position) {
        Ebook current = mEbooks.get(position);
        openFile(current.getTitle() + "_" + current.getAuthor() + "_" + current.getPublished().toString(), current.getDownloads().get(0).getExtension());
    }
}