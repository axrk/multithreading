import java.util.ArrayList;
import java.util.Vector;

public class Main {
    public static final int NUMBER_OF_THREADS = 3;
    public static final int ITERATIONS = 10;
    public static Thread[] threads = new Thread[NUMBER_OF_THREADS];
    public static ArrayList<Student> listMain = new ArrayList<>();

    public static void main(String[] args) {
        // Sequential generation performance
        long sequential_perf = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            sequential_perf += sequentialGeneration(150000);
        }

        sequential_perf /= ITERATIONS;
        System.out.println("Temps d'éxécution séquentielle moyen : " + (sequential_perf) + " millisecondes.\n");


        // Threaded generation performance
        long threaded_perf = 0;

        try {
            for (int i = 0; i < ITERATIONS; i++) {
                threaded_perf += threadedGeneration();
            }

            threaded_perf /= ITERATIONS;
            System.out.println("Temps d'éxécution multithreadé moyen : " + (threaded_perf) + " millisecondes.\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Saving to disk
        try {
            StudentWriter sw = new StudentWriter("resources/student.txt");
            sw.saveList(listMain);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static long sequentialGeneration(int nb){
        StudentGenerator sg_sequential = new StudentGenerator();
        long start_time = System.currentTimeMillis();

        sg_sequential.generate(nb, sg_sequential.globalList);

        long end_time = System.currentTimeMillis();
        long diff = end_time - start_time;

        return diff;
    }

    public static long threadedGeneration() throws InterruptedException {
        StudentGenerator sg_thread = new StudentGenerator();
        long start_time = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_THREADS; i++){
            threads[i] = new Thread(sg_thread);
            threads[i].start();
        }

        for (int i = 0; i < NUMBER_OF_THREADS; i++){
            threads[i].join();
        }

        for (ArrayList<Student> sublist : sg_thread.sublistList){
            sublist.addAll(sg_thread.globalList);
            System.out.println(sg_thread.globalList.size());
        }

        long end_time = System.currentTimeMillis();
        long diff = end_time - start_time;

        copyList(sg_thread.globalList);

        return diff;
    }

    public static void copyList(ArrayList<Student> list) {
        listMain.clear();
        listMain.addAll(list);
    }
}
