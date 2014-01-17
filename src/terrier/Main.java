package terrier;

/**
 * 
 * @author Feliciano Colella, Federico Scaccia
 * @date 17/01/2014
 * @organization Universitˆ degli Studi di Roma Tor Vergata
 * 
 * Get QRELs
 * Get Lexicon from DirInd
 * 
 * --- TRAINING PHASE ---
 * Classificator: SVM, Naive Bayes
 * SVM -> full cycle experiments
 * NB -> full cycle experiments
 * Generate 3 models
 * 		Opinion (Pos+Neg+Mix), Positive, Negative
 * 
 * --- TEST PHASE ---
 * For each classificator
 * 		For each model
 * 			For each test set -> classificate!
 * 
 * Check metrics: Accuracy, Precision, Recall
 * 
 */

public class Main {
	
	public static void main(String[] args){
		new MLP();
	}

}
