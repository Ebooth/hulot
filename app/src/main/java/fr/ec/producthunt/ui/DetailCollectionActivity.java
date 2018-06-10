package fr.ec.producthunt.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Post;

public class DetailCollectionActivity extends AppCompatActivity implements PostFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_content, PostFragment.getNewInstance(getIntent().getLongExtra("COLLECTION_ID", 0)))
                .commit();


    }

    @Override
    public void onClickPost(Post post) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.POST_URL_KEY, post.getPostUrl());

        startActivity(intent);

    }
}
