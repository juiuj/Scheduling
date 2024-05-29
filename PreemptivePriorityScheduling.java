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

public class PreemptivePriorityScheduling {
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
        performPriorityScheduling(processes);
        displayResults(processes, totalBurstTime);
    }

    public static void performPriorityScheduling(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int n = processes.length;
        Process currentProcess = null;
        boolean[] isProcessCompleted = new boolean[n];
        Arrays.fill(isProcessCompleted, false);

        while (completedProcesses < n) {
            int highestPriorityIndex = -1;
            for (int i = 0; i < n; i++) {
                if (processes[i].arrival_Time <= currentTime && !isProcessCompleted[i]) {
                    if (highestPriorityIndex == -1 || processes[i].priority < processes[highestPriorityIndex].priority) {
                        highestPriorityIndex = i;
                    }
                }
            }
            if (highestPriorityIndex != -1) {
                if (currentProcess == null || processes[highestPriorityIndex].priority < currentProcess.priority) {
                    if (currentProcess != null && currentProcess != processes[highestPriorityIndex]) {
                        System.out.println("Time " + currentTime + ": Process " + currentProcess.id + " is preempted by Process " + processes[highestPriorityIndex].id);
                    }
                    currentProcess = processes[highestPriorityIndex];
                }
                currentProcess.remaining_Time--;
                currentTime++;
                if (currentProcess.remaining_Time == 0) {
                    currentProcess.completion_Time = currentTime;
                    currentProcess.turnaround_Time = currentProcess.completion_Time - currentProcess.arrival_Time;
                    currentProcess.waiting_Time = currentProcess.turnaround_Time - currentProcess.burst_Time;
                    isProcessCompleted[currentProcess.id - 1] = true;
                    completedProcesses++;
                    System.out.println("Time " + currentTime + ": Process " + currentProcess.id + " is completed.");
                    currentProcess = null;
                }
            } else {
                currentTime++;
            }
        }
    }

    public static void displayResults(Process[] processes, int totalBurstTime) {
        System.out.println("프로세스ID\t도착시간\t실행시간\t우선순위\t완료시간\t대기시간\t반환시간");
        for (Process process : processes) {
            System.out.println(process.id + "\t\t" + process.arrivalTime + "\t\t" + process.burstTime + "\t\t" +
                    process.priority + "\t\t" + process.completionTime + "\t\t" + process.waitingTime + "\t\t" + process.turnaroundTime);
        }
        double avgWaitingTime = Arrays.stream(processes).mapToDouble(p -> p.waitingTime).average().orElse(0);
        double avgTurnaroundTime = Arrays.stream(processes).mapToDouble(p -> p.turnaroundTime).average().orElse(0);
        int totalExecutionTime = Arrays.stream(processes).mapToInt(p -> p.turnaroundTime).sum();
        double cpuUtilization = ((double) totalBurstTime / totalExecutionTime) * 100;

        System.out.println("평균 대기 시간: " + avgWaitingTime);
        System.out.println("평균 반환 시간: " + avgTurnaroundTime);
        System.out.println("CPU 사용률: " + cpuUtilization + "%");
        System.out.println("총 실행 시간: " + totalBurstTime);
    
    }
}






