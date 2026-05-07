# Rules and instructions for agents.

## Product
AstroBookings purpose is to support the employees of a fictional space travel agency in their daily operations. It is an MVP, so it does not require authentication or storage features.

## Technology
Is a simple system with an REST API at the backend and a web application as a frontNo database is needed, just memory for the moment.

### Back REST API
— Tech Stack: Java, Spring, Maven, Junit.
— Scaffolding: `/back`, `/back/pom.xml`
— Scripts: `mvn spring-boot:run` `mvn test`

### Front Web application 
— Tech Stack: TypeScript, HTML, CSS, Angular, Vitest, esbuild.
— Scaffolding: `/front`, `front/package.json`, `front/angular.json`
— Scripts: `npm start`, `npm test`
  
## Environment

### Configuration
— This file with the main guidelines at `/AGENTS.md`
— Skills, commands, prompts... at `/.agents/`
  
### Conventions
— Use conventional commit messaging
— Code branches are: `feat/` `fix/` `chore/`
— Generate short slugs for artifacts and names (less-than-20-chars).

### Behavior
— Be extremely concise. Sacrifice grammar for concision.
— Do not explain if not asked for.
— When you are in doubt, ask questions one by one with closed answers. 