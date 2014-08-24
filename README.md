#Laboratorio sobre el manejo de Exceptions en JEE

##Objetivo
El presente documento tiene como objetivo ayudar al estudiante a comprender cómo manejar las excepciones que ocurren en las aplicaciones, viendo estrategias para capturarlas correctamente  y evitar la interrupción de la ejecución. Es recomendable que el alumno tenga presente el laboratorio 1 para poder implementar el logueo de errores.

##Requisitos previos
Para este laboratorio son necesarios los siguientes items.
- Netbeans IDE o similar
- Es recomendable haber comprendido el concepto y el uso de la herramienta Log4j vista en el Laboratorio 1.

##Introducción
Las excepciones son un comportamiento no esperado en el flujo normal de la aplicación y suelen generar interrupciones en el mismo si no son controladas correctamente, siempre van a existir excepciones y por ese mismo motivo es que debemos “protegernos” de ellas. La idea principal es evitar que la aplicación deje de funcionar por la ocurrencia de excepciones, por este motivo es que implementamos diferentes tácticas para manejarlas  que veremos en este laboratorio. 
Existen tres categorías de excepciones que debemos considerar a la hora de desarrollar una aplicación:
- Errores: Son el tipo de excepciones que no podemos controlar, se asocian a temas que están por fuera del alcance de nuestra aplicación, por ejemplo: fallas en el hardware, errores de la JVM, etc. (Ej: OutOfMemoryError y StackOverflowError)
- Controladas: Son el tipo de excepción que nosotros podemos anticipar y recuperarnos de ellas (por ejemplo: FileNotFoundException, IOException, etc.), sabemos que pueden ocurrir pero nos anticipamos y desarrollamos un flujo a seguir cuando éstas se dan.
- Runtime: Son aquellas excepciones que se dan en tiempo de ejecución y que generalmente no son capturadas (por ejemplo: NullPointerException, ArrayOutOfBoundsException, etc). Estas excepciones suelen ser evitadas aplicando programación “defensiva” (por ejemplo: controlar que un objeto no sea nulo antes de invocar un método en él, controlar el largo de un array antes de obtener un elemento, etc).
En general, vamos a procurar siempre manejar correctamente las excepciones que son controladas (ya que los errores no podemos controlarlos y las de runtime las evitamos programando correctamente). 

##Excepciones propias
La idea es que nuestros métodos siempre tengan un comportamiento previsto para las excepciones que pueda tirar un método que invocamos de una capa de “más abajo”, es cierto que un método (por ejemplo de una API de Java) puede tirar muchas excepciones distintas, pero nuestros métodos deberían tirar pocas y conocidas, sobretodo conocer que excepción puede ocurrir, para eso usamos excepciones propias. Veremos en el ejemplo cómo crearlas y manejarlas correctamente.



##Ejemplo de uso
Desarrollaremos paso a paso una ejemplo de aplicación de lo comentado anteriormente. La idea del ejemplo es capturar excepciones y transformarlas en excepciones conocidas para poder manejarlas.

####1) Creación del proyecto
Para simplificar, este ejemplo será desarrollado como una Java Application para que se ejecute por consola. Cabe destacar que los ejemplos que veremos son análogos para los EJB y otros componentes en donde se quieran utilizar.

Abrir el NetBeans IDE, ir a “New Project”, se desplegará la una nueva ventana donde eleccionamos “Java Application” dentro de la carpeta “Java”, luego presionamos el botón “Next”. En la siguiente pantalla nos pide un nombre para el proyecto, en este caso se llamará “ExceptionTest”, luego presionamos el botón “Finish”.


####2) Creación de una excepción personalizada
Vamos a crear una excepción propia para poder manejarla dentro de nuestra aplicación, para eso, hacer click derecho sobre el proyecto, seleccionar New -> Java Exception, en el ejemplo la llamaremos “MiExcepcion”. El IDE nos generará una clase Java que hereda de Exception, similar a la siguiente:

```java
public class MiExcepcion extends Exception {
    
    public MiExcepcion() {
    }

    public MiExcepcion(String msg) {
        super(msg);
    }
}


```
A la clase autogenerada podemos agregarle los métodos que se quieran, o bien agregarle atributos nuevos, etc. A la nuestra, le agregaremos un nuevo atributo codError y algunos constructores para utilizarlos luego. A continuación vemos el código de la clase MiExcepcion:


```java
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

```


3) Ejemplo de manejo de errores
Crearemos una clase GeneradorExcepciones con un método que se encargue de buscar un archivo que no existe, por lo tanto tirará una excepción (FileNotFoundException) y nuestro método la transformará en nuestra excepción propia para lanzarla.
El código de la clase es el siguiente:

```java
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
```

La clase tiene un método que busca un archivo que no existe, por lo tanto genera una excepción controlada (FileNotFoundException) y definimos un comportamiento a seguir dentro del bloque catch (en este caso, se genera un log para la excepción y se lanza una excepción propia con un código y mensaje definido por nosotros). En el código anterior se ven varias palabras claves que explicaremos a continuación:
throws: Indica que el método podría lanzar una excepción del tipo que se indica (siempre se define en la firma del método).
- try: Encierra una porción de código que puede generar alguna de las excepciones que se definen en los bloques catch.
- catch: Es un bloque de código en el que solo se entrará si ocurre una excepción del tipo que se define.
- throw: Indica que “lanza” una excepción al método que lo llamó. El tipo de la excepción que se lanza debe ser igual al definido en la firma (luego del throws)
- finally: Es un bloque de código que se ejecutará siempre, por este motivo es que se suele utilizar para cerrar recursos que se usaron, conexiones, crear algun log, etc.

El código de la clase principal sería el siguiente:

```java
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
```

Como se ve en el código, al invocar el método sabemos que puede llegar a darnos una excepción de tipo MiExcepcion, por lo tanto definimos un comportamiento para cuando esto ocurra. El método main no sabe las excepciones que ocurren dentro del método que invoca, pero sí espera que ocurra una excepción propia.
