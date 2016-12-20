package com.dobody.bkk.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dobody.bkk.BaseActivity;
import com.dobody.bkk.R;
import com.dobody.bkk.adapter.BaseRecyclerAdapter;
import com.dobody.bkk.constant.Constants;
import com.dobody.bkk.dataaccess.UserInfo;
import com.dobody.bkk.utils.ClientUtils;
import com.dobody.bkk.utils.ConvertUtils;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class UserProfileActivity extends BaseActivity implements OnClickListener {
    private EditText txtID;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtDocumentValidityDate;
    private EditText txtDocumentType;
    private EditText txtEmail;
    private EditText txtAddress;
    private EditText txtDateOfBirth;
    private EditText txtCOB;
    private RadioButton rdMale;
    private View btnSave;
    Calendar dtValidityDate = Calendar.getInstance();
    Calendar dtDOB = Calendar.getInstance();
    //    private View ivLoading;
    JsonObject jsonObject;
    private View btnWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        jsonObject = ConvertUtils.toJsonObject(getIntent().getStringExtra(Constants.DATA_DATA));
        txtID = (EditText) findViewById(R.id.txtId);
        txtID.setEnabled(false);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtDocumentValidityDate = (EditText) findViewById(R.id.txtDocumentValidityDate);
        txtDocumentType = (EditText) findViewById(R.id.txtDocumentType);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtDateOfBirth = (EditText) findViewById(R.id.txtDateOfBirth);
        rdMale = (RadioButton) findViewById(R.id.rdMale);
        txtCOB = (EditText) findViewById(R.id.txtCOB);
        txtCOB.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);

        txtDocumentValidityDate.setOnClickListener(this);
        txtDateOfBirth.setOnClickListener(this);
        btnWarning = findViewById(R.id.btnWarning);
        if (ConvertUtils.toString(ConvertUtils.toJsonObject(ConvertUtils.toJsonObject(jsonObject.get("data")).get("user")).get("status")).equals("VERIFYING")) {
            btnWarning.setVisibility(View.VISIBLE);
            btnWarning.setOnClickListener(this);
        } else {
            btnWarning.setVisibility(View.GONE);
        }

        String id = getIntent().getStringExtra(Constants.DATA_ID);
        if (id != null)
            txtID.setText(id);

//        ivLoading = findViewById(R.id.ivProcessLoading);
        btnSave.setOnClickListener(this);
        country.put("Vietnam", "VN");
        country.put("Singapor", "SG");
        country.put("Thailand", "TH");
        country.put("Laos", "LA");
        country.put("Korea", "KR");

    }

    HashMap<String, String> country = new HashMap<String, String>();

    DatePickerDialog.OnDateSetListener dateValidity = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dtValidityDate.set(Calendar.YEAR, year);
            dtValidityDate.set(Calendar.MONTH, monthOfYear);
            dtValidityDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txtDocumentValidityDate.setText(ConvertUtils.toDateString(dtValidityDate.getTime().getTime(), "yyyy-MM-dd"));
        }

    };

    DatePickerDialog.OnDateSetListener dateDOB = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dtDOB.set(Calendar.YEAR, year);
            dtDOB.set(Calendar.MONTH, monthOfYear);
            dtDOB.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txtDateOfBirth.setText(ConvertUtils.toDateString(dtDOB.getTime().getTime(), "yyyy-MM-dd"));
        }

    };

    public static void open(Context context, String id, String data) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(Constants.DATA_ID, id);
        intent.putExtra(Constants.DATA_DATA, data);
        context.startActivity(intent);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtCOB:
                View view1 = getLayoutInflater().inflate(R.layout.dialog_select_country, null, false);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.recyclerView);
                final Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view1).create();
                dialog.show();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                new BaseRecyclerAdapter<ViewHolder, String>(R.layout.item_list_country, new BaseRecyclerAdapter.BaseViewHolder<ViewHolder, String>() {
                    @Override
                    public ViewHolder getViewHolder(View v) {
                        return new ViewHolder(v);
                    }

                    @Override
                    public void bindData(ViewHolder viewHolder, String data, int position) {
                        viewHolder.tvName.setText(data);
                    }
                }, ConvertUtils.toArrayList(String.class, "Vietnam", "Singapor", "Thailand", "Laos", "Korea"), new BaseRecyclerAdapter.OnClickListener() {
                    @Override
                    public void onClick(View v, int position, Object o) {
                        txtCOB.setText(o.toString());
                        dialog.dismiss();
                    }
                }).bindData(recyclerView);


                break;
            case R.id.btnWarning:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(R.layout.dialog_warning).setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VerificationActivity.open(getActivity());
                    }
                }).create();
                alertDialog.show();
                break;
            case R.id.txtDateOfBirth:
                new DatePickerDialog(getActivity(), dateDOB, dtDOB
                        .get(Calendar.YEAR), dtDOB.get(Calendar.MONTH),
                        dtDOB.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.txtDocumentValidityDate:
                new DatePickerDialog(getActivity(), dateValidity, dtValidityDate
                        .get(Calendar.YEAR), dtValidityDate.get(Calendar.MONTH),
                        dtValidityDate.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnSave:
                JsonObject jsonObject1 = ConvertUtils.toJsonObject(jsonObject.get("data"));
                jsonObject1 = ConvertUtils.toJsonObject(jsonObject1.get("user"));
                save(ConvertUtils.toString(jsonObject1.get("id")), txtFirstName.getText().toString(), txtLastName.getText().toString(), txtDocumentType.getText().toString(), txtDocumentValidityDate.getText().toString(), txtEmail.getText().toString(), "", txtAddress.getText().toString(), txtDateOfBirth.getText().toString(), rdMale.isChecked() ? "M" : "F", country.get(txtCOB.getText().toString()));
                break;
        }
    }

    private void save(final String id, final String firstName, final String lastName, final String documentType, final String documentExpiry, final String email, final String phone, final String address, final String dob, final String gender, final String country_of_birth) {
        new AsyncTask<Void, Void, ClientUtils.DataResponse>() {
            @Override
            protected void onPreExecute() {
                btnSave.setEnabled(false);
//                ivLoading.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final ClientUtils.DataResponse aVoid) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSave.setEnabled(true);
//                        ivLoading.setVisibility(View.GONE);
                        if (aVoid != null && aVoid.is200()) {
                        } else {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            jsonObject = ConvertUtils.toJsonObject(jsonObject.get("data"));
                        }
                    }
                }, 1000);
                super.onPostExecute(aVoid);

            }

            @Override
            protected ClientUtils.DataResponse doInBackground(Void... voids) {
                try {
                    return UserInfo.updateProfile(id, UserInfo.getToken(), firstName, lastName, documentType, documentExpiry, email, phone, address, dob, gender, country_of_birth);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}