/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lecturaescrituraxmldom;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DOMCreate {

    private static final String PERSONA_TAG = "persona";

    private static final String PERSONA_ATT_ID = "id";
    private static final String PERSONA_ATT_BORRADO = "borrado";

    private static final String PERSONA_DNI_TAG = "dni";
    private static final String PERSONA_NOMBRE_TAG = "nombre";
    private static final String PERSONA_EDAD_TAG = "edad";
    private static final String PERSONA_SALARIO_TAG = "salario";

    private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas.xml").toString();

    public static void main(String[] args) {

        long id = 0l;
        String nombre = "";
        String dni = "";
        int edad = 0;
        float salario = 0f;
        boolean borrado = false;
        int contador = 1;
        Persona persona = null;

        ArrayList<Persona> personas = new ArrayList<>();

        try {
            File inputFile = new File(PERSONAS_INPUT_FILE);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(PERSONA_TAG);

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    id = Long.parseLong(eElement.getAttribute(PERSONA_ATT_ID));
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

        } catch (Exception ex) {
            Logger.getLogger(DOMCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
