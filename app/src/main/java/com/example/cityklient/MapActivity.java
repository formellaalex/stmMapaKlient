package com.example.cityklient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;


public class MapActivity extends AppCompatActivity {

    private static final String NAMESPACE = "http://stm/";
    private static final String METHOD_NAME = "getMapFragment";
    private static final String URL = "http://10.0.2.2:8080/mapa_java_ee_8_war_exploded/services/MapFragmentService";
    private static final String SOAP_ACTION = "http://stm//getMapFragment";
    private int x1, x2, y1, y2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.x1 = getIntent().getIntExtra("x1", 0);
        this.y1 = getIntent().getIntExtra("y1", 0);
        this.x2 = getIntent().getIntExtra("x2", 0);
        this.y2 = getIntent().getIntExtra("y2", 0);

        RequestTask task = new RequestTask();
        try {
            String base64Image = task.execute().get();
            byte[] byteImage = (byte[]) Base64.getDecoder().decode(base64Image);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 350,
                    700, false));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class RequestTask extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... arg) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo propertyInfoArg0 = new PropertyInfo();
            propertyInfoArg0.setName("x1");
            propertyInfoArg0.setType(int.class);
            propertyInfoArg0.setValue(x1);

            PropertyInfo propertyInfoArg1 = new PropertyInfo();
            propertyInfoArg1.setName("y1");
            propertyInfoArg1.setType(int.class);
            propertyInfoArg1.setValue(y1);

            PropertyInfo propertyInfoArg2 = new PropertyInfo();
            propertyInfoArg2.setName("x2");
            propertyInfoArg2.setType(int.class);
            propertyInfoArg2.setValue(x2);

            PropertyInfo propertyInfoArg3 = new PropertyInfo();
            propertyInfoArg3.setName("y2");
            propertyInfoArg3.setType(int.class);
            propertyInfoArg3.setValue(y2);

            request.addProperty(propertyInfoArg0);
            request.addProperty(propertyInfoArg1);
            request.addProperty(propertyInfoArg2);
            request.addProperty(propertyInfoArg3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            SoapPrimitive resultsRequestSOAP = null;
            try {
                resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                return (String)resultsRequestSOAP.getValue();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }

            return null;
        }
    }
}
