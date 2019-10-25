import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* CPSC 501 - Assignment 2 - Reflection
 * by: Dalbir Brar
 * ID: 002 968 97
 * Bonus Program:
 * 		Takes 3 command line arguments:
 * 			1) the name of a class containing the inspect method
 * 			2) the name of a class to inspect
 * 			3) a boolean for recursive
 * 		Uses reflection to load the class indicated as the first command
 * 		line argument and run it's inspect method on the class of the 
 * 		the second command line argument. Uses third argument to determine
 * 		whether it should recursively do all objects within the class.
 * 
 * 		Once all parameters are checked for validity the inspect() method is run.
 */

public class DriverBonus {
	
	private static final String TERMINATED = "\nProgram terminated.";
	private static final String EXAMPLE = "Example: NewInspector java.lang.string true";
	private static final String CASESENSITIVE = "NOTE: argument is CaseSensitive";
	private static final String ERR_GET_CLASS = "ERROR %s: Given class \"%s\" not found.";
	private static final String ERR_SECURITY_EXCEPTION = "ERROR %s: SecurityException while trying to access %s.";

	public static void main(String[] args) {
		if (args.length < 3) {
			print("USAGE: Must pass 3 arguments: <inspector.class> <inspectee.class> <recursive:true/false>");
			print(EXAMPLE);
			print(TERMINATED);
			return;
		}
		
        // Parse arg1 for Inspector.inspect(Object, boolean)
		Class<?> clazz = getClass("arg1", args[0]);
		Object inspector = getObject(clazz, "arg1", args[0]);
		Method mInspect = getInspectMethod(clazz);
		
		// Parse arg2 for Object to inspect
		Object object = getInspecteeObject(args[1]);
		
		// Parse arg3 for recursive boolean value
		Boolean recursive = getRecursiveBoolean(args[2]);
		
		// validity check & execute
		if (inspector != null && mInspect != null && object != null && recursive != null) {
			try {
				print("======================================================");
				print("Inspector: " + clazz.getSimpleName());
				print("Object: " + object.getClass().getCanonicalName());
				print("Recursive: " + recursive);
				mInspect.setAccessible(true);
				mInspect.invoke(inspector, new Object[] {object, recursive});
				print("======================================================");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				print(String.format("ERROR: failed to invoke %s.inspect(%s, %s).", args[0], args[1], args[2]));
			}
		}
		
		print(TERMINATED);
	}
	
	private static Class<?> getClass(String arg, String cName) {
		try {
			Class<?> c = Class.forName(cName);
			return c;
		} catch (ClassNotFoundException e) {
			print(String.format(ERR_GET_CLASS, arg, cName));
			print(CASESENSITIVE);
		}
		return null;
	}

	private static Object getObject(Class<?> clazz, String arg, String objName) {
		if (clazz == null) return null;
		
		try {
			Object obj = clazz.getConstructor().newInstance();
			return obj;
		}
		catch (NoSuchMethodException | SecurityException e) {
			print("ERROR "+ arg + ": Failed to getConstructor() of " + objName);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			print("ERROR arg2: Failed to instantiate " + objName);
		}
		
		return null;
	}
	
	private static Method getInspectMethod(Class<?> clazz) {
		if (clazz == null) return null;
		try {
			Method m = clazz.getMethod("inspect", new Class[] {Object.class, boolean.class});
			return m;
		} catch (NoSuchMethodException e) {
			print("ERROR arg1: Given inspector class does not have the method inspect(Object, boolean).");
		} catch (SecurityException e) {
			print(String.format(ERR_SECURITY_EXCEPTION, "arg1", clazz.getSimpleName()));
		}
		return null;
	}
	
	private static Object getInspecteeObject(String objName) {
		Class<?> clazz = getClass("arg2", objName);
		return getObject(clazz, "arg2", objName);
	}
	
	private static Boolean getRecursiveBoolean(String input) {
		return Boolean.valueOf(input);
	}
	
	private static void print(String str) {
		System.out.println(str);
	}
}
