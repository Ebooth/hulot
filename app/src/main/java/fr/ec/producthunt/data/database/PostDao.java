package fr.ec.producthunt.data.database;

import android.database.Cursor;
import fr.ec.producthunt.data.model.Post;
import java.util.ArrayList;
import java.util.List;

import static fr.ec.producthunt.data.database.DataBaseContract.PostTable.CREATION_DATE_COLUMN;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class PostDao {

  private final ProductHuntDbHelper productHuntDbHelper;

  private final String SORTING_ORDER = CREATION_DATE_COLUMN + " DESC" ;

  public PostDao(ProductHuntDbHelper productHuntDbHelper) {
    this.productHuntDbHelper = productHuntDbHelper;
  }

  public long save(Post post) {
    return productHuntDbHelper.getWritableDatabase()
        .replace(DataBaseContract.PostTable.TABLE_NAME, null, post.toContentValues());
  }

  public List<Post> retrievePosts() {

    Cursor cursor = productHuntDbHelper.getReadableDatabase()
        .query(DataBaseContract.PostTable.TABLE_NAME,
            DataBaseContract.PostTable.PROJECTIONS,
            null, null, null, null, SORTING_ORDER);

    List<Post> posts = new ArrayList<>(cursor.getCount());

    if (cursor.moveToFirst()) {
      do {

        Post post = new Post();

        post.setId(cursor.getInt(0));
        post.setTitle(cursor.getString(1));
        post.setSubTitle(cursor.getString(2));
        post.setImageUrl(cursor.getString(3));
        post.setPostUrl(cursor.getString(4));
        post.setCommentsCount(cursor.getInt(5));

        posts.add(post);


      } while (cursor.moveToNext());
    }

    cursor.close();

    return posts;
  }

  public List<Post> retrievePosts(long collectionId) {

    Cursor cursor = productHuntDbHelper.getReadableDatabase()
            .query(DataBaseContract.PostTable.TABLE_NAME,
                    DataBaseContract.PostTable.PROJECTIONS,
                    DataBaseContract.PostTable.ID_COLUMN + "=" + collectionId, null, null, null, SORTING_ORDER);

    List<Post> posts = new ArrayList<>(cursor.getCount());

    if (cursor.moveToFirst()) {
      do {

        Post post = new Post();

        post.setId(cursor.getInt(0));
        post.setTitle(cursor.getString(1));
        post.setSubTitle(cursor.getString(2));
        post.setImageUrl(cursor.getString(3));
        post.setPostUrl(cursor.getString(4));
        post.setCommentsCount(cursor.getInt(5));

        posts.add(post);


      } while (cursor.moveToNext());
    }

    cursor.close();

    return posts;
  }
}
