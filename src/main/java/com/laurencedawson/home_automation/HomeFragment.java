package com.laurencedawson.home_automation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.*;
import asyncTasks.HttpJsonRequestAsync;
import asyncTasks.SendCmdHttpJsonRequestAsync;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionOptions;
import jsonObj.Action;
import jsonObj.Item;
import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HomeFragment extends Fragment {
	View mRoot;

	public static HomeFragment newInstance(){
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
	}

	//private Handler mHandler;
	private TextView mPowerTextView, mTemperatureTextView;
	private int mPower;
	private float mTemperature;
	private boolean mLounge = false;
	private boolean mKitchen = false;
	private boolean mBedroom = false;

	public static final String POWER = "power";
	public static final String TEMPERATURE = "temperature";
	public static final int STATS_GRABBED = 0;
	public static final int LIGHTS_GRABBED = 1;
	public static final int LIGHTS_UPDATED = 2;


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(POWER, mPower);
		outState.putFloat(TEMPERATURE, mTemperature);
		super.onSaveInstanceState(outState);
	}

	private Switch mLoungeSwitch, mKitchenSwitch, mBedroomSwitch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_home, null);

		LinearLayout mWrapperLayout = (LinearLayout) mRoot.findViewById(R.id.wrapper_layout);
		mWrapperLayout.setMotionEventSplittingEnabled(false);




		HttpJsonRequestAsync jsonRq = new HttpJsonRequestAsync(this);
		jsonRq.execute("https://domo.starn.fr/core/api/jeeApi.php","object::full","hj4hGCiQOlUexFZrqldCaCfEEbiTpxxEThQURTMd5nl373Q0");



//		Typeface roboto_light = ((HomeAutomationActivity)getActivity()).roboto_light;
//		Typeface roboto_bold = ((HomeAutomationActivity)getActivity()).roboto_bold;
//
//		((TextView)mRoot.findViewById(R.id.lounge_lights)).setTypeface(roboto_light);
//		((TextView)mRoot.findViewById(R.id.kitchen_lights)).setTypeface(roboto_light);
//		((TextView)mRoot.findViewById(R.id.bedroom_lights)).setTypeface(roboto_light);
//
//		mTemperatureTextView = (TextView) mRoot.findViewById(R.id.temperature_label);
//		mTemperatureTextView.setTypeface(roboto_bold);
//
//		mPowerTextView = (TextView) mRoot.findViewById(R.id.power_label);
//		mPowerTextView.setTypeface(roboto_bold);
//
//		mLoungeSwitch  = (Switch) mRoot.findViewById(R.id.lounge_switch);
//		mLoungeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(mLoungeSwitch.isEnabled()){
//					lightAction(1, isChecked);
//				}
//			}
//		});
//
//		mKitchenSwitch = (Switch) mRoot.findViewById(R.id.kitchen_switch);
//		mKitchenSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(mKitchenSwitch.isEnabled()){
//					lightAction(2, isChecked);
//				}
//			}
//		});
//
//		mBedroomSwitch = (Switch) mRoot.findViewById(R.id.bedroom_switch);
//		mBedroomSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(mBedroomSwitch.isEnabled()){
//					lightAction(3, isChecked);
//				}
//			}
//		});
//
//		mHandler = new Handler(){
//			public void handleMessage(android.os.Message msg) {
//				if(msg.what==STATS_GRABBED){
//					mRoot.findViewById(R.id.stats_buttons).setVisibility(View.VISIBLE);
//					mPowerTextView.setText(Integer.toString(mPower));
//					mTemperatureTextView.setText(Float.toString(mTemperature));
//				}else if(msg.what==LIGHTS_GRABBED){
//					mLoungeSwitch.setChecked(mLounge);
//					mKitchenSwitch.setChecked(mKitchen);
//					setSwitchesEnabled(true);
//				}else if(msg.what==LIGHTS_UPDATED){
//					setSwitchesEnabled(true);
//				}
//			};
//		};
//
//		mRoot.findViewById(R.id.stats_buttons).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				grabStats();
//			}
//		});

		if(savedInstanceState!=null){
			mPower = savedInstanceState.getInt(POWER);
			mTemperature = savedInstanceState.getFloat(TEMPERATURE);
			//mHandler.sendEmptyMessage(STATS_GRABBED);
		}else{
			grabStats();
		}

		return mRoot;
	}

	@Override
	public void onResume() {
		grabButtons();
		super.onResume();
	}

	public void refreshItems(List<Item> items){
		LinearLayout layout = (LinearLayout)mRoot.findViewById(R.id.light_buttons);

		for (Item item: items) {
			LinearLayout r = new LinearLayout(this.getActivity());
			TextView tv = new TextView(this.getActivity());
			tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			tv.setText(item.getName());
			r.addView(tv);
			for (final Action action: item.getActions()) {
				if (!action.isVisible()) continue;
				Button b = new Button(this.getActivity());
				b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				b.setText(action.getActionName());
				b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						(new Thread(){
							@Override
							public void run() {
								SendCmdHttpJsonRequestAsync jsonRq = new SendCmdHttpJsonRequestAsync(HomeFragment.this);
								jsonRq.execute("https://domo.starn.fr/core/api/jeeApi.php","cmd::execCmd","hj4hGCiQOlUexFZrqldCaCfEEbiTpxxEThQURTMd5nl373Q0",action.getActionID());
							}
						}).start();
					}
				});
				r.addView(b);
			}

			layout.addView(r);
		}


	}

	private void grabStats(){
		new Thread(){
			@Override
			public void run() {
				try{
//					DefaultHttpClient client = new DefaultHttpClient();
//					HttpGet request = new HttpGet(BASE_URL+"/cgi-bin/stats.sh");
//					HttpResponse response = client.execute(request);
//
//					// Check if the stream is gziped
//					InputStream is = response.getEntity().getContent();
//
//					// Grab the JSON
//					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//					String json = reader.readLine();
//
//					// Tidy up
//					reader.close();
//					is.close();
//					client.getConnectionManager().shutdown();
//
//					mPower = new JSONObject(json).getJSONObject("stats").getInt("watts");
//					mTemperature = Float.parseFloat(new JSONObject(json).getJSONObject("stats").getString("temp"));

					//mHandler.sendEmptyMessage(STATS_GRABBED);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void grabButtons(){

		new Thread(){
			@Override
			public void run() {
				try{




//					mLounge = new JSONObject(json).getBoolean("hallway");
//					mKitchen = new JSONObject(json).getBoolean("study_desk");
//					mBedroom = new JSONObject(json).getBoolean("study_window");

					//mHandler.sendEmptyMessage(LIGHTS_GRABBED);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	


	public void lightAction(final int id, final boolean action){
		new Thread(){
			@Override
			public void run() {
				try{
//					DefaultHttpClient client = new DefaultHttpClient();
//					HttpGet request = new HttpGet(BASE_URL+"/cgi-bin/light_"+(action?"on":"off")+".sh?"+id);
//					client.execute(request);
//					client.getConnectionManager().shutdown();

					//mHandler.sendEmptyMessage(LIGHTS_UPDATED);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
}