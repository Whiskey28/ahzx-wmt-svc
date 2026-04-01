# WMT Stage Checklist

Use with `wmt-fullstack-pipeline-orchestrator` (any profile).

## Global

- [ ] Current stage id is explicit.
- [ ] All required inputs exist.
- [ ] All stage outputs written.
- [ ] Profile gate run when the stage requires it.
- [ ] Next stage can read outputs without extra context.

## Artifacts By Stage

- discovery: `problem-statement.md`, `success-metrics.md`, `scope.md`
- planning: `backlog.json`, `milestones.md`, `risks.md`
- architecture: `architecture.md`, `tech-stack-decision.md`, `repo-layout.md`
- data_contracts: `erd.md`, `schema.sql`, `openapi.yaml`
- backend: `implementation-notes.md`, `artifacts/backend-test-report.txt`
- frontend: `ui-spec.md`, `artifacts/frontend-test-report.txt`
- qa: `test-plan.md`, `test-cases.md`, `artifacts/e2e-report.txt`
- deploy: `Dockerfile`, `docker-compose.yml`, `runbook.md`, `rollback.md`
- ops: `monitoring.md`, `alerts.md`, `slo-sli.md`, `incident-template.md`
