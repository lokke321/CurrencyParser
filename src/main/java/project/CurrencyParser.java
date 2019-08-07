package project;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyParser {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        String pre_apiURL = "http://www.cbr.ru/scripts/XML_daily.asp";
        System.out.println("url " + pre_apiURL);
        URL url = new URL(pre_apiURL);

        Document document = builder.parse(url.openStream());
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("Valute");

        List<Currency> currenciesList = new ArrayList<Currency>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            currenciesList.add(getCurrency(nodeList.item(i)));
        }

        ArrayList<Double> result = new ArrayList<Double>();

        for (Currency currency : currenciesList) {
            String nominal = currency.getNominal();
            Double nominalInt = Double.parseDouble(nominal.trim());
            String value = currency.getValue();
            String newVal = value.replace(",", ".");
            Double valueInt = Double.parseDouble(newVal.trim());
            Double d = valueInt / nominalInt;
            currency.setValuePerOne(d);
            result.add(d);
        }

        Double maxResult = Collections.max(result);
        Double minReult = Collections.min(result);

        for (Currency currency : currenciesList) {
            if (currency.getValuePerOne() == maxResult) {

                System.out.println("Самая дорогая валюта - " + currency.getName() + ", стоимость за единицу = " + maxResult + " руб.");
            }
            if (currency.getValuePerOne() == minReult) {
                System.out.println("Самая дешевая валюта - " + currency.getName() + ", стоимость за единицу = " + minReult + " руб.");
            }
        }
    }

    private static Currency getCurrency(Node node) {
        Currency currency = new Currency();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            currency.setNumCode(getTagValue("NumCode", element));
            currency.setCharCode(getTagValue("CharCode", element));
            currency.setNominal(getTagValue("Nominal", element));
            currency.setName(getTagValue("Name", element));
            currency.setValue(getTagValue("Value", element));
        }
        return currency;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
//    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer = tf.newTransformer();
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//        transformer.transform(new DOMSource(doc),
//                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
//    }

//            for (Currency currency: currenciesList) {
//                System.out.println(String.format("Name - %s, Nominal - %s, CharCode - %s " , currency.getName(), currency.getNominal(), currency.getCharCode()));
//            }

//  printDocument(document, System.out);

//        File file = new File("resources/currency.xml");
//        Document document = builder.parse(file);

// System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());