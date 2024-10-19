import java.io.*;
import java.util.*;

public class StudentGrouping {

    static class Student {
        String name;
        Map<String, Integer> preferences;

        public Student(String name) {
            this.name = name;
            this.preferences = new HashMap<>();
        }

        public void addPreference(String peer, int score) {
            this.preferences.put(peer, score);
        }

        public String toString() {
            String res = "\n";

            res += "Student: " + this.name + "\n";

            for (String key: this.preferences.keySet()) {
                res += "Prefences: " + "[ " + key + ", " + this.preferences.get(key) + "]\n";
            }

            return res;
        }
    }

    public static void main(String[] args) {
        List<Student> students = readCSV("compatability_withnames.csv");
        List<Set<String>> groups = formOptimalGroups(students, 7);
        printGroups(groups);
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

            // adds each student's preference to the hashmap
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

    public static List<Set<String>> formOptimalGroups(List<Student> students, int groupNum) {

        // Creating the List for groups and hashmap for scores
        List<Set<String>> groups = new ArrayList<>();
        Map<String, Map<String, Integer>> scores = new HashMap<>();
    
        // Creates a nested map to store each student's preference for every other student
        for (Student student : students) {
            scores.put(student.name, new HashMap<>());
            for (Map.Entry<String, Integer> entry : student.preferences.entrySet()) {
                scores.get(student.name).put(entry.getKey(), entry.getValue());
            }
        }
    
        // Cechks each pair of students, calculates the total preference score
        // for each pair and stores them in a list and gets sorted
        List<Pair> pairs = new ArrayList<>();
        for (Student student1 : students) {
            for (Student student2 : students) {
                if (!student1.name.equals(student2.name)) {
                    int totalScore = scores.get(student1.name).get(student2.name) + scores.get(student2.name).get(student1.name);
                    pairs.add(new Pair(student1.name, student2.name, totalScore));
                }
            }
        }
    
        // Sort pairs by total desirability score in descending order
        pairs.sort((p1, p2) -> Integer.compare(p2.score, p1.score));
    
        // Assign students to groups
        // Checks if students in each high-scoring pair are assigned or not
        // If not assigned, formas ag roup with them and adds the group to the list
        // repeated until target num of groups is met
        Set<String> assigned = new HashSet<>();
        int targetGroupSize = students.size() / groupNum;
        for (Pair pair : pairs) {
            if (!assigned.contains(pair.student1) && !assigned.contains(pair.student2)) {
                Set<String> group = new HashSet<>(Arrays.asList(pair.student1, pair.student2));
                assigned.add(pair.student1);
                assigned.add(pair.student2);
                groups.add(group);
                if (groups.size() == groupNum) {
                    break;
                }
            }
        }
    
        // Ensure all students are assigned even if not optimal
        // calculates which existing group would be most optimal for them based on their preference
        // places them in that group
        for (Student student : students) {
            if (!assigned.contains(student.name)) {
                int bestGroupIndex = -1;
                int bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < groups.size(); i++) {
                    Set<String> group = groups.get(i);
                    int currentScore = 0;
                    if (group.size() < targetGroupSize) {
                        for (String member : group) {
                            currentScore += scores.get(student.name).get(member);
                        }
                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestGroupIndex = i;
                        }
                    }
                }
                if (bestGroupIndex != -1) {
                    groups.get(bestGroupIndex).add(student.name);
                    assigned.add(student.name);
                }
            }
        }
    
        // Add remaining students to existing groups if any
        // Adds any unassigned students suboptimally
        // Ensures every single student is in a group even if suboptimal
        for (Student student : students) {
            if (!assigned.contains(student.name)) {
                for (Set<String> group : groups) {
                    if (group.size() < targetGroupSize + 1) { // Allowing for one more student per group
                        group.add(student.name);
                        assigned.add(student.name);
                        break;
                    }
                }
            }
        }
    
        return groups;
    }
    

    static class Pair {
        String student1;
        String student2;
        int score;

        Pair(String student1, String student2, int score) {
            this.student1 = student1;
            this.student2 = student2;
            this.score = score;
        }
    }

    public static void printGroups(List<Set<String>> groups) {
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group " + (i + 1) + ": " + groups.get(i));
        }
    }
}
