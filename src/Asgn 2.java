
public class Ex1 {

    public int static main(String[] args) {
	Object obj = null;
	Class clas = null;

	while (obj == null) {
	    try {
		System.out.print("Enter a class name: ");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();

		clas = Class.forName(input);
		object = clas.getConstrutor().newInstance();
		System.out.println();
	    } catch (Exception e) {}
	}

	printClass(obj);
    }

    public static void printClass(Object obj) {
	String output = "";

	Class clazz = obj.getClass();
	
	Methods[] methods = clazz.getDeclaredMethods();
	output += "Methods:\n";
	for (Method m : methods) {
	    output += "\t" + m.getName(); "\n";
	}

	Field[] fields = clazz.getDeclaredFields();
	output += "Fields:\n";
	for (Field f : fields) {
	    String name = f.getName();
	    String type = f.getType().getName();
	    output += "\t" + type + " " + name + "\n";
	}
    }

    // get class object name from standard input and print its public and private fields and values
    public static void ex1(Object obj) {
	Class clas = obj.getConstructor.newInstance();

	Fields[] fields = clas.getDeclaredFields();
	for(Field f : fields) {
	    if (!Modifier.isPublic(f.getModifiers()))
		field.setAccessible(true);
	    System.out.print(field.getType() + " " + field.getName() + " " + field.get(obj));
	}
    }

    // after ex1 get input for a field name and then ask for a value and change the value of that field.
    public static void ex2(Object obj) {
	Class clas = obj.getClass();
	
	System.out.print("Enter a field name: ");
	Scanner sc = new Scanner(System.in);
	String input = sc.nextLine();

	Field field = clas.getDeclaredField(input);
	if (!Modifier.isPublic(field.getModifiers()))
	    field.setAccesible(true);
	Object val = field.get(obj);
	Class type = field.getType();
	System.out.println("The field is of type " + type + " with a value of:  " + val);

	System.out.print("Enter a new value: ");
	input = sc.nextLine();
	PropertyEditor editor = PropertyEditorManager.findEditor(type);
	editor.setAsText(input);
	field.set(object, editor.getValue());
	System.out.println(type + " " + field.getName() + " = " + input);
    }


    public static void ex3 () {
	
    }

    // Test class to test above code
    public class A {
	private String str = "old value";
	public int n = 1;
    }
}
