import java.util.*;

public class Optimal {
    public static List<Set<String>> formOptimalGroups(List<Student> students, int groupNum) {
        // Initialize groups and preference scores map
        List<Set<String>> groups = new ArrayList<>();
        Map<String, Map<String, Integer>> scores = initializeScores(students);

        // Generate and sort student pairs based on preference scores
        List<Pair> pairs = generateAndSortPairs(students, scores);

        // Initial assignment to ensure each group has at least one student
        Set<String> assigned = initialAssignment(students, groupNum, groups);

        int targetGroupSize = (int) Math.ceil((double) students.size() / groupNum);

        // Fill remaining slots in groups with the highest-scoring pairs
        fillGroupsWithPairs(pairs, groups, assigned, scores, targetGroupSize);

        // Assign remaining students to groups optimally
        assignRemainingStudents(students, groups, assigned, scores, targetGroupSize);

        // Add any remaining unassigned students
        addRemainingStudents(students, groups, assigned, targetGroupSize);

        return groups;
    }

    // Initialize the scores map for each student's preferences
    private static Map<String, Map<String, Integer>> initializeScores(List<Student> students) {
        Map<String, Map<String, Integer>> scores = new HashMap<>();
        for (Student student : students) {
            scores.put(student.name, new HashMap<>());
            for (Map.Entry<String, Integer> entry : student.preferences.entrySet()) {
                scores.get(student.name).put(entry.getKey(), entry.getValue());
            }
        }
        return scores;
    }


    // Generate pairs of students with their preference scores and sort them
    private static List<Pair> generateAndSortPairs(List<Student> students, Map<String, Map<String, Integer>> scores) {
        List<Pair> pairs = new ArrayList<>();
        for (Student student1 : students) {
            for (Student student2 : students) {
                if (!student1.name.equals(student2.name)) {
                    int totalScore = scores.get(student1.name).get(student2.name) + scores.get(student2.name).get(student1.name);
                    pairs.add(new Pair(student1.name, student2.name, totalScore));
                }
            }
        }
        pairs.sort((p1, p2) -> Integer.compare(p2.score, p1.score));
        return pairs;
    }


    // Ensure each group has at least one student initially
    private static Set<String> initialAssignment(List<Student> students, int groupNum, List<Set<String>> groups) {
        Set<String> assigned = new HashSet<>();
        for (int i = 0; i < groupNum && i < students.size(); i++) {
            Set<String> group = new HashSet<>(Collections.singleton(students.get(i).name));
            assigned.add(students.get(i).name);
            groups.add(group);
        }
        return assigned;
    }

    // Fill groups with the highest-scoring pairs
    private static void fillGroupsWithPairs(List<Pair> pairs, List<Set<String>> groups, Set<String> assigned,
    Map<String, Map<String, Integer>> scores, int targetGroupSize) {
    for (Pair pair : pairs) {
        if (!assigned.contains(pair.student1) && !assigned.contains(pair.student2)) {
            int bestGroupIndex = findBestGroupForPair(groups, pair, scores, targetGroupSize);
                if (bestGroupIndex != -1) {
                groups.get(bestGroupIndex).add(pair.student1);
                groups.get(bestGroupIndex).add(pair.student2);
                assigned.add(pair.student1);
                assigned.add(pair.student2);
                }
            }
        }
    }


    // Find the best group for a pair based on preference scores
    private static int findBestGroupForPair(List<Set<String>> groups, Pair pair,
        Map<String, Map<String, Integer>> scores, int targetGroupSize) {
        int bestGroupIndex = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < groups.size(); i++) {
            Set<String> group = groups.get(i);
            int currentScore = calculateGroupScore(group, pair, scores);
            if (currentScore > bestScore && group.size() < targetGroupSize) {
                bestScore = currentScore;
                bestGroupIndex = i;
            }
        }
        return bestGroupIndex;
    }


    // Calculate the total score for a group with a given pair
    private static int calculateGroupScore(Set<String> group, Pair pair, Map<String, Map<String, Integer>> scores) {
        int currentScore = 0;
        for (String member : group) {
            currentScore += scores.get(pair.student1).get(member);
            currentScore += scores.get(pair.student2).get(member);
        }
        return currentScore;
    }


    // Assign any remaining students to the most suitable groups
    private static void assignRemainingStudents(List<Student> students, List<Set<String>> groups,
        Set<String> assigned, Map<String, Map<String, Integer>> scores, int targetGroupSize) {
        for (Student student : students) {
            if (!assigned.contains(student.name)) {
            int bestGroupIndex = findBestGroupForStudent(groups, student, scores, targetGroupSize);
                if (bestGroupIndex != -1) {
                    groups.get(bestGroupIndex).add(student.name);
                    assigned.add(student.name);
                }
            }
        }
    }


    // Find the best group for a single student based on preference scores
    private static int findBestGroupForStudent(List<Set<String>> groups, Student student,
        Map<String, Map<String, Integer>> scores, int targetGroupSize) {
        int bestGroupIndex = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < groups.size(); i++) {
            Set<String> group = groups.get(i);
            int currentScore = calculateStudentGroupScore(group, student, scores);
            if (currentScore > bestScore && group.size() < targetGroupSize) {
                bestScore = currentScore;
                bestGroupIndex = i;
            }
        }
        return bestGroupIndex;
    }


    // Calculate the total score for a group with a single student
    private static int calculateStudentGroupScore(Set<String> group, Student student, Map<String, Map<String, Integer>> scores) {
        int currentScore = 0;
        for (String member : group) {
            currentScore += scores.get(student.name).get(member);
        }
        return currentScore;
    }


    // Add any remaining unassigned students to the groups
    private static void addRemainingStudents(List<Student> students, List<Set<String>> groups,
        Set<String> assigned, int targetGroupSize) {
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
    }
}

