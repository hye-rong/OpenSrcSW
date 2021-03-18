package assigment;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException {
		System.out.println(args.length);
		System.out.println(args.toString());
		if(args.length!=2) {
			System.out.println("조대현 죽어라");
			return;
		}
		
		if(args[0].equals("-c")) {
			makeCollection mc = new makeCollection(args[1]);
			mc.createXML();
		}
		else if(args[0].equals("-k")) {
			makeKeyword mk = new makeKeyword(args[1]);
			mk.createXML();
		}
		else {
			return;
		}

	}

}
