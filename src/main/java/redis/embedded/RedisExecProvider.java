package redis.embedded;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;
import redis.embedded.util.OsArchitecture;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

public class RedisExecProvider {
    
    private final Map<OsArchitecture, Supplier<File>> executables = Maps.newHashMap();

    public static RedisExecProvider defaultProvider() {
        return new RedisExecProvider();
    }
    
    private RedisExecProvider() {
    }

    public RedisExecProvider override(OS os, Supplier<File> executable) {
        Preconditions.checkNotNull(executable);
        for (Architecture arch : Architecture.values()) {
            override(os, arch, executable);
        }
        return this;
    }

    public RedisExecProvider override(OS os, Architecture arch, Supplier<File> executable) {
        Preconditions.checkNotNull(executable);
        executables.put(new OsArchitecture(os, arch), executable);
        return this;
    }
    
    public File get() throws IOException {
        OsArchitecture osArch = OsArchitecture.detect();
        Supplier<File> executableSupplier = executables.get(osArch);
        File executable;
        if (executableSupplier != null) {
            executable = executableSupplier.get();
            if (executable == null || !executable.exists()) throw new IOException("No redis executable found for " + osArch);
            return executable;
        } else {
            throw new IOException("No executable supplier configured for " + osArch);
        }
    }
}
