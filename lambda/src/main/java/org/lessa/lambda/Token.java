package org.lessa.lambda;

public final class Token {

	private final String token;
	private final TokenClass tokenClass;

	public Token(final TokenClass tc, final String token) {
		this.tokenClass = tc;
		this.token = token;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		}
		else if (!token.equals(other.token))
			return false;
		if (tokenClass != other.tokenClass)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((tokenClass == null) ? 0 : tokenClass.hashCode());
		return result;
	}

	public String token() {
		return token;
	}

	public TokenClass tokenClass() {
		return tokenClass;
	}

	@Override
	public String toString() {
		return String.format("%s('%s')", tokenClass, token);
	}
}
