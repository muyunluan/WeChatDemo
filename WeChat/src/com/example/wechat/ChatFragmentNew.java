package com.example.wechat;

import java.util.ArrayList;
import java.util.List;

import com.example.swipmenulistview.SwipeMenuListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class ChatFragmentNew extends Fragment{
	private SwipeMenuListView swipeMenuListView ;
	private ArrayAdapter<String> adapter;
	private List<String> datas = new ArrayList<String>();
private final static String[] data = {"张飞","张辽","张角","张三丰","张牙舞爪","张灯结彩","张唑啉","张大民"};
	
	//创建数据源.  
    Zhang[] data2 = new Zhang[]{  
        new Zhang("张飞",38,"zhangfei@gmail.com","燕山"),  
        new Zhang("张辽",36,"zhangliao@sina.com","雁门"),  
        new Zhang("张角",51,"zhangjiao@gmail.com","钜鹿"),  
        new Zhang("张三丰",200,"sanfeng@gmail.com","辽东"),  
        new Zhang("张牙舞爪",25,"5zhao@gmail.com","冀州"),
        new Zhang("张灯结彩",25,"5zhao@gmail.com","冀州") ,
        new Zhang("张唑啉",25,"5zhao@gmail.com","冀州") ,
        new Zhang("张大民",25,"5zhao@gmail.com","冀州") ,
        new Zhang("张牙舞爪",25,"5zhao@gmail.com","冀州") };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View chatView = inflater.inflate(R.layout.fragment_chat_new, container, false);
		
		ListView listview = (ListView)chatView.findViewById(R.id.listview);
        /*
         * 第一种：普通字符串
         */
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,data);
        
        /*
         * 第二种：文艺类对象
         */
        ArrayAdapter<Zhang> adapter2 = new ArrayAdapter<Zhang>(this.getActivity(), 
        		android.R.layout.simple_list_item_1,data2);
        
        /*
         * 第三种：自定义适配器
         */
        ListAdapter adapter3 = new ListAdapter(this.getActivity(), R.layout.listview,data2) ;//okay, the resource id is passed.
        
        listview.setAdapter(adapter3);
		return chatView;
	}
	
	
}
