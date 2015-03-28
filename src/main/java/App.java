import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Selector on 28.03.2015.
 */
public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Выбирите файлы для конвертации");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
            chooser.setMultiSelectionEnabled(true);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                if (files == null) {
                    throw new Exception("Не найдены xml файлы для конвертации");
                }
                for (File f : files) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(f);

                    NodeList nList = document.getElementsByTagName("Файл");
                    String fileName = ((Element) nList.item(0)).getAttribute("ИдФайл");

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1251");
                    DOMSource source = new DOMSource(document);

                    if (f.renameTo(new File(f.getPath() + "/" + fileName + ".xml"))) {
                        FileOutputStream stream = new FileOutputStream(f);
                        StreamResult result = new StreamResult(stream);
                        transformer.transform(source, result);
                    }
                }
                JOptionPane.showMessageDialog(null, String.format("Конвертировано %s файлов", files.length));
            } else {
                throw new Exception("Файлы не выбранна");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
