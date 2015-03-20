package com.example.wechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Zhang> {
	private LayoutInflater mInflater; 
	
	public ListAdapter(Context context, int textViewResourceId,Zhang[] obj) {
		super(context, textViewResourceId,obj);
		// TODO Auto-generated constructor stub
		
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){  
            //�����µ�view��ͼ.  
            convertView = mInflater.inflate(R.layout.listview, null); //see above, you can use the passed resource id.
        }  
          
        ViewHolder holder = null;  
        if(holder==null){  
            holder = new ViewHolder();  
            //����ÿ��ViewItem��,������View,�Ž�holder��  
            holder.name = (TextView) convertView.findViewById(R.id.person_name);  
            holder.age = (TextView) convertView.findViewById(R.id.person_age);  
            holder.email = (TextView) convertView.findViewById(R.id.person_email);  
            holder.address = (TextView) convertView.findViewById(R.id.person_address);  
            //�����ÿ����ʾ��ViewItem��, ������View�����ö���  
            convertView.setTag(holder);  
        }
        else// I think this a bug, program can not run here!!!--linc2014.11.12
        {
        	holder = (ViewHolder)convertView.getTag(); 
        }
          
        //��ȡ��ǰҪ��ʾ������  
        Zhang person = getItem(position);  
  
        holder.name.setText(person.getName());  
        holder.age.setText(String.valueOf(person.getAge()));  
        holder.email.setText(person.getEmail());  
        holder.address.setText(person.getAddress());  
        return convertView;
    }
    
	private static class ViewHolder
	{
		TextView name;  
        TextView age;  
        TextView email;  
        TextView address;
	}
	
	
}
