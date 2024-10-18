import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        System.out.println(students);
    }

    public static List<Student> readCSV(String fileName) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String header = br.readLine();
            String[] names = header.split(",");
            Map<String, Student> studentMap = new HashMap<>();
            for (String name : names) {
                studentMap.put(name, new Student(name));
            }

            String line;
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

    public static void printGroups(List<Student> groups) {
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group " + (i + 1) + ": " + groups.get(i));
        }
    }

}
