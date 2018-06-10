package fr.ec.producthunt.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static fr.ec.producthunt.ui.CommentFragment.SyncCommentReceiver.ACTION_LOAD_COMMENTS;
import static fr.ec.producthunt.ui.CollectionFragment.SyncCollectionReceiver.ACTION_LOAD_COLLECTIONS;
import static fr.ec.producthunt.ui.PostFragment.SyncPostReceiver.ACTION_LOAD_POSTS;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SyncService extends IntentService {
  private static final String ACTION_FETCH_POSTS =
      "fr.ec.producthunt.data.action.FETCH_POSTS";
  private static final String ACTION_FETCH_NEW_POSTS =
          "fr.ec.producthunt.data.action.FETCH_NEW_POSTS";
  private static final String ACTION_FETCH_NEW_COLLECTIONS =
          "fr.ec.producthunt.data.action.FETCH_NEW_COLLECTIONS";
  private static final String ACTION_FETCH_NEW_COMMENTS =
          "fr.ec.producthunt.data.action.FETCH_NEW_COMMENTS";
  private static final String ACTION_FETCH_COMMENTS =
          "fr.ec.producthunt.data.action.FETCH_COMMENTS";

  public SyncService() {
    super("SyncService");
  }

  @Override public void onCreate() {
    super.onCreate();
  }

  /**
   * Starts this service to perform action Foo with the given parameters. If
   * the service is already performing a task this action will be queued.
   *
   * @see IntentService
   */
  public static void startSyncPosts(Context context) {
    Intent intent = new Intent(context, SyncService.class);
    intent.setAction(ACTION_FETCH_POSTS);
    context.startService(intent);
  }
  public static void startSyncNewPosts(Context context, long lastPostId) {
    Intent intent = new Intent(context, SyncService.class);
    intent.setAction(ACTION_FETCH_NEW_POSTS);
    intent.putExtra("LAST_POST_ID", lastPostId);
    context.startService(intent);
  }

  public static void startSyncCollections(Context context) {
    Intent intent = new Intent(context, SyncService.class);
    intent.setAction(ACTION_FETCH_NEW_COLLECTIONS);
    context.startService(intent);
  }

  public static void startSyncComments(Context context, long postId) {
    Intent intent = new Intent(context, SyncService.class);
    intent.setAction(ACTION_FETCH_COMMENTS);
    intent.putExtra("POST_ID", postId);
    context.startService(intent);
  }

  public static void startSyncNewComments(Context context, long postId, long lastCommentId){
    Intent intent = new Intent(context, SyncService.class);
    intent.setAction(ACTION_FETCH_NEW_COMMENTS);
    intent.putExtra("POST_ID", postId);
    intent.putExtra("LAST_COMMENT_ID", lastCommentId);
    context.startService(intent);
  }

  @Override protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      if (ACTION_FETCH_POSTS.equals(action)) {
        handleActionFetchPosts();
      }

      if (ACTION_FETCH_NEW_POSTS.equals(action)) {
        long lastPostId = intent.getLongExtra("LAST_POST_ID", -1);
        handleActionFetchNewPosts(lastPostId);
      }


      if(ACTION_FETCH_NEW_COLLECTIONS.equals(action)){
        handleActionFetchNewCollections();
      }

      if(ACTION_FETCH_COMMENTS.equals(action)){
        long id = intent.getLongExtra("POST_ID", -1);
        if(id == -1) return;
        handleActionFetchComments(id);
      }

      if(ACTION_FETCH_NEW_COMMENTS.equals(action)){
        long postId = intent.getLongExtra("POST_ID", -1);
        long lastCommentId = intent.getLongExtra("LAST_COMMENT_ID", -1);
        if(postId == -1 || lastCommentId == -1) return;
        handleActionFetchNewComments(postId, lastCommentId);
      }

    }
  }

  /**
   * Handle action Foo in the provided background thread with the provided
   * parameters.
   */
  private void handleActionFetchPosts() {

    DataProvider.getInstance(this.getApplication()).syncPost();
    Intent intentToSend = new Intent();
    intentToSend.setAction(ACTION_LOAD_POSTS);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend);
  }

  private void handleActionFetchNewPosts(long lastPostId) {

    DataProvider.getInstance(this.getApplication()).syncNewPost(lastPostId);
    Intent intentToSend = new Intent();
    intentToSend.setAction(ACTION_LOAD_POSTS);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend);
  }

  private void handleActionFetchNewCollections() {

    DataProvider.getInstance(this.getApplication()).syncCollection();
    Intent intentToSend = new Intent();
    intentToSend.setAction(ACTION_LOAD_COLLECTIONS);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend);
  }

  private void handleActionFetchComments(long id){
    DataProvider.getInstance(this.getApplication()).syncComments(id);
    Intent intentToSend = new Intent();
    intentToSend.setAction(ACTION_LOAD_COMMENTS);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend);
  }

  private void handleActionFetchNewComments(long postId, long lastCommentId){
    DataProvider.getInstance(this.getApplication()).syncNewComments(postId, lastCommentId);
    Intent intentToSend = new Intent();
    intentToSend.setAction(ACTION_LOAD_COMMENTS);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend);
  }
}
