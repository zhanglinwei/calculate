package cal;

public class Fun {
	private String funName;
	private String funExp;
	private String expression;
	private String args;

	public Fun(String funName, String funExp, String args) {
		super();
		this.funName = funName;
		this.funExp = funExp;
		this.args = args;
		StringBuffer temp = new StringBuffer(funName);
		temp.append("(");
		temp.append(args);
		temp.append(")");
		temp.append("=");
		temp.append(funExp);
		temp.append("\n");
		expression = temp.toString();
	}

	public String getExpression() {
		return expression;
	}

	
	public void read(){
		
	}

	public String getFunName() {
		return funName;
	}

	public String getFunExp() {
		return funExp;
	}

	public String getArgs() {
		return args;
	}
	
}
