package ca.bcit.comp2522.games.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * File watcher implementation for stylesheet hot-reloading during development.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class FileWatcher {

    private static final long TICK_MS = Math.round((1.0 / 60) * 1000);

    private final Map<Path, Long> filesToModified;
    private final Consumer<Path> onChange;
    private boolean stopped;

    /**
     * Creates a new watcher.
     *
     * @param onChange the method that consumes the updated path of a file
     */
    public FileWatcher(final Consumer<Path> onChange) {
        this.filesToModified = new HashMap<>();
        this.onChange = onChange;

        this.stopped = false;

        final Thread thread;
        thread = new Thread(() -> {
            while (!this.stopped) {
                this.watchTick();
                try {
                    Thread.sleep(FileWatcher.TICK_MS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

    /**
     * Sets this watcher to terminate. If it has already terminated, this will have no effect.
     * <p>
     * A watcher cannot be restarted.
     */
    public void stop() {
        this.stopped = true;
    }

    /**
     * Iterates through all watched files, sees if they have been modified, and acts accordingly.
     */
    private void watchTick() {
        for (final Map.Entry<Path, Long> entry : this.filesToModified.entrySet()) {
            final Path path;
            final Long lastModifiedMillis;

            path = entry.getKey();
            lastModifiedMillis = entry.getValue();

            try {
                final long currentModifiedMillis;
                currentModifiedMillis = Files.getLastModifiedTime(path).toMillis();

                if (currentModifiedMillis > lastModifiedMillis) {
                    this.filesToModified.put(path, currentModifiedMillis);
                    this.onChange.accept(path);
                }
            } catch (IOException e) {
                System.out.println("The path \"" + path + "\" caused an exception, removing from watcher. Error:" +
                                           System.lineSeparator() + e);
                this.unwatch(path);
            }
        }
    }

    /**
     * Adds this file so it starts being watched for updates.
     *
     * @param path the file to watch
     */
    public void watch(final Path path) {
        if (this.filesToModified.keySet().stream().anyMatch(path::equals)) {
            return;
        }

        this.filesToModified.put(path, System.currentTimeMillis());
    }

    /**
     * Removes this file from being watched for updates.
     *
     * @param path the file to unwatch
     */
    public void unwatch(final Path path) {
        this.filesToModified.keySet().removeIf(path::equals);
    }

}
