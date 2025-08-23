package us.whitedev.helpers;

public final class TimerHelper {
    public long time = System.nanoTime() / 1000000L;
    public long tick;
    public boolean enabling;

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public static boolean hasReached(long millisecounds) {
        long lastMS = 0L;
        return getCurrentMS() - lastMS >= millisecounds;
    }

    public void update() {
        this.time = this.enabling ? ++this.time : --this.time;
        if (this.time < 0L) {
            this.time = 0L;
        }

        if (this.time > (long) this.getMaxTime()) {
            this.time = this.getMaxTime();
        }

    }

    public int getMaxTime() {
        return 10;
    }

    public void on() {
        this.enabling = true;
    }

    public boolean delay(float milliSec) {
        long prevMS = 0L;
        return (float) (this.getTime() - prevMS) >= milliSec;
    }

    public long getTick() {
        return this.tick;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
}
