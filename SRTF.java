import java.util.*;

class Process {
    int id;
    int arrival_Time;
    int burst_Time;
    int remainingBurst_Time;
    int completion_Time;
    int waiting_Time;
    int turnaround_Time;
    boolean executed;

    Process(int id, int arrival_Time, int burst_Time) {
        this.id = id;
        this.arrival_Time = arrival_Time;
        this.burst_Time = burst_Time;
        this.remainingBurst_Time = burst_Time;
        this.executed = false;
    }
}

public class SRTF {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("프로세스 수 입력: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("프로세스 " + (i + 1) + "의 도착 시간 및 실행 시간 입력:");
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }
        int totalBurstTime = performSRTF(processes);
        displayResults(processes, totalBurstTime);
    }

    public static int performSRTF(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int totalBurstTime = 0;

        for (Process process : processes) {
            totalBurstTime += process.burst_Time;
        }
        while (completedProcesses < processes.length) {
            int shortestBurstTime = Integer.MAX_VALUE;
            int shortestIndex = -1;
            for (int i = 0; i < processes.length; i++) {
                if (!processes[i].executed && processes[i].arrival_Time <= currentTime && processes[i].remainingBurst_Time < shortestBurstTime) {
                    shortestBurstTime = processes[i].remainingBurst_Time;
                    shortestIndex = i;
                }
            }
            if (shortestIndex == -1) {
                currentTime++;
            } else {
                Process shortestProcess = processes[shortestIndex];
                shortestProcess.remainingBurst_Time--; 
                currentTime++;
                if (shortestProcess.remainingBurst_Time == 0) {
                    shortestProcess.completion_Time = currentTime;
                    shortestProcess.turnaround_Time = shortestProcess.completion_Time - shortestProcess.arrival_Time;
                    shortestProcess.waiting_Time = shortestProcess.turnaround_Time - shortestProcess.burst_Time;
                    shortestProcess.executed = true;
                    completedProcesses++;
                }
            }
        }
        return totalBurstTime;
    }

    public static void displayResults(Process[] processes, int totalBurstTime) {
        System.out.println("프로세스ID\t도착시간\t실행시간\t완료시간\t대기시간\t반환시간");

        for (Process process : processes) {
            System.out.println(process.id + "\t\t" + process.arrival_Time + "\t\t" + process.burst_Time + "\t\t" +
                    process.completion_Time + "\t\t" + process.waiting_Time + "\t\t" + process.turnaround_Time);
        }
        double avgWaitingTime = Arrays.stream(processes).mapToDouble(p -> p.waiting_Time).average().orElse(0); 
        double avgTurnaroundTime = Arrays.stream(processes).mapToDouble(p -> p.turnaround_Time).average().orElse(0);
        int totalExecutionTime = Arrays.stream(processes).mapToInt(p -> p.turnaround_Time).sum();
        double cpuUtilization = ((double)totalBurstTime /totalExecutionTime) * 100;
        
        System.out.println("평균 대기 시간: " + avgWaitingTime);
        System.out.println("평균 반환 시간: " + avgTurnaroundTime);
        System.out.println("CPU 사용률: " + cpuUtilization + "%");
        System.out.println("총 실행 시간: " + totalBurstTime);
    }
}
