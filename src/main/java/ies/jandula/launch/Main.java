package ies.jandula.launch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import ies.jandula.methods.Method;
import ies.jandula.tesoroExceptions.TesoroExceptions;

public class Main
{
    public static void main(String[] args) throws TesoroExceptions 
    {
        PrintWriter tableroInicialPrintWriter = null;
        PrintWriter historicoMovimientosPrintWriter = null;
        PrintWriter tableroActualPrintWriter = null;
        Scanner scannerDimensiones = new Scanner(System.in);
        Random random = new Random();

        Method method = new Method();

        try 
        {
            tableroInicialPrintWriter = new PrintWriter(method.TABLERO_INICIAL);
            historicoMovimientosPrintWriter = new PrintWriter(method.HISTORICO_MOVIMIENTOS);
            tableroActualPrintWriter = new PrintWriter(method.TABLERO_ACTUAL);

            int dimensiones;

            do 
            {
                System.out.println("Introduce las dimensiones (debe ser al menos 4): ");
                dimensiones = Integer.valueOf(scannerDimensiones.nextLine());
            }
            while (dimensiones < 4);

            method.creacionTablero(tableroInicialPrintWriter, random, dimensiones);

            Boolean continuar = true;
            while (continuar)
            {
                System.out.println("¿Quieres continuar el juego? (R para continuar, cualquier tecla para salir)");
                String respuesta = scannerDimensiones.nextLine();

                if (!respuesta.equalsIgnoreCase("R"))
                {
                	continuar = false;
                }

                if (!method.introducirCordenada(scannerDimensiones))
                {
                	continuar = false;
                }
            }

            method.guardarTablero(tableroActualPrintWriter);
            method.guardarHistoricoMovimientos(historicoMovimientosPrintWriter);
        } 
        catch (FileNotFoundException fileNotFoundException) 
        {
            String error = "Error: Fichero no encontrado";
            fileNotFoundException.printStackTrace();
            throw new TesoroExceptions(1, error, fileNotFoundException);
        } 
        finally 
        {
            if (tableroInicialPrintWriter != null)
            {
                tableroInicialPrintWriter.close();
            }
            if (historicoMovimientosPrintWriter != null)
            {
                historicoMovimientosPrintWriter.close();
            }
            if (tableroActualPrintWriter != null)
            {
                tableroActualPrintWriter.close();
            }
            scannerDimensiones.close();
        }
    }
}

