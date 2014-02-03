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
	 * Configure la cam�ra et pr�pare les param�tre pour allum� et �teindre le
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
	 * Verifie que la lampe n'est pas d�j� allum�. Eteint le flash si c'est le
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
	 * M�thode qui cr�e la notification
	 */
	private void createNotify() {
		// On cr�e un "gestionnaire de notification"
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// On cr�e la notification
		// Avec son ic�ne et son texte d�filant (optionnel si l'on ne veut pas
		// de texte d�filant on met cet argument � null)
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Lumi�re allum�e", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		// Le PendingIntent c'est ce qui va nous permettre d'atteindre notre
		// deuxi�me Activity
		// ActivityNotification sera donc le nom de notre seconde Activity
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Light.class), 0);
		// On d�finit le titre de la notification
		String titreNotification = "Lumi�re";
		// On d�finit le texte qui caract�rise la notification
		String texteNotification = "Appuyer pour �teindre la lumi�re";

		// On configure notre notification avec tous les param�tres que l'on
		// vient de cr�er
		notification.setLatestEventInfo(this, titreNotification,
				texteNotification, pendingIntent);

		// Enfin on ajoute notre notification et son ID � notre gestionnaire de
		// notification
		notificationManager.notify(ID_NOTIFICATION, notification);

	}

	/**
	 * M�thode pour supprimer de la liste de notification la notification que
	 * l'on vient de cr�er
	 */
	private void cancelNotify() {
		// On cr�e notre gestionnaire de notification
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// on supprime la notification gr�ce � son ID
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
