import { useEffect, useState } from 'react';
import type { Launch, LaunchPayload } from '../../shared/types/launch';
import { createLaunch, getLaunches, transitionLaunchStatus } from './launchesApi';

export function useLaunches() {
  const [launches, setLaunches] = useState<Launch[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    let active = true;
    getLaunches()
      .then((data) => {
        if (!active) return;
        setLaunches(data);
      })
      .catch(() => setError('Could not load launches'))
      .finally(() => active && setLoading(false));
    return () => {
      active = false;
    };
  }, []);

  async function loadLaunches() {
    setLoading(true);
    setError(null);
    try {
      const data = await getLaunches();
      setLaunches(data);
    } catch {
      setError('Could not load launches');
    } finally {
      setLoading(false);
    }
  }

  async function addLaunch(payload: LaunchPayload) {
    setSubmitting(true);
    setError(null);
    try {
      const created = await createLaunch(payload);
      setLaunches((current) => [...current, created]);
      return created;
    } catch (err) {
      setError('Could not create launch');
      throw err;
    } finally {
      setSubmitting(false);
    }
  }

  async function changeStatus(id: string, status: Launch['status']) {
    setSubmitting(true);
    setError(null);
    try {
      const updated = await transitionLaunchStatus(id, status);
      setLaunches((current) => current.map((l) => (l.id === id ? updated : l)));
      return updated;
    } catch (err) {
      setError('Could not change status');
      throw err;
    } finally {
      setSubmitting(false);
    }
  }

  return { launches, loading, error, submitting, loadLaunches, addLaunch, changeStatus } as const;
}
