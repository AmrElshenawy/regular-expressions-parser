import java.util.List;
import java.util.ArrayList;
import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;

public class Parser implements IParser {
  public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {

    Derivation word_Derivation = DeriveWord(cfg, w);

    if(word_Derivation != null){
      return true;
    }
    return false;
  }

  public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w){

    //If the input word is not in the language, exit early
    Derivation word_Derivation = DeriveWord(cfg, w);
    if (word_Derivation == null) {
      return null;
    }

    ArrayList<ParseTreeNode> Nodes = new ArrayList<ParseTreeNode>();
    Word latestWord = word_Derivation.getLatestWord();

    for(Symbol character : latestWord){
      ParseTreeNode node = new ParseTreeNode(character);
      Nodes.add(node);
    }

    //If the input word is empty word
    if(latestWord.equals(Word.emptyWord)){
      ParseTreeNode node = new ParseTreeNode(new Terminal('ε'));
      Nodes.add(node);
    }

    for(Step currentStep : word_Derivation){

      if (currentStep.isStartSymbol()){
        return Nodes.get(0);
      }

      int position = currentStep.getIndex();
      Rule rule = currentStep.getRule();
      Word production = rule.getExpansion();

      if(production.length() == 2){
        ParseTreeNode node = new ParseTreeNode(rule.getVariable(), Nodes.get(position), Nodes.get(position + 1));
        Nodes.set(position, node);
        Nodes.remove(position + 1);
      } 
      else{
        ParseTreeNode node = new ParseTreeNode(rule.getVariable(), Nodes.get(position));
        Nodes.set(position, node);
      }
    }

    return null;
  }

  //Compute all derivations of the input word
  private Derivation DeriveWord(ContextFreeGrammar cfg, Word w){

    ArrayList<Derivation> total_Derivations = new ArrayList<Derivation>();
    List<Rule> all_Rules = cfg.getRules();

    Derivation startVariableHead = new Derivation(new Word(cfg.getStartVariable())); 
    total_Derivations.add(startVariableHead);                                         

    //If the input word is empty word
    if(w.length() == 0){

      ArrayList<Derivation> all_Derivations = new ArrayList<Derivation>();

      Word latestWord = startVariableHead.getLatestWord();
      Symbol currentCharacter = latestWord.get(0);

      for(Rule rule : all_Rules){
        if(rule.getVariable().equals(currentCharacter)){
          Word newWord = latestWord.replace(0, rule.getExpansion());
          Derivation newDerivation = new Derivation(startVariableHead);
          newDerivation.addStep(newWord, rule, 0);
          all_Derivations.add(newDerivation);
        }
      }

      total_Derivations = all_Derivations;
    }

    //If input word is not an empty word
    if(w.length() != 0){

      //Compute derivations of the input word until the hard limit 2n-1 - where n is the word length
      for(int i = 0; i < 2 * w.length() - 1; i++){
        ArrayList<Derivation> toDerive = new ArrayList<Derivation>();

        for(Derivation derivation : total_Derivations){

          Word latestWord = derivation.getLatestWord();
          int position = 0;

          for(Symbol character : latestWord){
            
            ArrayList<Derivation> all_Derivations = new ArrayList<Derivation>();

            Symbol currentCharacter = latestWord.get(position);

            for(Rule rule : all_Rules){
              if(rule.getVariable().equals(currentCharacter)){
                Word newWord = latestWord.replace(position, rule.getExpansion());
                Derivation newDerivation = new Derivation(derivation);
                newDerivation.addStep(newWord, rule, position);
                all_Derivations.add(newDerivation);
              }
            }

            toDerive.addAll(all_Derivations);
            position++;
          }
        }
        total_Derivations = toDerive;
      }
    }
    
    //Scan the list of total_Derivations to find a match with input word
    for (Derivation derivative : total_Derivations){
      if (derivative.getLatestWord().equals(w)){
        return derivative;
      }
    }
    return null;
  }
}