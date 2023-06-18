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

public class RegisterActivity extends AppCompatActivity {
    TextView Login;
    EditText Semail, Spassword, Spassword2, Sname, Spnumber;
    Button Register;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        DBHelper dbh = new DBHelper(this);


        Login = findViewById(R.id.logIn);
        Sname = findViewById(R.id.txt_S_Name);
        Semail = findViewById(R.id.txt_S_Emails);
        Spassword = findViewById(R.id.txt_S_passwordss);
        Spassword2 = findViewById(R.id.txt_S_passwords2);
        Spnumber = findViewById(R.id.txt_S_PNumber);
        Register = findViewById(R.id.btn_S_Register);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = Sname.getText().toString().trim();
                String pass = Spassword.getText().toString().trim();
                String repass = Spassword2.getText().toString().trim();
                String username = Sname.getText().toString().trim();
                String email = Semail.getText().toString().trim();
                String Pnumber = Spnumber.getText().toString().trim();

                if (user.isEmpty()){
                    showError(Sname, "Fields Can't be Empty");
                }else if (!username.matches("^[A-Za-z]+$")) {
                    showError(Sname, "Username is Invalid");
                } else if(email.isEmpty()){
                    showError(Semail, "Fields Can't be Empty");
                }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    showError(Semail, "Email is Invalid");
                }else if(Pnumber.isEmpty()){
                    showError(Spnumber, "Fields Can't be Empty");
                }else if (!Pnumber.matches("^[0-9]*$")) {
                    showError(Spnumber, "Phone Number is Invalid");
                }else if(pass.isEmpty()) {
                    showError(Spassword, "Fields Can't be Empty");
                }else if(repass.isEmpty()) {
                    showError(Spassword2, "Fields Can't be Empty");
                } else if (Spassword.getText().toString().length() < 6) {
                    showError(Spassword, "Password need to be more than 6 Characters");
                } else if (!Spassword.getText().toString().equals(Spassword2.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.password_and_confirm_password_should_match_to_register);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();

                }   else if(user.equals("admin")||user.equals("Admin")){
                    showError(Sname, "Invalid name try another");
                }else {

                    if (pass.equals(repass)) {

                        Boolean checkUser = dbh.checkUsername(user);
                        Boolean checkemailaddress = dbh.checkEmail(email);
                        if(checkemailaddress== true){

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle(R.string.error);
                            builder.setMessage(R.string.user_email);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();

                        }
                        else if (checkUser == false) {
                            Boolean insert = dbh.insertUser(user,email,  Pnumber, pass);
                            if (insert == true) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle(R.string.Done);
                                builder.setMessage(R.string.registered_successfully);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    }
                                });
                                builder.show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                       else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle(R.string.error);
                            builder.setMessage(R.string.user_exitsts);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.passwords_not_matching);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                }


            }
        });
    }


    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}