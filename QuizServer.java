import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 1235;

    // 퀴즈의 문제, 답 설정
    private static final List<String[]> QUESTIONS = Arrays.asList(
        new String[]{"What is this lecture name?", "a) Computer and network", "b) Programming", "c) Algorithm", "d) Database", "a"},
        new String[]{"What is the currency of South Korea?", "a) Yen", "b) Dollar", "c) Won", "d) Euro", "c"},
        new String[]{"What is the capital of France?", "a) Paris", "b) Seoul", "c) Beijing", "d) New Delhi", "a"}
    );

    public static void main(String[] args) {
        System.out.println("Quiz Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    // Client 접속 승낙
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // Client 예외 처리
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) { //예외처리
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    // Client의 요청이 있는 경우, Client와 연결을 생성하고 요청 처리
    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Welcome to the Quiz Game!");

            int score = 0; // 점수 초기화

            for (int i = 0; i < QUESTIONS.size(); i++) {
                try {
                    // 문제 출제
                    String[] question = QUESTIONS.get(i);
                    out.println("Q" + (i + 1) + ": " + question[0]);
                    for (int j = 1; j <= 4; j++) {
                        out.println(question[j]);
                    }
                    out.println("Enter your answer (a/b/c/d): ");

                    // 즉각적인 피드백
                    String answer = in.readLine().trim().toLowerCase();
                    if (answer.isEmpty() || (!answer.equals("a") && !answer.equals("b") && !answer.equals("c") && !answer.equals("d"))) {
                        out.println("Invalid input! Please enter one of (a/b/c/d).\n");
                        i--; // 잘못된 입력 시 같은 질문을 다시
                        continue;
                    }

                    if (answer.equals(question[5])) {
                        score++; // 점수 +1
                        out.println("Correct!\n");
                    } else {
                        out.println("Incorrect! The correct answer was: " + question[5] + "\n");
                    }
                } catch (NullPointerException e) {
                    System.err.println("Error reading input: " + e.getMessage());
                    out.println("Error reading your answer. Skipping this question.\n");
                }
            }

            // 최종 점수 알려주고 연결 끊기
            out.println("\nYour final score is: " + score + "/" + QUESTIONS.size());
            out.println("Thanks for playing!");

        } catch (IOException e) {
            System.err.println("Error communicating with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
