package com.yaseen.latticeinnovationstest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText pinCodeEdt,mobilno,fullname,gender,dob,addressline1,addressline2;
    private Button pinCodeValidateBtn,registerBtn;
    private TextView districtTextview;

    Calendar myCalendar = Calendar.getInstance();

    String pinCode,pinCode2;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinCodeEdt=findViewById(R.id.editTextPincode);
        pinCodeValidateBtn=findViewById(R.id.btnvalidate);
        districtTextview=findViewById(R.id.districttext);
        mobilno=findViewById(R.id.mobilenoedittext);
        fullname=findViewById(R.id.full_nameedittext);
        gender=findViewById(R.id.dobedittext);
        addressline1=findViewById(R.id.addressline1edittext);
        addressline2=findViewById(R.id.addressline2edittext);
        registerBtn=findViewById(R.id.registerbtn);
        dob=findViewById(R.id.dobedittext);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(myCalendar.getTime());
        dob.setText(formattedDate);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,mydate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);




        pinCodeValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pinCode=pinCodeEdt.getText().toString();

                if (TextUtils.isEmpty(pinCode)){
                    Toast.makeText(MainActivity.this,"Enter Valid Pincode",Toast.LENGTH_LONG).show();
                }else {
                    getDataFromPincode(pinCode);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate()){
                    Toast.makeText(MainActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                    return;
                }

                registerBtn.setEnabled(true);

                Intent intent=new Intent(MainActivity.this,WeatherToday.class);
                startActivity(intent);

            }
        });
    }

    private boolean validate() {

        boolean valid=true;

        String mobilenostr=mobilno.getText().toString();
        String fullnamestr=fullname.getText().toString();
        String genderstr=gender.getText().toString();
        String dobstr=dob.getText().toString();
        String addressline1str=addressline1.getText().toString();
        String pincodestr=pinCodeEdt.getText().toString();

        if (mobilenostr.isEmpty()){
            valid=false;
            mobilno.setError("Enter Mobile No");
        }else {
            mobilno.setError(null);
        }

        if (fullnamestr.isEmpty()){
            valid=false;
            fullname.setError("Enter Mobile No");
        }else {
            fullname.setError(null);
        }

        if (genderstr.isEmpty()){
            valid=false;
            gender.setError("Enter Mobile No");
        }else {
            gender.setError(null);
        }

        if (dobstr.isEmpty()){
            valid=false;
            dob.setError("Enter Mobile No");
        }else {
            dob.setError(null);
        }

        if (addressline1str.isEmpty()){
            valid=false;
            addressline1.setError("Enter Mobile No");
        }else {
            addressline1.setError(null);
        }

        if (pincodestr.isEmpty()){
            valid=false;
            pinCodeEdt.setError("Enter Mobile No");
        }else {
            pinCodeEdt.setError(null);
        }

        return valid;

    }

    private void getDataFromPincode(String pinCode) {

        mRequestQueue.getCache().clear();

        String url = "https://api.postalpincode.in/pincode/" + pinCode;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    if (response.getString("Status").equals("Error")) {
                        districtTextview.setText("Pin code is not valid.");
                    } else {

                        JSONObject obj = postOfficeArray.getJSONObject(0);

                        String district = obj.getString("District");
                        String state = obj.getString("State");


                        districtTextview.setText("Details of pin code is : \n" + "District is : " + district + "\n" + "State : "
                                + state );
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    districtTextview.setText("Pin code is not valid");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                districtTextview.setText("Pin code is not valid");
            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
    }


    DatePickerDialog.OnDateSetListener mydate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            view.setMaxDate(System.currentTimeMillis());
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dob.setText(sdf.format(myCalendar.getTime()));
        }

    };
}