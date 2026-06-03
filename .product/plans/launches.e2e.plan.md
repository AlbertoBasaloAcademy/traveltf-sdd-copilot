
### **Phase 1: E2E Tests & Integration** (Dependencies: Phase 4 FE + Phase 1 BE; Parallel: No)

19. **Create Page Object** — [e2e/pages/LaunchesPage.ts](e2e/pages/LaunchesPage.ts)
   - Reuse HealthPage pattern: Page class with locators (no assertions)
   - Selectors: form inputs (rocket dropdown, datetime, price, occupancy), create button, launches list (table rows), launch items with status badge, transition button, error message, loading indicator
   - Helper method: `goto()`, `fillForm(payload)`, `submit()`, `transitionStatus(launchId, newStatus)`

20. **Create E2E Test Suite** — [e2e/tests/launches.spec.ts](e2e/tests/launches.spec.ts)
   - Reuse health.spec.ts pattern: test.describe(), test() with intent-revealing assertions
   - Test workflows:
     1. **Load launches**: Navigate to /launches, displays empty list initially
     2. **Create launch**: Fill form (select rocket, future date, price > 0, occupancy ≤ capacity), submit, launch appears in list with CREATED status
     3. **View launch details**: Click launch item, displays full details (all fields, timestamps)
     4. **Transition status**: CREATED→CONFIRMED, update shows new status
     5. **Invalid inputs**: Past date rejected, occupancy > rocket capacity rejected
     6. **Network delays**: Simulate slow response, verify loading state visible
     7. **Error handling**: Mock 400/409 errors, verify error message displayed

### **Phase 2: Integration & Verification** (Dependencies: Phase 5; Parallel: No)

21. **Update App Router** — [front/src/App.tsx](front/src/App.tsx)
   - Add route to LaunchesScheduler component (e.g., /launches path)
   - Add navigation link in header/menu

22. **Smoke Test**: Verify app starts (backend + frontend)
   - `cd back && mvn spring-boot:run` — should listen on 8080, DB initialized
   - `cd front && npm run dev` — should start on 5173 with launches route accessible
   - Manual: Navigate to /launches, form renders, can interact with UI

23. **Run E2E Tests**: `cd e2e && npm test`
   - All launch tests should pass
   - Auto-boots backend+frontend, verifies full workflows

---
