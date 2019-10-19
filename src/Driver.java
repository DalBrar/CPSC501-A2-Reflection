
import java.io.*;

public class Driver {

    public static void main(String[] args) throws Exception {
        boolean rec = true;
        if (args.length == 1) {
            rec = Boolean.parseBoolean(args[1]);
        }
        runTest("script1.txt", new ClassA(), rec);
        runTest("script2.txt", new ClassA(12), rec);
        runTest("script3.txt", new ClassB(), rec);
        runTest("script4.txt", new ClassD(32), rec);
        runTest("script5.txt", new ClassD(), rec);
        runTest("script6.txt", new ClassB[12], rec);
        runTest("script7.txt", new ClassB[12][12], rec);
        runTest("script8.txt", "Test String", rec);
    }

    public static void runTest(String filename, Object testObj, boolean recursive) {
        try {
            PrintStream old = System.out;
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            System.out.println("======================================================");
            System.out.println("Filename: " + filename);
            System.out.println("Running Test: " + testObj);
            System.out.println("Recursive: " + recursive);
            new Inspector().inspect(testObj, recursive);
            System.out.println("======================================================");
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
            System.setOut(old);
        } catch (IOException ioe) {
            System.err.println("Unable to open file: " + filename);
        } catch (Exception e) {
            System.err.println("Unable to compleatly run test: " + testObj);
            e.printStackTrace();
        }
    }
}
