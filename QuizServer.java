import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 1234;

    // 퀴즈의 문제와 답 설정하기
    private static final List<String[]> QUESTIONS = Arrays.asList(
        new String[]{"What is this lecture name?", "a) Computer and network", "b) Programming", "c) Algorithm", "d) Database", "a"},
        new String[]{"What is the currency of South Korea?", "a) Yen", "b) Dollar", "c) Won", "d) Euro", "c"},
        new String[]{"What is the capital of France?", "a) Paris", "b) Seoul", "c) Beijing", "d) New Delhi", "a"}
    );

    public static void main(String[] args) {
        System.out.println("Quiz Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Client 접속 승낙
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Client 요청에 대해 처리
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Client의 요청이 있는 경우 Client와 연결을 생성하고 요청 처리
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // 바이트를 문자 형태를 가지는 객체로
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Welcome to the Quiz Game!");

            int score = 0;      // 점수를 0점으로 설정

            for (int i = 0; i < QUESTIONS.size(); i++) {
                // Client에게 문제 제출
                String[] question = QUESTIONS.get(i);
                out.println("Q" + (i + 1) + ": " + question[0]);
                for (int j = 1; j <= 4; j++) {
                    out.println(question[j]);
                }
                out.println("Enter your answer (a/b/c/d): ");

                // Client에게 답을 받고(소문자로 설정해 대문자로 답을 썻을때 오류 없애기) 맞으면 점수를 1 올리고, 틀리면 피드백 알려주기
                String answer = in.readLine().trim().toLowerCase();
                if (answer.equals(question[5])) {
                    score++;
                    out.println("Correct!\n");
                } else {
                    out.println("Incorrect! The correct answer was: " + question[5] + "\n");
                }
            }

            // 최종점수 알려주고 연결 끊기
            out.println("\nYour final score is: " + score + "/" + QUESTIONS.size());
            out.println("Thanks for playing!");

            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}
