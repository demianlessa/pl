package org.lessa.lambda.examples;

import java.io.IOException;
import java.io.StringReader;

import org.lessa.lambda.Tokenizer;

public class TokenizerExample {

	public static void main(String[] args) throws IOException {
		printTokens("my tokens");
		printTokens("λ.my tokens");
		printTokens("((λfunc.λarg(func arg) arg) boing)");
		printTokens("((λfunc1.λarg2ζ(func1 arg2ζ) arg) boing)");
	}

	private static void printTokens(String tokens) throws IOException {
		final Tokenizer t = new Tokenizer(new StringReader(tokens));
		final StringBuffer b = new StringBuffer();
		int index = 0;
		b.append("\n{\n");
		while (t.hasNext()) {
			b.append("\t");
			b.append(index++);
			b.append(" => ");
			b.append(t.next());
			b.append("\n");
		}
		b.append("}");
		System.out.printf("%s => %s\n", tokens, b.toString());
	}
}
