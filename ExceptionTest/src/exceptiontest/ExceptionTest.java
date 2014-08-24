/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptiontest;

import static exceptiontest.ExceptionTest.log;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuario
 */
public class ExceptionTest {

    static Logger log = Logger.getLogger(ExceptionTest.class.getName());
    
    public static void main(String[] args) {
        GeneradorExcepciones ge = new GeneradorExcepciones();
        try {
            ge.encontrarArchivo();
        } catch (MiExcepcion ex) {
            log.error("Ocurrió un error buscando el archivo");
        }
    }
}
