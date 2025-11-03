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

                System.out.println(++id+"번 명언이 등록되었습니다.");
                applist.add(new App(text, author));

            }
        }

        sc.close();
    }
}

class App {
    private String text;
    private String author;

    public App(String text, String author) {
        this.text = text;
        this.author = author;
    }
}