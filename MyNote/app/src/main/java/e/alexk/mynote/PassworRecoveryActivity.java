package e.alexk.mynote;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

import static e.alexk.mynote.SessionConstants.URL;
import static e.alexk.mynote.SessionConstants.recoveryURL;

public class PassworRecoveryActivity extends AppCompatActivity {

    EditText EmailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwor_recovery);
        EmailText=findViewById(R.id.EmailText);
    }

    public void send_request(View v){
        Snackbar.make(findViewById(R.id.root_recovery), "Loading. Be patient. ", Snackbar.LENGTH_LONG ).show();

        StringRequest request = new StringRequest(Request.Method.POST, recoveryURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Element login_ok = doc.select("div#OK").first();
                        Element login_err = doc.select("div#Err").first();

                        if (login_ok!=null) {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else if(login_err!=null){
                            Toast.makeText(getApplicationContext(), login_err.text(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("send", "send");
                params.put("mobile", "mobile");
                params.put("mail",EmailText.getText().toString());
                params.put("captcha","4");

                return params;
            }
        };

        //Volley.newRequestQueue(this).add(request);
        Volley.newRequestQueue(this).add(request);
    }


}
