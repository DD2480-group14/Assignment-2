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


    /**
     * The builder class is used to clone a repository and build a project using maven. The class
     * uses the branchName and commitHash to decide which branch and commit to clone and build. The
     * output of the build is written to a log file in the cloned repository folder.
     * 
     * @param branchName name of the branch to be built
     * @param commitHash hash of the commit to be built
     */
    public Builder(String branchName, String commitHash) {
        this.branchName = branchName;
        this.commitHash = commitHash;
    }

    /**
     * Clones the repository to the directory location build, the function uses the branchName and
     * commitHash to decide which branch and commit to clone
     * 
     * @return true if the clone was successful, false otherwise
     */
    public boolean cloneRepo() {
        try {
            CloneCommand clone = Git.cloneRepository().setURI(repoURL)
                                    .setDirectory(new File(repoPath + "/" + commitHash));
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

    /**
     * Builds the project specified in repoURL, branchName and commitHash using maven. The function
     * writes the output to a log file in the cloned repository folder
     * 
     * @return true if the build was successful, false otherwise
     */
    public boolean buildRepo() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(repoPath + "/" + commitHash + "/pom.xml"));
        request.setGoals(java.util.Arrays.asList("clean", "verify"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        File logFile = new File(repoPath + "/" + commitHash + "/build.log");
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            request.setOutputHandler(outputLine -> {
                fileWriter.write(outputLine + System.lineSeparator());
            });
            try {
                InvocationResult result = invoker.execute(request);
                fileWriter.close();
                if (result.getExitCode() != 0) {
                    throw new IllegalStateException("Build failed.");
                }
                return true;
            } catch (Exception e) {
                fileWriter.close();
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
