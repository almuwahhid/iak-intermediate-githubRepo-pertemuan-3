
package iak.almuwahhid.com.iak1.Controller;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iak.almuwahhid.com.iak1.kelas.Github;

/**
 * Created by gueone on 5/6/2017.
 */

public class EventHandler {
    public ArrayList setToArray(ArrayList<Github> a, String x) throws JSONException {
        a = new ArrayList();
        JSONObject all_data = new JSONObject(x);
        JSONArray array_item = all_data.getJSONArray("items");
        for (int i =0; i<array_item.length();i++){
            Github github = new Github();
            JSONObject object_repo = array_item.getJSONObject(i);
            JSONObject owner_repo = object_repo.getJSONObject("owner");

            github.setUrl(object_repo.getString("html_url"));
            github.setName_repo(object_repo.getString("name"));
            github.setDesc(object_repo.getString("description"));
            github.setName_owner(owner_repo.getString("login"));
            github.setPhoto(owner_repo.getString("avatar_url"));
            Log.d("asd", "setToArray: ");
            a.add(github);
        }
        return a;
    }

    public boolean auth(SharedPreferences s){
        if(s.getString("username", null) != null){
            return true;
        }else{
            return false;
        }
    }

    public void setSESSION(SharedPreferences s, String username){
        SharedPreferences.Editor editor = s.edit();
        editor.putString("username", username);
        editor.commit();
    }
    public void setSESSIONNull(SharedPreferences s){
        SharedPreferences.Editor editor = s.edit();
        editor.putString("username", null);
        editor.commit();
    }
}
