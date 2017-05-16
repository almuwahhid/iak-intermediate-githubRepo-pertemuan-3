
package iak.almuwahhid.com.iak1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import iak.almuwahhid.com.iak1.Adapter.SavedRepoAdapter;
import iak.almuwahhid.com.iak1.Controller.EventHandler;
import iak.almuwahhid.com.iak1.Helper.DatabaseHandler;
import iak.almuwahhid.com.iak1.kelas.Github;

public class SavedRepo extends AppCompatActivity {
    EventHandler evt;
    DatabaseHandler db_handler;

    RecyclerView rv;
    ArrayList<Github> all_data;
    SavedRepoAdapter adapter;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_repo);

        evt = new EventHandler();
        db_handler = new DatabaseHandler(this);
        sp = getApplicationContext().getSharedPreferences("user", 0);

        // Verifikasi
        if(!evt.auth(sp)){
            startActivity(new Intent(this, Login.class));
        }
        rv = (RecyclerView) findViewById(R.id.rv_saved);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        all_data = db_handler.getAllData(all_data, sp.getString("username", null));
        adapter = new SavedRepoAdapter(all_data, this);
        rv.setAdapter(adapter);

        getSupportActionBar().setTitle("Saved Repository");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
