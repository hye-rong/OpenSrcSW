package assigment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {

	private static String fileName;

	public makeKeyword(String filePath) {
		fileName = filePath;
	}

	public static void createXML() throws ParserConfigurationException, IOException, TransformerException, SAXException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// collection.xml 읽기
		Document collection = docBuilder.parse(fileName);
		collection.getDocumentElement().normalize();
		NodeList nList = collection.getElementsByTagName("doc");

		// 새로운 doc생성 준비
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);

		if (nList.getLength() > 0) {
			for (int i = 0; i < nList.getLength(); i++) {
				Element nNode = (Element) nList.item(i);

				Element docElement = doc.createElement("doc");
				docs.appendChild(docElement);
				docElement.setAttribute("id", ((Element) nNode).getAttribute("id"));
				System.out.println(((Element) nNode).getAttribute("id")); //

				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(getTagValue("title", nNode)));
				System.out.println(getTagValue("title", nNode)); //
				docElement.appendChild(title);

				Element body = doc.createElement("body");
				System.out.println(keywordExtractor(getTagValue("body", nNode))); //
				body.appendChild(doc.createTextNode(keywordExtractor(getTagValue("body", nNode))));
				docElement.appendChild(body);

			}
		}

		makeXMlFile(doc);
	}

	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

	private static String keywordExtractor(String str) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(str, true);
		String rtString = "";
		for (int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			rtString += kwrd.getString() + ":" + kwrd.getCnt() + "#";
		}
		return rtString;
	}

	private static void makeXMlFile(Document doc) throws FileNotFoundException, TransformerException {
		// XML 파일로 쓰기
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("index.xml")));

		transformer.transform(source, result);
	}

}
