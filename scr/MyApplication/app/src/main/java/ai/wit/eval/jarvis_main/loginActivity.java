package ai.wit.eval.jarvis_main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class loginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        final Button serverButton = (Button)findViewById(R.id.serverButton);
        final TextView field = (TextView)findViewById(R.id.IP);

        serverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(field.getText().toString() != "")
                {
                    Intent i = new Intent(getApplicationContext(), mainActivity.class);
                    i.putExtra("IP",field.getText());
                    startActivity(i);
                }else
                {
                    Toast.makeText(getApplicationContext(),"Enter IP",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Check whether the input string is current format of an IP address
    private boolean matchIP(String IP)
    {
        //String regex = "[0-9]+.[0-9]+.[0-9]+.[0-9]+";
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
}
