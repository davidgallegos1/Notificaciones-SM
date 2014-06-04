package com.aksalj.notificaciones.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

import java.util.Vector;

public class StackNotification extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oferta);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Cancelamos la Notificacion que hemos comenzado
        nm.cancel(getIntent().getExtras().getInt("notificationID"));
        MainActivity.CONTADOR=0;
        MainActivity.VECTOR=new Vector();
    }

}