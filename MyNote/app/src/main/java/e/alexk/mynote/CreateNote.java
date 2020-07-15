package e.alexk.mynote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
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
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNote extends Activity {
    TextView DeadlineDate;
    Calendar dateAndTime=Calendar.getInstance();
    Button CryptButton;
    TextView UserNote;
    TextView cryptPass;
    int EncryptCount=0;
    int DeadlineCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        DeadlineDate=(TextView)findViewById(R.id.DeadlineDate);
        CryptButton=(Button) findViewById(R.id.EncryptButton);
        UserNote=(TextView)findViewById(R.id.UserNote);
        cryptPass=(TextView)findViewById(R.id.cryptPass);
        //setInitialDateTime();
        CryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cryptPass.getText().length()!=0){
                    //UserNote.setText("Все ок)");
                    String c=encrypt(UserNote.getText().toString(),cryptPass.getText().toString());
                    UserNote.setText(c);
                    EncryptCount++;
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Empty pass!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(CreateNote.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String temp =format1.format(dateAndTime.getTime());
        DeadlineDate.setText(temp);
                //DateUtils.formatDateTime(this,
               // dateAndTime.getTimeInMillis(),
               // DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DeadlineCount++;
            setInitialDateTime();
        }
    };


    public String encrypt(String textToEncrypt, String password) {

        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(password, "b21332f26c793d8a");
        String cipherText = encryptor.encrypt(textToEncrypt);
        /*String decryptedText = encryptor.decrypt(cipherText);
        System.out.println("Src: " + textToEncrypt);
        System.out.println("Cipher: " + cipherText);
        Toast.makeText(getApplicationContext(), "" + cipherText, Toast.LENGTH_SHORT).show();
        System.out.println("Decrypted: " + decryptedText);
        Toast.makeText(getApplicationContext(), "" + decryptedText, Toast.LENGTH_SHORT).show();
        System.out.println("__________________");*/
        return cipherText;
    }

    public String decrypt(String textToDecrypt, String password) {

        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(password, "b21332f26c793d8a");

        String decryptedText = encryptor.decrypt(textToDecrypt);

        //System.out.println("Decrypted: " + decryptedText);
        return decryptedText;
    }

    public void AddNote(View view) {

        Snackbar.make(findViewById(R.id.create_root), "Adding, please wait", Snackbar.LENGTH_LONG ).show();
        StringRequest request = new StringRequest(Request.Method.POST, SessionConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = Jsoup.parse(response);
                        Element login_ok = doc.select("div#OK").first();
                        Element login_err = doc.select("div#Err").first();

                        if (login_ok != null) {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Note_list3.class);
                            intent.putExtra("login", SessionConstants.login);
                            intent.putExtra("html", response);
                            intent.putExtra("URL", SessionConstants.URL);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                        } else if (login_err != null) {
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
                params.put("Text", "Text");
                params.put("text", UserNote.getText().toString());
                if(EncryptCount!=0){
                    params.put("EncryptedCheck1", "on");
                }
                if(DeadlineCount!=0)
                {
                    params.put("deadline_date",DeadlineDate.getText().toString());
                }

                params.put("login", SessionConstants.login);
                params.put("password", SessionConstants.password);
                if (SessionConstants.global_sort_mode == 1)
                    params.put("CreateDateSort", "1");
                else if (SessionConstants.global_sort_mode == 0)
                    params.put("DeadlineDateSort", "1");
                return params;
            }
        };

        //Volley.newRequestQueue(this).add(request);
        Volley.newRequestQueue(this).add(request);
        return;

    }
}
