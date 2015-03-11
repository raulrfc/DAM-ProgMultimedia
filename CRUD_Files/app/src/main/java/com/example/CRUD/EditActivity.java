package com.example.CRUD;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class EditActivity extends ActionBarActivity implements View.OnClickListener {

    Button accept, cancel;
    Item item;
    int pos;
    private EditText name;
    private EditText price;
    private EditText url;
    private RatingBar rb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        item = (Item) getIntent().getSerializableExtra("item");
        pos = getIntent().getIntExtra("pos", 0);

        name = (EditText) findViewById(R.id.editName);
        price = (EditText) findViewById(R.id.editPrice);
        url = (EditText) findViewById(R.id.editUrl);
        rb = (RatingBar) findViewById(R.id.ratingBar3);

        name.setText(item.getName());
        price.setText(item.getPrice());
        url.setText(item.getUrl());
        rb.setRating(item.getRating());

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

                item.setName(name.getText().toString());
                item.setPrice(price.getText().toString());
                item.setUrl(url.getText().toString());
                item.setRating(rb.getRating());

                data.putExtra("item", item);
                data.putExtra("pos", pos);

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
