import java.util.*;

class Process {
    int id;
    int arrival_Time;
    int burst_Time;
    int priority;
    int remaining_Time;
    int completion_Time;
    int waiting_Time;
    int turnaround_Time;

    Process(int id, int arrival_Time, int burst_Time, int priority) {
        this.id = id;
        this.arrival_Time = arrival_Time;
        this.burst_Time = burst_Time;
        this.priority = priority;
        this.remaining_Time = burst_Time;
    }
}

public class DynamicAgingScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("프로세스 수 입력: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        int totalBurstTime = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("프로세스 " + (i + 1) + "의 도착 시간, 실행 시간 및 우선순위 입력:");
            int arrival_Time = scanner.nextInt();
            int burst_Time = scanner.nextInt();
            int priority = scanner.nextInt();
            processes[i] = new Process(i + 1, arrival_Time, burst_Time, priority);
            totalBurstTime += burst_Time;
        }
        performDynamicAging(processes);
        displayResults(processes, totalBurstTime);
    }

    public static void performDynamicAging(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int n = processes.length;
        Process currentProcess = null;
        boolean[] isProcessCompleted = new boolean[n];
        Arrays.fill(isProcessCompleted, false);

        Queue<Process> queue = new LinkedList<>();

        while (completedProcesses < n) {
            for (Process process : processes) {
                if (process.arrival_Time == currentTime && !isProcessCompleted[process.id - 1]) {
                    queue.add(process);
                }
            }
            Process highestPriorityProcess = null;
            for (Process process : queue) {
                if (highestPriorityProcess == null || process.priority < highestPriorityProcess.priority) {
                    highestPriorityProcess = process;
                }
            }
            if (currentProcess != null && highestPriorityProcess != null && highestPriorityProcess.priority < currentProcess.priority) {
                queue.add(currentProcess);
                currentProcess = null;
            }
            if (currentProcess == null && highestPriorityProcess != null) {
                currentProcess = highestPriorityProcess;
                queue.remove(currentProcess);
            }
            if (currentProcess != null) {
                currentProcess.remaining_Time--;
                System.out.println("Time " + currentTime + ": Process " + currentProcess.id + " is executing.");

                if (currentProcess.remaining_Time == 0) {
                    currentProcess.completion_Time = currentTime + 1;
                    currentProcess.turnaround_Time = currentProcess.completion_Time - currentProcess.arrival_Time;
                    currentProcess.waiting_Time = currentProcess.turnaround_Time - currentProcess.burst_Time;
                    isProcessCompleted[currentProcess.id - 1] = true;
                    completedProcesses++;
                    System.out.println("Time " + (currentTime + 1) + ": Process " + currentProcess.id + " is completed.");
                    currentProcess = null;
                }
            }
            for (Process process : queue) {
                process.priority--;
            }

            currentTime++;
        }
    }

    public static void displayResults(Process[] processes, int totalBurstTime) {
        System.out.println("프로세스ID\t도착시간\t실행시간\t우선순위\t완료시간\t대기시간\t반환시간");
        for (Process process : processes) {
            System.out.println(process.id + "\t\t" + process.arrival_Time + "\t\t" + process.burst_Time + "\t\t" +
                    process.priority + "\t\t" + process.completion_Time + "\t\t" + process.waiting_Time + "\t\t" + process.turnaround_Time);
        }
        double avgWaitingTime = Arrays.stream(processes).mapToDouble(p -> p.waiting_Time).average().orElse(0);
        double avgTurnaroundTime = Arrays.stream(processes).mapToDouble(p -> p.turnaround_Time).average().orElse(0);
        int totalExecutionTime = Arrays.stream(processes).mapToInt(p -> p.turnaround_Time).sum();
        double cpuUtilization = ((double) totalBurstTime / totalExecutionTime) * 100;

        System.out.println("평균 대기 시간: " + avgWaitingTime);
        System.out.println("평균 반환 시간: " + avgTurnaroundTime);
        System.out.println("CPU 사용률: " + cpuUtilization + "%");
        System.out.println("총 실행 시간: " + totalBurstTime);
    }
}