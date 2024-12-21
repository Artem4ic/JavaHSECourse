import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static java.rmi.server.LogStream.log;

class Request {
    int pickupFloor;
    int destinationFloor;

    public Request(int pickupFloor, int destinationFloor) {
        this.pickupFloor = pickupFloor;
        this.destinationFloor = destinationFloor;
    }
}

class Elevator implements Runnable {
    private int id;
    private int currentFloor = 0;
    private Queue<Request> requests = new ConcurrentLinkedQueue<>();
    private boolean running = true;
    private JTextArea logArea;
    final int capacity = 5;
    private int currentLoad = 0; // Текущая загрузка
    private JLabel statusLabel;

    public Elevator(int id, JTextArea logArea) {
        this.id = id;
        this.logArea = logArea;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        updateStatus();
    }

    public void addRequest(Request request) {
        requests.offer(request);
    }

    @Override
    public void run() {
        while (running) {
            if (!requests.isEmpty()) {
                Request request = requests.poll();
                if (request != null) {
                    moveToFloor(request.pickupFloor);
                    log("Elevator " + id + " picked up passenger at floor " + request.pickupFloor);
                    currentLoad++;
                    updateStatus();
                    if (currentLoad > capacity) {
                        log("Elevator " + id + " is overloaded! Current load: " + currentLoad);
                        return;
                    }
                    moveToFloor(request.destinationFloor);
                    log("Elevator " + id + " dropping off passenger at floor " + request.destinationFloor);
                    decrementLoad();
                }
            }
        }
    }

    private void moveToFloor(int floor) {
        log("Elevator " + id + " moving from " + currentFloor + " to " + floor);
        currentFloor = floor;
        updateStatus();
        try {
            Thread.sleep(Math.abs(floor - currentFloor) * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void decrementLoad() {
        if (currentLoad > 0) {
            currentLoad--;
            updateStatus();
        }
    }

    void updateStatus() {
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Elevator " + id + ": Floor " + currentFloor + ", Load " + currentLoad));
        }
    }
}

class ElevatorSystem {
    private List<Elevator> elevators = new ArrayList<>();
    private ExecutorService executorService;
    private JTextArea logArea;

    public ElevatorSystem(int elevatorCount, JTextArea logArea) {
        this.logArea = logArea;
        executorService = Executors.newFixedThreadPool(elevatorCount);
        for (int i = 0; i < elevatorCount; i++) {
            Elevator elevator = new Elevator(i, logArea);
            elevators.add(elevator);
            executorService.submit(elevator);
        }
    }
    public void requestElevator(int pickupFloor, int destinationFloor) {
        Request request = new Request(pickupFloor, destinationFloor);
        Elevator bestElevator = findBestElevator(pickupFloor);
        bestElevator.addRequest(request);
        log("Request for elevator at floor " + pickupFloor + " to floor " + destinationFloor);
    }

    private Elevator findBestElevator(int requestedFloor) {
        return elevators.stream()
                .filter(elevator -> elevator.getCurrentLoad() < elevator.capacity)
                .min(Comparator.comparingInt(elevator -> Math.abs(elevator.getCurrentFloor() - requestedFloor)))
                .orElseThrow();
    }

    public void shutdown() {
        executorService.shutdownNow();
        for (Elevator elevator : elevators) {
            elevator.stop();
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}

public class ElevatorControlSystem extends JFrame {
    private ElevatorSystem elevatorSystem;

    public ElevatorControlSystem() {
        setTitle("Elevator Control System");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JTextField pickupFloorInput = new JTextField(5);
        JTextField destinationFloorInput = new JTextField(5);
        JButton callButton = new JButton("Call Elevator");

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pickupFloor;
                int destinationFloor;
                try {
                    pickupFloor = Integer.parseInt(pickupFloorInput.getText());
                    destinationFloor = Integer.parseInt(destinationFloorInput.getText());
                    if (pickupFloor < 0 || pickupFloor > 9 || destinationFloor < 0 || destinationFloor > 9)
                        throw new NumberFormatException();

                    elevatorSystem.requestElevator(pickupFloor, destinationFloor);
                    pickupFloorInput.setText("");
                    destinationFloorInput.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ElevatorControlSystem.this, "Введите корректные этажи (0-9).");
                }
            }
        });

        panel.add(new JLabel("Pickup Floor:"));
        panel.add(pickupFloorInput);
        panel.add(new JLabel("Destination Floor:"));
        panel.add(destinationFloorInput);
        panel.add(callButton);

        JPanel elevatorStatusPanel = new JPanel();
        elevatorStatusPanel.setLayout(new GridLayout(0, 1));

        elevatorSystem = new ElevatorSystem(2, logArea); // Создаем систему с 2 лифтами

        for (Elevator elevator : elevatorSystem.getElevators()) {
            JLabel statusLabel = new JLabel();
            elevatorStatusPanel.add(statusLabel);
            elevator.setStatusLabel(statusLabel);
            elevator.updateStatus();
        }

        add(panel, BorderLayout.SOUTH);
        add(elevatorStatusPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ElevatorControlSystem controlSystem = new ElevatorControlSystem();
            controlSystem.setVisible(true);
        });
    }
}
