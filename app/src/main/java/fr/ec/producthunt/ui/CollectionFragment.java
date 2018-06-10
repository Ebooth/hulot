package fr.ec.producthunt.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Collection;

import static fr.ec.producthunt.ui.CollectionFragment.SyncCollectionReceiver.ACTION_LOAD_COLLECTIONS;



public class CollectionFragment extends Fragment {
    private DataProvider dataProvider;
    private CollectionAdapter collectionAdapter;
    private SyncCollectionReceiver syncPostReceiver;
    private ListView listView;
    private Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) getActivity();

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_collection_list_fragment, container, false);
        collectionAdapter = new CollectionAdapter();
        listView = rootView.findViewById(R.id.list_item);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Collection collection = (Collection) parent.getAdapter().getItem(position);
                callback.onClickCollection(collection);

            }
        });

        listView.setAdapter(collectionAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProvider = DataProvider.getInstance(getActivity().getApplication());
        syncPostReceiver = new SyncCollectionReceiver();
        loadCollections();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOAD_COLLECTIONS);
        LocalBroadcastManager.getInstance(this.getContext())
                .registerReceiver(syncPostReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncPostReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                refreshCollections();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SyncCollectionReceiver extends BroadcastReceiver {
        public static final String ACTION_LOAD_COLLECTIONS = "fr.ec.producthunt.data.action.LOAD_COLLECTIONS";



        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_LOAD_COLLECTIONS)) {
                loadCollections();
            }
        }
    }

    public interface Callback{
        void onClickCollection(Collection collection);
    }

    private void refreshCollections() {
        SyncService.startSyncCollections(getContext());
    }

    private void loadCollections() {
        FetchCollectionsAsyncTask fetchCollectionsAsyncTask = new FetchCollectionsAsyncTask();
        fetchCollectionsAsyncTask.execute();
    }

    private class FetchCollectionsAsyncTask extends AsyncTask<Void, Void, List<Collection>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //viewAnimator.setDisplayedChild(PROGRESS_CHILD);
        }

        @Override
        protected List<Collection> doInBackground(Void... params) {

            return dataProvider.getCollectionsFromDatabase();
        }

        @Override
        protected void onPostExecute(List<Collection> collections) {
            if (collections != null && !collections.isEmpty()) {
                collectionAdapter.showCollections(collections);
            }
            //viewAnimator.setDisplayedChild(LIST_CHILD);
        }
    }

}
