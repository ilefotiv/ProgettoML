package terrier;

public class Parola{
	
	private String text;
	private int TF;
	
	public Parola(String text, int TF){
		this.text = text;
		this.TF =TF;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTF() {
		return TF;
	}

	public void setTF(int tF) {
		TF = tF;
	}
	
	public String toString(){
		return text+" ===> "+TF;
	}

}
