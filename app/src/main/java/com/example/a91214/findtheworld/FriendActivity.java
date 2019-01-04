package com.example.a91214.findtheworld;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
Button button_back,button_add,button_edit;
public  final  String file ="file";
ListView listView;
  int judge=1;
 ArrayList<Person>friend;
Listadapter listadapter;
LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        button_back=(Button)findViewById(R.id.btn_friends_list_radar);
        button_add=(Button)findViewById(R.id.btn_friends_list_add);
        button_edit=(Button)findViewById(R.id.btn_friends_list_edit);
       listView=(ListView)findViewById(R.id.lvw_friends_list);
      friend=MainActivity.friend;
       //PersonOperater personOperater=new PersonOperater(file);
       //friend=personOperater.readObject(FriendActivity.this);

       //friend= MainActivity.friend;
        listadapter=new Listadapter(friend);
        listView.setAdapter(listadapter);

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judge==1){
                    judge=0;
                    listadapter.notifyDataSetChanged();
                }
                else {
                    judge=1;
                    listadapter.notifyDataSetChanged();

                }
            }
        });


        button_add.setOnClickListener(new View.OnClickListener() {

            AlertDialog.Builder dialog=new AlertDialog.Builder(FriendActivity.this);
            @Override
            public void onClick(View v) {
                linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_add_friend,null);
                dialog.setTitle("添加朋友").setView(linearLayout);
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et1=linearLayout.findViewById(R.id.txt_friend_name);
                        EditText et2=linearLayout.findViewById(R.id.txt_friend_number);
                        Person person=new Person();
                        person.setNumber(et2.getText().toString());
                        person.setName(et1.getText().toString());
                        friend.add(person);
                        PersonOperater personOperater=new PersonOperater(file);
                        personOperater.saveObject(FriendActivity.this,friend);
                        listadapter.notifyDataSetChanged();
                     dialog.cancel();
                    }
                });
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
                    }
                });

                dialog.create();
                dialog.show();



                listadapter.notifyDataSetChanged();
            }
        });
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public  class Listadapter extends BaseAdapter{
     private      ArrayList<Person>person;


     public  Listadapter(ArrayList<Person> a){
         this.person=a;
     }

        public  int getCount(){
            return  person.size();
        }
        public Person getItem(int position){
            return  person.get(position);
        }
        public  long getItemId(int position){
            return  position;
        }
        public View getView(final int position, View convertview, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) FriendActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout =inflater.inflate(R.layout.friends_list_item,null);
           TextView t1=(TextView) layout.findViewById(R.id.name_cell);
           Button btn=(Button)layout.findViewById(R.id.delete_button_cell);
           t1.setText(person.get(position).getName());
           if(judge==1){
               btn.setVisibility(View.INVISIBLE);

           }
           else {
               btn.setVisibility(View.VISIBLE);

           }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    person.remove(position);
                    listadapter.notifyDataSetChanged();
                }
            });

    return  layout;

        }
    }

}
