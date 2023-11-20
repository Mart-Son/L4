package com.example.l4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collection;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Lists extends AppCompatActivity {
    private static final String data_file = "ListData";
    private static final String pref_file = "LangPrefs";
    SharedPreferences shrPref;
    ArrayList<String> stringArray = new ArrayList<String>();
    SharedPreferences.Editor editor;//
    String lang;

    ListView listView;// = findViewById(R.id.listik);
    Button B_Add;// = findViewById(R.id.add);
    Button B_Delete;// = findViewById(R.id.delete);
    EditText entry;// = findViewById(R.id.edit_1);
    TextView text_hint;

    ArrayAdapter<String> TxtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.v("Вызван метод", "Lists.onStart");

        shrPref = getSharedPreferences(pref_file,Context.MODE_PRIVATE);
        editor  = shrPref.edit();

        listView = findViewById(R.id.listik);
        B_Add = findViewById(R.id.add);
        B_Delete = findViewById(R.id.delete);
        entry = findViewById(R.id.edit_1);
        text_hint = findViewById(R.id.text);

        Log.d("Вызван метод: ", "onCreate");

        TxtAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_multiple_choice,stringArray);



        lang = shrPref.getString("lang","");
        if(lang.equals("РУС"))
        {
            B_Add.setText(shrPref.getString("ru_add_b", "ДОБАВИТЬ"));
            B_Delete.setText(shrPref.getString("ru_del_b", "УДАЛИТЬ"));
            text_hint.setText(shrPref.getString("ru_List_Hint", "Введите строку, затем добавьте её в список\nИли удалите выбранные строки из списка"));
        }
        else
        {
            B_Add.setText(shrPref.getString("en_add_b", "ADD"));
            B_Delete.setText(shrPref.getString("en_del_b", "DELETE"));
            text_hint.setText(shrPref.getString("en_List_Hint", "Enter a string, then add it to ListView\nOr delete an entry from ListView"));
        }

        shrPref = getSharedPreferences(data_file,Context.MODE_PRIVATE);
        editor = shrPref.edit();

        int list_size = shrPref.getInt("list_size",0);
        String back_up, back_up_i;
        for(int i = 0;i<list_size;i++)
        {
            back_up_i = "_Num%"+i;
            back_up = shrPref.getString(back_up_i,"");
            if(back_up.isEmpty()) break;
            stringArray.add(back_up);
        }

        listView.setAdapter(TxtAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("Вызван метод", "Lists.onResume");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray selected = listView.getCheckedItemPositions();

                B_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=stringArray.size();i>=0;i--)
                        {
                            if(selected.get(i))
                            {
                                editor.remove (shrPref.getString((stringArray.get(i)+"_Num%"+i),stringArray.get(i)));
                                editor.putInt("list_size",stringArray.size());
                                stringArray.remove(i);
                            }
                        }
                        editor.apply();
                        listView.setAdapter(TxtAdapter);
                    }
                });
            }
        });

        B_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = entry.getText().toString();
                String res_i = "_Num%" +stringArray.size();
                if(!res.isEmpty())
                {
                    stringArray.add(res);
                    editor.putString(res_i,res);
                    editor.putInt("list_size",stringArray.size());
                    editor.apply();
                    listView.setAdapter(TxtAdapter);
                }
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v("Вызван метод", "Lists.onPause");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("Вызван метод", "Lists.onRestart");
    }

    protected void onStop(){
        super.onStop();
        Log.i("Вызван метод", "Lists.onStop");


    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("Вызван метод", "Lists.onDestroy");
    }
}