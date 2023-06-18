package toy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toy.R;

import java.util.ArrayList;

public class OrdrAdapter extends BaseAdapter {

    private Context context;
    private DBHelper dbHelper;
    ArrayList<Order> arrayList;
    Button action;

    public OrdrAdapter(Context context, ArrayList<Order> arrayList, DBHelper dbHelper) {
        this.context = context;
        this.arrayList = arrayList;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView  = inflater.inflate(R.layout.oder_layout, null);

        TextView ToyName, Quantity,UserName, Email, Address,PostalCode,Date,Status;


        ToyName = convertView.findViewById(R.id.toyNameTextView);
        Quantity = convertView.findViewById(R.id.quantityTextView);
        Date = convertView.findViewById(R.id.dateTextView);
        Status = convertView.findViewById(R.id.statusTextView);
        Button actionButton = convertView.findViewById(R.id.actionButton);

        Order post = arrayList.get(position);
        int id=post.getId();
        String name = post.getToyName();
        String  date= post.getDate();
        String  status= post.getStatus();
        String qty = post.getQuantity();
        String Username = post.getUserName();
        String email = post.getEmail();
        String address = post.getAddress();
        String postalCode = post.getPostalCode();

        ToyName.setText("Toy Name:"+name);
        Quantity.setText("Quantity: " +qty);
        Date.setText("Date: "+date);
        Status.setText("Status: "+status);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Set the title and message for the dialog
                builder.setTitle("Select an option");
                builder.setMessage("Choose what you want to do:");

                // Set up the buttons for the dialog
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean checkupdatedata=dbHelper.RemoveItem(id);
                        if (checkupdatedata==true){
                            Toast.makeText(context.getApplicationContext(), "Order Removed ", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context.getApplicationContext(), "Failed to Remove Order ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                        builder.setTitle("Order Details");
                        builder.setMessage(
                                " Order Date : "+date+"\n"+
                                " Toy Name : "+name+"\n"+
                                " Quantity : "+qty+"\n"+
                                        " User Name : "+Username+"\n"+
                                        " Email : "+email+"\n"+
                                        " Address : "+address+"\n"+
                                        " Postal Code : "+postalCode+"\n"+
                                        " Status : "+status+"\n"


                        );
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();

                    }
                });

                builder.setNeutralButton("Change Status", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean checkupdatedata=dbHelper.updateOrderStatus(id, "Delivered");
                        if (checkupdatedata==true){
                            Toast.makeText(context.getApplicationContext(), "Status Updated ", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context.getApplicationContext(), "Failed to Update Status ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Show the dialog
                builder.show();
            }
        });


        return convertView;
    }

}
