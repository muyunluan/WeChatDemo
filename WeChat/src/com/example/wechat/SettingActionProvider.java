package com.example.wechat;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

public class SettingActionProvider extends ActionProvider {

	private Context context;
	
	public SettingActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add(context.getString(R.string.action_album))
		       .setIcon(R.drawable.ofm_photo_icon)
		       .setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					Intent album_intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			        context.startActivity(album_intent);
					return true;
				}
		    	   
		       });
	}
	
	@Override
	public boolean hasSubMenu() {
		return true;
	}
}
