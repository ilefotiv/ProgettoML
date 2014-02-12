package terrier;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import org.terrier.structures.DirectIndex;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.utility.ApplicationSetup;

class TextConsole implements RMainLoopCallbacks
{
    public void rWriteConsole(Rengine re, String text, int oType) {
        System.out.print(text);
    }
    
    public void rBusy(Rengine re, int which) {
        System.out.println("rBusy("+which+")");
    }
    
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        System.out.print(prompt);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String s=br.readLine();
            return (s==null||s.length()==0)?s:s+"\n";
        } catch (Exception e) {
            System.out.println("jriReadConsole exception: "+e.getMessage());
        }
        return null;
    }
    
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \""+message+"\"");
    }
	
    public String rChooseFile(Rengine re, int newFile) {
	FileDialog fd = new FileDialog(new Frame(), (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
	fd.show();
	String res=null;
	if (fd.getDirectory()!=null) res=fd.getDirectory();
	if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
	return res;
    }
    
    public void   rFlushConsole (Rengine re) {
    }
	
    public void   rLoadHistory  (Rengine re, String filename) {
    }			
    
    public void   rSaveHistory  (Rengine re, String filename) {
    }			
}

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
		if (!Rengine.versionCheck()) {
		    System.err.println("** Version mismatch - Java files don't match library version.");
		    System.exit(1);
		}
	        System.out.println("Creating Rengine (with arguments)");
			// 1) we pass the arguments from the command line
			// 2) we won't use the main loop at first, we'll start it later
			//    (that's the "false" as second argument)
			// 3) the callbacks are implemented by the TextConsole class above
			Rengine re=new Rengine(null, false, new TextConsole());
	        System.out.println("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until it's ready
	        if (!re.waitForR()) {
	            System.out.println("Cannot load R");
	            return;
	        }
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
