package sem;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {

  private static ArrayList<String> pages =
      new ArrayList<String>() {
        {
          add("1");
        }
      };

  public static void main(String[] args) {

    Semaphore sem = new Semaphore(5);
    CommonResource res = new CommonResource();
    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(new CountThread(res, sem, String.format("CountThread %d", i)));
      t.start();
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class CommonResource {

  int x = 0;
}

class CountThread implements Runnable {

  CommonResource res;
  Semaphore sem;
  String name;

  CountThread(CommonResource res, Semaphore sem, String name) {
    this.res = res;
    this.sem = sem;
    this.name = name;
  }

  public void run() {

    try {
      System.out.println(name + " ожидает разрешение");
      sem.acquire();
      res.x = 1;
      for (int i = 1; i < 3; i++) {
        System.out.println(this.name + ": " + res.x);
        res.x++;
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
    System.out.println(name + " освобождает разрешение");
    sem.release();
  }
}
