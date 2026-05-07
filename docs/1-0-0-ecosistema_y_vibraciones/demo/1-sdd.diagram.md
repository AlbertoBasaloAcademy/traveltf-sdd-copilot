# Level 1 SDD workflow

```mermaid
flowchart TD
    classDef nd fill:#f8fafc,stroke:#00c4cc,color:#457b9d
    classDef sg fill:#f1f5f9,stroke:#00f2ff,color:#457b9d 

    subgraph P["PRODUCT"]
        REQ["*.spec.md"]:::nd
    end

    subgraph A["AGENTS"]
        AGT["AGENTS.md"]:::nd
    end  

    subgraph S["SOLUTION"]
        COD[Source Code]:::nd
    end

    REQ -->|/codify| COD
    AGT -.-> COD  

    class P,A,S sg
```

## Commands

- `/codify` - Run the implementation cycle for one specification: generate plans, produce code, and validate with tests.

## Artifacts

- `*.spec.md` - The specification of a requirement or bug to fix.

- `Source Code` - The implementation of the system, including unit tests.
