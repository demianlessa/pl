package org.lessa.lambda.examples;

import java.io.IOException;

import org.lessa.lambda.Parser;
import org.lessa.lambda.Parser.Application;
import org.lessa.lambda.Parser.Definition;
import org.lessa.lambda.Parser.Function;
import org.lessa.lambda.Parser.LambdaVisitor;
import org.lessa.lambda.Parser.Name;
import org.lessa.lambda.Parser.Program;

public class ParserExample {

	public static void main(String[] args) throws IOException {

		final LambdaVisitor pv = createPrinter(true);

		new Parser().parse(
				"def identity = λx.x", 
				"def self_apply = λs.(s s)", 
				"def apply = λfunc.λarg.(func arg)",
		      "def select_first = λfirst.λsecond.first", 
		      "def select_second = λfirst.λsecond.second",
		      "def make_pair = λfirst.λsecond.λfunc.((func first) second)")
			.accept(pv);
		new Parser().parse("λfirst.λsecond.first").accept(pv);
		new Parser().parse("λf.λa.(f a)").accept(pv);
		new Parser().parse("(λx.x λa.λb.b)").accept(pv);
		new Parser().parse("(λx.x λx.x)").accept(pv);
		new Parser().parse("λS.(S S)").accept(pv);
		new Parser().parse("(λx.x λS.(S S))").accept(pv);
		new Parser().parse("(λS.(S S) λx.x)").accept(pv);
		new Parser().parse("(λS.(S S) λS.(S S))").accept(pv);
		new Parser().parse("λfunc.λarg.(func arg)").accept(pv);
		new Parser().parse("((λfunc.λarg.(func arg) λx.x) λS.(S S))").accept(pv);
	}

	private static LambdaVisitor createPrinter(boolean asLambda) {
		return asLambda ? new LambdaPrinter() : new PrettyPrintVisitor();
	}

	private static class LambdaPrinter implements LambdaVisitor {

		@Override
		public void visit(Application node) {
			System.out.print("(");
			node.leftExpression().accept(this);
			System.out.print(" ");
			node.rightExpression().accept(this);
			System.out.print(")");
		}

		@Override
		public void visit(Definition node) {
			System.out.print("def ");
			node.name().accept(this);
			System.out.print(" = ");
			node.expression().accept(this);
			System.out.println();
		}

		@Override
		public void visit(Function node) {
			System.out.print("λ");
			node.name().accept(this);
			System.out.print(".");
			node.body().accept(this);
			System.out.print("");
		}

		@Override
		public void visit(Name node) {
			System.out.print(node.name());
		}

		@Override
		public void visit(Program node) {
			for (final Definition def : node.definitions()) {
				def.accept(this);
			}
			if (node.expression() != null) {
				if (!node.definitions().isEmpty()) {
					System.out.println();
				}
				node.expression().accept(this);
				System.out.println();
			}
		}
	}

	private static class PrettyPrintVisitor implements LambdaVisitor {

		@Override
		public void visit(Application node) {
			System.out.print("Application(LeftExp(");
			node.leftExpression().accept(this);
			System.out.print("), RightExp(");
			node.rightExpression().accept(this);
			System.out.print("))");
		}

		@Override
		public void visit(Definition node) {
			System.out.print("Definition(Name(");
			node.name().accept(this);
			System.out.print("), Expression(");
			node.expression().accept(this);
			System.out.println("))");
		}

		@Override
		public void visit(Function node) {
			System.out.print("Function(Name(");
			node.name().accept(this);
			System.out.print("), Body(");
			node.body().accept(this);
			System.out.print("))");
		}

		@Override
		public void visit(Name node) {
			System.out.print(node.name());
		}

		@Override
		public void visit(Program node) {
			for (final Definition def : node.definitions()) {
				def.accept(this);
			}
			if (node.expression() != null) {
				if (!node.definitions().isEmpty()) {
					System.out.println();
				}
				node.expression().accept(this);
			}
			System.out.println();
		}
	}
}