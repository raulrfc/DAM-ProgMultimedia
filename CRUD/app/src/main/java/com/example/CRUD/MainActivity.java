package com.example.CRUD;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;


public class MainActivity extends ActionBarActivity {
    //////////////// VARIABLES \\\\\\\\\\\\\\\\
    Item item;
    ArrayList<Item> items = new ArrayList<>();
    ListView lv;
    CustomAdapter adapter;
    SQLHelper sqlh = new SQLHelper(this, null,1);
    SQLiteDatabase db;
    String flag = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = sqlh.getWritableDatabase();

        fillWishes();
        loadListView();
    }


    /////////////////////// MÉTODOS AUXILIARES \\\\\\\\\\\\\\\\\\\\\\\\\
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

    private void loadListView(){
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

    private void showDialog(final String titulo, String mensaje) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        if (titulo.equals("XML GEN")) {
                            try {
                                generateXML(value.toString() + ".xml");
                                Toast.makeText(MainActivity.this,"XML generado correctamente" ,Toast.LENGTH_LONG).show();
                                } catch  (Exception e) {}
                        } else {
                            new UpdateTask().execute(value.toString());
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    //////////////////// ADAPTADOR \\\\\\\\\\\\\\\\\\\\\
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
            lblPrice.setText(items.get(position).getPrice() + " €");

            RatingBar rb = (RatingBar)item.findViewById(R.id.ratingBar);
            rb.setRating(items.get(position).getRating());

            return(item);
        }

        public void addItem(Item item) {
            sqlh.insertWish(db, item.getName(), item.getUrl(), item.getPrice(), item.getRating());
            arrayItems.add(item);
        }

        public void deleteItem(Item item) {
            sqlh.deleteWish(db, item.getId());
            arrayItems.remove(item);

            adapter.notifyDataSetChanged();
        }

        public void UpdateItem(int position, Item item, String oldurl) {
            sqlh.updateWish(db, item.getName(), oldurl, item.getPrice(), item.getRating());
            arrayItems.get(position).setName(item.getName());
            arrayItems.get(position).setUrl(item.getUrl());
            arrayItems.get(position).setPrice(item.getPrice());
            arrayItems.get(position).setRating(item.getRating());
        }
    } // FIN DE CUSTOM ADAPTER


    ////////////////////////// MENUS ///////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                startActivityForResult(new Intent("ADD"), 1);
                return true;
            case R.id.MnuOpc2:
                flag = "sax";
                showDialog("XML SAX", "Introduce la dirección para leer el xml");
                return true;
            case R.id.MnuOpc3:
                flag = "dom";
                showDialog("XML DOM", "Introduce la dirección para leer el xml");
                return true;
            case R.id.MnuOpc4:
                showDialog("XML GEN", "Introduce el nombre del fichero a generar");
                return true;
            case R.id.MnuOpc5:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,1,Menu.NONE,"Ver");
        menu.add(Menu.NONE,2,Menu.NONE,"Modificar");
        menu.add(Menu.NONE,3,Menu.NONE,"Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            case 1: // Ver
                openURL(info.position);
                return true;
            case 2: // Actualizar
                Intent intent = new Intent("EDIT");

                intent.putExtra("url", items.get(info.position).getUrl());
                intent.putExtra("item", items.get(info.position));
                intent.putExtra("pos", info.position);

                startActivityForResult(intent, 2);
                return true;
            case 3: // Eliminar
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar")
                        .setMessage("Estás seguro de eliminar esta entrada?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(items.get(info.position));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //////////// ACTIVITY RESULTADOS \\\\\\\\\\\\\\\\\\
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String price = data.getStringExtra("price");
                String url = data.getStringExtra("url");
                Float rating = data.getFloatExtra("rating", 0);

                item = new Item(name, price, url, rating);
                adapter.addItem(item);
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                adapter.UpdateItem(data.getIntExtra("pos", 0), (Item) data.getSerializableExtra("item"), data.getStringExtra("oldurl"));
                adapter.notifyDataSetChanged();
            }
        }
    }

    /////////// BASE DE DATOS \\\\\\\\\\\\\\\

    public void fillWishes() {
        Cursor c = sqlh.getAllWishes(db);
            if(c.moveToFirst()) {
            do {
                item = new Item(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getFloat(4));
                items.add(item);
            } while(c.moveToNext());
        }
    }

    ///////////// ASYNCTASK \\\\\\\\\\\\\\

    private class UpdateTask extends AsyncTask<String, String , String> {
        protected String doInBackground(String... urls) {
            try {
                if (flag.equals("dom")) {
                    Parser parser = new Parser(urls[0]);
                    items = parser.parse();

                } else if (flag.equals("sax")) {
                    XmlParser parser = new XmlParser(urls[0]);
                    items = parser.parse();

                }
                return items.toString();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String result) {
            loadListView();
        }
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {
        JSONParser jParser = new JSONParser();

        ArrayList<Item> itemList;

        // url to get all products list
        private String url_all_wishes = "http://10.0.2.2/android_connect/get_all_wishes.php";

        // JSON Node names
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_WISHES = "wish";
        private static final String TAG_ID = "ID";
        private static final String TAG_NOMBRE = "Nombre";
        private static final String TAG_PRECIO = "Precio";
        private static final String TAG_URL = "URL";
        private static final String TAG_RATING = "Rating";

        // products JSONArray
        JSONArray wishes = null;

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_wishes, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Wishes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    wishes = json.getJSONArray(TAG_WISHES);

                    // looping through All Products
                    for (int i = 0; i < wishes.length(); i++) {
                        JSONObject c = wishes.getJSONObject(i);

                        // Storing each json item in variable
                        int id = Integer.parseInt(c.getString(TAG_ID));
                        String name = c.getString(TAG_NOMBRE);
                        String price = c.getString(TAG_PRECIO);
                        String url = c.getString(TAG_URL);
                        float rating = Float.parseFloat(c.getString(TAG_RATING));

                        Item it = new Item(id, name, price, url, rating);

                        // adding item to ArrayList
                        itemList.add(it);
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                }
            });

        }
    }


    ///////////////// GENERAR XML \\\\\\\\\\\\\\\\\\\\\\\

    public void generateXML( String titulo)throws IOException {
        //Creación del serializer
        XmlSerializer ser = Xml.newSerializer();

        //Crea un fichero en memoria interna
        OutputStreamWriter fout =
                new OutputStreamWriter(
                        openFileOutput(titulo,
                                Context.MODE_PRIVATE));

        //Asignación el resultado del serializer al fichero
        ser.setOutput(fout);

        //Carga la codificación e identación para el documento
        ser.startDocument("UTF-8", false);
        ser.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        //Construcción el XML
        ser.startTag("", "wishes");

        for (int i = 0; i < items.size(); i++) {

            ser.startTag("", "wish");

            ser.startTag("", "nombre");
            ser.text(items.get(i).getName());
            ser.endTag("", "nombre");

            ser.startTag("", "precio");
            ser.text(items.get(i).getPrice());
            ser.endTag("", "precio");

            ser.startTag("", "url");
            ser.text(items.get(i).getUrl());
            ser.endTag("", "url");

            ser.startTag("", "rating");
            ser.text(String.valueOf(items.get(i).getRating()));
            ser.endTag("", "rating");

            ser.endTag("", "wish");

        }
        ser.endTag("", "wishes");

        ser.endDocument();

        fout.close();
    }


}
