package com.plter.linkgame;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.plter.lib.android.java.controls.ArrayAdapter;

import java.util.List;

public class MainActivity extends Activity {

	
	private ArrayAdapter<GameListCellData> adapter;
	private ProgressDialog dialog=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		translucentStatusBar(this,true);
		
		adapter=new ArrayAdapter<MainActivity.GameListCellData>(this,R.layout.game_list_cell) {
			
			@Override
			public void initListCell(int position, View listCell, ViewGroup parent) {
				ImageView iconIv = (ImageView) listCell.findViewById(R.id.iconIv);
				TextView labelTv=(TextView) listCell.findViewById(R.id.labelTv);
				
				GameListCellData data = getItem(position);
				iconIv.setImageResource(data.iconResId);
				labelTv.setText(data.label);
			}
		};

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(onItemClickListener);
		
		adapter.add(new GameListCellData("水果连连看", R.drawable.sg_icon, "sg_config.json"));
		adapter.add(new GameListCellData("蔬菜连连看", R.drawable.sc_icon, "sc_config.json"));
		adapter.add(new GameListCellData("动物连连看", R.drawable.dw_icon, "dw_config.json"));
		adapter.add(new GameListCellData("爱心连连看", R.drawable.love_icon, "love_config.json"));
		adapter.add(new GameListCellData("宝石连连看", R.drawable.coin_icon, "coin_config.json"));
	}
	
	
	@Override
	protected void onPause() {
		
		if (dialog!=null) {
			dialog.dismiss();
			dialog=null;
		}
		
		super.onPause();
	}


	public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
		Window window = activity.getWindow();
		//添加Flag把状态栏设为可绘制模式
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (hideStatusBarBackground) {
			//如果为全透明模式，取消设置Window半透明的Flag
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//设置状态栏为透明
			window.setStatusBarColor(Color.TRANSPARENT);
			//设置window的状态栏不可见
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		} else {
			//如果为半透明模式，添加设置Window半透明的Flag
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//设置系统状态栏处于可见状态
			//  window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		}
		//view不根据系统窗口来调整自己的布局
//		ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
//		View mChildView = mContentView.getChildAt(0);
//		if (mChildView != null) {
//			mChildView.setFitsSystemWindows(false);
//			ViewCompat.requestApplyInsets(mChildView);
//		}
	}
	


	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			dialog=ProgressDialog.show(MainActivity.this, "请稍候", "正在加载游戏资源");

			GameListCellData data = adapter.getItem(position);
			Intent i = new Intent(MainActivity.this, LinkGameActivity.class);
			i.putExtra("configFile", data.gameConfigFile);
			startActivity(i);
		}
	};
	
	
	public static class GameListCellData{
		
		public GameListCellData(String label,int iconResId,String gameConfigFile) {
			this.label=label;
			this.iconResId=iconResId;
			this.gameConfigFile=gameConfigFile;
		}
		
		public String label=null;
		public int iconResId=0;
		public String gameConfigFile=null;
	}
}
