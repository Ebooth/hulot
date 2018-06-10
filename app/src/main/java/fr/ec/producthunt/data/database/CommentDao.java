package fr.ec.producthunt.data.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fr.ec.producthunt.data.model.Comment;

import static fr.ec.producthunt.data.database.DataBaseContract.PostTable.CREATION_DATE_COLUMN;


public class CommentDao {
    private final ProductHuntDbHelper productHuntDbHelper;

    private final String SORTING_ORDER = CREATION_DATE_COLUMN + " DESC";


    public CommentDao(ProductHuntDbHelper productHuntDbHelper) {
        this.productHuntDbHelper = productHuntDbHelper;
    }

    public long save(Comment comment) {
        return productHuntDbHelper.getWritableDatabase()
                .replace(DataBaseContract.CommentTable.TABLE_NAME, null, comment.toContentValues());
    }

    public List<Comment> retrieveComments(long postId) {


        Cursor cursor = productHuntDbHelper.getReadableDatabase()
                .query(DataBaseContract.CommentTable.TABLE_NAME,
                        DataBaseContract.CommentTable.PROJECTIONS,
                        DataBaseContract.CommentTable.POST_ID_COLUMN + "=" + postId, null, null, null, SORTING_ORDER);

        List<Comment> comments = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                Comment comment = new Comment();

                comment.setId(cursor.getInt(0));
                comment.setPostId(cursor.getInt(1));
                comment.setBody(cursor.getString(2));
                comment.setFullName(cursor.getString(3));
                comment.setUserName(cursor.getString(4));
                comment.setHeadLine(cursor.getString(5));
                comment.setImageUrl(cursor.getString(6));
                comments.add(comment);

            } while (cursor.moveToNext());
        }

        cursor.close();


        return comments;
    }
}
