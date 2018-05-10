package cn.thinker.wechatmomentsdemo.common;

public class LogLevel {
    public static final int FINEST = 0;
    public static final int DEBUG = 1;

    public static boolean isLoggerable (int level) {
        return level <= sLevel;
    }

    private static int sLevel = DEBUG;

    public static void setLogLevel(int level) {
        sLevel = level;
    }
}
