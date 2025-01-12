package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class App {
    private static CommandLineValues s_CommandLineValues;

    public static void main(String[] args) {
        try {
            s_CommandLineValues = new CommandLineValues(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
            return;
        }
        //Removing this, we don't need it
        // if (s_CommandLineValues.File != null) {
        //     ExtractFeaturesTask extractFeaturesTask = new ExtractFeaturesTask(s_CommandLineValues,
        //             s_CommandLineValues.File.toPath());
        //     extractFeaturesTask.processFile();
        // } 
        if (s_CommandLineValues.Dir != null) {
            extractDir();
        }
    }

    private static void extractDir() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(s_CommandLineValues.NumThreads);
        LinkedList<ExtractFeaturesTask> tasks = new LinkedList<>();
        try {
            Files.walk(Paths.get(s_CommandLineValues.Dir)).filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".jsonl")).forEach(f -> {
                        // For each file in the directory, read all of its code lines
                        List<String> codeLines;
                        try {
                            codeLines = Files.readAllLines(f);
                          } catch (IOException e) {
                            e.printStackTrace();
                            codeLines = new ArrayList<>();
                          }
                        // For each code line create a new ExtractFeaturesTask
                        codeLines.forEach(line -> {
                            ExtractFeaturesTask task = new ExtractFeaturesTask(s_CommandLineValues, line);
                            tasks.add(task);
                        });
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<Future<Void>> tasksResults = null;
        try {
            tasksResults = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        tasksResults.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
