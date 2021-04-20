package assigment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import assigment.indexer.FileData;

public class midterm {
	

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		String filename = args[1];
		int one=0, two=0, three=0, four = 0;
		int max = 0;
		
		ArrayList<String> arrayList;
		arrayList = new ArrayList();
		
		String str  = args[3];
		String[] split = str.split(" ");
		
		File file = new File(filename);
		int i=0;
		for(i=0; i<file.list().length; i++) {
			arrayList.add(file.list()[i]);
		}
		
		for(i=0; i<split.length; i++) {
			if(arrayList.get(0).contains(split[i])) {
				one++;
			}
			if(arrayList.get(1).contains(split[i])) {
				two++;
			}
			if(arrayList.get(2).contains(split[i])) {
				three++;
			}
			if(arrayList.get(3).contains(split[i])) {
				four++;
			}
		}
		
		max = 0;
		for(i=0; i<4; i++) {
			
		}
		System.out.println(arrayList.get(max));
		
			
		
	}

}
