package com.back;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {

    private static final String DIR = "db/wiseSaying";
    private static final String LAST_ID_FILE = DIR + "/lastId.txt";

    static Scanner sc = new Scanner(System.in); // 전역으로 선언

    public static void main(String[] args)
    {
        List<App> applist = new ArrayList<>();


        initStorage();
        int id = readLastId();

        loadAllQuotes(applist);

        System.out.println("== 명언 앱 ==");

//        Scanner sc = new Scanner(System.in);
        String text;
        String author;

        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            if (cmd.equals("종료")) {
                break;
            }
            else if (cmd.equals("등록"))
            {
                System.out.print("명언 : ");
                text = sc.nextLine();
                System.out.print("작가 : ");
                author = sc.nextLine();

                // 4단계 완료
                System.out.println(++id+"번 명언이 등록되었습니다.");
                App app = new App(id, text, author);

                applist.add(app);

                saveQuote(app);       // {id}.json 저장/갱신
                writeLastId(id);      // lastId.txt 갱신

            }
            else if (cmd.equals("목록"))
                printList(applist);
            else if (cmd.equals("빌드"))
                buildDataJson(applist);
            else if (cmd.startsWith("삭제"))
            {
                // ?를 기준으로 나뉨 [삭제],[id=1]
                String[] parts = cmd.split("\\?");

                String param = parts[1]; // "id=1"
                String[] keyValue = param.split("=");

                int targetId = Integer.parseInt(keyValue[1]);
                boolean deleted = deleteById(applist, targetId);

                if (deleted)
                {
                    deleteQuoteFile(targetId);
                    System.out.println(targetId + "번 명언이 삭제되었습니다.");
                }
                else    // 명언 삭제 예외처리
                    System.out.println(targetId + "번 명언은 존재하지 않습니다.");

            }
            else if (cmd.startsWith("수정"))
            {
                // ?를 기준으로 나뉨 [수정],[id=1]
                String[] parts = cmd.split("\\?");

                String param = parts[1]; // "id=1"
                String[] keyValue = param.split("=");

                int targetId = Integer.parseInt(keyValue[1]);
                boolean updated  = updateById(applist, targetId);
                if (!updated) {
                    System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                }
            }
        }
        sc.close();
    }

    private static void printList(List<App> applist)
    {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for(int i = applist.size() - 1 ; i>=0; i--)
        {
            App app = applist.get(i);
            System.out.println(app.getId() + " / " + app.getAuthor() + " / " + app.getText());
        }
    }
    // 삭제
    private static boolean deleteById(List<App> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                list.remove(i);
                return true;
            }
        }
        return false; // 못 찾으면 false 반환
    }

    // 수정
    private static boolean updateById(List<App> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                App app = list.get(i);

                System.out.println("명언(기존) : " + app.getText());
                System.out.print("명언 : ");
                String newText = sc.nextLine();

                System.out.println("작가(기존) : " + app.getAuthor());
                System.out.print("작가 : ");
                String newAuthor = sc.nextLine();

                app.setText(newText);
                app.setAuthor(newAuthor);

                saveQuote(app); // 수정 시 파일 덮어쓰기
                System.out.println(id + "번 명언이 수정되었습니다.");
                return true;
            }
        }
        System.out.println(id + "번 명언은 존재하지 않습니다.");
        return false; // 못 찾으면 false 반환
    }

    private static void initStorage() {
        File folder = new File(DIR);
        if (!folder.exists()) folder.mkdirs();

        File lastId = new File(LAST_ID_FILE);
        if (!lastId.exists()) {
            try (FileWriter fw = new FileWriter(lastId)) {
                fw.write("0");
            } catch (IOException e) {
                System.out.println("초기화 오류: " + e.getMessage());
            }
        }
    }

    private static int readLastId() {
        try (Scanner fileScanner = new Scanner(new File(LAST_ID_FILE))) {
            return fileScanner.hasNextInt() ? fileScanner.nextInt() : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    private static void writeLastId(int id) {
        try (FileWriter fw = new FileWriter(LAST_ID_FILE)) {
            fw.write(String.valueOf(id));
        } catch (IOException e) {
            System.out.println("lastId 저장 오류: " + e.getMessage());
        }
    }

    private static void saveQuote(App app) {
        String path = DIR + "/" + app.getId() + ".json";
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("{\n");
            fw.write("  \"id\": " + app.getId() + ",\n");
            fw.write("  \"text\": " + jsonString(app.getText()) + ",\n");
            fw.write("  \"author\": " + jsonString(app.getAuthor()) + "\n");
            fw.write("}");
        } catch (IOException e) {
            System.out.println("명언 저장 오류(id=" + app.getId() + "): " + e.getMessage());
        }
    }

    private static void deleteQuoteFile(int id) {
        File f = new File(DIR + "/" + id + ".json");
        if (f.exists() && !f.delete()) {
            System.out.println("명언 파일 삭제 실패(id=" + id + ")");
        }
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        String esc = s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        return "\"" + esc + "\"";
    }

    // JSON 파일을 읽어서 App 객체로 변환
    private static App loadQuote(int id) {
        String path = DIR + "/" + id + ".json";
        File file = new File(path);

        if (!file.exists()) return null;

        try (Scanner fileScanner = new Scanner(file)) {
            StringBuilder sb = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                sb.append(fileScanner.nextLine());
            }
            String json = sb.toString();

            int quoteId = parseJsonInt(json, "id");
            String text = parseJsonString(json, "text");
            String author = parseJsonString(json, "author");

            return new App(quoteId, text, author);

        } catch (IOException e) {
            System.out.println("명언 로드 오류(id=" + id + "): " + e.getMessage());
            return null;
        }
    }

    // JSON에서 숫자 값 추출
    private static int parseJsonInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search) + search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);

        String value = json.substring(start, end).trim();
        return Integer.parseInt(value);
    }

    // JSON에서 문자열 값 추출
    private static String parseJsonString(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return "";

        start = json.indexOf("\"", start + search.length()) + 1;
        int end = start;

        while (end < json.length()) {
            if (json.charAt(end) == '\"' && json.charAt(end - 1) != '\\') {
                break;
            }
            end++;
        }

        String value = json.substring(start, end);

        // 이스케이프 문자 복원
        value = value.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");

        return value;
    }

    // 모든 명언 파일을 읽어서 applist에 추가
    private static void loadAllQuotes(List<App> applist) {
        File folder = new File(DIR);
        if (!folder.exists()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().endsWith(".json")) { // json 파일만
                String fileName = file.getName();
                String idStr = fileName.substring(0, fileName.lastIndexOf(".json"));

                try {
                    int id = Integer.parseInt(idStr);
                    App app = loadQuote(id);

                    if (app != null) {
                        applist.add(app);
                    }
                } catch (NumberFormatException e) {
                    // lastId.txt 같은 파일 무시
                    continue;
                }
            }
        }
    }

    // data.json 파일 생성
    private static void buildDataJson(List<App> applist) {
        String path = DIR + "/data.json";

        try (FileWriter fw = new FileWriter(path)) {
            fw.write("[\n");

            for (int i = 0; i < applist.size(); i++) {
                App app = applist.get(i);

                fw.write("  {\n");
                fw.write("    \"id\": " + app.getId() + ",\n");
                fw.write("    \"content\": " + jsonString(app.getText()) + ",\n");
                fw.write("    \"author\": " + jsonString(app.getAuthor()) + "\n");
                fw.write("  }");

                if (i < applist.size() - 1) {
                    fw.write(",");
                }
                fw.write("\n");
            }

            fw.write("]");

            System.out.println("data.json 파일의 내용이 갱신되었습니다.");

        } catch (IOException e) {
            System.out.println("data.json 생성 오류: " + e.getMessage());
        }
    }


}