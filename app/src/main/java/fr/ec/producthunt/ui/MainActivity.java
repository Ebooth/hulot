package fr.ec.producthunt.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Post;

public class MainActivity extends AppCompatActivity implements PostFragment.Callback, CollectionFragment.Callback {

    private boolean towPane;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FrameLayout detailContainer = findViewById(R.id.home_detail_container);

        if (detailContainer != null) {
            towPane = true;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_detail_container, DetailPostFragment.getNewInstance(null))
                    .addToBackStack(null)
                    .commit();
        }

        else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_content, new PostFragment())
                    .addToBackStack(null)
                    .commit();
        }


        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()){
                            case R.id.nav_posts:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_activity_content, new PostFragment())
                                        .addToBackStack(null)
                                        .commit();


                                break;
                            case R.id.nav_collections:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_activity_content, new CollectionFragment())
                                        .addToBackStack(null)
                                        .commit();

                                break;
                        }

                        mDrawerLayout.closeDrawers();




                        return true;
                    }
                });
        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    public void onClickPost(Post post) {

        if (towPane) {
            DetailPostFragment detailPostFragment =
                    (DetailPostFragment) getSupportFragmentManager().findFragmentById(R.id.home_detail_container);

            if (detailPostFragment != null) {
                detailPostFragment.loadUrl(post.getPostUrl());
            }
        } else {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.POST_URL_KEY, post.getPostUrl());

            startActivity(intent);
        }
    }

    @Override
    public void onClickCollection(Collection collection){

        Intent intent = new Intent(MainActivity.this, DetailCollectionActivity.class);
        intent.putExtra("COLLECTION_ID", collection.getId());
        startActivity(intent);

    }



}
