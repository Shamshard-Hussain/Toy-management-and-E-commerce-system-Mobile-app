package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

public class Add_Toys extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    ImageView Toyimage,back;
    EditText ToyNmae,Qty,Price,Agegroup;
    Spinner SpinnerCategoryName;
    Button Add_details;
    String[] Category = {"Action Figures", "Dolls", "Games and Puzzles", "Building Sets and Blocks"};

    private Toy toy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_toys);

        back=findViewById(R.id.button);
        Toyimage= findViewById(R.id.imageView);
        ToyNmae= findViewById(R.id.txt_toyName);
        Qty= findViewById(R.id.txt_qty);
        Price= findViewById(R.id.txt_price);
        Agegroup= findViewById(R.id.txt_Agegroup);
        Add_details = findViewById(R.id.btn_Item);
        SpinnerCategoryName=(Spinner) findViewById(R.id.sp_Category);
        DBHelper dbh = new DBHelper(this);

        Vector<String> vecCategory=dbh.getCategory_Name();
        ArrayAdapter ad=new ArrayAdapter(this, android.R.layout.simple_spinner_item,vecCategory);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCategoryName.setAdapter(ad);

        Toyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");

                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
            }
        });



        Add_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ToyName = ToyNmae.getText().toString().trim();
                String ToyPrice = Price.getText().toString().trim();
                String ToyAgeGroup = Agegroup.getText().toString().trim();
                String ToyQty = Qty.getText().toString().trim();

                String ToyCategory = SpinnerCategoryName.getSelectedItem().toString().trim();

                Boolean checkToyName = dbh.checkToyName(ToyName);

                if (ToyName.isEmpty()){
                    showError(ToyNmae, "Fields Can't be Empty");
                }else if (!ToyName.matches("^[A-Za-z ]+$")) {
                    showError(ToyNmae, "Toy Name is In Valid");
                }else if (ToyPrice.isEmpty()){
                    showError(Price, "Fields Can't be Empty");
                } else if (ToyAgeGroup.isEmpty()) {
                    showError(Agegroup, "Fields Can't be Empty");
                }else if (ToyQty.isEmpty()){
                    showError(Qty, "Fields Can't be Empty");
                }else if  (!Toyimage.getDrawable().isFilterBitmap()){
                    Toast.makeText(getApplicationContext(), "Please Insert a image ", Toast.LENGTH_LONG).show();
                }else if (checkToyName == true) {
                    showError(ToyNmae, "This Item is Already Add");
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add_Toys.this);
                    alertDialog.setTitle("Confirm...");
                    alertDialog.setMessage("Are you sure you want save data");

                    alertDialog.setIcon(R.drawable.ic_right);
                    // Setting Positive "Yes" Btn
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    byte[] img = imageViewToByte(Toyimage);
                                    Boolean insert = dbh.insertPost(ToyName, ToyCategory, img, ToyAgeGroup, ToyPrice, ToyQty);
                                    if (insert == true) {
                                        Toast.makeText(Add_Toys.this, "Toy Add Successfully", Toast.LENGTH_SHORT).show();

                                        ToyNmae.setText("");
                                        Agegroup.setText("");
                                        Price.setText("");
                                        Qty.setText("");
                                        Toyimage.setImageResource(R.drawable.ic_image_search);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to insert Toy Data", Toast.LENGTH_LONG).show();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Toys.this, Admin_Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Toyimage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] imageViewToByte(@NonNull ImageView imageView) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}