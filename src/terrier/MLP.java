package terrier;

public class MLP {
	
	public MLP(){
		setProperties(); //Setto i path di terrier
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
