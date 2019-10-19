import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Inspector {
	
    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	if (c == Object.class)
    		return;
    	
    	Output output = new Output(depth);
    	
    	// 1) Get class Name
    	String title = "";
    	int mods = c.getModifiers();
    	String modifiers = getModifiers(mods);
    	String name = c.getSimpleName();
    	title = modifiers + name;
    	
    	// 2) Name of immediate super-class
    	title += getSuperClass(c);
    	
    	// 3) Name of each interface
    	title += getInterfaces(c);
    	
    	title += " {";
    	output.add(title);
    	
    	// 4) Constructors: name, parameter types, modifiers
    	output.add(getConstructors(c));
    	output.addln();
    	
    	// 5) Methods: modifiers, name, parameter types, exceptions, return type
    	output.add(getMethods(c));
    	output.addln();
    	
    	output.add("}");
    	output.print();
    }
    
    public class Output {
    	private StringBuilder sb = new StringBuilder();
    	private String tabs = "";
    	
    	public Output(int depth) {
    		for (int i = 0; i < depth; i++)
    			tabs += "\t";
    	}
    	
    	public void addln() {
    		add("");
    	}
    	public void add(String str) {
    		sb.append(tabs + str + "\n");
    	}
    	public void add(List<String> list) {
    		for(String line : list)
    			add(line);
    	}
    	
    	public void print() {
    		System.out.println(sb);
    	}
    }

    private static String getModifiers(int mod) {
    	String out = "";
    	
    	if(Modifier.isPublic(mod))
    		out += "public ";
    	else if(Modifier.isPrivate(mod))
    		out += "private ";
    	else if(Modifier.isProtected(mod))
    		out += "protected ";
    	
    	if(Modifier.isStatic(mod))
    		out += "static ";
    	if(Modifier.isFinal(mod))
    		out += "final ";
    	else if(Modifier.isVolatile(mod))
    		out += "volatile ";
    	
    	if(Modifier.isAbstract(mod))
    		out += "abstract ";
    	else if(Modifier.isInterface(mod))
    		out += "interface ";
    	
    	if(Modifier.isTransient(mod))
    		out += "transient ";
    	if(Modifier.isNative(mod))
    		out += "native ";
    	if(Modifier.isStrict(mod))
    		out += "strict ";
    	if(Modifier.isSynchronized(mod))
    		out += "synchronized ";
    	
    	return out;
    }

    @SuppressWarnings("rawtypes")
	private static String getSuperClass(Class c) {
    	Class cSuper = c.getSuperclass();
    	if (cSuper != null && cSuper != Object.class)
    		return " extends " + cSuper.getSimpleName();
    	return "";
    }
    
    @SuppressWarnings("rawtypes")
	private static String getInterfaces(Class c) {
    	Class[] interfaces = c.getInterfaces();
    	String output = "";
    	if (interfaces.length > 0) {
    		output += " implements ";
    		for(Class i : interfaces) {
    			output += i.getSimpleName() + ", ";
    		}
    		output = output.substring(0, output.length()-2);
    	}
    	return output;
    }

    @SuppressWarnings("rawtypes")
	private static List<String> getConstructors(Class c) {
    	String name = c.getSimpleName();
    	List<String> output = new ArrayList<String>();
    	output.add("\t// Constructors");
    	
    	Constructor[] constructors = c.getDeclaredConstructors();
    	for(Constructor con : constructors)
    	{
    		String conOutput = "\t" + getModifiers(con.getModifiers());
    		conOutput += name + "(";
    		
    		int numParams = con.getParameterCount();
    		if (numParams == 0)
    			conOutput += ") {}";
    		else {
    			Parameter[] params = con.getParameters();
    			String conParams = "";
    			for(Parameter conParam : params) {
    				String type = conParam.getType().getSimpleName();
    				String paramName = "";
    				if (conParam.isNamePresent())
    					paramName = " " + conParam.getName();
    				conParams += type + paramName + ", ";
    			}
    			conParams = conParams.substring(0, conParams.length()-2);
    			conOutput += conParams + ") {}";
    		}
    		output.add(conOutput);
    	}
    	return output;
    }
    
    @SuppressWarnings("rawtypes")
	private static List<String> getMethods(Class c) {
    	List<String> output = new ArrayList<String>();
    	return output;
    }
    
    
}
