/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lecturaescrituraxmldom;

import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Persona;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author David Marín
 */
public class DOMCreateNS {

    private static final String PERSONAS_TAG = "personas";
    private static final String PERSONA_TAG = "persona";

    private static final String PERSONA_ATT_ID = "id";
    private static final String PERSONA_ATT_BORRADO = "borrado";

    private static final String PERSONA_NOMBRE_TAG = "nombre";
    private static final String PERSONA_DNI_TAG = "nombre";
    private static final String PERSONA_EDAD_TAG = "api";
    private static final String PERSONA_SALARIO_TAG = "numero";

    private static final String PERSONAS_OUTPUT_FILE = Paths.get("src", "docs", "personas_ns_output.xml").toString();

    private static final String PERSONAS_NS_URI = "http://www.personas.com";
    private static final String PERSONAS_NS_URI_PREFIX = "a";
    
    private static final String PERSONA_Q_TAG = "a:persona";

    public static void main(String[] args) {
        try {

            ArrayList<Persona> personas = crearPersonas();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            DOMImplementation implementation = builder.getDOMImplementation();
            //DTDs no soportan namespaces => docType es null

            //Crea un document con un elemento raiz. Si añadimos aquí el namespace, se añadirá al elemento raíz con el prefijo que se indique en el 2º argumento.
            //Si no se indica prefijo, será el ns por defecto
            Document document = implementation.createDocument(null, PERSONAS_TAG, null);

            //Si no se necesita DOCTYPE se podría llamar a createDocument con el tercer parámetro a null
            //Document document = implementation.createDocument(null, PERSONAS_TAG, docType);
            //Obtenemos el elemento raíz
            Element root = document.getDocumentElement();
            //Para añadir más de un namespace 
            root.setAttribute("xmlns:" + PERSONAS_NS_URI_PREFIX, PERSONAS_NS_URI);

            //Existe otra posibilidad para la creación de un document totalmente vacío, al que hay que añadirle un elemento raíz:
//            //Crear un nuevo documento XML VACÍO
//            Document document = builder.newDocument();
//            //Crear el nodo raíz y añadirlo al documento
//            Element root = document.createElement(PERSONAS_TAG);
//            document.appendChild(root);
            int contador = 1;
            for (Persona persona : personas) {
                //Para ejemplificar cómo pueden convivir etiquetas calificadas con no calificadas usamos
                // el criterio: "los elementos de la lista con índice impar irán calificadas, los de índice par no:"

                boolean par = (contador % 2 == 0);
                Element ePersona = null;
                if (par) {
                    ePersona = document.createElement(PERSONA_TAG);
                } else {
                    ePersona = document.createElementNS(PERSONAS_NS_URI, PERSONA_Q_TAG);
                }

                ePersona.setAttribute(PERSONA_ATT_ID, String.valueOf(persona.getId()));
                ePersona.setAttribute(PERSONA_ATT_BORRADO, String.valueOf(persona.isBorrado()));

                addElementConTexto(document, ePersona, PERSONA_NOMBRE_TAG, persona.getNombre());
                addElementConTexto(document, ePersona, PERSONA_DNI_TAG, persona.getDni());
                addElementConTexto(document, ePersona, PERSONA_EDAD_TAG, String.valueOf(persona.getEdad()));
                addElementConTexto(document, ePersona, PERSONA_SALARIO_TAG, String.valueOf(persona.getSalario()));

                root.appendChild(ePersona);
                contador++;
            }

            //Para generar un documento XML con un objeto Document
            //Generar el tranformador para obtener el documento XML en un fichero
            TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
            //Espacios para indentar cada línea
            fabricaTransformador.setAttribute("indent-number", 4);
            Transformer transformador = fabricaTransformador.newTransformer();
            //Insertar saltos de línea al final de cada línea
            //https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/OutputKeys.html
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");

            //Si se quisiera añadir el <!DOCTYPE>:
            // transformador.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
            //El origen de la transformación es el document
            Source origen = new DOMSource(document);
            //El destino será un stream a un fichero 
            Result destino = new StreamResult(PERSONAS_OUTPUT_FILE);
            transformador.transform(origen, destino);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        } catch (TransformerException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        }

    }

    private static ArrayList<Persona> crearPersonas() {
        ArrayList<Persona> personas = new ArrayList<>();

        Persona personaA = new Persona(1, "12345678A", 23, 25000.33f, "Lucas");
        Persona personaB = new Persona(2, "12345678B", 72, 1000.42f, "Juana");
        Persona personaC = new Persona(3, "12345678C", 54, 40000.33f, "Eva");
        Persona personaD = new Persona(4, "12345678D", 99, 900.42f, "Unai");
        
        personas.add(personaA);
        personas.add(personaB);
        personas.add(personaC);
        personas.add(personaD);

        return personas;
    }

    private static void addElementConTexto(Document document, Node padre, String tag, String text) {
        //Creamos un nuevo nodo de tipo elemento desde document
        Node node = document.createElement(tag);
        //Creamos un nuevo nodo de tipo texto también desde document
        Node nodeTexto = document.createTextNode(text);
        //añadimos a un nodo padre el nodo elemento
        padre.appendChild(node);
        //Añadimos al nodo elemento su nodo hijo de tipo texto
        node.appendChild(nodeTexto);
    }

}
