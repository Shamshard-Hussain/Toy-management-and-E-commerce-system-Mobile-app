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

public class PostAdapter extends BaseAdapter {

    Context context;
    ArrayList<Toy> arrayList;


    public PostAdapter(Context context, ArrayList<Toy> arrayList) {
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
        convertView  = inflater.inflate(R.layout.show_layout, null);
        TextView Toyname, price, category,ageGroup;
        ImageView imageView = convertView.findViewById(R.id.imageView);

        Toyname = convertView.findViewById(R.id.txtTitle);
        category = convertView.findViewById(R.id.txtDescription);
        ageGroup = convertView.findViewById(R.id.age);
        price = convertView.findViewById(R.id.price);

        Toy post = arrayList.get(position);
        String name = post.getToyName();
        String toyCategory = post.getCategoryName();
        byte[] img = post.getImage();
        String  toyPrice= post.getAgeGroup();
        String  age= post.getPrice();
        String qty = post.getQuantity();


        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        Toyname.setText(name);
        category.setText("Category: " +toyCategory);
        ageGroup.setText("Age Group: "+toyPrice+"+");
        price.setText("Rs. "+age+".00/=");

        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
