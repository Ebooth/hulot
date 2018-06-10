package fr.ec.producthunt.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.data.model.Comment;




public class JsonCommentParser {
    public List<Comment> jsonToComments(String json) {

        try {

            JSONObject commentResponse = new JSONObject(json);
            JSONArray commentsJson = commentResponse.getJSONArray("comments");

            int size = commentsJson.length();

            ArrayList<Comment> comments = new ArrayList(size);

            for (int i = 0; i < commentsJson.length(); i++) {
                JSONObject commentJson = (JSONObject) commentsJson.get(i);

                comments.add(jsonToComment(commentJson));
            }

            return comments;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Comment jsonToComment(JSONObject commentJson) throws JSONException {
        Comment comment = new Comment();
        //"thumbnail": {
        //  "id": 139278,
        //      "media_type": "image",
        //      "image_url": "https://ph-files.imgix.net/b27175ba-ff99-4099-9092-1e8e8fb1cc77?auto=format&w=430&h=570&fit=max",
        //      "metadata": {}


        comment.setId(commentJson.getInt("id"));
        comment.setBody(commentJson.getString("body"));
        comment.setCreationDate(commentJson.getString("created_at").split("\\.")[0]);
        comment.setPostId(commentJson.getLong("post_id"));


        JSONObject jsonUser = commentJson.getJSONObject("user");

        comment.setUserName(jsonUser.getString("username"));
        comment.setFullName(jsonUser.getString("name"));

        String headline = jsonUser.getString("headline") ;
        headline = headline.equals("null") ? "" : headline;
        comment.setHeadLine(headline);

        comment.setImageUrl(jsonUser.getJSONObject("image_url").getString("120px"));


        return comment;
    }
}
