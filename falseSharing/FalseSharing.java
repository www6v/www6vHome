package test;

public final class FalseSharing
    implements Runnable
{
    public final static int NUM_THREADS = 4; // change
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;
 
    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
    static
    {
        for (int i = 0; i < longs.length; i++)
        {
            longs[i] = new VolatileLong();
        }
    }
 
    public FalseSharing(final int arrayIndex)
    {
        this.arrayIndex = arrayIndex;
    }
 
    public static void main(final String[] args) throws Exception
    {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }
 
    private static void runTest() throws InterruptedException
    {
        Thread[] threads = new Thread[NUM_THREADS];
 
        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(new FalseSharing(i));
        }
 
        for (Thread t : threads)
        {
            t.start();
        }
 
        for (Thread t : threads)
        {
            t.join();
        }
    }
 
    public void run()
    {
        long i = ITERATIONS + 1;
        while (0 != --i)
        {
            longs[arrayIndex].value = i;
        }
    }
 
    public final static class VolatileLong
    {
        public volatile long value = 0L;
        //public long p1, p2, p3, p4, p5, p6;   comment out 
    }
}

// 1. duration = 22344563916  // 有cache line padding
// 2. duration = 22012580114  // 有cache line padding
// 3. duration = 11167549431  // 有cache line padding
// 4. duration = 19736854183  // 有cache line padding

// 1. duration = 23658423881  // comment out
// 2. duration = 33335707670  // comment out
// 3. duration = 35890190024  // comment out
// 4. duration = 29516958769  // comment out
// 5. duration = 35027213671  // comment out

 