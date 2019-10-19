import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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
    	int mods = c.getModifiers();
    	String modifiers = getModifiers(mods);
    	String name = c.getSimpleName();
    	
    	// 2) Name of immediate super-class
    	Class cSuper = c.getSuperclass();
    	if (cSuper != null && cSuper != Object.class)
    		name += " extends " + cSuper.getSimpleName();
    	
    	// 3) Name of each interface
    	Class[] interfaces = c.getInterfaces();
    	if (interfaces.length > 0) {
    		name += " implements ";
    		for(Class i : interfaces) {
    			name += i.getSimpleName() + ", ";
    		}
    		name = name.substring(0, name.length()-2);
    	}
    	
    	name += " {";
    	output.add(modifiers + name);
    	
    	// 4) Constructors: name, parameter types, modifiers
    	output.add("\t// Constructors");
    	
    	
    	output.add("}");
    	output.print();
    	
    	/*
    	// 2) Name of the super-class
    	Class cSuper = c.getSuperclass();
    	if (cSuper != Object.class) {
    		int modifiers = cSuper.getModifiers();
    		
    		// parent is Abstract
    		if (Modifier.isAbstract(modifiers)) {
    			name += " extends " + cSuper.getName();
    		}
    		// 2-a) parent is not Interface nor Abstract => recurse
    		else if (!cSuper.isInterface()) {
    			
    		}
    		// 3-a) Names of interfaces and recurse
    		else
    		{
    			Class[] interfaces = cSuper.getInterfaces();
    		}
    	}
			try
			{
				Object oSuper = cSuper.getConstructor().newInstance();
		    	new Inspector().inspectClass(cSuper, oSuper, recursive, depth+1);
			}
			catch (InstantiationException |
					 IllegalAccessException |
					 IllegalArgumentException |
					 InvocationTargetException |
					 NoSuchMethodException |
					 SecurityException e)
			{
				e.printStackTrace();
			}
    	*/
    }
    
    public class Output {
    	private StringBuilder sb = new StringBuilder();
    	private String tabs = "";
    	
    	public Output(int depth) {
    		for (int i = 0; i < depth; i++)
    			tabs += "\t";
    	}
    	
    	public void add(String str) {
    		sb.append(tabs + str + "\n");
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
}
