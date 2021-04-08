package assigment;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
		System.out.println(args.length);
		
		
		if(args[0].equals("-c")) {
			makeCollection mc = new makeCollection(args[1]);
			mc.createXML();
		}
		else if(args[0].equals("-k")) {
			makeKeyword mk = new makeKeyword(args[1]);
			mk.createXML();
		}
		else if(args[0].equals("-i")) {
			indexer mi = new indexer(args[1]);
			mi.readIndexFile();
		}
		else if(args[0].equals("-s")&&args[2].equals("-q")) {
			searcher search = new searcher(args[1], args[3]);
			search.test();
		}
		else {
			return;
		}

	}

}
