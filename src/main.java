import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;

/**
 * 
 * @author Feliciano Colella, Federico Scaccia
 * @date 17/01/2014
 * 
 * Get QRELs
 * Get Lexicon from DirInd
 * 
 * --- TRAINING PHASE ---
 * Classificator: SVM, Naive Bayes
 * SVM -> full cycle exp
 * NB -> full cycle exp
 * Generate 3 models
 * 		Opinion (Pos+Neg+Mix), Positive, Negative
 * 
 * --- TEST PHASE ---
 * For each classificator
 * 		For each model
 * 			For each test set -> classificate!
 * Check metrics: Accuracy, Precision, Recall, ecc...
 * 
 */

public class main {

	Index index = Index.createIndex(ApplicationSetup.TERRIER_INDEX_PATH,ApplicationSetup.TERRIER_INDEX_PREFIX);

}
