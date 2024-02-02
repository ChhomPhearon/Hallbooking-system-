import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HallBookingSystem {
    // Constants
    static int rows;
    static int columns;
    static int[][] morningHall;
    static int[][] afternoonHall;
    static int[][] eveningHall;
    private static List<String> bookingHistory = new ArrayList<>();
    private static LocalDate bookingDate;

    public static int[][] createArray2D(int rows, int columns) {
        return new int[rows][columns];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        createHall(scanner);
        char choice;
        do {
            System.out.println("===========  HALL BOOKING SYSTEM MENU ================");
            System.out.println("=          <> A. Booking                             =");
            System.out.println("=          <> B. Check available Hall                =");
            System.out.println("=          <> C. Show time                           =");
            System.out.println("=          <> D. History                             =");
            System.out.println("=          <> E. Hall Rebooting                      =");
            System.out.println("=          <> F. Exit                                =");
            System.out.println("======================================================");
            System.out.print("Please enter Menu No: ");
            choice = scanner.next().toUpperCase().charAt(0);
            switch (choice) {
                case 'A' -> bookHall(scanner);
                case 'B' -> showmall();
                case 'C' -> showtime();
                case 'E' -> rebootHistory();
                case 'D' -> showBookingHistory();
                case 'F' -> {
                    System.out.println("Exiting Hall Booking System. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 'F');
    }

    private static void bookHall(Scanner scanner) {
        System.out.println("===========    START BOOKING    ==========\n");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.next();

        boolean validChoice = false;
        char showtimeChoice = '\0';
        int[][] selectedHall = null;

        while (!validChoice) {
            System.out.println("===========( Select a Showtime) ==========\n");
            System.out.println(">>>>>    A. Morning (10:00AM - 12:30PM)        ");
            System.out.println(">>>>>    B. Afternoon (3:00PM - 5:30PM)        ");
            System.out.println(">>>>>    C. Evening   (7:00PM - 9:30PM)        ");
            System.out.println(">>>>>    Please choose the showtime you want to book (A/B/C) below: ");

            String input = scanner.next().toUpperCase();

            if (input.length() == 1 && (input.charAt(0) == 'A' || input.charAt(0) == 'B' || input.charAt(0) == 'C')) {
                showtimeChoice = input.charAt(0);
                validChoice = true;

                switch (showtimeChoice) {
                    case 'A' -> selectedHall = morningHall;
                    case 'B' -> selectedHall = afternoonHall;
                    case 'C' -> selectedHall = eveningHall;
                }
            } else {
                System.out.println("Invalid input. Please choose A, B, or C.");
            }
        }

        System.out.println("____Hall Details____ :");

        createArray2D(selectedHall);

        System.out.print("Enter the seats separated by a comma (Example: A-1,B-2): ");
        String seatsInput = scanner.next().toUpperCase();

        // Split the input into individual seat choices
        String[] seatChoices = seatsInput.split(",");

        boolean allSeatsValid = true;
        for (String seatChoice : seatChoices) {
            if (!isValidSeatFormat(seatChoice)) {
                System.out.println("Invalid seat format. Booking canceled.");
                allSeatsValid = false;
                break;
            }
        }

        if (allSeatsValid) {
            System.out.print("Are you sure you want to book these seats? (y/n): ");
            char confirmationChoice = scanner.next().toUpperCase().charAt(0);

            if (confirmationChoice == 'Y') {
                for (String seatChoice : seatChoices) {
                    char rowChoice = (char) (seatChoice.charAt(0) - 'A');
                    int columnChoice = Integer.parseInt(seatChoice.substring(2)) - 1;

                    // Check if the seat is available
                    if (selectedHall[rowChoice][columnChoice] == 0) {
                        selectedHall[rowChoice][columnChoice] = 1; // Booked

                        LocalTime bookingTime = LocalTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String formattedTime = bookingTime.format(formatter);
                        bookingDate = LocalDate.now();

                        // Add booking to history
                        addBookingToHistory(seatChoice, studentId,formattedTime, bookingDate);
                    } else {
                        System.out.println("Seat " + seatChoice + " is already booked. Please choose another seat.");
                    }
                }
                System.out.println("Congratulation!!!!!!!!!!!!!!!!!!");
                System.out.println("Booking successful for all seats!");
                System.out.println(":) ");
            } else {
                System.out.println("Booking canceled by user.");
            }
        }
    }
    private static void showBookingHistory() {
        System.out.println("======= BOOKING HISTORY =======\n");
        if (bookingHistory.isEmpty()) {
            System.out.println(" >>>No booking history available<<< ");
        } else {
            for (String bookingInfo : bookingHistory) {
                System.out.println(bookingInfo);
            }
        }
        System.out.println("");
        System.out.println("______________________________");
    }

    private static void addBookingToHistory(String seatChoice, String studentId, String formattedTime, LocalDate bookingDate) {
        String bookingInfo = "Your Seat: " + seatChoice + ", Student ID: " + studentId +
                ", Date: " + bookingDate + ", Time: " + formattedTime;
        bookingHistory.add(bookingInfo);
    }

    private static boolean isValidSeatFormat(String seatChoice) {
        return seatChoice.matches("[A-Z]-[1-9][0-9]*");
    }

    private static void createHall(Scanner scanner) {
        System.out.println("=====================================================================================");
        System.out.println(">                           WELCOME TO PRO CINEPLEX                                  <");
        System.out.println("=====================================================================================");
        boolean validInput = false;

        do {
            System.out.print("=> Please config total rows in your hall: ");
            if (scanner.hasNextInt()) {
                rows = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println("=> Invalid input. Please enter a valid numeric value for rows.");
                scanner.next();
            }
        } while (!validInput);

        validInput = false;

        do {
            System.out.print("=> Please config total seats per in your hall: ");
            if (scanner.hasNextInt()) {
                columns = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println("=>  Invalid input. Please enter value as number: .");
                scanner.next();
            }
        } while (!validInput);


        morningHall = createArray2D(rows, columns);
        afternoonHall = createArray2D(rows, columns);
        eveningHall = createArray2D(rows, columns);
    }
    private static void rebootHistory() {
        bookingHistory.clear();
        System.out.println("Booking history rebooted.");
    }


    private static void showtime() {
        System.out.println("=========== SHOWTIMES ==============");
        System.out.println(" # A. Morning   (10:00AM - 12:30PM)");
        System.out.println(" # B. Afternoon (3:00 PM - 5:30PM)");
        System.out.println(" # C. Evening   (7:00PM - 9:30PM)");
        System.out.println("====================================");
    }

    private static void showmall() {
        System.out.println("=========== ( About Our Hall ) =========");
        System.out.println("_______________________________________");
        System.out.println("=========== ( Morning Hall ) ==========");

        createArray2D(morningHall);
        System.out.println("=========== ( Afternoon Hall )=========");

        createArray2D(afternoonHall);
        System.out.println("=========== ( Evening  Hall ) =========");

        createArray2D(eveningHall);
    }

    public static void createArray2D(int[][] hall) {
        char rowChar = 'A';
        for (int i = 0; i < hall.length; i++) {
            int columnValue = 1;
            for (int j = 0; j < hall[i].length; j++) {
                String status = hall[i][j] == 0 ? "AV" : "BO";
                System.out.print("[" + rowChar + "-" + columnValue + "::" + status + "]");
                columnValue++;
            }
            System.out.println();
            rowChar++;
        }
    }
}
