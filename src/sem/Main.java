package sem;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;

public class Main {

  private static ArrayList<String> pages =
          new ArrayList<String>() {
            {
              add("1");
            }
          };

  public static void main(String[] args) {

    Semaphore sem = new Semaphore(20);
    CommonResource res = new CommonResource();
    ArrayList<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(new CountThread(res, sem, String.format("CountThread %d", i)));
      t.start();
      threads.add(t);
    }
    for (Thread t : threads) {
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
      out.println(name + " ожидает разрешение");
      sem.acquire();
      out.println(name + " захватывает разрешение");
      res.x = 1;
      for (int i = 1; i < 5; i++) {
        out.println(this.name + ": " + res.x);
        res.x++;
        Thread.sleep(5000);
      }
    } catch (InterruptedException e) {
      out.println(e.getMessage());
    }
    out.println(name + " освобождает разрешение");
    sem.release();
  }
}
