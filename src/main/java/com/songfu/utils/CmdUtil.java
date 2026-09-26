package com.songfu.utils;

import java.io.IOException;
import java.util.List;

public class CmdUtil {

    public static void runCommand(List<String> cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        pb.environment().putIfAbsent("HOME", "/home/songfu");

        Process process = pb.start();

        // 读取输出
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("命令执行失败，exitCode=" + exitCode + " output:\n" + output);
        }
    }
}
