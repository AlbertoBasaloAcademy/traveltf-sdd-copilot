import './App.css';
import { HealthStatus } from './features/health/HealthStatus';
import { LaunchesScheduler } from './features/launches/LaunchesScheduler';
import { RocketsFleet } from './features/rockets/RocketsFleet';

function App() {
  return (
    <main className="app-shell">
      <h1 className="app-hero">ab-java-react</h1>
      <div className="app-grid">
        <HealthStatus />
        <RocketsFleet />
        <LaunchesScheduler />
      </div>
    </main>
  );
}

export default App;
