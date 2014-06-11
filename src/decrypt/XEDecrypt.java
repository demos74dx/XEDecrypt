package decrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import decrypt.structure.EncryptedToken;

public class XEDecrypt {

	public static void main(String[] args) throws IOException {
		String text = readFile("encrypted");
		List<Integer> tokens = tokenize(text);
		List<EncryptedToken> triples = triplize(tokens);
		List<EncryptedToken> dedupeMap = dedupeMap(triples);
		int key = findKey(dedupeMap);
		String decrypt = decrypt(key, triples);
		System.out.println(decrypt);
	}

	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
	public static List<Integer> tokenize(String text) {
		text = text.replaceAll("(\\r|\\n)", "");
		List<Integer> list = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(text);
		while(st.hasMoreTokens()) {
			int token = Integer.valueOf(st.nextToken("."));
			list.add(token);
		}
		return list;
	}
	
	public static List<EncryptedToken> triplize(List<Integer> singles) {
		List<EncryptedToken> encrypted = new ArrayList<EncryptedToken>();
		Iterator<Integer> iter = singles.iterator();
		while(iter.hasNext()) {
			int first = iter.next();
			int second = iter.next();
			int third = iter.next();
			encrypted.add(new EncryptedToken(first + second + third));
		}
		return encrypted;
	}

	public static List<EncryptedToken> dedupeMap(List<EncryptedToken> tokens) {
		List<EncryptedToken> dedup = new ArrayList<EncryptedToken>();
		Iterator<EncryptedToken> iter = tokens.iterator();
		dedup.add(iter.next());
		while(iter.hasNext()) {
			EncryptedToken token = iter.next();
			Iterator<EncryptedToken> deIter = dedup.iterator();
			boolean found = false;
			while(deIter.hasNext()) {
				EncryptedToken deToken = deIter.next(); 
				if(token.getEncryptedToken() == deToken.getEncryptedToken()) {
					deToken.increment();
					found = true;
				}
			}
			if(!found) {
				dedup.add(token);
			}
		}
		return dedup;
	}
	
	public static int findKey(List<EncryptedToken> dedupMap) {
		//highest count of encrypted values is the space
		Iterator<EncryptedToken> iter = dedupMap.iterator();
		EncryptedToken space = iter.next();
		while(iter.hasNext()) {
			EncryptedToken et = iter.next();
			if(et.getCount() > space.getCount()) {
				space = et;
			}
		}
		int key = space.getEncryptedToken() - 32;
		//System.out.println(key);
		return key;
	}
	
	public static String decrypt(int key, List<EncryptedToken> list) {
		String message = "";
		Iterator<EncryptedToken> iter = list.iterator();
		while(iter.hasNext()) {
			EncryptedToken token = iter.next();
			message += token.decryptToken(key);
		}
		return message;
	}

}
