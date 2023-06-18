package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class User_Cart extends AppCompatActivity {

    ImageView back;
    ListView listView;
    Button Buy;
    TextView subtotal;
    private Account account;
    ArrayList<Cart> arrayList;
    CartAdapter adapter;
    DBHelper dbh = new DBHelper(this);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent();
        intent1.setClass(getApplicationContext(), UserHome.class);
        intent1.putExtra("UserInfo", account);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_cart);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = dateFormat.format(calendar.getTime());

        subtotal=findViewById(R.id.textView3);
        back=findViewById(R.id.imageView3);
        listView =findViewById(R.id.listView);
        Buy=findViewById(R.id.btn_buy);

        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("UserInfo");
        String UId = account.getUsername();
        String name = account.getEmail();
        String email=account.getTpNumber();


        arrayList = dbh.getCartPost(UId);

        if (arrayList.isEmpty()) {
            subtotal.setVisibility(View.INVISIBLE);
            Buy.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Cart is Empty!.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

            intent.putExtra("UserInfo", account);
            adapter = new CartAdapter(this, arrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



        int totalPrice = 0;
        // Iterate through the list of items
        for (int i = 0; i < arrayList.size(); i++) {
            Cart post = arrayList.get(i);
            String toyPrice = post.getPrice();
            String qty = post.getQuantity();
            int price = Integer.parseInt(toyPrice);
            int quantity = Integer.parseInt(qty);

            // Calculate the total price of this item
            int itemPrice = price * quantity;

            // Add the item price to the total price
            totalPrice += itemPrice;
        }
       // Toast.makeText(getApplicationContext(), "Total Price: Rs. " + totalPrice + ".00/=", Toast.LENGTH_SHORT).show();

        subtotal.setText("Total Price: Rs. " + totalPrice + ".00/=");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(User_Cart.this);
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want to remove item?");

                alertDialog.setIcon(R.drawable.ic_error);
                // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Cart cartPost = arrayList.get(position);
                                int cartId = cartPost.getId();
                                Boolean remove = dbh.RemoveCartItem(cartId);
                                if (remove == true) {
                                    Toast.makeText(User_Cart.this, "Item Removed", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(User_Cart.this, User_Cart.class);
                                    intent.putExtra("UserInfo", account);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(User_Cart.this, "Failed to remove Item", Toast.LENGTH_SHORT).show();
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



        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(User_Cart.this);

                // Set the title and message for the dialog
                builder.setTitle("Enter your details");
                builder.setMessage("Please enter your name, email, address, and postal code:");

                // Inflate a layout to use as the dialog's view
                View view = LayoutInflater.from(User_Cart.this).inflate(R.layout.dialog_layout, null);

                // Get references to the EditText fields in the dialog layout
                EditText nameEditText = view.findViewById(R.id.nameEditText);
                EditText emailEditText = view.findViewById(R.id.emailEditText);
                EditText addressEditText = view.findViewById(R.id.addressEditText);
                EditText postalCodeEditText = view.findViewById(R.id.postalCodeEditText);

                nameEditText.setText(name);
                emailEditText.setText(email);

                // Add the view to the dialog builder
                builder.setView(view);

                // Set up the positive button (OK button)
                builder.setPositiveButton("OK", null);

                // Set up the negative button (Cancel button)
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing - the dialog will be dismissed automatically
                    }
                });

                // Show the dialog
                final AlertDialog dialog = builder.create();
                dialog.show();

                // Override the onClick method of the positive button to handle validation
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the text from the EditText fields and do something with it
                        String name = nameEditText.getText().toString().trim();
                        String email = emailEditText.getText().toString().trim();
                        String address = addressEditText.getText().toString().trim();
                        String postalCode = postalCodeEditText.getText().toString().trim();

                        // Do something with the user's input
                        if (name.isEmpty()){
                            showError(nameEditText, "Fields Can't be Empty");
                        }else if (!name.matches("^[A-Za-z]+$")) {
                            showError(nameEditText, "Username is Invalid");
                        } else if(email.isEmpty()){
                            showError(emailEditText, "Fields Can't be Empty");
                        }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                            showError(emailEditText, "Email is Invalid");
                        }else if(address.isEmpty()){
                            showError(addressEditText, "Fields Can't be Empty");
                        }else if (!postalCode.matches("^[0-9]*$")) {
                            showError(postalCodeEditText, "Postal Code is Invalid");
                        }else {

                            for (int i = 0; i < arrayList.size(); i++) {
                                Cart post = arrayList.get(i);
                                String UserId = post.getUserId();
                                String toyName = post.getToyName();
                                String qty = post.getQuantity();
                                String UserName = nameEditText.getText().toString().trim();
                                String UserEmail = emailEditText.getText().toString().trim();
                                String UserAddress = addressEditText.getText().toString().trim();
                                String UserpostalCode = postalCodeEditText.getText().toString().trim();

                                // Insert the item into the database
                                Boolean insert = dbh.insertOrder(toyName, qty, UserName, UserEmail, UserAddress, UserpostalCode,todayDate,"Pending");
                                if (insert == true) {

                                    try {
                                        dbh.RemoveAllCartItem(UserId);
                                        // The method call was successful
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        // An exception was thrown
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle(R.string.Done);
                                    builder.setMessage(R.string.Ordered);
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                            Intent intent = new Intent(User_Cart.this, UserHome.class);
                                            intent.putExtra("UserInfo", account);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    });
                                    builder.show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle(R.string.error);
                                    builder.setMessage(R.string.Ordere_failed);
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();

                                }
                            }
                        }



                    }
                });
            }
        });



    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}