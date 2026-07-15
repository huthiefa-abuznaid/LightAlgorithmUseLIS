# Max LED Lighting (Dynamic Programming)

This project is an implementation of the **Max LED Lighting** problem, developed for the *Design and Analysis of Algorithms (COM336)* course at Birzeit University.

The goal is to connect a set of sorted power sources $S = \langle 1, 2, \dots, n \rangle$ to a permuted set of LEDs $L$ using unshielded wires. Because the wires cannot cross, the program uses **Dynamic Programming (DP)** to find the optimal connections that light up the maximum number of LEDs.

## 🚀 Features
- **Dynamic Programming Engine:** Computes the optimal, non-crossing connections with high time and space efficiency.
- **DP Table Visualization:** Generates and prints the step-by-step decision matrix.
- **Interactive UI:** A user-friendly interface to visualize the circuit boards and the connected wires.
- **Test Case Generator:** Easily generates large, random datasets to test performance boundaries.

## 📥 Input & Output Format

### Input
The program reads the number of LEDs ($n$) and their specific permutation on Board $L$:
```text
6      # Number of LEDs
2      # Position of LED 1
6      # Position of LED 2
3      ...
5
4
1
