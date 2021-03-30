package assigment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {

	private static String fileName;
	private int fileNum = 0;// 전체 문서의 수
	ArrayList<FileData> arrayList;
	HashMap<String, ArrayList> tempMap;
	HashMap<String, String> freMap;

	class FileData {
		private int fileID;
		private int frequency;

		FileData(int fID, int fre) {
			fileID = fID;
			frequency = fre;
		}

		public void print() {
			System.out.println("배열 : id=" + fileID + "빈도=" + frequency);
		}
	}

	public indexer(String filePath) {
		fileName = filePath;
		arrayList = new ArrayList<FileData>();
		tempMap = new HashMap<String, ArrayList>();
	}

	public void readIndexFile() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// index.xml 읽기
		Document collection = docBuilder.parse(fileName);
		collection.getDocumentElement().normalize();
		NodeList nList = collection.getElementsByTagName("doc");

		fileNum = nList.getLength(); // 전체 문서 개수 저장

		if (nList.getLength() > 0) {
			// 각 문서별로 parsing
			for (int i = 0; i < nList.getLength(); i++) {
				Element nNode = (Element) nList.item(i);

				// 문서 id
				System.out.println(((Element) nNode).getAttribute("id")); //

				// body parsing(key와 문서 빈도수)
				System.out.println(getTagValue("body", nNode));
				String bodyString = getTagValue("body", nNode);
				String[] split = bodyString.split("#");
				for (int j = 0; j < split.length; j++) {
					String[] split2 = split[j].split(":");
					if (tempMap.containsKey(split2[0])) {

					} else {
						tempMap.put(split2[0], new ArrayList<FileData>());
					}
					tempMap.get(split2[0]).add(new FileData(Integer.parseInt(((Element) nNode).getAttribute("id")),
							Integer.parseInt(split2[1])));
				}

			}
		}
		Iterator<String> keys = tempMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.println(String.format("키 : %s", key));
			int totalElements = tempMap.get(key).size();// arrayList의 요소의 갯수를 구한다.
			for (int index = 0; index < totalElements; index++) {
				((FileData) tempMap.get(key).get(index)).print();
			}

		}

		createHashMap();
		toPostFile();
	}

	private void createHashMap() {
		freMap = new HashMap<String, String>();
		// hashmap key를 기반으로 가중치 계산 후 String으로 저장
		Iterator<String> keys = tempMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();

			int totalElements = tempMap.get(key).size();// arrayList의 요소의 갯수를 구한다.
			String valueStr = "";

			for(int index=0; index<totalElements; index++) {
				double weight = ((FileData) tempMap.get(key).get(index)).frequency
						* Math.log((double) (fileNum) / totalElements);
				valueStr += ((FileData) tempMap.get(key).get(index)).fileID + " " + String.format("%.2f", weight) + " ";
			}
			
			freMap.put(key, valueStr);
		}

		Iterator<String> k = freMap.keySet().iterator();
		while (k.hasNext()) {
			String key = k.next();
			System.out.println(String.format("키 : %s, 값 : %s", key, freMap.get(key)));

		}

	}

	private void toPostFile() throws IOException {
		if (freMap != null) {
			FileOutputStream fileStream = new FileOutputStream("index.post");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

			objectOutputStream.writeObject(freMap);
			objectOutputStream.close();
		}
	}

	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

}
