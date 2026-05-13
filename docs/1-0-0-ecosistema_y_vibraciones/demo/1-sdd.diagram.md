# Level 1 SDD workflow

```mermaid
flowchart TD
    classDef nd fill:#f8fafc,stroke:#00c4cc,color:#457b9d
    classDef sg fill:#f1f5f9,stroke:#00f2ff,color:#457b9d 

    subgraph P["PRODUCT"]
        REQ["{slug}.spec.md"]:::nd
    end

    subgraph T["TECHNOLOGY"]
        AGT["AGENTS.md"]:::nd
    end  

    subgraph S["SOLUTION"]
        COD[Source Code]:::nd
    end

    REQ -->|/codify| COD
    AGT -.-> COD  

    class P,T,S sg
```

## Commands

- `/codify` - Run the implementation cycle for one specification: generate plans, produce code, and validate with tests.

## Artifacts

### Technology

- `/AGENTS.md` - The entry point for any agent joining the project; defines how agents should operate, including rules, workflows, and artifact conventions.

### Product

- `{slug}.spec.md` - A detailed specification (problem, solution, verification) of a feature or technical requirement.

### Solution

- `Source Code` - The implementation of the system, including unit tests.
