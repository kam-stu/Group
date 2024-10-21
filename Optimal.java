import java.util.*;

public class Optimal {
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

        // Checks each pair of students, calculates the total preference score
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

        // Initial assignment to ensure each group has at least one student
        Set<String> assigned = new HashSet<>();
        for (int i = 0; i < groupNum && i < students.size(); i++) {
            Set<String> group = new HashSet<>(Collections.singleton(students.get(i).name));
            assigned.add(students.get(i).name);
            groups.add(group);
        }

        int targetGroupSize = (int) Math.ceil((double)students.size() / groupNum);

        // Fill remaining slots in groups with the highest-scoring pairs
        for (Pair pair : pairs) {
            if (!assigned.contains(pair.student1) && !assigned.contains(pair.student2)) {
                int bestGroupIndex = -1;
                int bestScore = Integer.MIN_VALUE;

                for (int i = 0; i < groups.size(); i++) {
                    Set<String> group = groups.get(i);
                    int currentScore = 0;
                    
                    for (String member : group) {
                        currentScore += scores.get(pair.student1).get(member);
                        currentScore += scores.get(pair.student2).get(member);
                    }

                    if (currentScore > bestScore && group.size() < targetGroupSize) {
                        bestScore = currentScore;
                        bestGroupIndex = i;
                    }
                }

                if (bestGroupIndex != -1) {
                    groups.get(bestGroupIndex).add(pair.student1);
                    groups.get(bestGroupIndex).add(pair.student2);
                    assigned.add(pair.student1);
                    assigned.add(pair.student2);
                }
            }
        }

        // Ensure all students are assigned optimally
        for (Student student : students) {
            if (!assigned.contains(student.name)) {
                int bestGroupIndex = -1;
                int bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < groups.size(); i++) {
                    Set<String> group = groups.get(i);
                    int currentScore = 0;
                    for (String member : group) {
                        currentScore += scores.get(student.name).get(member);
                    }
                    if (currentScore > bestScore && group.size() < targetGroupSize) {
                        bestScore = currentScore;
                        bestGroupIndex = i;
                    }
                }
                if (bestGroupIndex != -1) {
                    groups.get(bestGroupIndex).add(student.name);
                    assigned.add(student.name);
                }
            }
        }

        // Add remaining students to existing groups if any
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
}
