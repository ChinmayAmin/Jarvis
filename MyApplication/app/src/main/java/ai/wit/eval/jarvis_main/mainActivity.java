package ai.wit.eval.jarvis_main;

import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

import org.json.*;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;
import com.koushikdutta.ion.*;
import com.koushikdutta.async.util.*;

public class mainActivity extends ActionBarActivity implements IWitListener {

    Wit _wit;
    private String serverIP = "";
    TextToSpeech ttobj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ImageView view = (ImageView)findViewById(R.id.imageView);
        Ion.with(view).load("http://i.imgur.com/EKV311B.gif");

        String accessToken = "QMHHCMETHDYV2E4F3YHQ2N5YDVJQVPVZ";
        _wit = new Wit(accessToken, this);

        //Retrieve the IP address and set it to that of server
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverIP = extras.getString("IP");
        }

        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        ttobj.setLanguage(Locale.UK);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggle(View v) {
        try {
            _wit.toggleListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void witDidGraspIntent(ArrayList<WitOutcome> witOutcomes, String messageId, Error error) {
//        TextView jsonView = (TextView) findViewById(R.id.jsonView);
//        jsonView.setMovementMethod(new ScrollingMovementMethod());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        if (error != null) {
//            jsonView.setText(error.getLocalizedMessage());
//            return ;
//        }
        String jsonOutput = gson.toJson(witOutcomes);
//        jsonView.setText(jsonOutput);
        //((TextView) findViewById(R.id.txtText)).setText("Done!");
        new backTask().execute(jsonOutput);
    }

    private class backTask extends android.os.AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String intent = "";
                for(String s : params)
                {
                    intent += s;
                }
                JSONArray arr = new JSONArray(intent);
                JSONObject obj = arr.getJSONObject(0);
                intent = obj.getString("intent");

                Socket client;
                PrintWriter printwriter;
                Log.d("SERVER",serverIP);
                client = new Socket(serverIP, 4544);
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.write(intent);
                printwriter.flush();
                printwriter.close();
                client.close();
                ttobj.speak("Okay I'll do that", TextToSpeech.QUEUE_FLUSH, null);
                Log.d("something","SENT");
            }catch(Exception e){}

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
        }

    }

    @Override
    public void witDidStartListening() {
      //  ((TextView) findViewById(R.id.txtText)).setText("Witting...");
    }

    @Override
    public void witDidStopListening() {
        //((TextView) findViewById(R.id.txtText)).setText("Processing...");
    }

    @Override
    public void witActivityDetectorStarted() {
        //((TextView) findViewById(R.id.txtText)).setText("Listening");
    }

    @Override
    public String witGenerateMessageId() {
        return null;
    }

    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.wit_button, container, false);
        }
    }

}