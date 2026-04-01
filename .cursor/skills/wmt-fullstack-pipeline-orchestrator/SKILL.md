---
name: wmt-fullstack-pipeline-orchestrator
description: WMT-owned orchestration skill for staged fullstack delivery (discovery through ops) with explicit artifact handoffs and quality gates. Use when standardizing web project workflows, chaining skills in order, or requiring stage outputs as next-stage inputs; pair with wmt-fullstack-pipeline-orchestrator-springboot or wmt-fullstack-pipeline-orchestrator-node for stack-specific gates.
---

# WMT Fullstack Pipeline Orchestrator

## Purpose

Coordinate other skills in strict stage order. Each stage produces files consumed by the next. This skill is **WMT-owned** (prefix `wmt-`).

## Source Of Truth

- Pipeline: `templates/wmt-fullstack-skill-pipeline/pipeline.yaml`
- Stage prompt hints: `templates/wmt-fullstack-skill-pipeline/scripts/run-stage.md`
- Artifact seeds: `templates/wmt-fullstack-skill-pipeline/stages/artifact-templates.md`

**Gates (pick one profile skill):**

- Spring Boot: `templates/wmt-fullstack-skill-pipeline/profiles/springboot/check-gates.ps1`
- Node: `templates/wmt-fullstack-skill-pipeline/profiles/node/check-gates.ps1`

If the project copied the template to `.delivery-pipeline`, use paths relative to that copy.

## Stack-Specific Entry

- Java / Spring Boot monorepo: use **`wmt-fullstack-pipeline-orchestrator-springboot`** for defaults and gate commands.
- Node backend + React: use **`wmt-fullstack-pipeline-orchestrator-node`**.

## Operating Rules

1. Do not skip stages unless user explicitly requests it.
2. Read all declared inputs for the current stage before writing.
3. Write only the current stage outputs.
4. Run the appropriate profile `check-gates.ps1` after backend, frontend, qa, and deploy stages as applicable.
5. On gate failure: fix, rerun gate; do not advance.
6. Prefer machine-readable artifacts (`json`, `yaml`, structured markdown).

## Stage Sequence And Skill Routing

Same nine stages as `pipeline.yaml`: discovery, planning, architecture, data_contracts, backend, frontend, qa, deploy, ops.

Recommended non-WMT skills per stage (examples):

| Stage | Skills |
| ----- | ------ |
| discovery | `superpowers/brainstorming`, `superpowers/writing-plans` |
| planning | `superpowers/writing-plans`, `superpowers/executing-plans` |
| architecture | `docker-expert`, `code-refactoring` |
| data_contracts | `supabase-postgres-best-practices` (PostgreSQL) |
| backend | `backend-testing`, `superpowers/test-driven-development`, `superpowers/verification-before-completion` |
| frontend | `frontend-design`, `ui-ux-pro-max`, `superpowers/verification-before-completion` |
| qa | `backend-testing`, `playwright-best-practices`, `superpowers/verification-before-completion` |
| deploy | `docker-expert`, `multi-stage-dockerfile` |
| ops | `work-retrospectives`, `superpowers/systematic-debugging` |

## Response Contract

When active, answer in this order:

1. Stage goal  
2. Inputs verified  
3. Skills invoked (WMT + external)  
4. Outputs written  
5. Gate result (pass/fail + how verified)  
6. Next stage readiness  

## Project Bootstrap

1. Copy `templates/wmt-fullstack-skill-pipeline` to `.delivery-pipeline` (or project root folder name of choice).  
2. Create `00-intake` … `09-ops` and `artifacts/`.  
3. Seed from `stages/artifact-templates.md`.  
4. Start at **discovery**.  

See also [stage-checklist.md](stage-checklist.md).
