public class DeadlockDetection {

    public static class Resource {
        private int id;

        public Resource(int id) {
            this.id = id;
        }

        public synchronized void acquire() {
            System.out.println("Resource " + id + " acquired by Thread: " + Thread.currentThread().getName());
        }

        public synchronized void release() {
            System.out.println("Resource " + id + " released by Thread: " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        final Resource resource1 = new Resource(1);
        final Resource resource2 = new Resource(2);

        Thread thread1 = new Thread(() -> {
            resource1.acquire();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource2.acquire();
        });

        Thread thread2 = new Thread(() -> {
            resource2.acquire();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource1.acquire();
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
