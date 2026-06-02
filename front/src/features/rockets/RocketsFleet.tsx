import { useEffect, useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import { createRocket, decommissionRocket, getRockets, updateRocket } from './rocketsApi';
import type { Rocket, RocketPayload, RocketRange } from '../../shared/types/rocket';
import './RocketsFleet.css';

const ranges: RocketRange[] = ['Earth', 'Moon', 'Mars'];

const emptyForm: RocketPayload = {
  name: '',
  capacity: 1,
  range: 'Earth',
};

export function RocketsFleet() {
  const [rockets, setRockets] = useState<Rocket[]>([]);
  const [form, setForm] = useState<RocketPayload>(emptyForm);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getRockets()
      .then((catalog) => setRockets(catalog))
      .catch(() => setError('Could not load the rockets catalog.'))
      .finally(() => setLoading(false));
  }, []);

  const buttonLabel = useMemo(() => (editingId ? 'Save changes' : 'Register rocket'), [editingId]);

  function startEditing(rocket: Rocket) {
    setEditingId(rocket.id);
    setForm({
      name: rocket.name,
      capacity: rocket.capacity,
      range: rocket.range,
    });
  }

  function cancelEditing() {
    setEditingId(null);
    setForm(emptyForm);
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);

    try {
      if (editingId) {
        const updated = await updateRocket(editingId, form);
        setRockets((current) => current.map((item) => (item.id === editingId ? updated : item)));
      } else {
        const created = await createRocket(form);
        setRockets((current) => [...current, created]);
      }
      cancelEditing();
    } catch {
      setError('Rocket operation failed. Validate input and try again.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleDecommission(id: string) {
    setError(null);
    try {
      await decommissionRocket(id);
      setRockets((current) =>
        current.map((item) => (item.id === id ? { ...item, decommissioned: true } : item))
      );
    } catch {
      setError('Rocket decommission failed.');
    }
  }

  return (
    <section className="fleet-card">
      <header className="fleet-header">
        <h2>Rockets Fleet</h2>
        <p>Register, update and decommission launch vehicles.</p>
      </header>

      <form className="fleet-form" onSubmit={handleSubmit}>
        <label>
          Name
          <input
            value={form.name}
            onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
            maxLength={80}
            required
          />
        </label>
        <label>
          Capacity
          <input
            type="number"
            min={1}
            max={9}
            value={form.capacity}
            onChange={(event) =>
              setForm((current) => ({ ...current, capacity: Number(event.target.value) || 1 }))
            }
            required
          />
        </label>
        <label>
          Range
          <select
            value={form.range}
            onChange={(event) =>
              setForm((current) => ({ ...current, range: event.target.value as RocketRange }))
            }
          >
            {ranges.map((range) => (
              <option key={range} value={range}>
                {range}
              </option>
            ))}
          </select>
        </label>
        <div className="fleet-actions">
          <button type="submit" disabled={submitting}>
            {buttonLabel}
          </button>
          {editingId ? (
            <button type="button" className="ghost" onClick={cancelEditing}>
              Cancel
            </button>
          ) : null}
        </div>
      </form>

      {error ? (
        <p className="fleet-error" role="alert" data-testid="fleet-error">
          {error}
        </p>
      ) : null}

      {loading ? <p>Loading catalog...</p> : null}

      <ul className="fleet-list" data-testid="fleet-list">
        {rockets.map((rocket) => (
          <li key={rocket.id} className={rocket.decommissioned ? 'fleet-item decommissioned' : 'fleet-item'}>
            <div>
              <strong>{rocket.name}</strong>
              <span>
                {rocket.capacity} seats | {rocket.range}
              </span>
            </div>
            <div className="fleet-item-actions">
              <button type="button" onClick={() => startEditing(rocket)} disabled={rocket.decommissioned}>
                Edit
              </button>
              <button
                type="button"
                className="warn"
                onClick={() => handleDecommission(rocket.id)}
                disabled={rocket.decommissioned}
              >
                {rocket.decommissioned ? 'Decommissioned' : 'Decommission'}
              </button>
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
}
