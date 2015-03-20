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
private final static String[] data = {"�ŷ�","����","�Ž�","������","������צ","�ŵƽ��","������","�Ŵ���"};
	
	//��������Դ.  
    Zhang[] data2 = new Zhang[]{  
        new Zhang("�ŷ�",38,"zhangfei@gmail.com","��ɽ"),  
        new Zhang("����",36,"zhangliao@sina.com","����"),  
        new Zhang("�Ž�",51,"zhangjiao@gmail.com","��¹"),  
        new Zhang("������",200,"sanfeng@gmail.com","�ɶ�"),  
        new Zhang("������צ",25,"5zhao@gmail.com","����"),
        new Zhang("�ŵƽ��",25,"5zhao@gmail.com","����") ,
        new Zhang("������",25,"5zhao@gmail.com","����") ,
        new Zhang("�Ŵ���",25,"5zhao@gmail.com","����") ,
        new Zhang("������צ",25,"5zhao@gmail.com","����") };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View chatView = inflater.inflate(R.layout.fragment_chat_new, container, false);
		
		ListView listview = (ListView)chatView.findViewById(R.id.listview);
        /*
         * ��һ�֣���ͨ�ַ���
         */
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,data);
        
        /*
         * �ڶ��֣����������
         */
        ArrayAdapter<Zhang> adapter2 = new ArrayAdapter<Zhang>(this.getActivity(), 
        		android.R.layout.simple_list_item_1,data2);
        
        /*
         * �����֣��Զ���������
         */
        ListAdapter adapter3 = new ListAdapter(this.getActivity(), R.layout.listview,data2) ;//okay, the resource id is passed.
        
        listview.setAdapter(adapter3);
		return chatView;
	}
	
	
}
