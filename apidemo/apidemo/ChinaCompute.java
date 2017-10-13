package apidemo;

//this class does periodic computation of chinadata
class ChinaCompute implements Runnable {

    static ChinaCompute cc = new ChinaCompute();

    static ChinaCompute getChinaCompute() {
        return cc;
    }

    private ChinaCompute() {
    }

    @Override
    public void run() {
        try {
            ChinaStock.compute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("interrupted");
        }
    }
}
