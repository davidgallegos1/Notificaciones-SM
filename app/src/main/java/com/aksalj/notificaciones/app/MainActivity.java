package com.aksalj.notificaciones.app;

import android.content.Context;
import android.net.Uri;
import android.os.*;
import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.annotation.TargetApi;
import android.support.v4.app.NotificationCompat;
import android.graphics.Bitmap;

import java.util.List;
import java.util.Vector;

public class MainActivity extends Activity {

    EditText    eCosto,
                eInformacion,
                eTitulo;
    Button  stackNotification,
            customNotification;
    static  String COSTO,INFORMACION;
    static CharSequence TITULO;



    static String TITULO_NOTIFICACION = "TITULO DE NOTIFICACION";
    static String TITLE_BIG_NOTIIFICATION = "TITULO DE NOTIFICACION GRANDE";
    static Vector VECTOR;
    static int CONTADOR=0;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTitulo = (EditText)findViewById(R.id.textTitulo);
        eCosto = (EditText)findViewById(R.id.costo);
        eInformacion = (EditText)findViewById(R.id.informacion);
        VECTOR = new Vector();
        customNotification = (Button)findViewById(R.id.btnCustomNotification);
        stackNotification = (Button)findViewById(R.id.btnStackNotification);

        customNotification.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                TITULO = eTitulo.getText().toString();
                COSTO = "$" + eCosto.getText().toString();
                INFORMACION = eInformacion.getText().toString();

                if (Build.VERSION.SDK_INT<16) {
                    displayNotification();
                }
                else
                {
                    new TaskNotificacion().execute();
                }
            }
        });

        stackNotification.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                TITULO = eTitulo.getText().toString();
                INFORMACION = eInformacion.getText().toString();
                CONTADOR++;
                notificacionBigText();
            }
        });
    }

    //NOTIFICACIONES PARA ANDROID SDK MAYOR A 16 CON BIG NOTIFICATION LAYOUT CUSTOM
    private class TaskNotificacion extends AsyncTask<String,Void,Void>{

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected Void doInBackground(String... params) {
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.campo);

            //Para la actividad de la notificacion
            //1. Se crea un objeto Intent el cual contiene la referencia a la actividad actual y a la que iremos mas tarde
            Intent i = new Intent(getApplicationContext(), NotificationView.class);

            //2. Se agrega un ID a la notificacion, usaremos este ID para cancelarla y eliminarla de la barra de notificaciones.
            i.putExtra("notificationID", 1);

            //3. Se crea otro Intent de la clase PendingIntent este realiza una acción en nuestra aplicación
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

            //Para mostrar layout persoalizado en notificacion big
            //1. Se crea un objeto de RemoteViews al cual se le asiganara un Layout
            RemoteViews smallView = new RemoteViews(MainActivity.this.getPackageName(),R.layout.notification_small);
            RemoteViews bigView = new RemoteViews(MainActivity.this.getPackageName(),R.layout.notification_big);

            //2. Se le asignan valores dinamicos a los objetos del layout small personalizado
            smallView.setTextViewText(R.id.smallTextInformacion, INFORMACION);
            smallView.setTextViewText(R.id.smallTextCosto,COSTO);
            smallView.setImageViewBitmap(R.id.smallViewImagen,icon1);

            // Asigna valores a los textView de layoutBig
            bigView.setTextViewText(R.id.bigTextInformacion, INFORMACION);
            bigView.setTextViewText(R.id.bigTextCosto,COSTO);
            bigView.setImageViewBitmap(R.id.bigViewImagen,icon1);

            //3. Se crea la notificacion y se le asigna el contexto
            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    //4. Se agregan caracteristicas a la notificacion
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setTicker(TITULO)
                    .setContent(smallView)
                    .setAutoCancel(true)
                    .build();
            notification.bigContentView = bigView;
            notification.defaults = Notification.DEFAULT_ALL;

            //Obtenemos la instancia de la clase NotificationManager, la cual usaremos para lanzar luego nuestra Notificacion
            NotificationManager NotificationManager = (NotificationManager)
                    getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

            //Lanzamos la notificación
            NotificationManager.notify(0, notification);

            return null;
        }
    }

    //NOTIFICACIONES PARA ANDROID SDK  MENOR A 16 NOTIFICATION LAYOUT CUSTOM
    protected void displayNotification(){
        Intent i = new Intent(this, NotificationView.class);
        i.putExtra("notificationID", 1);

        RemoteViews smallView = new RemoteViews(MainActivity.this.getPackageName(),R.layout.notification_small);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        smallView.setTextViewText(R.id.smallTextInformacion, INFORMACION);
        smallView.setTextViewText(R.id.smallTextCosto,COSTO);
        smallView.setViewVisibility(R.id.smallFlecha,View.GONE);
        smallView.setImageViewResource(R.id.smallViewImagen,R.drawable.campo);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setTicker(TITULO)
                .setContentTitle(COSTO)
                .setContentText(INFORMACION)
                .setSmallIcon(R.drawable.ic_launcher)
                .addAction(R.drawable.ic_launcher, TITULO, pendingIntent)
                .setContent(smallView)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 250, 100, 500})
                .build();
        nm.notify(1, noti);
    }

    //NOTIFICACIONES BigText PARA ANDROID SDK MAYOR A 16
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notificacionBigText() {

        Intent resultIntent = new Intent(getApplication(),StackNotification.class);
        resultIntent.putExtra("notificationID", 3);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);

        //Se crea InboxStyle que sera el contenedor de la informacion de cada notificacion
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        //Se obtienen los datos que seran mostrados como detalles en la notificacion expandida
        VECTOR.add(TITULO + "  " +INFORMACION);

        inboxStyle.setBigContentTitle(TITLE_BIG_NOTIIFICATION);//TITULO DE LA NOTIFICACION EXPANDIDA

        // Agrega en  una nueva linea a la notificacion del contenido del vector
        for (int i = 0; i < VECTOR.size(); i++) {
            inboxStyle.addLine((CharSequence) VECTOR.get(i));
        }

        //Se crea la notificacion y los atributos que contendra
        Notification mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)    //Se agrega la funcion de la notificación
                .setContentTitle(CONTADOR + " " + TITULO)   //Numero de notificaciones en el titulo
                .setContentText(eInformacion.getText().toString())  //Contenido de la notificacion estandar
                .setTicker(TITULO)  //Titulo de entrada de notificación
                .setSmallIcon(R.drawable.ic_launcher)   //Icono que se mostrara en la barra de tareas
                .setStyle(inboxStyle)   //Poca informacion de lo que contiene cada notificacion
                .setNumber(CONTADOR)    //Numero pequeño de la notificacion estandar
                .setAutoCancel(true)    //Permite eliminar la notificacion despues de ser abierta
                .build();
        mBuilder.defaults = Notification.DEFAULT_ALL;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(3, mBuilder);
    }
}