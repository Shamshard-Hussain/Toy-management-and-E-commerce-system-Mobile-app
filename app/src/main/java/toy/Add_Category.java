package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

public class Add_Category extends AppCompatActivity {
    Button add_category,Back;
    EditText category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_category);

        Back= findViewById(R.id.buttonBack);
        add_category = findViewById(R.id.btn_Item);
        category=findViewById(R.id.txt_add_category);
        DBHelper dbh = new DBHelper(this);


        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ToyCategoryName = category.getText().toString().trim();


              if (ToyCategoryName.isEmpty()){
                    showError(category, "Fields Can't be Empty");
                }else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add_Category.this);
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want save data");

                alertDialog.setIcon(R.drawable.ic_right);
                // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Boolean insert = dbh.Category(ToyCategoryName);
                                if (insert == true) {
                                    Toast.makeText(Add_Category.this, "Category Add Successfully", Toast.LENGTH_SHORT).show();

                                    category.setText("");

                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to insert category", Toast.LENGTH_LONG).show();
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
            }
        });


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Add_Category.this,Admin_Home.class );
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}