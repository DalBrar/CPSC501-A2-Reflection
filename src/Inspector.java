import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inspector {
	
	private static final Set<Class<?>> PRIMITIVES = new HashSet<Class<?>>(Arrays.asList(
			byte.class,
			char.class,
			short.class,
			int.class,
			long.class,
			float.class,
			double.class,
			boolean.class,
			void.class,
			Byte.class,
			Short.class,
			Integer.class,
			Long.class,
			Float.class,
			Double.class,
			Boolean.class,
			String.class
	));
	
    @SuppressWarnings("rawtypes")
	public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    @SuppressWarnings({ "rawtypes" })
	private static void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	if (c == Object.class)
    		return;
    	//TODO: bonus
    	//TODO: deal with Arrays
    	Output output = new Output(depth);
    	
    	// 1) Get class Name
    	String title = "";
    	int mods = c.getModifiers();
    	String modifiers = getModifiers(mods);
    	String type = "class ";
    	if (c.isInterface())
    		type = "interface ";
    	String name = c.getSimpleName();
    	title = modifiers + " " + type + name;
    	
    	// 2) Name of immediate super-class
    	title += getSuperClass(c, recursive, depth);
    	
    	// 3) Name of each interface
    	title += getInterfaces(c, recursive, depth);
    	
    	title += " {";
    	output.add(title);
    	
    	// 4) Constructors: name, parameter types, modifiers
    	List<String> constructors = getConstructors(c);
    	if (constructors.size() > 1) {
    		output.add(constructors);
    		output.addln();
    	}
    	
    	// 5) Methods: modifiers, name, parameter types, exceptions, return type
    	List<String> methods = getMethods(c);
    	if (methods.size() > 1) {
    		output.add(methods);
    		output.addln();
    	}
    	
    	// 6) Fields: modifiers, type, name, value
    	List<String> fields = getFields(c, obj, recursive, depth);
    	if (fields.size() > 1)
    		output.add(fields);
    	
    	output.add("}");
    	output.print();
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
    	
    	if (out.length() > 0)
    		out = out.substring(0, out.length()-1);
    	return out;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getSuperClass(Class c, boolean recursive, int depth) {
    	Class cSuper = c.getSuperclass();
    	if (cSuper != null && cSuper != Object.class) {
    		try {
    			Object obj = null;
    			int mods = cSuper.getModifiers();
    			if (!cSuper.isInterface() && !Modifier.isAbstract(mods))
    				obj = cSuper.getConstructor().newInstance();
    			System.out.println(cSuper.getSimpleName() + ": " + (obj == null));
				inspectClass(cSuper, obj, recursive, depth+1);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
    		return " extends " + cSuper.getSimpleName();
    	}
    	return "";
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getInterfaces(Class c, boolean recursive, int depth) {
    	Class[] interfaces = c.getInterfaces();
    	String output = "";
    	if (interfaces.length > 0) {
    		output += " implements ";
    		for(Class i : interfaces) {
    			output += i.getSimpleName() + ", ";
        		try {
        			Object obj = null;
        			if (!i.isInterface())
        				obj = i.getConstructor().newInstance();
    				inspectClass(i, obj, recursive, depth+1);
    			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
    					| InvocationTargetException | NoSuchMethodException | SecurityException e) {}
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
    	for(Constructor constr : constructors)
    	{
    		String conOutput = "\t" + getModifiers(constr.getModifiers());
    		conOutput += " " + name + getParameters(constr);
    		conOutput += " " + getExceptions(constr);
    		output.add(conOutput);
    	}
    	return output;
    }
    
    @SuppressWarnings("rawtypes")
	private static List<String> getMethods(Class c) {
    	List<String> output = new ArrayList<String>();
    	output.add("\t// Methods");
    	Method[] methods = c.getDeclaredMethods();
    	for(Method method : methods) {
    		if (!Modifier.isPublic(method.getModifiers()))
    			method.setAccessible(true);
    		
    		String line = "\t" + getModifiers(method.getModifiers());
    		line += " " + method.getReturnType().getSimpleName();
    		line += " " + method.getName() + getParameters(method);
    		line += " " + getExceptions(method);
    		output.add(line);
    	}
    	return output;
    }
    
    @SuppressWarnings("rawtypes")
	private static List<String> getFields(Class c, Object obj, boolean recursive, int depth) {
    	List<String> output = new ArrayList<String>();
    	output.add("\t// Fields");
    	
    	Field[] fields = c.getDeclaredFields();
    	for(Field field : fields) {
    		if(Modifier.isPrivate(field.getModifiers()))
    			field.setAccessible(true);
    		
    		String line = "\t" + getModifiers(field.getModifiers());
    		line += " " + field.getType().getSimpleName();
    		line += " " + field.getName();
    		line += " = " + getValue(field, obj, recursive, depth);
    		output.add(line);
    	}
    	
    	return output;
    }
    
    private static String getParameters(Executable exec) {
		String output = "(";
		
		int numParams = exec.getParameterCount();
		if (numParams == 0)
			output += ")";
		else {
			for(Parameter param : exec.getParameters()) {
				String type = param.getType().getSimpleName();
				String paramName = "";
				if (param.isNamePresent())
					paramName = " " + param.getName();
				output += type + paramName + ", ";
			}
			output = output.substring(0, output.length()-2);
			output += ")";
		}
		return output;
    }

    private static String getExceptions(Executable exec) {
    	String output = "";
    	Class<?>[] exceps = exec.getExceptionTypes();
    	if (exceps.length > 0)
    		output += "throws ";
    	for(Class<?> eclass : exceps) {
    		output += eclass.getSimpleName() + ", ";
    	}
    	if (exceps.length > 0)
    		output = output.substring(0, output.length()-2);
    	return output;
    }
    
    @SuppressWarnings("rawtypes")
	private static String getValue(Field field, Object obj, boolean recursive, int depth) {
    	Class type = field.getType();
    	try {
			Object val = field.get(obj);
			
			if(isPrimitiveType(type))
				return String.valueOf(val);
			if (val == null)
				return "null";
			if (recursive) {
				Class clazz = val.getClass();
				inspectClass(clazz, val, recursive, depth + 1);
			}
			return val.getClass().getCanonicalName() + "@" + Integer.toHexString(System.identityHashCode(val));
				
		} catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
			System.out.println("" + (field == null) + " : " + (obj == null));
			e.printStackTrace();
		}
		return "???";
    }
    
    private static boolean isPrimitiveType(Class<?> clazz) {
    	return PRIMITIVES.contains(clazz);
    }
 
    public static class Output {
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

}
