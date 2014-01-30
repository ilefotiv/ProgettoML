package terrier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.terrier.structures.DirectIndex;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.utility.ApplicationSetup;

public class MLP {
	
	protected Map<String,Integer> categorizeTweets;
	protected Index index;
	protected HashMap<ArrayList<String>,Integer> testSet;
	protected HashMap<ArrayList<String>,Integer> trainingSet;
	protected List<Tweet> tweets;
	
	public MLP(){
		categorizeTweets = new HashMap<String,Integer>();
		testSet = new HashMap<>();
		trainingSet = new HashMap<>();
		tweets = new ArrayList<>();
		
		setProperties(); //Setto i path di terrier
		getCategorizeTweets("./twitter/benchmark-SA/benchmark-opinion/training-1.qrel"); //Prendo le categorie dei tweet
		//createFile("./twitter/opinion.qrel");
		
		loadIndex(); //Carico l'index
		
		createInputSet();
	}
	
	private void createInputSet() {
		DirectIndex diri = index.getDirectIndex(); //Prendo l'Indice Diretto
		Lexicon<String> lex = index.getLexicon(); //Prendo il Lexicon
		MetaIndex metaIndex = index.getMetaIndex(); //Prendo le meta informazioni
		DocumentIndex doci = index.getDocumentIndex(); //Prendo il Document Index
		
		Iterator<String> iterator = categorizeTweets.keySet().iterator(); 
		
		try {
			while (iterator.hasNext()) { 
				String key = iterator.next().toString(); 
				int docid = metaIndex.getDocument("docno",key);
				int[][] postings = diri.getTerms(docid);
				
				ArrayList<String> tweet = new ArrayList<>();
				
				for(int i=0;i<postings[0].length; i++){
					Entry<String, LexiconEntry> le = lex.getLexiconEntry(postings[0][i]);
					String s = le.getKey(); //Parola
					tweet.add(s);
				}
				trainingSet.put(tweet, categorizeTweets.get(key));
				//System.out.println(((k++/n)*100)+"%");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Legge i file .QREL e crea un HashMap di tipo <String,String> dove:<br>
	 * - KEY => DOCNO del tweet<br>
	 * - VALUE => categoria del tweet
	 * <br><br>
	 * Il campo VALUE ha i seguenti valori:<br>
	 * 0 -> Non so<br>
	 * 1 -> Misto<br>
	 * 2 -> Negativo<br>
	 * 3 -> Neutro<br>
	 * 4 -> Positivo<br>
	 *
	 * @param fileRelevance Percorso del file .QREL relativo alla rilevanza dei tweet
	 * @param fileQREL Percorso del file .QREL relativo all'opnione dei tweet
	 */
	private void getCategorizeTweets(String fileQREL){
		try {
			BufferedReader readerOp = new BufferedReader(new FileReader(fileQREL)); //Carico il file
			
			String lineOp;
			while((lineOp = readerOp.readLine()) != null){ //Scorro riga x riga
				String[] op = lineOp.split(" "); //Separo gli elementi di una riga tramite lo spazio
				categorizeTweets.put(op[2], Integer.parseInt(op[3]));
			}
			readerOp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Carica un indice gia' creato da terminale tramite il comando ./trec_terrier.sh -i
	 */
	private void loadIndex(){
		index = Index.createIndex(ApplicationSetup.TERRIER_INDEX_PATH,ApplicationSetup.TERRIER_INDEX_PREFIX);
	}
	
	private void createFile(String fileQREL){
		FileOutputStream discoFileStream;//Percorso in uscita
		PrintStream disco; //Oggetto che punta a
		File file; //Il file
		
		file = new File("opinion.csv");
		try {
			discoFileStream = new FileOutputStream(file);
			disco = new PrintStream(discoFileStream);
			BufferedReader readerOp = new BufferedReader(new FileReader(fileQREL)); //Carico il file
			String lineOp;
			while((lineOp = readerOp.readLine()) != null){ //Scorro riga x riga
				String[] op = lineOp.split(" "); //Separo gli elementi di una riga tramite lo spazio
				disco.println(op[2]+";"+op[3]); //Altrimenti usa la sua categoria
			}
			readerOp.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo usato per stampare una mappa con il campo <b>value</b> di tipo String
	 * @param map Mappa da stampare
	 */
	private void printStringMap(Map<String,String> map){
		Iterator<String> iterator = map.keySet().iterator();  
		   
		while (iterator.hasNext()) {  
		   String key = iterator.next().toString();  
		   String value = map.get(key).toString();  
		   
		   System.out.println(key + "\t" + value);  
		}
	}
	
	private void setProperties(){
		
		if(System.getProperty("user.home").equals("/Users/Federico")) {
			System.setProperty("terrier.home", System.getProperty("user.home")+"/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/");
			System.setProperty("terrier.etc", System.getProperty("terrier.home")+"/etc");
			System.setProperty("terrier.share", System.getProperty("terrier.home")+"/share");
			System.setProperty("terrier.var", System.getProperty("terrier.home")+"/var");
			System.setProperty("terrier.index.path", System.getProperty("terrier.home")+"/var/index");
		}
		if(System.getProperty("user.home").equals("/Users/apple_develop")) {
			System.setProperty("terrier.home", System.getProperty("user.home")+"/Desktop/ProgettoML/terrier-ML-torvergata/terrier-3.5.1/");
			System.setProperty("terrier.etc", System.getProperty("terrier.home")+"/etc");
			System.setProperty("terrier.share", System.getProperty("terrier.home")+"/share");
			System.setProperty("terrier.var", System.getProperty("terrier.home")+"/var");
			System.setProperty("terrier.index.path", System.getProperty("terrier.home")+"/var/index");
		}
	}

}
