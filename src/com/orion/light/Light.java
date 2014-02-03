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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class Light extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			  PackageManager pm = getPackageManager();
			  if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
				  startService(new Intent(this, LightFlash.class));
			  else
				  startActivity(new Intent(this, LightScreen.class));
			} catch (Exception e) {
				startActivity(new Intent(this, LightScreen.class));
			}

		finish();

	}

}
