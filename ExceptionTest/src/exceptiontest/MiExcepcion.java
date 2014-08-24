/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptiontest;


public class MiExcepcion extends Exception {

    private String codError;
    
    public MiExcepcion() {
        // Invocamos al constructor de Exception con un mensaje por defecto
        super("Error por defecto");
        codError = "AAA000";
    }

    public MiExcepcion(String msg, String codError) {
        super(msg);
        this.codError = codError;
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }
}


