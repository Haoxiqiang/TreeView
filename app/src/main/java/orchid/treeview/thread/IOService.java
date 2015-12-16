package orchid.treeview.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by haoxiqiang on 15/12/4.
 */
public final class IOService {

    private static volatile ExecutorService INSTANCE;

    private IOService() {

    }

    public static ExecutorService getInstance() {
        if (INSTANCE == null) {
            synchronized (IOService.class) {
                if (INSTANCE == null) {
                    INSTANCE = Executors.newCachedThreadPool();
                }
            }
        }
        return INSTANCE;
    }
}
