import { httpClient } from '../../shared/api/httpClient';
import type { Launch, LaunchPayload } from '../../shared/types/launch';
import { createLaunch, getLaunchById, getLaunches, transitionLaunchStatus } from './launchesApi';

vi.mock('../../shared/api/httpClient', () => ({ httpClient: { get: vi.fn(), post: vi.fn() } }));

const sample: Launch = {
  id: 'l-1',
  rocketId: 'r-1',
  launchTime: '2026-06-30T10:00:00Z',
  ticketPrice: 100,
  minimumOccupancy: 2,
  status: 'created',
  createdAt: '2026-06-01T10:00:00Z',
  updatedAt: '2026-06-01T10:00:00Z',
};

beforeEach(() => {
  vi.mocked(httpClient.get).mockReset();
  vi.mocked(httpClient.post).mockReset();
});

test('getLaunches calls httpClient.get', async () => {
  vi.mocked(httpClient.get).mockResolvedValue([sample]);
  const result = await getLaunches();
  expect(httpClient.get).toHaveBeenCalledWith('/api/launches');
  expect(result).toEqual([sample]);
});

test('createLaunch posts payload', async () => {
  const payload: LaunchPayload = { rocketId: 'r-1', launchTime: '2026-06-30T10:00', ticketPrice: 50, minimumOccupancy: 1 };
  vi.mocked(httpClient.post).mockResolvedValue(sample);
  const result = await createLaunch(payload);
  expect(httpClient.post).toHaveBeenCalledWith('/api/launches', payload);
  expect(result).toEqual(sample);
});

test('getLaunchById calls correct path', async () => {
  vi.mocked(httpClient.get).mockResolvedValue(sample);
  const result = await getLaunchById('l-1');
  expect(httpClient.get).toHaveBeenCalledWith('/api/launches/l-1');
  expect(result).toEqual(sample);
});

test('transitionLaunchStatus posts status', async () => {
  vi.mocked(httpClient.post).mockResolvedValue(sample);
  const result = await transitionLaunchStatus('l-1', 'confirmed');
  expect(httpClient.post).toHaveBeenCalledWith('/api/launches/l-1/status', { status: 'confirmed' });
  expect(result).toEqual(sample);
});
