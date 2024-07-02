package net.local.poc.orderorch.application.workflow.api;

import reactor.core.publisher.Mono;

public interface WorkflowStep {

    Mono<Boolean> process();
    Mono<Boolean> revert();
    WorkflowStepStatus getStatus();
}
