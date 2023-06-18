package toy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.toy.R;

import java.util.ArrayList;

public class CategoryPost extends BaseAdapter {

    Context context;
    ArrayList<Category> arrayList;


    public CategoryPost(Context context, ArrayList<Category> arrayList) {
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
        convertView  = inflater.inflate(R.layout.grid_item, null);

        TextView  category;

        category = convertView.findViewById(R.id.txtDescription);


        Category cat = arrayList.get(position);
        String name = cat.getCategoryName();
        String id = cat.getId();

        category.setText(name);

        return convertView;
    }
}
