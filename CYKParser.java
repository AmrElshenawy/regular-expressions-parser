import java.util.*; 
import computation.contextfreegrammar.*; 
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;

public class CYKParser implements IParser {
	  
	public boolean isInLanguage(ContextFreeGrammar cfg, Word w){

	    int no_Variables = cfg.getVariables().size();
	    List<Rule> rules = cfg.getRules();
      Variable startVariable = cfg.getStartVariable();

      if(w.length() == 0){
        for(Rule rule : rules){
          if(rule.getExpansion() == Word.emptyWord){
            return true;
          } 
        }
        return false;
      }
	    
      for(int i = 0; i < rules.size(); i++){
        if(rules.get(i).getExpansion() == Word.emptyWord){
          rules.remove(i);
        }
      }
	    
	    Variable[][][] CYK_Table = new Variable[w.length() + 1][w.length() + 1][no_Variables + 1];
	    
      for(int i = 1; i <= w.length(); i++) {
	    	int counter = 1;
	    	for(Rule rule: rules) {
	    		if(rule.getExpansion().get(0).equals(w.get(i - 1))) {
	    			CYK_Table[i][i][counter] = rule.getVariable();
	    			counter++;
	    		}
	    	}
	    }

	    for(int a = 2; a <= w.length(); a++){
	    	for(int i = 1; i <= w.length() - a + 1; i++){

          boolean leftVariable = false;
          boolean rightVariable = false;
          int b = i + a - 1;
	    		int counter = 1;

	    		for(int k = i; k <= b - 1; k++){
	    			for(Rule rule: rules) {
	    				if(rule.getExpansion().length() == 2){

	    					for(int y = 1; y <= CYK_Table[i][k].length - 1; y++) {
                  if(rule.getExpansion().get(0).equals(CYK_Table[i][k][y])){
                    leftVariable = true;
                    break;
                  }
                  else{
                    leftVariable = false;
                  }
                }

                for(int z = 1; z <= CYK_Table[k + 1][b].length - 1; z++) {
                  if(rule.getExpansion().get(1).equals(CYK_Table[k + 1][b][z])){
                    rightVariable = true;
                    break;
                  }
                  else{
                    rightVariable = false;
                  }
                }

	    					if(leftVariable && rightVariable){
	    						CYK_Table[i][b][counter] = rule.getVariable();
	    						counter++;
	    					}
	    	    	}
	    	    }
	    		}
	    	}
	    }

      boolean startVariableCell = false;

	    for(int x = 1; x <= CYK_Table[1][w.length()].length - 1; x++) {
        if(rules.get(0).getVariable().equals(CYK_Table[1][w.length()][x])){
          
          startVariableCell = true;
          break;
        }
        else{
          startVariableCell = false;
        }
      }
      
	    if(startVariableCell){
        return true; 
	    }
      else{
        return false;
	    } 	       
	}

	public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
    
    return null;	    
  }
}