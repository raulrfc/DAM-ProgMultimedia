package com.pm.niar.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements View.OnClickListener {

    //Variables
    Button uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero, suma, resta, multi, dividir, clear, igual;
    EditText pantalla;
    int num1;
    int num2;
    char operacion = '=';
    Editable str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asignación de los botones con sus ID correspondientes
        uno = (Button) findViewById(R.id.uno);
        dos = (Button) findViewById(R.id.dos);
        tres = (Button) findViewById(R.id.tres);
        cuatro = (Button) findViewById(R.id.cuatro);
        cinco = (Button) findViewById(R.id.cinco);
        seis = (Button) findViewById(R.id.seis);
        siete = (Button) findViewById(R.id.siete);
        ocho = (Button) findViewById(R.id.ocho);
        nueve = (Button) findViewById(R.id.nueve);
        cero = (Button) findViewById(R.id.cero);
        suma = (Button) findViewById(R.id.suma);
        resta = (Button) findViewById(R.id.resta);
        multi = (Button) findViewById(R.id.multi);
        dividir = (Button) findViewById(R.id.dividir);
        clear = (Button) findViewById(R.id.clear);
        igual = (Button) findViewById(R.id.igual);
        pantalla = (EditText) findViewById(R.id.pantalla);

        //Asignación del método onClick a cada botón
        uno.setOnClickListener(this);
        dos.setOnClickListener(this);
        tres.setOnClickListener(this);
        cuatro.setOnClickListener(this);
        cinco.setOnClickListener(this);
        seis.setOnClickListener(this);
        siete.setOnClickListener(this);
        ocho.setOnClickListener(this);
        nueve.setOnClickListener(this);
        cero.setOnClickListener(this);
        suma.setOnClickListener(this);
        resta.setOnClickListener(this);
        multi.setOnClickListener(this);
        dividir.setOnClickListener(this);
        clear.setOnClickListener(this);
        igual.setOnClickListener(this);
        pantalla.setOnClickListener(this);
    } // FIN DE ON CREATE

    private void escribirEnPantalla(String text) {
        pantalla.setText(text);
    }
    
    private void obtenerNum2(){
        num2 = Integer.parseInt(pantalla.getText().toString()); // convierte el número del string en pantalla a integer y lo guarda en la variable "num2"
        escribirEnPantalla(""); // vacía la pantalla    
    }
    
    private void sumar(){
        num1 = num1 + num2; // realiza la operación y la guarda en "num1"
        escribirEnPantalla(Integer.toString(num1)); // muestra por pantalla "num1"
    }
    private void restar(){
        num1 = num1 - num2; 
        escribirEnPantalla(Integer.toString(num1)); 
    }
    private void multiplicar(){
        num1 = num1 * num2; 
        escribirEnPantalla(Integer.toString(num1)); 
    }
    private void dividir(){
        num1 = num1 / num2;
        escribirEnPantalla(Integer.toString(num1)); 
    }
    /*
        @desc   método para realizar las operaciones de la calculadora
        @param  char op - la operación que va a realizarse
     */
    private void operaciones(char op) {
        obtenerNum2();
        switch (op) {    
            case '+':
                sumar();
                break;
            case '-':
                restar();
                break;
            case '*':
                multiplicar();
                break;
            case '/':
                dividir();
                break;
        }
    }//FIN DE OPERACIONES

    /*
        @desc   método para agregar el número del botón pulsado al que muestra la pantalla
        @param  Button btn - el botón que será pulsado
     */
    private void addNumber(Button btn){
        if(num2 != 0){ // si "num2" es distinto de cero le asignamos el cero y limpiamos la pantalla
            num2 = 0;
            escribirEnPantalla("");
        }
        str = str.append(btn.getText());  // asignamos a la variable "str" el número del botón
        escribirEnPantalla(str.toString()); // mostramos por pantalla la variable "str" que contiene el número
    }

    /*
        @desc   método para agregar la operación del botón pulsado
        @param  char op - operación que va a agregarse
     */
    private void addOperation(char op) {
        if(num1 == 0){  // si "num1" es igual a cero le asignamos el número de pantalla y limpiamos la pantalla
            num1 = Integer.parseInt(pantalla.getText().toString());
            escribirEnPantalla("");
        }
        else if(num2 != 0){ // si "num2" es distinto de cero le asignamos el valor 0 y limpiamos la pantalla
            num2 = 0;
            escribirEnPantalla("");
        }
        else{
            operaciones(op); // sino realizamos las operaciones correspondientes
        }
    }

    /*
        @desc   método onClick para cada uno de los botones de la calculadora
        @param  View v - contiene el botón para el cual se realiza el click
     */
    @Override
    public void onClick(View v) {
        str = pantalla.getText(); // asignación a "str" del texto de la pantalla
        int id = v.getId(); // asignación a la variable "id" de la id de la vista

        switch (id) { // switch según la id de las vistas
            case R.id.uno:
                addNumber(uno);
                break;
            case R.id.dos:
                addNumber(dos);
                break;
            case R.id.tres:
                addNumber(tres);
                break;
            case R.id.cuatro:
                addNumber(cuatro);
                break;
            case R.id.cinco:
                addNumber(cinco);
                break;
            case R.id.seis:
                addNumber(seis);
                break;
            case R.id.siete:
                addNumber(siete);
                break;
            case R.id.ocho:
                addNumber(ocho);
                break;
            case R.id.nueve:
                addNumber(nueve);
                break;
            case R.id.cero:
                addNumber(cero);
                break;
            case R.id.clear:
                num1 = 0;
                num2 = 0;
                escribirEnPantalla("");
                pantalla.setHint("Realiza una operación");
                break;
            case R.id.suma:
                operacion = '+';
                addOperation('+');
                break;
            case R.id.resta:
                operacion = '-';
                addOperation('-');
                break;
            case R.id.multi:
                operacion = '*';
                addOperation('*');
                break;
            case R.id.dividir:
                operacion = '/';
                addOperation('/');
                break;
            case R.id.igual:
                if (num2 != 0) {
                    if (operacion == '+') {
                        escribirEnPantalla("");
                        num1 = num1 + num2;
                        pantalla.setText(Integer.toString(num1));
                    } else if (operacion == '-') {
                        escribirEnPantalla("");
                        num1 = num1 - num2;
                        pantalla.setText(Integer.toString(num1));
                    } else if (operacion == '*') {
                        escribirEnPantalla("");
                        num1 = num1 * num2;
                        pantalla.setText(Integer.toString(num1));
                    } else if (operacion == '/') {
                        escribirEnPantalla("");
                        num1 = num1 / num2;
                        pantalla.setText(Integer.toString(num1));
                    }
                } else {
                    operaciones(operacion);
                }
        }
    }//FIN DE ONCLICK
}
