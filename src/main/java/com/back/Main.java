package com.back;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args)
    {
        List<App> applist = new ArrayList<>();
        int id = 0; // 자동 증가

        System.out.println("== 명언 앱 ==");

        Scanner sc = new Scanner(System.in);
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
}