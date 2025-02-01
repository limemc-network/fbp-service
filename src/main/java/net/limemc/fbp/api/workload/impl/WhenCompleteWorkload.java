package net.limemc.fbp.api.workload.impl;

import lombok.RequiredArgsConstructor;
import net.limemc.fbp.api.workload.Workload;

/**
 * @author TheGaming999
 */
@RequiredArgsConstructor
public class WhenCompleteWorkload implements Workload {

    private final Runnable runnable;

    @Override
    public void compute() {
        runnable.run();
    }
}
