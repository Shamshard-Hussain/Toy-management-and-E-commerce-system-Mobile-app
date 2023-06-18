package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.util.ArrayList;

public class Search_Category extends AppCompatActivity {

    TextView search, Home;
    ListView listView;
    EditText SearchText;
    ArrayList<Toy> arrayList;
    PostAdapter adapter;
    DBHelper dbh = new DBHelper(this);
    EditText SearchToy;
    ImageView cart,BtnSearch,logout;
    private Account account;

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
        setContentView(R.layout.activity_search_category);

        logout=findViewById(R.id.imagelogout);
        listView = findViewById(R.id.listView);
        search= findViewById(R.id.txtSearchName);
        SearchText=findViewById(R.id.txt_S_Name);
        Home=findViewById(R.id.textView7);
        SearchToy= findViewById(R.id.txt_S_Name);
        cart=findViewById(R.id.imageButton);
        BtnSearch=findViewById(R.id.btn_Search);

        Intent intent = getIntent();
        String clickedItem = intent.getStringExtra("CLICKED_ITEM_NAME");
        account = (Account) intent.getSerializableExtra("UserInfo");

        //Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("UserInfo");

        search.setText(clickedItem);

        arrayList = dbh.searchToys(clickedItem);

        if (arrayList.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Searched Toy Not Found.")
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
            adapter = new PostAdapter(this, arrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(Search_Category.this, User_View_Toy.class);
                intent.putExtra("POSITION", String.valueOf(position));
                intent.putExtra("UserInfo", account);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search_Category.this, UserHome.class);
                intent.putExtra("UserInfo", account);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = SearchToy.getText().toString().trim();
                if (searchQuery.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.fieldes_cant_be_empty);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {


                    Intent intent = new Intent(Search_Category.this, User_Search_Toys.class);
                    intent.putExtra("SEARCH_QUERY", searchQuery);
                    intent.putExtra("UserInfo", account);
                    startActivity(intent);
                }
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Category.this, User_Cart.class);
                intent.putExtra("UserInfo", account);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        final View mainLayout = findViewById(R.id.main_layout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mainLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight = mainLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // If keyboard is visible
                    listView.setVisibility(View.INVISIBLE);
                } else { // If keyboard is not visible
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Search_Category.this);
                alertDialog2.setTitle("Confirm Logout...");
                alertDialog2.setMessage("Are you sure you want logout?");

                alertDialog2.setIcon(R.drawable.exit);
                // Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Search_Category.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });

                // Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                // Showing Alert Dialog
                alertDialog2.show();

            }
        });

    }

}