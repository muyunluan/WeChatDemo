package com.example.wechat;

import java.util.ArrayList;
import java.util.List;

import com.example.contacts.Contact;
import com.example.contacts.ContactAdapter;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AlphabetIndexer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class ContactsFragment extends Fragment {
	
	/**
	 * 分组的布局
	 */
	private LinearLayout titleLayout;

	/**
	 * 分组上显示的字母
	 */
	private TextView title;

	/**
	 * 联系人ListView
	 */
	private ListView contactsListView;

	/**
	 * 联系人列表适配器
	 */
	private ContactAdapter adapter;

	/**
	 * 用于进行字母表分组
	 */
	private AlphabetIndexer indexer;

	/**
	 * 存储所有手机中的联系人
	 */
	private List<Contact> contacts = new ArrayList<Contact>();

	/**
	 * 定义字母表的排序规则
	 */
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;


	/**
	 * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。
	 */
	private void setupContactsListView() {
		contactsListView.setAdapter(adapter);
		contactsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				int section = indexer.getSectionForPosition(firstVisibleItem);
				int nextSecPosition = indexer.getPositionForSection(section + 1);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(String.valueOf(alphabet.charAt(section)));
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});

	}

	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 * 
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View contactsView = inflater.inflate(R.layout.fragment_contacts, container, false);
		adapter = new ContactAdapter(getActivity(), R.layout.contact_item, contacts);
		titleLayout = (LinearLayout) contactsView.findViewById(R.id.title_layout);
		title = (TextView) contactsView.findViewById(R.id.title);
		contactsListView = (ListView) contactsView.findViewById(R.id.contacts_list_view);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Cursor cursor = getActivity().getContentResolver().query(uri,
				new String[] { "display_name", "sort_key" }, null, null, "sort_key");
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(0);
				String sortKey = getSortKey(cursor.getString(1));
				Contact contact = new Contact();
				contact.setName(name);
				contact.setSortKey(sortKey);
				contacts.add(contact);
			} while (cursor.moveToNext());
		}
		//startManagingCursor(cursor);
		indexer = new AlphabetIndexer(cursor, 1, alphabet);
		adapter.setIndexer(indexer);
		if (contacts.size() > 0) {
			setupContactsListView();
		}
		return contactsView;
	}
}