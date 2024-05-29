import java.util.*;

class Process {
    int id;
    int arrival_Time;
    int burst_Time;
    int completion_Time;
    int waiting_Time;
    int turnaround_Time;
    
    Process(int id, int arrival_Time, int burst_Time) {
        this.id = id;
        this.arrival_Time = arrival_Time;
        this.burst_Time = burst_Time;
    }
}

public class FCFS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("프로세스 수를 입력: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        int totalBurstTime = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("프로세스 " + (i + 1) + "의 도착 시간 및 실행 시간 입력:");
            int arrival_Time = scanner.nextInt();
            int burst_Time = scanner.nextInt();
            processes[i] = new Process(i + 1, arrival_Time, burst_Time);
            totalBurstTime += burst_Time;
        }
        performFCFS(processes);
        displayResults(processes, totalBurstTime);
    }

    public static void performFCFS(Process[] processes) {
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrival_Time));
        int current_Time = 0;
        for (Process process : processes) {
            if (process.arrival_Time > current_Time) {
                current_Time = process.arrival_Time;
            }
            process.completion_Time = current_Time + process.burst_Time;
            process.turnaround_Time = process.completion_Time - process.arrival_Time;
            process.waiting_Time = process.turnaround_Time - process.burst_Time;
            current_Time = process.completion_Time;
        }
    }

    public static void displayResults(Process[] processes, int totalBurstTime) {
        System.out.println("프로세스ID\t도착시간\t실행시간\t완료시간\t대기시간\t반환시간");
        for (Process process : processes) {
            System.out.println(process.id + "\t\t" + process.arrival_Time + "\t\t" + process.burst_Time + "\t\t" +
                    process.completion_Time + "\t\t" + process.waiting_Time + "\t\t" + process.turnaround_Time);
        }
        double avgwaiting_Time = Arrays.stream(processes).mapToDouble(p -> p.waiting_Time).average().orElse(0);
        double avgTurnaround_Time = Arrays.stream(processes).mapToDouble(p -> p.turnaround_Time).average().orElse(0);
        int totalExecutionTime = Arrays.stream(processes).mapToInt(p -> p.turnaround_Time).sum();
        double cpuUtilization = ((double) totalBurstTime / totalExecutionTime) * 100;
        
        System.out.println("평균 대기 시간: " + avgwaiting_Time);
        System.out.println("평균 반환 시간: " + avgTurnaround_Time);
        System.out.println("CPU 사용률: " + cpuUtilization + "%");
        System.out.println("총 실행 시간: " + totalBurstTime);
    }
}