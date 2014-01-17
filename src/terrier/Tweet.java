package terrier;
import java.util.ArrayList;
import java.util.List;


public class Tweet {

	private String docNO;
	private int docID;
	private double score;
	private List<Parola> parole;
	private String cat;
	
	public Tweet(String docNO, int docID, double score, ArrayList<Parola> parole, String cat){
		this.docNO = docNO;
		this.docID = docID;
		this.score = score;
		this.parole = new ArrayList<Parola>(parole);
		this.cat = cat;
	}
	
	public String getDocNO() {
		return docNO;
	}
	public void setDocNO(String docNO) {
		this.docNO = docNO;
	}
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public double getScore() {
		return score;
	}
	public void setScores(double score) {
		this.score = score;
	}
	public List<Parola> getParole() {
		return parole;
	}
	public void setParole(List<Parola> parole) {
		this.parole = parole;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
}
