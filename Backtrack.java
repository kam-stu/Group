import java.io.*;
import java.util.*;

public class Backtrack {

    public static List<List<String>> backtrackGroups(List<Student> students, int groupNum) {
        List<List<String>> bestGroups = new ArrayList<>();
        List<List<String>> currentGroups = new ArrayList<>();
        for (int i = 0; i < groupNum; i++) {
            currentGroups.add(new ArrayList<>());
        }
        
        Map<String, Student> studentMap = new HashMap<>();
        for (Student student : students) {
            studentMap.put(student.name, student);
        }
        
        int[] bestScore = {Integer.MIN_VALUE}; // Use an array to modify in-place

        // Initial assignment to ensure each group has at least one student
        for (int i = 0; i < groupNum && i < students.size(); i++) {
            currentGroups.get(i).add(students.get(i).name);
        }

        backtrack(students, groupNum, currentGroups, bestGroups, studentMap, bestScore);
        return bestGroups;
    }

    private static void backtrack(List<Student> students, int index, List<List<String>> currentGroups,
                                  List<List<String>> bestGroups, Map<String, Student> studentMap, int[] bestScore) {
        if (index == students.size()) {
            int currentScore = calculateTotalScore(currentGroups, studentMap);
            if (currentScore > bestScore[0]) {
                bestScore[0] = currentScore;
                bestGroups.clear();
                for (List<String> group : currentGroups) {
                    bestGroups.add(new ArrayList<>(group));
                }
            }
            return;
        }

        for (List<String> group : currentGroups) {
            if (group.size() < (students.size() + currentGroups.size() - 1) / currentGroups.size()) { // to handle remainder students
                group.add(students.get(index).name);
                backtrack(students, index + 1, currentGroups, bestGroups, studentMap, bestScore);
                group.remove(group.size() - 1);
            }
        }
    }

    private static int calculateTotalScore(List<List<String>> groups, Map<String, Student> studentMap) {
        int totalScore = 0;
        for (List<String> group : groups) {
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    totalScore += studentMap.get(group.get(i)).preferences.get(group.get(j));
                    totalScore += studentMap.get(group.get(j)).preferences.get(group.get(i));
                }
            }
        }
        return totalScore;
    }
}
