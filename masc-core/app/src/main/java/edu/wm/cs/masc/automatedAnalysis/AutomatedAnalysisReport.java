package edu.wm.cs.masc.automatedAnalysis;

public class AutomatedAnalysisReport {
    private int killedMutants = 0;
    private int unKilledMutants = 0;
    public int errorCount = 0;

    public void mutantKilled(){
        killedMutants++;
    }

    public void mutantUnkilled(){
        unKilledMutants++;
    }

    public void registerError(){
        errorCount++;
    }

    public void printReport(){
        int total = killedMutants + unKilledMutants + errorCount;
        double detectionRate = killedMutants / (total * 1.0);
        int dr =  (int)Math.round(detectionRate * 100.0);

        System.out.println("\n----------------------------------------");
        System.out.println("Analyzed: " + total);
        System.out.println("Killed: " + killedMutants);
        System.out.println("Unkilled: " + unKilledMutants);
        System.out.println("Errors: " + errorCount);
        System.out.println("Detection rate: " + dr + "%");
        System.out.println("----------------------------------------");
    }
}
