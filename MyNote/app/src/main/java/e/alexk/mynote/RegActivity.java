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

import static e.alexk.mynote.SessionConstants.recoveryURL;
import static e.alexk.mynote.SessionConstants.regURL;

public class RegActivity extends AppCompatActivity {
    EditText NameText;
    EditText Email_Text;
    EditText LoginText;
    EditText PasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        NameText=findViewById(R.id.FullnameText);
        Email_Text=findViewById(R.id.EmailText);
        LoginText=findViewById(R.id.UsernameText);
        PasswordText=findViewById(R.id.PasswordText);
    }
    public void send_reg_request(View v){
        Snackbar.make(findViewById(R.id.root_reg), "Loading. Be patient. ", Snackbar.LENGTH_LONG ).show();

        StringRequest request = new StringRequest(Request.Method.POST, regURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Element login_ok = doc.select("div#OK").first();
                        Element login_err = doc.select("div#Err").first();

                        if (login_ok!=null) {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), login_ok.text(), Toast.LENGTH_SHORT).show();
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
                params.put("register", "register");
                params.put("mobile", "mobile");
                params.put("full_name",NameText.getText().toString());
                params.put("email",Email_Text.getText().toString());
                params.put("username",LoginText.getText().toString());
                params.put("password",PasswordText.getText().toString());

                return params;
            }
        };

        //Volley.newRequestQueue(this).add(request);
        Volley.newRequestQueue(this).add(request);
    }

}
