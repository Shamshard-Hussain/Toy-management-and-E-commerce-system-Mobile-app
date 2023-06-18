package toy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class User_View_Toy extends AppCompatActivity {

    ImageView back,Toyimage,Viewcart;
    EditText Qty;
    TextView Name,Category,Agegroup,AQty,Id,Price;
    Button cart;
    private Account account;

    public class MinValueInputFilter implements InputFilter {
        private int minValue;

        public MinValueInputFilter(int minValue) {
            this.minValue = minValue;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (input >= minValue) {
                    return null;
                }
            } catch (NumberFormatException e) {
                // Do nothing, just return null below
            }

            return "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent();
        intent1.setClass(getApplicationContext(), UserHome.class);
        intent1.putExtra("UserInfo", account);
        startActivity(intent1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_view_toy);


        Viewcart=findViewById(R.id.imageButton);
        back=findViewById(R.id.imageback);
        Name=findViewById(R.id.txt_T_Name);
        Category=findViewById(R.id.textCategory);
        Agegroup=findViewById(R.id.textAgeGroup);
        Price=findViewById(R.id.textPrice);
        Qty=findViewById(R.id.editTextNumber3);
        Id=findViewById(R.id.textid);
        cart=findViewById(R.id.buttoncart);
        Toyimage=findViewById(R.id.imagetoy);
        AQty=findViewById(R.id.textAvailabaleQty);
        DBHelper dbh = new DBHelper(this);

        Qty.setFilters(new InputFilter[]{new MinValueInputFilter(1)});


        Intent intent = getIntent();
        String pos = intent.getStringExtra("POSITION");
        account = (Account) intent.getSerializableExtra("UserInfo");
        String UserId = account.getUsername();

        int position = Integer.parseInt(pos);
        ArrayList<Toy> arrayList = dbh.getPost();
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
        Name.setText(name);
        Category.setText("Category : "+category);
        Price.setText("Price : Rs."+price+".00/=");
        Agegroup.setText("Age Group : "+age+"+");
       // AQty.setText("Available Quantity : "+qty);
        Id.setText(id);
        Qty.setText("1");




        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wantQty = Qty.getText().toString().trim();

                Boolean checkcartItems = dbh.checkCart(UserId, name);

                if(wantQty.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Quantity!", Toast.LENGTH_LONG).show();
                }else if (checkcartItems == true) {
                    Toast.makeText(getApplicationContext(), "This Item is Already Add to Cart", Toast.LENGTH_LONG).show();
                }else {
                    byte[] img = imageViewToByte(Toyimage);
                    Boolean insert = dbh.addToCart(UserId,name, category, img, price, wantQty);
                    if (insert == true) {
                        Toast.makeText(User_View_Toy.this, "Successfully Add to Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Add Cart", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_View_Toy.this, User_Cart.class);
                intent.putExtra("UserInfo", account);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("UserInfo", account);
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });



    }
    private byte[] imageViewToByte(@NonNull ImageView imageView) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
}