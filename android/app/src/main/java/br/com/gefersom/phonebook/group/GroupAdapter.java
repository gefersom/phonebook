package br.com.gefersom.phonebook.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.Group;

/**
 * Created by me on 1/9/2016.
 */
public class GroupAdapter extends BaseAdapter {

    private Context context;
    private List<Group> groupList;

    public GroupAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null )
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView.setTag( new ViewHolder(convertView) );
        }

        Group group = groupList.get(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.imageView.setImageResource(group.getImageId());
        holder.nameView.setText(group.getName());
        holder.descriptionView.setText(group.size() + " Contatos");

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView nameView;
        TextView descriptionView;

        ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.title);
//            descriptionView = (TextView) view.findViewById(R.id.description);
        }
    };
}
