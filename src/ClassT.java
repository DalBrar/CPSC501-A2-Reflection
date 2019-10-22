
public final class ClassT implements Animal {

    private int val = 33;
    @SuppressWarnings("unused")
	private double val2 = 2.2;
    @SuppressWarnings("unused")
	private boolean val3 = true;

    public ClassT() {
        val = 55;
    }

    public ClassT(int i) {
        try {
            setVal(i);
        } catch (Exception e) {
        }
    }

    public void run() {
    }

    public int getVal() {
        return val;
    }

    public void setVal(int i) throws Exception {
        if (i < 0) {
            throw new Exception("negative value");
        }
        val = i;
    }

    public String toString() {
        return "ClassA";
    }

    @SuppressWarnings("unused")
	private static void printSomething() {
        System.out.println("Something");
    }

	@Override
	public void animalSound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sleep() {
		// TODO Auto-generated method stub
		
	}

}
