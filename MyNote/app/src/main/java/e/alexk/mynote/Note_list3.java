package e.alexk.mynote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static e.alexk.mynote.LoginActivity.APP_PREFERENCES;


public class Note_list3 extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Note: " + getIntent().getStringExtra("login") + "'s notes");
        setSupportActionBar(toolbar);
        String html = getIntent().getStringExtra("html");
        Document doc = Jsoup.parse(html);
        Element login_ok = doc.select("div#OK").first();
        if (login_ok != null) {
            Toast.makeText(getApplicationContext(), login_ok.text(), Toast.LENGTH_SHORT).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewNoteActivity();
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });
        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        data = new ArrayList<DataModel>();
        Element numrows = doc.select("div#numrows").first();
        Integer notes_length = Integer.parseInt(numrows.text());

        Elements note_forms = doc.select("div.note_card");

        for (Element note_form : note_forms) {

            String note_text = note_form.selectFirst("textarea.text_note").text();
            String note_date = note_form.selectFirst("div.create_date").text();
            Integer encryped = Integer.parseInt(note_form.selectFirst("div.encrypt").text());

            Integer note_id = Integer.parseInt(note_form.selectFirst("div.Id").text());

            Element deadline = note_form.selectFirst("div.time_left");
            if (deadline != null) {
                String time_till_deadline = note_form.selectFirst("div.time_left").text();
                String date_of_deadline = note_form.selectFirst("div.deadline_date").text();
                String deadline_color=note_form.selectFirst("div.deadline_color").text();
                data.add(new DataModel(note_text, note_date, note_id, date_of_deadline, time_till_deadline,deadline_color, encryped));
                try {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date_of_deadline);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    SessionConstants.Date.add(cal);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

            } else
                data.add(new DataModel(note_text, note_date, note_id, encryped));

        }

        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    void startNewNoteActivity() {

        Intent intent = new Intent(getApplicationContext(), CreateNote.class);

        //intent.putExtra("selectedItemPosition", selectedItemPosition);
        startActivity(intent);
    }

    /*
        public void delete(Integer selectedItemPosition) {
            int selectedItemId = -1;
                for (int i = 0; i < dataSet.length; i++) {
                    if (selectedName.equals(MyData.nameArray[i])) {
                        selectedItemId = MyData.id_[i];
                    }
                }
            removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    */
    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            open_note(v);
        }

        private void open_note(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView note_textView
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewNoteText);
            String note_text = (String) note_textView.getText();

            TextView note_creation
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewCreationDate);
            String note_creation_string = (String) note_creation.getText();

            TextView note_deadline
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewTillDeadline);
            String note_deadline_string = (String) note_deadline.getText();

            TextView note_deadline_date
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewDeadlineDate);
            String note_deadline_date_string = (String) note_deadline_date.getText();

            TextView Id_view
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewId);
            String Id = (String) Id_view.getText();


            CheckBox note_encrypted
                    = (CheckBox) viewHolder.itemView.findViewById(R.id.CheckBoxEncrypted);
            boolean is_encrypted = note_encrypted.isChecked();

            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("note_text", note_text);
            intent.putExtra("note_creation_string", note_creation_string);
            intent.putExtra("note_deadline_string", note_deadline_string);
            intent.putExtra("note_deadline_date_string", note_deadline_date_string);
            intent.putExtra("is_encrypted", is_encrypted);
            intent.putExtra("Id", Id);
            intent.putExtra("selectedItemPosition", selectedItemPosition);
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.notes_list3_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.calendar) {
            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.reload) {
            Snackbar.make(findViewById(R.id.root), "Reloading", Snackbar.LENGTH_LONG ).show();
            //Toast.makeText(this, "reloading", Toast.LENGTH_SHORT).show();
            reload();

        }
        if (item.getItemId() == R.id.Sort_mode) {
            if(SessionConstants.global_sort_mode==0)
                SessionConstants.global_sort_mode=1;
            else if (SessionConstants.global_sort_mode==1)
                SessionConstants.global_sort_mode=0;
            Snackbar.make(findViewById(R.id.root), "Reloading with sorting", Snackbar.LENGTH_LONG ).show();
            //Toast.makeText(this, "reloading with sorting", Toast.LENGTH_SHORT).show();
            reload();

        }
        if (item.getItemId() == R.id.logout) {
           //Snackbar.make(findViewById(R.id.root), "Reloading", Snackbar.LENGTH_LONG ).show();
            //Toast.makeText(this, "reloading", Toast.LENGTH_SHORT).show();
            //reload();
            logout();

        }
        return true;
    }
    void logout(){
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finishAffinity();

    }
    void reload() {
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
                params.put("sign_mobile", "sign_mobile");
                params.put("login", SessionConstants.login);
                params.put("password", SessionConstants.password);
                if (SessionConstants.global_sort_mode == 1)
                    params.put("CreateDateSort", "1");
                else if (SessionConstants.global_sort_mode == 0)
                    params.put("DeadlineDateSort", "1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}

