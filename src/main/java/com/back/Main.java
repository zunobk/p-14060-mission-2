package com.back;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    static Scanner sc = new Scanner(System.in); // 전역으로 선언

    public static void main(String[] args)
    {
        List<App> applist = new ArrayList<>();
        int id = 0; // 자동 증가

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
                applist.add(new App(id, text, author));

            }
            else if (cmd.equals("목록"))
                printList(applist);
            else if (cmd.startsWith("삭제"))
            {
                // ?를 기준으로 나뉨 [삭제],[id=1]
                String[] parts = cmd.split("\\?");

                String param = parts[1]; // "id=1"
                String[] keyValue = param.split("=");

                int targetId = Integer.parseInt(keyValue[1]);
                boolean deleted = deleteById(applist, targetId);

                if (deleted)
                    System.out.println(targetId + "번 명언이 삭제되었습니다.");
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
                boolean deleted = updateById(applist, targetId);
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

                list.get(i).setText(newText);
                list.get(i).setAuthor(newAuthor);
                System.out.println(id + "번 명언이 수정되었습니다.");
                return true;
            }
        }
        System.out.println(id + "번 명언은 존재하지 않습니다.");
        return false; // 못 찾으면 false 반환
    }


}
class App {
    private int id;
    private String text;
    private String author;

    public App(int id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getAuthor() { return author; }

    public void setText(String text) {
        this.text = text;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}