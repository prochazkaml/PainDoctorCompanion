package ml.prochazka.paindoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    SeekBar painControl;
    SeekBar timeControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView painDesc = findViewById(R.id.PainControlDesc);
        TextView timeDesc = findViewById(R.id.TimeDesc);

        painControl = findViewById(R.id.PainController);
        timeControl = findViewById(R.id.TimeController);

        String[] painLevels = new String[]{
                "Can I play, Daddy? 🍼", // the least painful
                "Do you want a tickle?",
                "Still warming up...",
                "That's my boi, keep going",
                "Get ready for a proper electric oomph!",
                "Yeah this is gonna hurt quite a bit", // moderately painful
                "I told you it would hurt!",
                "Woohoo, it’s finally getting interesting!",
                "My condolences, buddy",
                "This option will literally fry your balls off",
                "We are not responsible for any permanent health problems this level of pain will cause. Enjoy!" // the most painful
        };


        String[] timeLevels = new String[]{
                "5 minutes", "10 minutes", "15 minutes", "20 minutes", "30 minutes", "45 minutes",
                "1 hour", "1 hour 30 minutes", "2 hours", "4 hours", "Forever (you're dead btw)"
        };

        painDesc.setText(painLevels[5]);
        timeDesc.setText(timeLevels[5]);

        painControl.setProgress(5);
        painControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                painDesc.setText(painLevels[progress]);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        timeControl.setProgress(5);
        timeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeDesc.setText(timeLevels[progress]);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void start(View view) {
        findViewById(R.id.TimeController).setEnabled(false);
        findViewById(R.id.PainController).setEnabled(false);
        findViewById(R.id.LaunchButton).setEnabled(false);

        Snackbar snackbar = Snackbar.make(findViewById(R.id.ScreenLayout), "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setText("Connecting to PainDoctor™...");
        snackbar.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://192.168.69.123/?l=" + ((char) (65 + painControl.getProgress())) + "&t=" + ((char) (65 + timeControl.getProgress())),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.ScreenLayout), "", Snackbar.LENGTH_LONG);

                        if(response.equals("OK" + ((char) (65 + painControl.getProgress())) + ((char) (65 + timeControl.getProgress())))) {
                            snackbar.setText("You PainDoctor™ is ready, good luck! \uD83D\uDE01");
                        } else {
                            snackbar.setText("You should never see this text. If you do, something is really fucked. (" + response + ")");
                        }

                        snackbar.show();

                        findViewById(R.id.TimeController).setEnabled(true);
                        findViewById(R.id.PainController).setEnabled(true);
                        findViewById(R.id.LaunchButton).setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.ScreenLayout), "", Snackbar.LENGTH_LONG);

                try {
                    snackbar.setText("Yikes! Could not connect to PainDoctor™! (HTTP " + error.networkResponse.statusCode + ")");
                } catch(NullPointerException e) {
                    snackbar.setText("Yikes! Could not connect to PainDoctor™! (Network error)");
                }

                snackbar.show();

                findViewById(R.id.TimeController).setEnabled(true);
                findViewById(R.id.PainController).setEnabled(true);
                findViewById(R.id.LaunchButton).setEnabled(true);
            }
        });

        queue.add(stringRequest);
    }
}