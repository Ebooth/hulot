package fr.ec.producthunt.data.model;

import android.content.ContentValues;

import java.sql.DatabaseMetaData;

import fr.ec.producthunt.data.database.DataBaseContract;

/**
 * Created by pauldejean on 12/03/2018.
 */

public class Comment {
    private long id;
    private long postId;
    private String body;
    private String userName;
    private String fullName;
    private String headLine;
    private String imageUrl;
    private String creationDate;

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getPostId() {return postId;}

    public void setPostId(long postId) {this.postId = postId;}

    public String getBody() {return body;}

    public void setBody(String body) {this.body = body;}

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getHeadLine() {return headLine;}

    public void setHeadLine(String headLine) {this.headLine = headLine;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getCreationDate() {return creationDate;}

    public void setCreationDate(String creationDate) {this.creationDate = creationDate;}

    public ContentValues toContentValues(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CommentTable.ID_COLUMN, id);
        contentValues.put(DataBaseContract.CommentTable.POST_ID_COLUMN, postId);
        contentValues.put(DataBaseContract.CommentTable.BODY_COLUMN, body);
        contentValues.put(DataBaseContract.CommentTable.USER_NAME_COLUMN, userName);
        contentValues.put(DataBaseContract.CommentTable.FULL_NAME_COLUMN, fullName);
        contentValues.put(DataBaseContract.CommentTable.IMAGE_URL_COLUMN, imageUrl);
        contentValues.put(DataBaseContract.CommentTable.HEADLINE_COLUMN, headLine);
        contentValues.put(DataBaseContract.CommentTable.CREATION_DATE_COLUMN, creationDate);

        return contentValues;
    }

    @Override
    public String toString() {
        return "comment - id : " + id + " , postId : " + postId + ", username : " + userName +  " , fullname : " + fullName +
                " , headline : " + headLine + " , image_url : " + imageUrl + " , body : + " + body  ;
    }

}
