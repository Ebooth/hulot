package fr.ec.producthunt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import fr.ec.producthunt.R;

public class DetailActivity extends AppCompatActivity {
  public static final String POST_URL_KEY = "post_url_key";
  private EditText searchBar;

  private String postUrl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detail_activity);

    searchBar = findViewById(R.id.search_bar);
    postUrl = obtainPostUrlFromIntent();
    FragmentManager fragmentManager = getSupportFragmentManager();

    fragmentManager.beginTransaction()
        .add(R.id.detail_container, DetailPostFragment.getNewInstance(obtainPostUrlFromIntent()))
        .commit();
  }

  private String obtainPostUrlFromIntent() {

    Intent intent = getIntent();
    if (intent.getExtras().containsKey(POST_URL_KEY)) {
      return intent.getExtras().getString(POST_URL_KEY);
    } else {
      throw new IllegalStateException("Il faut passer l'url du post");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.detail_post_activity_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }



  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {

      case R.id.display_url:
        searchBar.setVisibility(View.VISIBLE);
        searchBar.setText(postUrl);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
