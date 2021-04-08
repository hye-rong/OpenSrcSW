package assigment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class searcher {
	
	String fileName;
	String inputString;
	int fileNum = 0;
	ArrayList<String> docTitle;

	HashMap<Integer, Double> fileSim;
	HashMap<String, String> indexMap;
	HashMap<String, Integer> queryMap;
	
	public searcher(String fileName, String query) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
		docTitle = new ArrayList<String>();
		fileSim = new HashMap<Integer,Double>();
		this.fileName = fileName;
		inputString = query;
		init();
		
	}
	
	
	private void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
		/* collection.xml파일 열어서 문서 개수 가져오고, 이름 가져오기 */
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document collection = docBuilder.parse("C:\\Users\\EO\\Desktop\\lec\\OpenSource\\SimpleIR\\collection.xml");
		collection.getDocumentElement().normalize();
		NodeList nList = collection.getElementsByTagName("doc");

		fileNum = nList.getLength();
		if (nList.getLength() > 0) {
			for (int i = 0; i < nList.getLength(); i++) {
				Element nNode = (Element) nList.item(i);
				docTitle.add(nNode.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue());
				System.out.println(nNode.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue());
			}
		}
		
		/* index.post으로부터 HashMap 만들기 */
		FileInputStream fileStream = new FileInputStream(fileName);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		indexMap = (HashMap)object;
		printHash(indexMap);
	}
	
	
	public void test() throws IOException, ParserConfigurationException, SAXException {
		
		/* 문장 꼬꼬마 분석 */
		queryMap = new HashMap<String, Integer>();
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(inputString, true);
		for (int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			queryMap.put(kwrd.getString(), 1);
		}
		printHash(queryMap);
		
		/* 문서 별로 유사도 계산 */
		for(int i=0; i<fileNum; i++) {
			fileSim.put(i,InnerProduct(i));
		}
		
		/* 정렬 */
		List<Integer> keySetList = new ArrayList<>(fileSim.keySet());
		Collections.sort(keySetList, (o1, o2) -> (fileSim.get(o2).compareTo(fileSim.get(o1))));
		for(Integer key : keySetList) {
			System.out.println("key : " + key + " / " + "value : " + fileSim.get(key));
		}

		if(keySetList.get(0)==0) {
			System.out.println("검색 결과가 없습니다.");
			return;
		}
		for(int i=0; i<3; i++) {
			if(keySetList.get(i)!=0)
				System.out.println(i+1 + ") " + docTitle.get(keySetList.get(i)));
		}
		
	}
	
	
	private double InnerProduct(int i) {
		double sum = 0;
		Iterator<String> keys = queryMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if(indexMap.containsKey(key)){
				String[] split = indexMap.get(key).split(" ");
				for(int index=0; index<split.length; index+=2) {
					if(Integer.parseInt(split[index])==i) {
						sum += Math.pow(Double.parseDouble(split[index+1]),2);
						break;
					}
				}
			}
		}
		double rValue = 1/(2*Math.sqrt(sum));
		return rValue;
	}
	
	
	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}
	
	private void printHash(HashMap hs) {
		Iterator<String> keys = hs.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.println(String.format("키 : %s", key));
			System.out.println("값 : " + hs.get(key));
			
		}
	}

}
