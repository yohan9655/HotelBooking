package com.example.hotelbooking;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    View mDecorView;
    FloatingActionButton fab,fab1,fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isopen = false;
    LottieAnimationView animationView;
    ArrayList<String> listitems;
    public static ArrayAdapter adapter;
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null && adapter.getCount() > 0)
        adapter.clear();
        viewData();
        //fill your Collection with new data
        // setAdapter or notify your adapter
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDecorView = getWindow().getDecorView();
        animationView = findViewById(R.id.animation_view);
        animationView.setMinAndMaxProgress(0.2f,0.3f);
        animationView.playAnimation();
        animationView.reverseAnimationSpeed();
        //hideSystemUI();
        getWindow().setExitTransition(new Explode());


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black_overlay));

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);



        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }

        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());



            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView = (ListView) findViewById(R.id.list);
        listitems = new ArrayList<>();
        viewData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listView.getItemAtPosition(position).toString();
                int k = s.indexOf(" ", s.indexOf(" "));
                String res = s.substring(k,s.length()-3);
                delete(res);

            }
        });


    }

    public void delete(String s) {

        SQLiteDatabase db = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
        String query = "DELETE FROM CheckIn WHERE id =" + s;
        String query1 = "DELETE FROM facility WHERE id =" + s;
        db.execSQL(query1);
        db.execSQL(query);
        listitems.clear();
        viewData();
        adapter.notifyDataSetChanged();

    }

    public void viewData() {
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            String query = "SELECT * FROM CheckIn";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    listitems.add(cursor.getString(0) + "  " + cursor.getString(4) + "  " + cursor.getString(6));
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitems);
                    listView.setAdapter(adapter);

                }
            } else {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Log.i("error", e.toString());
        }



    }

    private void  animateFab(){
        if(isopen)
        {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isopen = false;
        }

        else
        {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);

            isopen = true;
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
