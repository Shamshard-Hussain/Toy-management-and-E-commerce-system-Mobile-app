package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.util.ArrayList;

public class Admin_View_Orders extends AppCompatActivity {
    ListView listView;
    ArrayList<Order> arrayList;
    OrdrAdapter adapter;
    DBHelper dbh = new DBHelper(this);
    ImageView Search,Back;
    EditText SearchWord;
    Button pending,all,Delivered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_view_orders);

        listView = findViewById(R.id.listView);
        Back=findViewById(R.id.button19);
        SearchWord=findViewById(R.id.txt_S_Name);
        Search=findViewById(R.id.btn_Search);
        all=findViewById(R.id.btn_all);
        Delivered=findViewById(R.id.btn_Deliverd);
        pending=findViewById(R.id.btn_Pending);
       // action=findViewById(R.id.actionButton);
        showOrderData();




        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_View_Orders.this, Admin_Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = SearchWord.getText().toString().trim();
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


                    Intent intent = new Intent(Admin_View_Orders.this, Admin_search_Orders.class);
                    intent.putExtra("SEARCH_QUERY", searchQuery);
                    startActivity(intent);
                }
            }
        });

        Delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_View_Orders.this, Admin_search_Orders.class);
                intent.putExtra("SEARCH_QUERY", "Delivered");
                startActivity(intent);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_View_Orders.this, Admin_search_Orders.class);
                intent.putExtra("SEARCH_QUERY", "Pending");
                startActivity(intent);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_View_Orders.this, Admin_View_Orders.class);
                startActivity(intent);
            }
        });


        final View mainLayout = findViewById(R.id.main_l);
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
    }

    private void showOrderData() {
        arrayList = dbh.ViewOrder();
        adapter = new OrdrAdapter(this, arrayList,dbh);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}