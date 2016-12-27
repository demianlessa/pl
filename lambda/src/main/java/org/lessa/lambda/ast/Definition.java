package org.lessa.lambda.ast;

/**
 * <p>
 * An AST node representing a named expression definition.
 * </p>
 *
 * <pre>
 * definition ::= 'def' name '=' expression
 * </pre>
 * 
 * <p>
 * Support for definitions is not required for lambda calculus, this is just a
 * convenience for writing modular code. In this implementation, a definition's
 * expression can reference any previously defined name in a definition list.
 * </p>
 * 
 * <p>
 * Semantics: definition names are replaced across other definitions and the
 * program expression in a preliminary stage, before the application of any
 * conversion or reduction rules.
 * </p>
 * 
 * <p>
 * Let d<sub>1</sub>, ..., d<sub>n</sub> be the ordered list of definitions and
 * e<sub>1</sub>, ..., e<sub>n</sub> a list of expressions constructed as
 * follows:
 * </p>
 * 
 * <ol>
 * <li>Let e<sub>1</sub> = expression(d<sub>1</sub>).</li>
 * <li>Let e<sub>k</sub> = replaceNames(expression(d<sub>k</sub>)), 1 &lt; k
 * &le; n, where 'replaceNames' is a function that replaces every occurrence of
 * name(d<sub>j</sub>) as a free variable in expression(d<sub>k</sub>) with
 * expression(d<sub>j</sub>), for all 1 &le; j &lt; k.</li>
 * </ol>
 * 
 * <p>
 * The list of resolved definitions such that no expression references the name
 * of another definition as a free variable is constructed by mapping
 * name(d<sub>k</sub>) to e<sub>k</sub> for every 1 &le; k &le; n. Definition
 * names are replaced in the program's expression in a similar manner.
 * </p>
 */
public interface Definition extends AstNode, BindingNode {

   /**
    * The expression represented by this definition's name.
    * 
    * @return expression object representing this definition's expression
    */
   Expression expression();

   /**
    * The name defined to represent this definition's expression.
    * 
    * @return name object representing the name of this definition
    */
   Name name();
}