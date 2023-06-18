package toy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toy.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView Signup;
    EditText Lusername, Lpassword;
    Button L_singin;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        Signup = findViewById(R.id.singUp);
        Lusername = findViewById(R.id.txt_L_username);
        Lpassword = findViewById(R.id.txt_L_Passwords);
        L_singin = findViewById(R.id.btn_L_singIn);
        DBHelper dbh = new DBHelper(this);


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        L_singin.setOnClickListener(v -> {
            String user = Lusername.getText().toString().trim();
            String pass = Lpassword.getText().toString().trim();



            ArrayList<Account> userDetails = dbh.ValidLogin(Lusername.getText().toString(), Lpassword.getText().toString());

            if (user.isEmpty()){
                showError(Lusername, "Fields Can't be Empty");
            }else if(pass.isEmpty()) {
                showError(Lpassword, "Fields Can't be Empty");
            }
            else if(user.equals("admin")&&pass.equals("1234567")){
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Admin_Home.class);
                startActivity(intent);
            }
            else if (userDetails.size() != 0) {
                Account acount = userDetails.get(0);

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserHome.class);
                intent.putExtra("UserInfo", acount);
                startActivity(intent);


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.error);
                builder.setMessage(R.string.Sign_in_failed);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                builder.show();
            }

        });











    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}