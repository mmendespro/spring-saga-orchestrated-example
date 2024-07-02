package net.local.poc.orderorch.application.workflow.flows;

import java.util.List;

import net.local.poc.orderorch.application.workflow.api.Workflow;
import net.local.poc.orderorch.application.workflow.api.WorkflowStep;

public class OrderWorkflow implements Workflow {

    private final List<WorkflowStep> steps;

    public OrderWorkflow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public List<WorkflowStep> getSteps() {
        return this.steps;
    }
}