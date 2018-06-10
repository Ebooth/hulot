package fr.ec.producthunt.data;

import android.app.Application;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.ec.producthunt.data.database.CollectionDao;
import fr.ec.producthunt.data.database.CommentDao;
import fr.ec.producthunt.data.database.PostDao;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.data.model.Post;

import static android.content.ContentValues.TAG;

public class DataProvider {
    private static final String ACCESS_TOKEN = "access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";


    private static final String API_URL = "https://api.producthunt.com/v1/";

    private static final String POST_API_END_POINT =
            API_URL + "posts?" + ACCESS_TOKEN;

    private static final String COLLECTION_API_END_POINT =
            API_URL + "collections?" + ACCESS_TOKEN;




    private JsonPostParser jsonPostParser = new JsonPostParser();
    private JsonCollectionParser jsonCollectionParser = new JsonCollectionParser();
    private JsonCommentParser jsonCommentParser = new JsonCommentParser();


    private final PostDao postDao;
    private final CollectionDao collectionDao;
    private final CommentDao commentDao;

    private static DataProvider dataProvider;

    public static DataProvider getInstance(Application application) {

        if (dataProvider == null) {
            dataProvider = new DataProvider(ProductHuntDbHelper.getInstance(application));
        }
        return dataProvider;
    }

    public DataProvider(ProductHuntDbHelper dbHelper) {

        postDao = new PostDao(dbHelper);
        collectionDao = new CollectionDao(dbHelper);
        commentDao = new CommentDao(dbHelper);
    }

    private String getDataFromWeb(String apiUrl) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Contiendra la réponse JSON brute sous forme de String .
        String posts = null;

        try {
            // Construire l' URL de l'API ProductHunt
            URL url = new URL(apiUrl);

            // Creer de la requête http vers  l'API ProductHunt , et ouvrir la connexion
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lire le  input stream et le convertir String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Si le stream est vide, on revoie null;
                return null;
            }
            posts = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return posts;
    }

    public List<Post> getPostsFromDatabase() {
        return postDao.retrievePosts();
    }



    public Boolean syncPost() {

        List<Post> list = jsonPostParser.jsonToPosts(getDataFromWeb(POST_API_END_POINT));

        int nb = 0;

        for (Post post : list) {
            postDao.save(post);
            nb++;
        }
        return nb > 0;
    }

    public Boolean syncNewPost(long lastPostId) {
        String NEW_POST_API_END_POINT = API_URL + "posts/all?newer=" + lastPostId + "&" +  ACCESS_TOKEN;
        List<Post> list = jsonPostParser.jsonToPosts(getDataFromWeb(NEW_POST_API_END_POINT));

        int nb = 0;

        for (Post post : list) {
            postDao.save(post);
            nb++;
        }
        return nb > 0;
    }

    public List<Collection> getCollectionsFromDatabase() {
        return collectionDao.retrieveCollections();
    }

    public Boolean syncCollection() {
        List<Collection> list = jsonCollectionParser.jsonToCollections(getDataFromWeb(COLLECTION_API_END_POINT));

        int nb = 0;

        for (Collection collection : list) {
            //Log.d("DataProvider", collection.toString());
            collectionDao.save(collection);
            nb++;
        }
        return nb > 0;


    }





    public List<Comment> getCommentsFromDatabase(long postId){
        return commentDao.retrieveComments(postId);
    }

    public Boolean syncComments(long id){
        String COMMENTS_API_END_POINTS = API_URL + "posts/" + id + "/comments?" + ACCESS_TOKEN;
        Log.d("DataProvider", COMMENTS_API_END_POINTS);

        List<Comment> list = jsonCommentParser.jsonToComments(getDataFromWeb(COMMENTS_API_END_POINTS));

        int nb = 0;

        for (Comment comment : list) {

            //Log.d("DataProvider", comment.toString());
            commentDao.save(comment);
            nb++;
        }
        return nb > 0;
    }

    public Boolean syncNewComments(long postId, long lastCommentId){
        String NEW_COMMENTS_API_END_POINTS = API_URL + "posts/" + postId + "/comments?" + ACCESS_TOKEN + "&newer=" + lastCommentId;


        List<Comment> list = jsonCommentParser.jsonToComments(getDataFromWeb(NEW_COMMENTS_API_END_POINTS));

        int nb = 0;

        for (Comment comment : list) {
            //Log.d("DataProvider", collection.toString());
            Log.d("DataProvider", comment.toString());
            commentDao.save(comment);
            nb++;
        }
        Log.d("DataProvider","new comments retrieved : " + nb);
        return nb > 0;
    }

    public List<Post> fetchPostsFromCollection(long collectionId){
        String POST_FROM_COLLECTION_API_END_POINT = API_URL + "collections/" + collectionId + "?" + ACCESS_TOKEN;
        Log.d("DataProvider", POST_FROM_COLLECTION_API_END_POINT);

        List<Post> list = jsonPostParser.jsonCollectionToPosts(getDataFromWeb(POST_FROM_COLLECTION_API_END_POINT));


        for(Post post : list){
            Log.d("DataProvider", list.toString());
        }
        return list;
    }

}

