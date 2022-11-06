/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lecturaescrituraxmldom;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import model.Persona;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author David Marín
 */
public class DOMLeer {

    private static final String PERSONA_TAG = "persona";
    
    private static final String PERSONA_ATT_ID = "id";
    private static final String PERSONA_ATT_BORRADO = "borrado";
    
    private static final String PERSONA_NOMBRE_TAG = "nombre";
    private static final String PERSONA_DNI_TAG = "dni";
    private static final String PERSONA_EDAD_TAG = "edad";
    private static final String PERSONA_SALARIO_TAG = "salario";
    
    private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas.xml").toString();

    public static void main(String[] args) {

        long id = 0l;
        boolean borrado = false;
        
        String nombre = "";
        String dni = "";
        int edad = 0;
        float salario = 0f;
        
        Persona persona = null;

        ArrayList<Persona> personas = new ArrayList<>();
        int contador = 1;

        try {
            File inputFile = new File(PERSONAS_INPUT_FILE);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            //elimina hijos con texto vacío y fusiona en un único nodo de texto varios adyacentes.
            doc.getDocumentElement().normalize();

            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

       
            NodeList nList = doc.getElementsByTagName(PERSONA_TAG);

            System.out.println("----------------------------");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    
                    id = Long.valueOf(eElement.getAttribute(PERSONA_ATT_ID));
                    borrado = Boolean.parseBoolean(eElement.getAttribute(PERSONA_ATT_BORRADO));
                    
                    nombre = eElement.getElementsByTagName(PERSONA_NOMBRE_TAG).item(0).getTextContent();
                    dni = eElement.getElementsByTagName(PERSONA_DNI_TAG).item(0).getTextContent();
                    edad = Integer.parseInt(eElement.getElementsByTagName(PERSONA_EDAD_TAG).item(0).getTextContent());
                    salario = Float.parseFloat(eElement.getElementsByTagName(PERSONA_SALARIO_TAG).item(0).getTextContent());

                    persona = new Persona(id, dni, edad, salario, nombre);
                    persona.setBorrado(borrado);

                    personas.add(persona);
                }
            }

         
            for (Persona p : personas) {
                System.out.println("Persona: " + contador + " " + p);
                contador++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        }
    }

}
