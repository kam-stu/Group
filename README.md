# GroupCreation
Program that creates the most optimal groups given a CSV file of students and their ratings of each other

## Table of Contents
- [Inputs](#inputs)
- [Optimal Algorithm](#optimal-algorithm)
- [Backtracking Algorithm](#backtracking-algorithm)

## Inputs
The program will ask for a few parameters before generating the group.

```bash
Enter the file name/pathaway
```

``bash
Enter the amount of groups:
```

```bash
Which algorithm would you like to use?
Optimal (1) : Backtracking (2)
```

Note:
1) The CSV file must be in the same directory as the project
2) The CSV file input must be typed EXACTLY as the file name (ie. capital/lowercase letters in the same spot)

Following this, the most optimal group will be printed with their score and the average score of the total as such:

```bash
Group 1 (Score: n): [Student1, Student2]
Group 2 (Score: n): [Student3, Student4]
Average Score: n
```

## Optimal Algorithm
The optimal solution I came up with is as follows:

- Constructs a nested Hashmap to store each student's preference for every other student

- Creates and sorts pairs of students baed on combined preference scores

- Fills groups with the highest-scoring pairs of students

- Finds the best group for a given pair based on score

- Calculates the score of the group in respect of the Student in the group

- Calculates the total score for a group

Overall, this algorithm is complexity O(n^3)

## Backtracking Algorithm
The Backtracking Algorithm is more optimal with making the correct groups, however, it is borderline unuseable past a certain point due to its complexity.  This algorithm works the following way:

- Recurseively goes through every possible group combination

- Score is calculated for each group

- Best groups are returned

Overall, the complexity is O(n!), which is far from optimal and leads to astronomically high run times for high student/group counts