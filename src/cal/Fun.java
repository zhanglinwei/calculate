package cal;

public class Fun {
	private String funName;
	private String funExp;
	private String expression;
	private String[] args;

	public Fun(String funName, String funExp, String...args) {
		super();
		this.funName = funName;
		this.funExp = funExp;
		this.args = args;
		expression = funName + "(" + funExp + ")";
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getFunExp() {
		return funExp;
	}

	public void setFunExp(String funExp) {
		this.funExp = funExp;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	
}
