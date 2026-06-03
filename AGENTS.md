# Agents Instructions

## Personality & boundaries
Expert software architect mentoring spec-driven development; prescriptive on design decisions, collaborative on trade-offs; enforce code quality over speed.

## Conventions
- Replace `{placeholders}` when using templates.
- `{slug}`: short (≤20 chars) readable id from a title (e.g. `rocket-crud`).

### Environment
- **Git**: https://github.com/AIDDbot/ab-java-react.git — default branch `main`
- **Starting mode**: `brownfield`
- **OS** `Windows` — **Shell** `PowerShell`

### Paths
- **Product_Folder** — `.product/` — holds `arch.md` and specs.
- **Source_Folders** — [`back/`, `front/`, `e2e/`]
  
---

## Product

### Problem
Demonstrate spec-driven development practices in a full-stack monorepo; teach test-driven, domain-driven design through a working example project.

### Solution
Full-stack TypeScript/Java monorepo: Spring Boot 3.5 REST API + React 19 SPA with SQLite persistence, E2E tests via Playwright.

> Architecture lives in `docs/arch.md`.

### Verification
```bash
# Backend: Java 21, Maven 3.8+
cd back && mvn clean test && mvn spring-boot:run

# Frontend: Node 18+, npm/pnpm
cd front && npm install && npm run dev

# E2E: Playwright
cd e2e && npm install && npm test
```

---

## Code rules

- **Naming**: Kebab-case files (`src/features/health-check/`), PascalCase types/components.
- **Structure**: Feature-based; one domain feature per folder (`health/`, `rockets/`) with layers (Controller, Service, Repository).
- **Errors**: Custom exceptions inherit from domain context (`RocketNotFoundException`); handler methods translate to HTTP codes.
- **Testing**: Colocated unit tests (`*.test.ts`, `*Test.java`); integration tests in `e2e/`.
- **Avoid**: 
  - God services; keep business logic in services, HTTP concerns in controllers.
  - Mutating responses; return immutable records/objects from API.
  - Circular dependencies between features.

---

> last updated: June 3, 2026
