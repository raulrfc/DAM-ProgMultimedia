package com.example.CRUD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends ActionBarActivity implements View.OnClickListener {

    Button accept, cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        accept = (Button)findViewById(R.id.btnOK);
        cancel = (Button)findViewById(R.id.btnCancel);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) { // switch según la id de las vistas
            case R.id.btnOK:
                Intent data = new Intent();
                EditText name = (EditText) findViewById(R.id.editName);
                EditText price = (EditText) findViewById(R.id.editPrice);
                EditText url = (EditText) findViewById(R.id.editUrl);

                data.putExtra("name", name.getText().toString());
                data.putExtra("price", price.getText().toString());
                data.putExtra("url", url.getText().toString());

                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
