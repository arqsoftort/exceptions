/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptiontest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class GeneradorExcepciones {
    
    static Logger log = Logger.getLogger(GeneradorExcepciones.class.getName());
    
    public void encontrarArchivo() throws MiExcepcion{
        FileInputStream input = null;
        String path =  "ArchivoNoExiste";
        try {
            log.info("Buscando archivo: " + path);
            input = new FileInputStream(path);
            log.info("Archivo encontrado: " + path);
        } catch (FileNotFoundException ex) {
            log.error(ex);
            throw new MiExcepcion("Archivo no encontrado", "AAA001");
        }finally {
            log.debug("Cerrando recursos");
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
            log.debug("Fin cierre de recursos");
        }
    }
}
