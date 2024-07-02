package net.local.poc.orderorch.application.workflow.api;

import java.util.List;

public interface Workflow {
    List<WorkflowStep> getSteps();
}
