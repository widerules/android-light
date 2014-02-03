/*  Copyright (C) <2014>  Farges Maelyss
  	Application : https://play.google.com/store/apps/developer?id=Orion+Games
  	Blog : https://www.facebook.com/OrionGamesBlog 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation version 3.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    see <http://www.gnu.org/licenses/> */

package com.orion.light;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class LightScreen extends Activity {
	PowerManager.WakeLock wl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		 * White screen with brightness and wake lock
		 */
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Light");
		wl.acquire();

		WindowManager.LayoutParams layout = getWindow().getAttributes();
		layout.screenBrightness = 1F;
		getWindow().setAttributes(layout);

		//setContentView(R.layout.activity_light);

	}
	
	
}
