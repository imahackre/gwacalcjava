import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GradeCalculator {
    
    // Grade conversion table for 1.0-point system (customizable)
    private static final double[][] PERCENTAGE_TO_POINT = {
        {98.0, 100.0, 1.0},
        {95.0, 97.99, 1.25},
        {92.0, 94.99, 1.5},
        {89.0, 91.99, 1.75},
        {85.0, 88.99, 2.0},
        {82.0, 84.99, 2.25},
        {80.0, 81.99, 2.5},
        {77.0, 79.99, 2.75},
        {75.0, 76.99, 3.0}, // no 4.0
        {0.0, 74.99, 5.0} // Failed
    };
    
    public static class Subject {
        private String name;
        private int units;
        private double prelimGrade;
        private double midtermGrade;
        private double finalGrade;
        private double prelimWeight;
        private double midtermWeight;
        private double finalWeight;
        
        public Subject(String name, int units, double prelimWeight, double midtermWeight, double finalWeight) {
            this.name = name;
            this.units = units;
            this.prelimWeight = prelimWeight;
            this.midtermWeight = midtermWeight;
            this.finalWeight = finalWeight;
        }
        
        public void setGrade(double prelim, double midterm, double finals) {
            this.prelimGrade = prelim;
            this.midtermGrade = midterm;
            this.finalGrade = finals;
        }
        
        public double calculateGrade() {
            return (prelimGrade * prelimWeight) + 
                   (midtermGrade * midtermWeight) + 
                   (finalGrade * finalWeight);
        }
        
        public double calculateGradePoint() {
            return percentageToPoint(calculateGrade());
        }
        
        // Getters
        public String getSubjectName() { return name; }
        public int getUnits() { return units; }
    }
    
    // Converts percentage grade to 1.0-point system //

    public static double percentageToPoint(double percentage) {
        for (double[] range : PERCENTAGE_TO_POINT) {
            if (percentage >= range[0] && percentage <= range[1]) {
                return range[2];
            }
        }
        return 5.0; // Default if not in any range (failed)
    }
    
    // Calculates GWA from a list of subjects //
    
    public static double calculateGWA(List<Subject> subjects) {
        double totalWeightedPoints = 0.0;
        int totalUnits = 0;
        
        for (Subject subject : subjects) {
            double gradePoint = subject.calculateGradePoint();
            totalWeightedPoints += gradePoint * subject.getUnits();
            totalUnits += subject.getUnits();
        }
        
        return totalWeightedPoints / totalUnits;
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<Subject> subjects = new ArrayList<>();
        
        System.out.println("Grade and GWA Calculator");
        System.out.println("------------------------");
        
        // Input subjects
        while (true) {
            System.out.print("\nEnter subject name (or 'done' to finish): ");
            String name = input.nextLine();
            if (name.equalsIgnoreCase("done")) break;
            
            System.out.print("Enter number of units: ");
            int units = input.nextInt();
            
            System.out.print("Enter prelim weight (e.g., 0.3 for 30%): ");
            double prelimWeight = input.nextDouble();
            
            System.out.print("Enter midterm weight: ");
            double midtermWeight = input.nextDouble();
            
            System.out.print("Enter finals weight: ");
            double finalWeight = input.nextDouble();
            
            input.nextLine();
            
            // Validate weights sum to 1.0 (100%)
            if (Math.abs((prelimWeight + midtermWeight + finalWeight) - 1.0) > 0.001) {
                System.out.println("Error: Weights must sum to 1.0 (100%)");
                continue;
            }
            
            Subject subject = new Subject(name, units, prelimWeight, midtermWeight, finalWeight);
            
            // Input grades
            System.out.print("Enter prelim grade (%): ");
            double prelimGrade = input.nextDouble();
            
            System.out.print("Enter midterm grade (%): ");
            double midtermGrade = input.nextDouble();
            
            System.out.print("Enter finals grade (%): ");
            double finalGrade = input.nextDouble();
            
            input.nextLine(); // Clear the newline
            
            subject.setGrade(prelimGrade, midtermGrade, finalGrade);
            subjects.add(subject);
            
            // Show immediate results for this subject
            double finalPercentage = subject.calculateGrade();
            double gradePoint = subject.calculateGradePoint();
            
            System.out.printf("\n%s Results:\n", subject.getSubjectName());
            System.out.printf("Final Grade: %.2f%%\n", finalPercentage);
            System.out.printf("Grade Point: %.2f\n", gradePoint);
        }
        
        // Calculate and display GWA if subjects were entered
        if (!subjects.isEmpty()) {
            double gwa = calculateGWA(subjects);
            System.out.printf("\nOverall GWA: %.2f\n", gwa);
            
            // Detailed breakdown
            System.out.println("\nSubject Breakdown:");
            System.out.println("--------------------------------------------------");
            System.out.println("Subject\t\tUnits\tFinal%\tPoint");
            System.out.println("--------------------------------------------------");
            
            for (Subject subject : subjects) {
                System.out.printf("%s\t%d\t%.2f\t%.2f\n", 
                    subject.getSubjectName(),
                    subject.getUnits(),
                    subject.calculateGrade(),
                    subject.calculateGradePoint());
            }
        } else {
            System.out.println("No subjects entered.");
        }
        
        input.close();
    }
}
