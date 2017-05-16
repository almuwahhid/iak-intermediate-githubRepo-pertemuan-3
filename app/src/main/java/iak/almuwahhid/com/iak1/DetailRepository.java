
package iak.almuwahhid.com.iak1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import iak.almuwahhid.com.iak1.Controller.EventHandler;
import iak.almuwahhid.com.iak1.Helper.DatabaseHandler;
import iak.almuwahhid.com.iak1.Services.InsertService;

public class DetailRepository extends AppCompatActivity {

    String a, b, c, d, e;
    TextView name_repo, name_owner, desc;
    ImageView photo;
    EventHandler handler;
    DatabaseHandler dbHandler;
    SharedPreferences sp;
    int check_saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_repository);

        handler = new EventHandler();

        sp = getApplicationContext().getSharedPreferences("user", 0);
        if(!handler.auth(sp)){
            startActivity(new Intent(this, Login.class));
        }

        ambilIntent();
        handler = new EventHandler();
        dbHandler = new DatabaseHandler(this);

        name_repo = (TextView) findViewById(R.id.detail_repo_namerepo);
        name_owner = (TextView) findViewById(R.id.detail_repo_owner);
        desc = (TextView) findViewById(R.id.detail_repo_desc);
        photo = (ImageView) findViewById(R.id.detail_repo_img);

        name_repo.setText(a);
        name_owner.setText(b);
        desc.setText(c);
        Picasso.with(this)
                .load(e)
                .placeholder(android.R.drawable.ic_dialog_alert)
                .error(android.R.drawable.ic_dialog_alert)
                .into(photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Repositori");
    }

    private void ambilIntent(){
        Intent intent = getIntent();
        a = intent.getStringExtra("name_repo");
        b = intent.getStringExtra("name_owner");
        c = intent.getStringExtra("desc");
        d = intent.getStringExtra("url");
        e = intent.getStringExtra("photo");
        check_saved = intent.getIntExtra("saved", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_toolbar :
                shareText(d);
                break;
            case R.id.browser_toolbar :
                openWebPage(d);
                break;
            case R.id.service_toolbar :
                Bundle bundle = new Bundle();
                bundle.putString("a", a);
                bundle.putString("b", b);
                bundle.putString("c", c);
                bundle.putString("d", d);
                bundle.putString("e", e);
                bundle.putString("f", sp.getString("username", null));
                Intent intent = new Intent(DetailRepository.this, InsertService.class);
                intent.putExtras(bundle);

                Log.d("freak", "onOptionsItemSelected: bener");
                startService(intent);
                break;
            case R.id.add_toolbar:
                if(check_saved == 1){
                    Toast.makeText(this, "Sudah Disimpan", Toast.LENGTH_SHORT).show();
                }else{
                    if(dbHandler.addData(a,b,c,d,e,sp.getString("username", null))){
                        Toast.makeText(this, "Berhasil tambah data", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private void shareText(String textToShare) {
        String mimeType = "text/plain";
        String title = "Learning How to Share";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }
}
