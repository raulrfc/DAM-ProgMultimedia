package com.example.CRUD;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XmlHandler extends DefaultHandler {
    private ArrayList<Item> items;
    private Item itemActual;
    private StringBuilder sbTexto;

    public ArrayList<Item> getWishes(){
        return items;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        super.characters(ch, start, length);

        if (this.itemActual != null)
            sbTexto.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {

        super.endElement(uri, localName, name);

        if (this.itemActual != null) {

            if (localName.equals("nombre")) {
                itemActual.setName(sbTexto.toString());
            } else if (localName.equals("url")) {
                itemActual.setUrl(sbTexto.toString());
            } else if (localName.equals("precio")) {
                itemActual.setPrice(sbTexto.toString());
            } else if (localName.equals("rating")) {
                itemActual.setRating(Float.parseFloat(sbTexto.toString()));
            } else if (localName.equals("wish")) {
                items.add(itemActual);
            }
            sbTexto.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {

        super.startDocument();

        items = new ArrayList<>();
        sbTexto = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName,
                             String name, Attributes attributes) throws SAXException {

        super.startElement(uri, localName, name, attributes);

        if (localName.equals("wish")) {
            itemActual = new Item();
        }
    }
}
