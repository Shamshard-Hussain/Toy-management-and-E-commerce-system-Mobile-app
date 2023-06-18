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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.util.ArrayList;

public class View_toy_List extends AppCompatActivity {

    ListView listView;
    ImageView backBtn,BtnSearch;
    ArrayList<Toy> arrayList;
    PostAdapter adapter;

    ArrayList<Category> arrayList2;
    CategoryPost adapter2;
    GridView gridview;
    EditText SearchToy;
    DBHelper dbh = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_toy_list);

        backBtn = findViewById(R.id.button19);
        listView = findViewById(R.id.listView);
        gridview= findViewById(R.id.gridView);
        SearchToy= findViewById(R.id.txt_S_Name);
        BtnSearch=findViewById(R.id.btn_Search);



        showPostData();
        showCategoryData();


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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(View_toy_List.this, Update_Toy.class);
                intent.putExtra("POSITION", String.valueOf(position));
                startActivity(intent);

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


                    Intent intent = new Intent(View_toy_List.this, Admin_Toy_search.class);
                    intent.putExtra("SEARCH_QUERY", searchQuery);
                    startActivity(intent);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_toy_List.this, Admin_Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = arrayList2.get(position).getCategoryName(); // replace "getItemName()" with your own method to get the item's name
                Intent intent = new Intent(View_toy_List.this, Admin_Toy_search.class);
                intent.putExtra("SEARCH_QUERY", clickedItem);
                startActivity(intent);
            }
        });



    }

    private void showPostData() {
        arrayList = dbh.getPost();
        adapter = new PostAdapter(this, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showCategoryData() {
        arrayList2 = dbh.getCategoryPost();
        adapter2 = new CategoryPost(this, arrayList2);
        gridview.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }
}