## Learned User Preferences

- Prefers curated checklists with official or reputable blog links for deep topics, rather than long in-chat explanations of fundamentals.
- When using official references (for example Spring Boot Reference), prefers narrow, goal-driven study: align documentation version to the project, read only sections tied to a concrete question, then verify behavior in this repository.
- Avoid creating new Markdown files in the repository unless explicitly requested; learning artifacts under `docs/learning-roadmap/` are written only when the user asks to persist them.

## Learned Workspace Facts

- Backend is a Maven multi-module Java 17 codebase using Spring Boot 3.5.x with the unified BOM `wmt-dependencies-jdk17`; the runnable application module is `wmt-server`, and shared framework code lives in the WMT `wmt-framework-jdk17` stack.
- Long-form learning notes and roadmap documents for this project are kept under `docs/learning-roadmap/`.
- The credit-report admin frontend is maintained in a separate repository (`CreditService_Report_Web`); typical deployment exposes `/CreditServiceReport/` and `/admin-api/` via Nginx for browser-to-backend calls.
