package terrier;

public class MLP {
	
	public MLP(){
		setProperties(); //Setto i path di terrier
	}
	
	private void setProperties(){
		
		if(System.getProperty("user.home").equals("/Users/Federico")){
			System.setProperty("terrier.home", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/");
			System.setProperty("terrier.etc", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/etc");
			System.setProperty("terrier.share", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/share");
			System.setProperty("terrier.var", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/var");
			System.setProperty("terrier.index.path", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/var/index");
		}else{
			System.setProperty("terrier.home", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/");
			System.setProperty("terrier.etc", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/etc");
			System.setProperty("terrier.share", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/share");
			System.setProperty("terrier.var", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/var");
			System.setProperty("terrier.index.path", "/Users/Federico/Documents/MyDocuments/Universita/ML/Progetto/terrier-3.5.1/var/index");
		}
	}

}
