package toy;

import static android.R.layout.simple_list_item_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.util.ArrayList;

public class Category_List extends AppCompatActivity {
    ListView ListCATEGORY;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category_list);


        DBHelper dbh = new DBHelper(this);
        ListCATEGORY=(ListView) findViewById(R.id.listView);
        back=findViewById(R.id.btnBack);


        ArrayList<String> theList=new ArrayList<>();
        Cursor cursor=dbh.SearchAllCategory();

        if(cursor.getCount()==0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Data Found")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            while (cursor.moveToNext()){
                theList.add(cursor.getString(0));
                ListAdapter listAdapter=new ArrayAdapter<>(this, simple_list_item_1,theList);
                ListCATEGORY.setAdapter(listAdapter);
            }
        }


        ListCATEGORY.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Category_List.this);
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want to delete data");

                alertDialog.setIcon(R.drawable.ic_warning);
                // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String selectedItem = parent.getItemAtPosition(position).toString();
                                Boolean checkudeletedata = dbh.deletedata(selectedItem);
                                if (checkudeletedata == true) {
                                    Toast.makeText(Category_List.this, "Category Deleted", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Category_List.this, Category_List.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(Category_List.this, "Category Not Deleted", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                // Setting Negative "NO" Btn
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                // Showing Alert Dialog
                alertDialog.show();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Category_List.this,Admin_Home.class );
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }
}