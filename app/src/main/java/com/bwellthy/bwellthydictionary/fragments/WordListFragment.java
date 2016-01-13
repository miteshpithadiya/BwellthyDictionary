package com.bwellthy.bwellthydictionary.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bwellthy.bwellthydictionary.R;
import com.bwellthy.bwellthydictionary.adapters.WordListAdapter;
import com.bwellthy.bwellthydictionary.dao.DBHelper;
import com.bwellthy.bwellthydictionary.helpers.Utils;
import com.bwellthy.bwellthydictionary.models.Dictionary;
import com.bwellthy.bwellthydictionary.models.Word;
import com.bwellthy.bwellthydictionary.network.RestCallback;
import com.bwellthy.bwellthydictionary.network.RestClient;
import com.bwellthy.bwellthydictionary.network.RestError;
import com.bwellthy.bwellthydictionary.rest.DictionaryService;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

/**
 * Lists all the words in the dictionary. Created by miteshp on 13/01/2016.
 */
public class WordListFragment extends Fragment {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 101;

    /**
     * The Rest Client for making api calls.
     */
    private DictionaryService mDictionaryService;

    /**
     * The list of all the words.
     */
    private List<Word> mWordList;

    /**
     * Displays message when no data is available.
     */
    private TextView mtxtEmptyView;

    /**
     * The context of the hosting activity.
     */
    private Context mContext;

    /**
     * Displays progress while the data is fetched from the api.
     */
    private ProgressBar mProgressBar;


    private RecyclerView.Adapter mWordListAdapter;
    private SharedPreferences sharedPref;

    public WordListFragment() {
        // Empty Constructor
    }

    public static WordListFragment newInstance() {
        return new WordListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDictionaryService = RestClient.getRestAdapter().create(DictionaryService.class);
        mWordList = new ArrayList<>();
        mWordListAdapter = new WordListAdapter(mContext, mWordList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        mtxtEmptyView = (TextView) rootView.findViewById(R.id.txt_empty);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWordListAdapter);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(getString(R.string.db_exists), false)) {
            loadDataFromDB();
        } else {
            if (Utils.isNetworkAvailable(mContext)) {
                syncDictionary();
            } else {
                mProgressBar.setVisibility(View.GONE);
                mtxtEmptyView.setText(mContext.getString(R.string.no_network_available));
            }
        }

        return rootView;
    }

    /**
     * Gets the dictionary data from local DB.
     */
    private void loadDataFromDB() {
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                DBHelper dbHelper = new DBHelper(mContext);
                mWordList.clear();
                mWordList.addAll(dbHelper.getWords());
                setListAdapter();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgressBar.setVisibility(View.GONE);
            }
        }.execute();
    }

    /**
     * Gets all the activities by making an api call.
     */
    private void syncDictionary() {

        // Displaying the progress bar.
        mProgressBar.setVisibility(View.VISIBLE);

        mDictionaryService.getActivities(new RestCallback<Dictionary>() {

            @Override
            public void failure(RestError restError) {
                mProgressBar.setVisibility(View.GONE);
                mtxtEmptyView.setText(restError.getStrMessage());
            }

            @Override
            public void success(Dictionary dictionary, Response response) {
                mProgressBar.setVisibility(View.GONE);
                mWordList.clear();
                mWordList.addAll(dictionary.getWords());

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        DBHelper dbHelper = new DBHelper(mContext);
                        switch (dbHelper.bulkInsert(mWordList)) {
                            case DBHelper.SUCCESS:
                                sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(getString(R.string.db_exists), true);
                                editor.commit();
                                break;
                            case DBHelper.FAILURE:
                                Toast.makeText(mContext, "Sync Failed!", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                        }
                        return null;
                    }
                }.execute();
                setListAdapter();
            }
        });
    }

    /**
     * Re populates the list into adapter.
     */
    private void setListAdapter() {
        mWordListAdapter.notifyDataSetChanged();
    }

}
