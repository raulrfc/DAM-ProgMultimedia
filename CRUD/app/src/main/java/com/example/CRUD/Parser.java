package com.example.CRUD;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parser {
        private URL rssUrl;

        public Parser(String url) {
            try {
                this.rssUrl = new URL(url);
            } catch (MalformedURLException e){
                throw new RuntimeException(e);
            }
        }

        public ArrayList<Item> parse(){
            //Instanciamos la fábrica para DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            ArrayList<Item> items = new ArrayList<Item>();

            try {
                //Creamos un nuevo parser DOM
                DocumentBuilder builder = factory.newDocumentBuilder();

                //Realizamos la lectura completa del XML
                Document dom = builder.parse(this.getInputStream());

                //Nos posicionamos en el nodo principal del árbol (<wishes>)
                Element root = dom.getDocumentElement();

                //Localizamos todos los elementos <wish>
                NodeList wishes = root.getElementsByTagName("wish");

                //Recorremos la lista de deseos
                for (int i=0; i<wishes.getLength(); i++)
                {
                    Item item = new Item();

                    //Obtenemos el deseo actual
                    Node wish = wishes.item(i);

                    //Obtenemos la lista de datos del deseo actual
                    NodeList datosWish = wish.getChildNodes();

                    //Procesamos cada dato
                    for (int j=0; j<datosWish.getLength(); j++)
                    {
                        Node dato = datosWish.item(j);
                        String etiqueta = dato.getNodeName();

                        if (etiqueta.equals("nombre"))
                        {
                            String texto = obtenerTexto(dato);
                            item.setName(texto);
                        }
                        else if (etiqueta.equals("precio"))
                        {
                            item.setPrice(dato.getFirstChild().getNodeValue());
                        }
                        else if (etiqueta.equals("url"))
                        {
                            String texto = obtenerTexto(dato);
                            item.setUrl(texto);
                        }
                        else if (etiqueta.equals("rating"))
                        {
                            item.setRating(Float.parseFloat(dato.getFirstChild().getNodeValue()));
                        }
                    }
                    items.add(item);
                }
            } catch (Exception ex){
                throw new RuntimeException(ex);
            }

            return items;
        }

    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
                texto.append(fragmentos.item(k).getNodeValue());
        }
        return texto.toString();
    }

    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
