package assigment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {

	public static void main(String[] args)
			throws ParserConfigurationException, IOException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);

		final File folder = new File("C:\\Users\\EO\\Desktop\\수업 자료\\오픈소스SW\\SimpleIR\\2주차 실습 html");
		int fileNum = 0;
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {// fileEntry에 각 html파일이 들어있음
				// doc태그 생성과 id부여
				Element docElement = doc.createElement("doc");
				docs.appendChild(docElement);
				docElement.setAttribute("id", Integer.toString(fileNum));
				fileNum++;

				System.out.println(fileEntry.getName());
				Element body = doc.createElement("body");
				List<String> lines = Files.readAllLines(Paths.get(fileEntry.getAbsolutePath()), StandardCharsets.UTF_8);
				
				for (String line : lines) {
					
					if(line.contains("<title>")) {
						Element title = doc.createElement("title");
						title.appendChild(doc.createTextNode(line.substring(line.indexOf("<title>")+7,line.indexOf("</title>"))));
						docElement.appendChild(title);
					}
					else if(line.contains("</body>")) {
						docElement.appendChild(body);
					}
					else if(line.contains("<p>")&&line.contains("</p>")) {
						body.appendChild(doc.createTextNode(line.substring(line.indexOf("<p>")+3,line.lastIndexOf("</p>"))));
					}
					
				}

			}
		}

		// XML 파일로 쓰기
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("collection.xml")));
		
		transformer.transform(source, result);
	}

}
