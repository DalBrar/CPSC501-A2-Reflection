
public class ClassB extends ClassC implements Runnable {

    private ClassA val = new ClassA();
    private ClassA val2 = new ClassA(12);
    private ClassA val3;

    public ClassB() throws Exception {
        super(2, 3);
    }

    public void run() {
    }

    public String toString() {
        return "ClassB";
    }

    public void func3(int a) {

    }
}
