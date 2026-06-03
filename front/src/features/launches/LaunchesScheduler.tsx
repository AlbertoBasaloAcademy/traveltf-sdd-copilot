import { useEffect, useState } from 'react';
import type { LaunchPayload } from '../../shared/types/launch';
import type { Rocket } from '../../shared/types/rocket';
import { getRockets } from '../rockets/rocketsApi';
import './LaunchesScheduler.css';
import { useLaunches } from './useLaunches';

export function LaunchesScheduler() {
  const { launches, loading, error, submitting, addLaunch, changeStatus } = useLaunches();
  const [rockets, setRockets] = useState<Rocket[]>([]);
  const [form, setForm] = useState<LaunchPayload>({ rocketId: '', launchTime: '', ticketPrice: 1, minimumOccupancy: 1 });

  useEffect(() => {
    getRockets().then(setRockets).catch(() => {});
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    await addLaunch(form);
    setForm({ rocketId: '', launchTime: '', ticketPrice: 1, minimumOccupancy: 1 });
  }

  return (
    <section className="launches-card">
      <header>
        <h2>Launches Scheduler</h2>
        <p>Schedule and manage upcoming launches.</p>
      </header>

      <form className="launch-form" onSubmit={handleSubmit}>
        <label>
          Rocket
          <select value={form.rocketId} onChange={(e) => setForm((c) => ({ ...c, rocketId: e.target.value }))} required>
            <option value="">Select rocket</option>
            {rockets.map((r) => (
              <option key={r.id} value={r.id}>
                {r.name}
              </option>
            ))}
          </select>
        </label>
        <label>
          Launch time
          <input
            type="datetime-local"
            value={form.launchTime}
            onChange={(e) => setForm((c) => ({ ...c, launchTime: e.target.value }))}
            required
          />
        </label>
        <label>
          Ticket price
          <input type="number" min={1} value={form.ticketPrice} onChange={(e) => setForm((c) => ({ ...c, ticketPrice: Number(e.target.value) || 1 }))} required />
        </label>
        <label>
          Minimum occupancy
          <input type="number" min={1} value={form.minimumOccupancy} onChange={(e) => setForm((c) => ({ ...c, minimumOccupancy: Number(e.target.value) || 1 }))} required />
        </label>
        <div className="launch-actions">
          <button type="submit" disabled={submitting}>Schedule launch</button>
        </div>
      </form>

      {error ? <p className="launch-error">{error}</p> : null}

      {loading ? <p>Loading launches...</p> : null}

      <ul className="launch-list">
        {launches.map((l) => (
          <li key={l.id} className={`launch-item ${l.status}`}>
            <div>
              <strong>{l.rocketId}</strong>
              <span>{new Date(l.launchTime).toLocaleString()}</span>
            </div>
            <div className="launch-item-actions">
              <span className="badge">{l.status}</span>
              {l.status !== 'completed' && l.status !== 'cancelled' ? (
                <>
                  {l.status === 'created' ? (
                    <button onClick={() => changeStatus(l.id, 'confirmed')}>Confirm</button>
                  ) : null}
                  {l.status === 'confirmed' ? (
                    <button onClick={() => changeStatus(l.id, 'completed')}>Complete</button>
                  ) : null}
                  <button onClick={() => changeStatus(l.id, 'cancelled')}>Cancel</button>
                </>
              ) : null}
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
}
