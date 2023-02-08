package assignment;

import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.eclipse.jgit.api.Git;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.commons.io.FileUtils;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONObject;
import org.json.JSONArray;


public class Builder {
    private static final String repoUrl = "https://github.com/DD2480-group14/Assignment-2";
    private static final String repoPath = "build";
    private AtomicReference<String> log;

    int id;
    Result status;

    enum Result {
        Success, FailCompile, FailTest, FailVerify
    }

    /**
     * The builder class is used to clone a repository and build a project using maven. The class
     * uses the branchName and commitHash to decide which branch and commit to clone and build. The
     * output of the build is written to a log file in the cloned repository folder.
     *
     * @param commitHash hash of the commit to be built
     */
    public Builder(String commitHash) throws Exception {
        clone(commitHash);
        status = runStages();

        Path buildsFile = Paths.get("builds.json");
        String content = new String(Files.readAllBytes(buildsFile));
        JSONArray json = new JSONArray(content);
        id = json.length();
        JSONObject b = new JSONObject();
        b.put("origin", "https://github.com/DD2480-group14/Assignment-2/commit/" + commitHash);
        b.put("status", status);
        b.put("log", log.get());
        json.put(b);
        Files.write(buildsFile, json.toString().getBytes(), StandardOpenOption.CREATE);
    }

    private synchronized Result runStages() {
        if (!runMaven(java.util.Arrays.asList("clean", "compile"))) {
            return Result.FailCompile;
        }
        if (!runMaven(java.util.Arrays.asList("test"))) {
            return Result.FailTest;
        }
        if (!runMaven(java.util.Arrays.asList("verify"))) {
            return Result.FailVerify;
        }
        return Result.Success;
    }

    /**
     * Clones the repository to the directory location build, the function uses the branchName and
     * commitHash to decide which branch and commit to clone
     */
    private static void clone(String commitHash) throws Exception {
        File repo = new File(repoPath);
        FileUtils.deleteDirectory(repo);
        Git.cloneRepository().setURI(repoUrl).setDirectory(repo).call().checkout()
           .setName(commitHash).call();
    }

    private boolean runMaven(List<String> goals) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(repoPath + "/pom.xml"));
        request.setGoals(goals);
        Invoker invoker = new DefaultInvoker();
        log = new AtomicReference<String>("");
        request.setOutputHandler(outputLine -> {
            log.set(log.get() + outputLine + "\n");
        });
        InvocationResult result;
        try {
            result = invoker.execute(request);
        } catch (MavenInvocationException e) {
            return false;
        }
        if (result.getExitCode() != 0) {
            return false;
        }
        return true;
    }
}
