import java.io.*;
import java.net.*;
import java.util.Properties;

public class QuizClient {

    private static final String CONFIG_FILE = "server_info.dat";
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_SERVER_PORT = 1234;

    public static void main(String[] args) {
        // configuration이 있는지 확인하고 있으면 IP, Port 주소 사용, 없으면 default 값 사용하기
        String serverIp = DEFAULT_SERVER_IP;        // 서버 Ip를 default serverIp 설정
        int serverPort = DEFAULT_SERVER_PORT;       // 서버 Port를 default serverPort 설정

        // configuration file 있는지 확인하기
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            // 있으면 서버IP, Port 불러오기
            try (FileReader reader = new FileReader(configFile)) {
                Properties props = new Properties();
                props.load(reader);

                serverIp = props.getProperty("SERVER_IP", DEFAULT_SERVER_IP);
                serverPort = Integer.parseInt(props.getProperty("SERVER_PORT", String.valueOf(DEFAULT_SERVER_PORT)));
            } catch (IOException e) {
                System.out.println("Error reading configuration file. Using default values.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number in configuration file. Using default values.");
            }
        } else {
            System.out.println("Configuration file not found. Using default values.");
        }

        System.out.println("Connecting to server at " + serverIp + ":" + serverPort);

        BufferedReader in = null;
        BufferedReader console = null;
        PrintWriter out = null;
        Socket socket = null;

        // 받아온 server IP, Port를 사용하여 서버에 접속
        try {
            socket = new Socket(serverIp, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            console = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;

            // 서버로 부터 문제를 받기
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);

                // 서버가 답에 대해 응답하고 있는지 확인
                if (serverMessage.contains("Enter your answer")) {
                    System.out.print("Your answer: ");
                    String answer = console.readLine();
                    out.println(answer); // Send answer to server
                }

                // 서버가 문제를 끝냈는지 확인
                if (serverMessage.startsWith("GOODBYE")) {
                    break;
                }
            }

            System.out.println("Connection closed.");

        } catch (UnknownHostException e) {
            System.err.println("Error: Unknown host " + serverIp);
        } catch (IOException e) {
            System.err.println("Error: Unable to connect to the server. Please check your network connection or server settings.");
            e.printStackTrace(); // 추가적인 디버깅을 위한 stack trace 출력
        } finally {
            // 리소스 정리 (socket, BufferedReader, PrintWriter 등)
            try {
                if (in != null) in.close();
                if (console != null) console.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
