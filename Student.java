import java.util.HashMap;
import java.util.Map;

// Class for getting each student with their preferences
public class Student {
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

