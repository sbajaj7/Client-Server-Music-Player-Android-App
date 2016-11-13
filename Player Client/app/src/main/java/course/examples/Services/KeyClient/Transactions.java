package course.examples.Services.KeyClient;

import android.app.Activity;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import course.examples.Services.KeyCommon.KeyGenerator;

public class Transactions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        String arr[] = new String[500];
        try {
            if (KeyServiceUser.mKeyGeneratorService != null) {
                arr = KeyServiceUser.mKeyGeneratorService.getAllData();
                //System.out.println("Static object not null");
            }
        } catch (Exception e) {
            e.getMessage();
        }


        /*if(res.getCount() == 0){
            return;
        }

        String arr[] = new String[100];
        int i = 0;
        while (res.moveToNext()){
            arr[i] = res.getString(1) + " " + res.getString(2);
            i++;
        }*/

        if (arr != null) {
            //System.out.println("Array object not null");

            //List<String> stringList = new ArrayList<String>(Arrays.asList(arr));
            List<String> list = new ArrayList<String>();

            for (String s : arr) {
                if (s != null && s.length() > 0) {
                    list.add(s);
                }
            }
                //System.out.println(list);

                ListView lv = (ListView) findViewById(R.id.transactions);
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_listview, list);
                lv.setAdapter(adapter);

        }

    }

}