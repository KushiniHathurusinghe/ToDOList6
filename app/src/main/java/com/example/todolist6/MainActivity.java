package com.example.todolist6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private EditText enterTask;
    private Button pending;
    private Button completed;
    ListView listView;

    private DatabaseHelper databaseHelper;
    private User user;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.btnAdd);
        pending = findViewById(R.id.Pending);
        completed = findViewById(R.id.Completed);
        enterTask = findViewById(R.id.enterTask);

        listView = findViewById(R.id.listView1);
        mContext = this;


        databaseHelper = new DatabaseHelper(MainActivity.this);
        user = new User();

        loadTask( "Pending");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = "Pending";


                user.setName(enterTask.getText().toString().trim());
                user.setStatus(s.trim());

                String t = enterTask.getText().toString().trim();

                databaseHelper.addUser(user);

                // Snack Bar to show success message that record saved successfully
                Toast.makeText(MainActivity.this, "Successfully inserted", Toast.LENGTH_SHORT).show();


                loadPendingTask("Pending");

                enterTask.setText(null);


            }
        });


        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadPendingTask("Pending");
            }
        });


        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = "Completed";

                //Select
                ArrayList ta = databaseHelper.getTask(s);


                final String[] listValue = new String[ta.size()];

                for (int i = 0; i < ta.size(); i++) {

                    listValue[i] = ta.get(i).toString();
                }

                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listValue);

                ArrayAdapter<String> adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, listValue);

                ListView listView = findViewById(R.id.listView1);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                OnItemClickListener itemClickListener = new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        // AdapterView is the parent class of ListView
                        ListView lv = (ListView) arg0;
                        if (lv.isItemChecked(position)) {
                           // Toast.makeText(getBaseContext(), "You checked " + listValue[position], Toast.LENGTH_SHORT).show();
                            final String ck = listValue[position];
                           // boolean isUpdate = databaseHelper.updatePending(ck);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                            builder.setMessage("Do you want to Delete or send back ?");


                            builder.setTitle("Alert !");


                            builder.setCancelable(false);


                            builder.setNegativeButton(
                                            "DELETE",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {
                                                    databaseHelper.deleteTask(ck);
                                                    loadTask( "Completed");
                                                }
                                            });


                            builder.setPositiveButton(
                                    "PENDING",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {
                                            databaseHelper.updateCompleteToPending(ck);
                                            loadTask( "Completed");
                                        }
                                    });
                            // Set the Negative button with No name
                            // OnClickListener method is use
                            // of DialogInterface interface.
                            builder
                                    .setNeutralButton(
                                            "Cancel",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    // If user click no
                                                    // then dialog box is canceled.
                                                    dialog.cancel();
                                                }
                                            });

                            // Create the Alert dialog
                            AlertDialog alertDialog = builder.create();

                            // Show the Alert Dialog box
                            alertDialog.show();

                        }
                    }
                };

                listView.setOnItemClickListener(itemClickListener);


            }


        });


    }



    private void loadPendingTask( String sta)
    {
        String s = sta;

        //Select
        ArrayList ta = databaseHelper.getTask(s);


        final String[] listValue = new String[ta.size()];

        for(int i=0 ; i < ta.size() ; i++)
        {

            listValue[i] = ta.get(i).toString();
        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listValue);

        ArrayAdapter<String> adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, listValue);

        ListView listView =  findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        OnItemClickListener itemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        // AdapterView is the parent class of ListView
        ListView lv = (ListView) arg0;
        if(lv.isItemChecked(position))
        {
            Toast.makeText(getBaseContext(), "You checked " + listValue[position], Toast.LENGTH_SHORT).show();
            String ck =  listValue[position];
            boolean isUpdate = databaseHelper.updatePendingToComplete(ck);
            loadTask( "Pending");


            if(isUpdate == true)
                Toast.makeText(MainActivity.this,"Data Update",Toast.LENGTH_LONG).show();


        }
        else
       {
            Toast.makeText(getBaseContext(), "You unchecked " + listValue[position], Toast.LENGTH_SHORT).show();
        }
    }
    };

        listView.setOnItemClickListener(itemClickListener);
        loadTask( "Pending");


    }


    private void loadTask( String sta) {
        String s = sta;

        //Select
        ArrayList ta = databaseHelper.getTask(s);


        final String[] listValue = new String[ta.size()];

        for (int i = 0; i < ta.size(); i++) {

            listValue[i] = ta.get(i).toString();
        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listValue);

        ArrayAdapter<String> adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, listValue);

        ListView listView = findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }



}
