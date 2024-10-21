import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Student> students = getFile(scanner);
        int numGroups = getGroups(scanner, students);
        getAlgorithm(scanner, students, numGroups);
    }

    public static List<Student> getFile(Scanner scanner) {
        List<Student> students = null;
        String csv;
        while (students == null) {
            try {
                System.out.println("Enter the file name/pathway: ");
                csv = scanner.nextLine();

                File file = new File(csv);
                if (!file.exists()) {
                    throw new FileNotFoundException("File does not exist");
                }
                students = readCSV(csv);
                break;
            } 
            catch (FileNotFoundException e) {
                System.out.println("File not found or is not a valid file. Please enter a valid file path.");
                scanner.reset();
            }
        }
        return students;
    }

    // Parses the CSV and adds each person and their preference to a hashmap
    // Where person is a string and prefence is a Java List
    public static List<Student> readCSV(String fileName) {
        List<Student> students = new ArrayList<>();

        // Adds each student into the hashmap
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String header = br.readLine();
            String[] names = header.split(",");
            Map<String, Student> studentMap = new HashMap<>();
            for (String name : names) {
                studentMap.put(name, new Student(name));
            }

            String line;

            // adds each student's preference as a value to the hashmap
            // with the key being the student
            while ((line = br.readLine()) != null) {
                String[] scores = line.split(",");
                String studentName = scores[0];
                Student student = studentMap.get(studentName);
                for (int i = 1; i < scores.length; i++) {
                    student.addPreference(names[i], Integer.parseInt(scores[i]));
                }
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static int getGroups(Scanner scanner, List<Student> students) {
        while (true) {
            try {
                System.out.println("Enter the amount of groups: ");
                int numGroups = scanner.nextInt();

                if (numGroups <= students.size() && numGroups > 0) {
                    return numGroups;
                }
            } 
            catch (Exception e) {
                System.out.println("Enter a valid number.");
                scanner.nextLine(); // clears input
            }
        }
    }

    public static void getAlgorithm(Scanner scanner, List<Student> students, int numGroups) {
        while (true) {
            try {
                System.out.println("Which algorithm would you like to use?");
                System.out.println("Optimal (1) : Backtracking (2)");
                int algorithmChoice = scanner.nextInt();

                if (algorithmChoice == 1) {
                    List<Set<String>> groups = Optimal.formOptimalGroups(students, numGroups);
                    printGroups(groups, students);
                    break;
                }
                else if (algorithmChoice == 2) {
                    List<List<String>> backtrackGroup = Backtrack.backtrackGroups(students, numGroups);
                    printGroups(backtrackGroup, students);
                    break;
                }
                else {
                    System.out.println("Enter a valid input.");
                }
            }
            catch (Exception e){
                System.out.println("Invalid input.  Enter 1 or 0");
            }
        }
        scanner.close();
    }

    public static void printGroups(List<?> groups, List<Student> students) {
        Map<String, Student> studentMap = new HashMap<>();
        for (Student student : students) {
            studentMap.put(student.name, student);
        }
    
        int totalScore = 0;
        for (int i = 0; i < groups.size(); i++) {
            List<String> group;
            if (groups.get(i) instanceof Set) {
                group = new ArrayList<>((Set<String>) groups.get(i));
            } else {
                group = (List<String>) groups.get(i);
            }
    
            int groupScore = 0;
            for (String member1 : group) {
                for (String member2 : group) {
                    if (!member1.equals(member2)) {
                        groupScore += studentMap.get(member1).preferences.get(member2);
                    }
                }
            }
            totalScore += groupScore;
            System.out.println("Group " + (i + 1) + " (Score: " + groupScore + "): " + group);
        }
    
        if (!groups.isEmpty()) {
            int averageScore = totalScore / groups.size();
            System.out.println("Average Score: " + averageScore);
        } else {
            System.out.println("No groups formed, average score calculation skipped.");
        }
    } 
}
