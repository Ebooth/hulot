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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Comment;



public class CommentFragment extends Fragment {
    private CommentAdapter commentAdapter;
    private ListView mListView;
    private DataProvider dataProvider;
    private SyncCommentReceiver syncCommentReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private long postId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.comments_list_fragment, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefreshlayout);
        commentAdapter = new CommentAdapter();
        syncCommentReceiver = new SyncCommentReceiver();

        mListView = rootView.findViewById(R.id.comments_list);



        mListView.setAdapter(commentAdapter);

        postId = this.getArguments().getLong("POST_ID");


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long lastCommentId = ((Comment)commentAdapter.getItem(0)).getId();
                SyncService.startSyncNewComments(getContext(), postId, lastCommentId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });





        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProvider = DataProvider.getInstance(getActivity().getApplication());
        loadComments(postId);
    }
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncCommentReceiver.ACTION_LOAD_COMMENTS);
        LocalBroadcastManager.getInstance(this.getContext())
                .registerReceiver(syncCommentReceiver, intentFilter);
        refreshComments();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncCommentReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                refreshComments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadComments(long postId) {
        FetchCommentsAsyncTask fetchPostsAsyncTask = new FetchCommentsAsyncTask();
        Log.d("CommentFragment", "executing fetch comments from database , id : " + postId);
        fetchPostsAsyncTask.execute(postId);
    }

    public class SyncCommentReceiver extends BroadcastReceiver {
        public static final String ACTION_LOAD_COMMENTS = "fr.ec.producthunt.data.action.LOAD_COMMENTS";


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_LOAD_COMMENTS)) {
                loadComments(postId);
            }
        }
    }

    private void refreshComments() {
        SyncService.startSyncComments(getContext(), postId);
    }



    private class FetchCommentsAsyncTask extends AsyncTask<Long, Void, List<Comment>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //viewAnimator.setDisplayedChild(PROGRESS_CHILD);
        }

        @Override
        protected List<Comment> doInBackground(Long... params) {
            long postId = params[0];
            Log.d("CommentFragment", "postId : " + postId);


            return dataProvider.getCommentsFromDatabase(postId);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            for(Comment comment : comments){
                Log.d("CommentFragment", comment.toString());
            }
            if (comments != null && !comments.isEmpty()) {
                commentAdapter.showComments(comments);
            }
            //viewAnimator.setDisplayedChild(LIST_CHILD);
        }
    }


}
