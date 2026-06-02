import { HealthStatus } from './features/health/HealthStatus';
import { RocketsFleet } from './features/rockets/RocketsFleet';
import './App.css';

function App() {
  return (
    <main className="app-shell">
      <h1 className="app-hero">ab-java-react</h1>
      <div className="app-grid">
        <HealthStatus />
        <RocketsFleet />
      </div>
    </main>
  );
}

export default App;
