package com.provinceofmusic.jukebox;

import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleCaptureExample {


    PrintStream originalOut;
    PrintStream originalErr;

    // Create custom output streams to capture console messages
    OutputStreamCapturer outCapturer;
    OutputStreamCapturer errCapturer;
    public void start() {
        // Save the original System.out and System.err streams
         originalOut = System.out;
         originalErr = System.err;

        // Create custom output streams to capture console messages
         outCapturer = new OutputStreamCapturer(originalOut);
         errCapturer = new OutputStreamCapturer(originalErr);

        // Set the custom streams as the new System.out and System.err
        System.setOut(new PrintStream(outCapturer));
        System.setErr(new PrintStream(errCapturer));

        // Your application code here

    }

    public void end(){
        // Restore the original System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);

        // Print captured messages
        System.out.println("Captured System.out:");
        System.out.println(outCapturer.getCapturedOutput());

        System.out.println("Captured System.err:");
        System.out.println(errCapturer.getCapturedOutput());
    }



    static class OutputStreamCapturer extends OutputStream {
        private StringBuilder buffer = new StringBuilder();
        private PrintStream original;

        OutputStreamCapturer(PrintStream original) {
            this.original = original;
        }

        @Override
        public void write(int b) {
            buffer.append((char) b);
            original.write(b);
        }

        String getCapturedOutput() {
            return buffer.toString();
        }
    }
}