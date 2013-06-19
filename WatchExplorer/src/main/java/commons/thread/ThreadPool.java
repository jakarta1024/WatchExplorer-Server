package commons.thread;

/**
 * Created with IntelliJ IDEA.
 * User: Kipp Li
 * Date: 6/19/13
 * Time: 11:42 PM
 *
 */

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池
 * 需要在独立的线程中进行的操作可以直接add runnable到该pool
 * @author ruikunh
 *
 */
public class ThreadPool {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    private Map<String, Schedule> schedule_map = new ConcurrentHashMap<String, Schedule>();
    private final ExecutorService pool;
    private final ExecutorService schedule_pool;
    private boolean server_running = false;

    private static ThreadPool instance = new ThreadPool();

    public static ThreadPool getInstance(){
        return instance;
    }

    private ThreadPool(){
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        schedule_pool = Executors.newScheduledThreadPool(1);
    }

    public void init(){
        server_running = true;
        ((ScheduledExecutorService) schedule_pool).scheduleAtFixedRate(new MyTimer(), 20, 20, TimeUnit.MINUTES);
        new Thread(){
            public void run(){
                System.out.println("ThreadPool init ...");
                exec();
                init();
            }
        }.start();
    }

    public void destroy(){
        server_running = false;
        ((ScheduledExecutorService) schedule_pool).shutdown();
    }

    public boolean offer(Runnable obj){
        return queue.offer(obj);
    }

    public void schedule(String key, Schedule s){
        schedule_map.put(key, s);
    }

    public void cancelSchedule(String key){
        schedule_map.remove(key);
    }

    private void exec(){
        while(true){
            try{
                if(!server_running){
                    break;
                }
                Runnable obj = queue.poll(5, TimeUnit.SECONDS);
                if(obj!=null){
                    pool.execute(obj);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyTimer implements Runnable {

        @Override
        public void run() {
            System.out.println("MyTimer is running...");
            for(String key : schedule_map.keySet()){
                try{
                    Schedule s = schedule_map.get(key);
                    s.run();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}

