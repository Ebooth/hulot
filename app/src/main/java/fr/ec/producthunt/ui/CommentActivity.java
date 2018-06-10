package fr.ec.producthunt.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.ec.producthunt.R;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String title = bundle.getString("POST_TITLE", "Commentaires");
        setTitle(title);

        Fragment commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.comments_activity_content, commentFragment)
                .commit();


    }
}
