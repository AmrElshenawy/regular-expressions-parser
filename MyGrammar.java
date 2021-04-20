import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import computation.contextfreegrammar.ContextFreeGrammar;
import computation.contextfreegrammar.Rule;
import computation.contextfreegrammar.Terminal;
import computation.contextfreegrammar.Variable;
import computation.contextfreegrammar.Word;

public abstract class MyGrammar {
	public static ContextFreeGrammar makeGrammar() {

    // Variables Initializations
    Variable Z = new Variable('Z');
    Variable T = new Variable('T');
    Variable F = new Variable('F');
    Variable E = new Variable('E');
    Variable C = new Variable('C');
    Variable Z1 = new Variable("Z1");
    Variable Z2 = new Variable("Z2");
    Variable A1 = new Variable("A1");
    Variable A2 = new Variable("A2");
    Variable A3 = new Variable("A3");

    // Adding Variables to Set
    HashSet<Variable> variables = new HashSet<>();
    variables.add(Z);
    variables.add(T);
    variables.add(F);
    variables.add(E);
    variables.add(C);
    variables.add(Z1);
    variables.add(Z2);
    variables.add(A1);
    variables.add(A2);
    variables.add(A3);

    // Terminal Initializations
    Terminal one = new Terminal('1');
    Terminal zero = new Terminal('0');
    Terminal x = new Terminal('x');
    Terminal minus = new Terminal('-');
    Terminal plus = new Terminal('+');
    Terminal multiply = new Terminal('*');

    // Adding Terminals to Set
    HashSet<Terminal> terminals = new HashSet<>();
    terminals.add(one);
    terminals.add(zero);
    terminals.add(x);
    terminals.add(minus);
    terminals.add(plus);
    terminals.add(multiply);

    // Creating Rules ArrayList
    ArrayList<Rule> rules = new ArrayList<>();

    // Rules for Z
    rules.add(new Rule(Z, new Word(E, Z1)));
    rules.add(new Rule(Z, new Word(T, Z2)));
    rules.add(new Rule(Z, new Word(one)));
    rules.add(new Rule(Z, new Word(zero)));
    rules.add(new Rule(Z, new Word(x)));
    rules.add(new Rule(Z, new Word(A3, C)));
    
    // Rules for E  
    rules.add(new Rule(E, new Word(E, Z1)));
    rules.add(new Rule(E, new Word(T, Z2)));
    rules.add(new Rule(E, new Word(one)));
    rules.add(new Rule(E, new Word(zero)));
    rules.add(new Rule(E, new Word(x)));
    rules.add(new Rule(E, new Word(A3, C)));

    // Rules for T
    rules.add(new Rule(T, new Word(T, Z2)));
    rules.add(new Rule(T, new Word(one)));
    rules.add(new Rule(T, new Word(zero)));
    rules.add(new Rule(T, new Word(x)));
    rules.add(new Rule(T, new Word(A3, C)));

    // Rules for F
    rules.add(new Rule(F, new Word(one)));
    rules.add(new Rule(F, new Word(zero)));
    rules.add(new Rule(F, new Word(x)));
    rules.add(new Rule(F, new Word(A3, C)));

    // Rules for C
    rules.add(new Rule(C, new Word(one)));
    rules.add(new Rule(C, new Word(zero)));
    rules.add(new Rule(C, new Word(x)));

    // Rule for Z1
    rules.add(new Rule(Z1, new Word(A1, T)));

    // Rule for Z2
    rules.add(new Rule(Z2, new Word(A2, F)));

    // Rules for A1 -> A6
    rules.add(new Rule(A1, new Word(plus)));
    rules.add(new Rule(A2, new Word(multiply)));
    rules.add(new Rule(A3, new Word(minus)));

    

		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, Z);
		return cfg;
	}

	public static void main(String...args) {
		ContextFreeGrammar cfg = makeGrammar();
		System.out.println(cfg);

		assert(cfg.isInChomskyNormalForm());

		assert(cfg.equals(new ContextFreeGrammar(cfg.getRules())));
	}
}
