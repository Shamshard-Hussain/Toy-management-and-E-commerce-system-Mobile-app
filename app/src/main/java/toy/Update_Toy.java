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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

public class Update_Toy extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    ImageView Toyimage,back,Delete;
    EditText ToyNmae,Qty,Price,Agegroup;
    Spinner SpinnerCategoryName;
    Button Update;
    TextView Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_toy);

        Delete=findViewById(R.id.delete);
        Id=findViewById(R.id.text_id);
        Update=findViewById(R.id.btn_Item_update);
        Toyimage= findViewById(R.id.imageView);
        ToyNmae= findViewById(R.id.txt_toyName);
        Qty= findViewById(R.id.txt_qty);
        Price= findViewById(R.id.txt_price);
        Agegroup= findViewById(R.id.txt_Agegroup);
        SpinnerCategoryName=(Spinner) findViewById(R.id.sp_Category);
        DBHelper dbh = new DBHelper(this);
        back=findViewById(R.id.button);


        ArrayList<Toy> arrayList = dbh.getPost();
        Intent intent = getIntent();
        String pos = intent.getStringExtra("POSITION");
        int position = Integer.parseInt(pos);
        Toy post = arrayList.get(position);

        byte[] image = post.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        String name = post.getToyName();
        String category = post.getCategoryName();
        String price = post.getPrice();
        String age = post.getAgeGroup();
        String qty = post.getQuantity();
        String id = post.getId();
        int idAsInt = Integer.parseInt(id);

        Toyimage.setImageBitmap(bitmap);
        ToyNmae.setText(name);
        Qty.setText(qty);
        Price.setText(price);
        Agegroup.setText(age);
        Id.setText(id);



        Spinner spinner = findViewById(R.id.sp_Category);
        Vector<String> categoryNames = dbh.getCategory_Name();  // assuming dbh is an instance of your database helper class

        // create an ArrayList with the category names
        ArrayList<String> categoryList = new ArrayList<>(categoryNames);

        // add the category variable to the beginning of the ArrayList
        categoryList.add(0, category);

        // create an ArrayAdapter with the ArrayList as the data source
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);

        // set the adapter as the data source for the spinner
        spinner.setAdapter(adapter);

        Toyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");

                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ToyName = ToyNmae.getText().toString();
                String ToyPrice = Price.getText().toString();
                String ToyAgeGroup = Agegroup.getText().toString();
                String ToyQty = Qty.getText().toString().trim();
                byte[] img = covertImageToByteArray(Toyimage);
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
                }else if (!name.equals(ToyName)) {
                    if (checkToyName == true) {
                        showError(ToyNmae, "This Item is Already Add");
                    }
                }

                else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Update_Toy.this);
                    alertDialog.setTitle("Confirm...");
                    alertDialog.setMessage("Are you sure you want update data");

                    alertDialog.setIcon(R.drawable.ic_right);
                    // Setting Positive "Yes" Btn
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Boolean checkupdatedata=dbh.updateToydata(idAsInt,ToyName, ToyCategory, img, ToyAgeGroup, ToyPrice, ToyQty);
                                    if (checkupdatedata==true){
                                        Toast.makeText(Update_Toy.this, "Successfully Toy Updated ", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Update_Toy.this, "Failed to Update Toy ", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Update_Toy.this, View_toy_List.class);
                startActivity(intent);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Update_Toy.this);
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want delete toy");

                alertDialog.setIcon(R.drawable.ic_right);
                // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Boolean checkudeletedata = dbh.deleteToy(idAsInt);
                                if (checkudeletedata == true) {
                                    Toast.makeText(Update_Toy.this, "Toy Deleted", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Update_Toy.this, View_toy_List.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(Update_Toy.this, "Toy Not Deleted", Toast.LENGTH_SHORT).show();
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


    }
    private byte[] covertImageToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

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

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}