package toy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toy.R;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cart> arrayList;

    public CartAdapter(Context context, ArrayList<Cart> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        convertView  = inflater.inflate(R.layout.cart_layout, null);
        TextView Toyname, price, category,Tqty;

        ImageView imageView = convertView.findViewById(R.id.ToyimageView);
        Toyname = convertView.findViewById(R.id.product_name);
        category = convertView.findViewById(R.id.product_description);
        Tqty = convertView.findViewById(R.id.product_quantity);
        price = convertView.findViewById(R.id.product_price);




        Cart post = arrayList.get(position);
        String name = post.getToyName();
        String toyCategory = post.getCategoryName();
        String  toyPrice= post.getPrice();
        String qty = post.getQuantity();
        byte[] img = post.getImage();




        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        Toyname.setText(name);
        category.setText("Category: " +toyCategory);
        price.setText("Price: Rs. "+toyPrice+".00/=");
        Tqty.setText("Qty: "+qty);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
