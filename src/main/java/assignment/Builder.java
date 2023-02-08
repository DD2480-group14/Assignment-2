package assignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;


public class Builder {
    private String branchName;
    private String repoURL = "https://github.com/DD2480-group14/Assignment-2";
    private String repoPath = "build";
    private String commitHash;

    public Builder(String branchName, String commitHash) {
        this.branchName = branchName;
        this.commitHash = commitHash;
    }

    public boolean cloneRepo() {
        try {
            // check if repo exists
            File repo = new File(repoPath);
            if (repo.exists() && repo.isDirectory()) {
                deleteDirectory(repo);
            }
            CloneCommand clone =
                Git.cloneRepository().setURI(repoURL).setDirectory(new File(repoPath));
            if (branchName != "main") {
                clone.setBranch(branchName);
            }
            clone.call();
            return true;
        } catch (TransportException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
            return false;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        }



    }

    public void deleteRepo() {
        File repo = new File(repoPath);
        if (repo.exists() && repo.isDirectory()) {
            deleteDirectory(repo);
        }
    }

    public static boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    public boolean deleteLog(String commit) {
        File logDir = new File("log");
        if (!logDir.exists()) {
            return false;
        }
        File logFile = new File(logDir, commit + ".txt");
        if (!logFile.exists()) {
            return false;
        }
        return logFile.delete();
    }

    public boolean build() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(repoPath + "/pom.xml"));
        request.setGoals(java.util.Arrays.asList("clean", "verify"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN-HOME")));
        // create a directory for the log file
        File logDir = new File("log");
        if (!logDir.exists()) {
            logDir.mkdir();
        }
        File logFile = new File(logDir, commitHash + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            request.setOutputHandler(outputLine -> {
                fileWriter.write(outputLine + System.lineSeparator());
            });
            try {
                InvocationResult result = invoker.execute(request);
                if (result.getExitCode() != 0) {
                    fileWriter.write("Build failed.");
                    fileWriter.close();
                    throw new IllegalStateException("Build failed.");
                }
                fileWriter.close();
                return true;
            } catch (Exception e) {
                fileWriter.write(e.getMessage());
                fileWriter.close();
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

    }
}
