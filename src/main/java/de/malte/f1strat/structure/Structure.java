package de.malte.f1strat.structure;

public class Structure {

    protected long[] objectArrToIntArr (Object[] obj) {

        long[] res = new long[obj.length];

        for (int i = 0; i < obj.length; i++) {

            try {
                res[i] = (long) obj[i];
            } catch (Exception e) {
                System.err.println("Error while parsing from Object to Long");
                System.out.println(e.getMessage());
            }

        }
        return res;
    }

    protected double[] objectArrToDoubleArr (Object[] obj) {

        double[] res = new double[obj.length];

        for (int i = 0; i < obj.length; i++) {

            try {
                res[i] = (double) obj[i];
            } catch (Exception e) {
                System.err.println("Error while parsing from Object to Double");
            }
        }
        return res;
    }

    protected String[] objectArrToStringArr (Object[] obj) {
        String[] res = new String[obj.length];

        for (int i = 0; i < obj.length; i++) {
            try {
                res[i] = (String) obj[i];
            } catch (Exception e) {
                System.err.println("Error while parsing from Object to Double");
            }
        }
        return res;
    }
}
