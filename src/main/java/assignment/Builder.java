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


/**
 * The builder class is used to clone a repository and build a project using maven.
 */
public class Builder {
    private static final String repoUrl = "https://github.com/DD2480-group14/Assignment-2";
    private static final String repoPath = "build";
    private AtomicReference<String> log;

    /** The build id. */
    public int id;
    /** Status code of the build. */
    public Result status;

    /** Result contains all possible status codes. */
    public enum Result {
        /** Indicates that the build was successful. */
        Success,
        /** Indicates that the build failed at compilation stage. */
        FailCompile,
        /** Indicates that the build failed some tests. */
        FailTest,
        /** Indicates that the build failed some additional checks. */
        FailVerify
    }

    /**
     * Clones the repository, checkouts the commit with commitHash, and builds it.
     *
     * <p>
     * The build is done in steps to provide information about the stage at which fail occurs, if
     * there is a fail.
     *
     * <p>
     * The results are saved in builds.json, for each build it contains a link to the origin commit,
     * status code and full build log of last stage.
     *
     * <p>
     * Variables id and status are initialized.
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
