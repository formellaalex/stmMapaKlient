package com.example.cityklient;

import android.os.AsyncTask;
import android.os.Bundle;

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


public class MapActivity extends AppCompatActivity {

    private static final String NAMESPACE = "http://stm/";
    private static final String METHOD_NAME = "getMapFragment";
    private static final String URL = "http://localhost:8080/mapa_java_ee_8_war_exploded/services/MapFragmentService";
    private static final String SOAP_ACTION = "http://stm//getMapFragment";
    private int x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.x1 = getIntent().getIntExtra("x1", 0);
        this.y1 = getIntent().getIntExtra("y1", 0);
        this.x2 = getIntent().getIntExtra("x2", 0);
        this.y2 = getIntent().getIntExtra("y2", 0);

        RequestTask task = new RequestTask();
        task.execute();

    }

    class RequestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg) {
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
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }

            return null;
        }
    }
}
