package net.limemc.fbp.api.workload;

/**
 * <a href="https://www.spigotmc.org/threads/guide-on-workload-distribution-or-how-to-handle-heavy-splittable-tasks.409003/">...</a>
 */
public interface Workload {
    void compute();
}
