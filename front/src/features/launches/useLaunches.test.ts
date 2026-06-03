import { act, renderHook, waitFor } from '@testing-library/react';
import * as api from './launchesApi';
import { useLaunches } from './useLaunches';

vi.mock('./launchesApi', () => ({ getLaunches: vi.fn(), createLaunch: vi.fn(), transitionLaunchStatus: vi.fn() }));

beforeEach(() => {
  vi.mocked(api.getLaunches).mockReset();
  vi.mocked(api.createLaunch).mockReset();
  vi.mocked(api.transitionLaunchStatus).mockReset();
});

test('loads launches on mount', async () => {
  vi.mocked(api.getLaunches).mockResolvedValue([]);
  const { result } = renderHook(() => useLaunches());
  await waitFor(() => expect(result.current.loading).toBe(false));
  expect(result.current.launches).toEqual([]);
});

test('addLaunch calls createLaunch and updates state', async () => {
  const created = { id: 'l-1', rocketId: 'r-1', launchTime: 't', ticketPrice: 1, minimumOccupancy: 1, status: 'created', createdAt: 'c', updatedAt: 'u' };
  vi.mocked(api.getLaunches).mockResolvedValue([]);
  vi.mocked(api.createLaunch).mockResolvedValue(created as any);

  const { result } = renderHook(() => useLaunches());
  await waitFor(() => expect(result.current.loading).toBe(false));

  await act(async () => {
    await result.current.addLaunch({ rocketId: 'r-1', launchTime: 't', ticketPrice: 1, minimumOccupancy: 1 });
  });

  expect(result.current.launches).toContainEqual(created as any);
});
