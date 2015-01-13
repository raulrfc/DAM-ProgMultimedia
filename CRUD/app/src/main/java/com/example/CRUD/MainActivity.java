package com.example.CRUD;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    //////////////// VARIABLES \\\\\\\\\\\\\\\\
    Item item;
    ArrayList<Item> items = new ArrayList<>();
    ListView lv;
    private CustomAdapter adapter;
    String filename = "wishlist.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();
        //rellenarItems();

        adapter = new CustomAdapter(this);
        lv = (ListView)findViewById(R.id.list);
        registerForContextMenu(lv);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openURL(position);
            }
        });
    }

    private void openURL(int position) {
        String url = items.get(position).getUrl();
        url = checkURL(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private String checkURL(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    class CustomAdapter extends ArrayAdapter<Item> {

        Activity context;
        ArrayList<Item> arrayItems;

        CustomAdapter(Activity context) {
            super(context, R.layout.custom_layout, items);
            this.context = context;
            this.arrayItems = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.custom_layout, null);

            TextView lblName = (TextView)item.findViewById(R.id.name);
            lblName.setText(items.get(position).getName());

            TextView lblPrice = (TextView)item.findViewById(R.id.price);
            lblPrice.setText(items.get(position).getPrice());

            TextView lblUrl = (TextView)item.findViewById(R.id.url);
            lblUrl.setText(items.get(position).getUrl());

            return(item);
        }

        public void addItem(Item item) {
            arrayItems.add(item);
        }

        public void deleteItem(Item item) {
            arrayItems.remove(item);
            adapter.notifyDataSetChanged();
        }

        public void setItem(int position, Item item) {
            arrayItems.get(position).setName(item.getName());
            arrayItems.get(position).setUrl(item.getUrl());
            arrayItems.get(position).setPrice(item.getPrice());
        }
    } // FIN DE CUSTOM ADAPTER


    ////////////////////////// MENUS ///////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        Log.d("menu", "evento de menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                startActivityForResult(new Intent("ADD"), 1);
                return true;
            case R.id.MnuOpc2:

                return true;
            case R.id.MnuOpc3:
                saveItems(items);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,1,Menu.NONE,"Ver");
        menu.add(Menu.NONE,2,Menu.NONE,"Actualizar");
        menu.add(Menu.NONE,3,Menu.NONE,"Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            case 1: // Ver
                openURL(info.position);
                return true;
            case 2: // Actualizar
                Intent intent = new Intent("EDIT");

                intent.putExtra("item", items.get(info.position));
                intent.putExtra("pos", info.position);

                startActivityForResult(intent, 2);
                return true;
            case 3: // Eliminar
                adapter.deleteItem(items.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void rellenarItems() {
        Item c1 = new Item("Numb", "Linkin Park", "3:48");
        Item c2 = new Item("Nothing Else Matters", "Metallica", "5:23");
        Item c3 = new Item("Jump", "Van Halen", "3:35");

        items.add(c1);
        items.add(c2);
        items.add(c3);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String price = data.getStringExtra("price");
                String url = data.getStringExtra("url");

                item = new Item(name, price, url);
                adapter.addItem(item);
                adapter.notifyDataSetChanged();
                saveItems(items);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                adapter.setItem(data.getIntExtra("pos", 0), (Item) data.getSerializableExtra("item"));
                adapter.notifyDataSetChanged();
                saveItems(items);
            }
        }
    }


    public void loadItems() {
        ObjectInputStream input = null;

        try {

            input = new ObjectInputStream(new FileInputStream(new File(getFilesDir(),"") + File.separator+filename));
            items = (ArrayList<Item>) input.readObject();
            input.close();


        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveItems(ArrayList<Item> items) {
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),"") + File.separator+filename));


            out.writeObject(items);
            out.close();

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }
}
