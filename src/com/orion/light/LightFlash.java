package com.orion.light;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.IBinder;

public class LightFlash extends Service {

	private static final int ID_NOTIFICATION = 0;

	boolean flashOn = false;

	Camera cam;

	// Parameters pOn, pOff;

	/**
	 * Configure la caméra et prépare les paramètre pour allumé et éteindre le
	 * flash
	 */
	@Override
	public void onCreate() {

		/*
		 * Config camera
		 */
		cam = Camera.open();
		// pOn = cam.getParameters();
		// pOn.setFlashMode(Parameters.FLASH_MODE_TORCH);
		// pOff = cam.getParameters();
		// pOff.setFlashMode(Parameters.FLASH_MODE_OFF);
		// cam.release();

		createNotify();

	}

	/**
	 * Verifie que la lampe n'est pas déjà allumé. Eteint le flash si c'est le
	 * cas
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!flashOn) {
			turnOnOffFlash(false);
			turnOnOffFlash(true);

		} else {
			stopSelf();

		}

		return 1;
	}

	/**
	 * Méthode qui crée la notification
	 */
	private void createNotify() {
		// On crée un "gestionnaire de notification"
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// On crée la notification
		// Avec son icône et son texte défilant (optionnel si l'on ne veut pas
		// de texte défilant on met cet argument à null)
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Lumière allumée", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		// Le PendingIntent c'est ce qui va nous permettre d'atteindre notre
		// deuxième Activity
		// ActivityNotification sera donc le nom de notre seconde Activity
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Light.class), 0);
		// On définit le titre de la notification
		String titreNotification = "Lumière";
		// On définit le texte qui caractérise la notification
		String texteNotification = "Appuyer pour éteindre la lumière";

		// On configure notre notification avec tous les paramètres que l'on
		// vient de créer
		notification.setLatestEventInfo(this, titreNotification,
				texteNotification, pendingIntent);

		// Enfin on ajoute notre notification et son ID à notre gestionnaire de
		// notification
		notificationManager.notify(ID_NOTIFICATION, notification);

	}

	/**
	 * Méthode pour supprimer de la liste de notification la notification que
	 * l'on vient de créer
	 */
	private void cancelNotify() {
		// On crée notre gestionnaire de notification
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// on supprime la notification grâce à son ID
		notificationManager.cancel(ID_NOTIFICATION);
	}

	private void turnOnOffFlash(boolean on) {

		if (on && !flashOn) {
			//cam.release();
			//cam = Camera.open();
			// cam.setParameters(pOn);
			// cam.startPreview();
			Parameters params = cam.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			cam.setParameters(params);
			//cam.stopPreview();
			try {
				cam.startPreview();
				
			}
			catch (RuntimeException e)
			{}
			// preview.setCamera(cam);
			cam.autoFocus(new AutoFocusCallback() {
				public void onAutoFocus(boolean success, Camera camera) {
				}
			});
			

			flashOn = true;
		} else if (!on) {
			// cam.setParameters(pOff);
			Parameters params = cam.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(params);

			// cam.stopPreview();
			// cam.release();

			flashOn = false;
		}

	}

	@Override
	public void onDestroy() {
		turnOnOffFlash(false);

		cancelNotify();
		cam.stopPreview();
		cam.release();

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;

	}

}
