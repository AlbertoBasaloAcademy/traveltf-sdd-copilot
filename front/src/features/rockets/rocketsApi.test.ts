import { createRocket, decommissionRocket, getRockets, updateRocket } from './rocketsApi';
import { httpClient } from '../../shared/api/httpClient';
import type { Rocket } from '../../shared/types/rocket';

vi.mock('../../shared/api/httpClient', () => ({
  httpClient: { get: vi.fn(), post: vi.fn(), put: vi.fn(), del: vi.fn() },
}));

const sample: Rocket = {
  id: 'r-1',
  name: 'Aurora',
  capacity: 4,
  range: 'Moon',
  decommissioned: false,
};

test('getRockets requests /api/rockets', async () => {
  vi.mocked(httpClient.get).mockResolvedValue([sample]);

  const result = await getRockets();

  expect(httpClient.get).toHaveBeenCalledWith('/api/rockets');
  expect(result).toEqual([sample]);
});

test('createRocket posts payload to /api/rockets', async () => {
  vi.mocked(httpClient.post).mockResolvedValue(sample);

  const result = await createRocket({ name: 'Aurora', capacity: 4, range: 'Moon' });

  expect(httpClient.post).toHaveBeenCalledWith('/api/rockets', {
    name: 'Aurora',
    capacity: 4,
    range: 'Moon',
  });
  expect(result).toEqual(sample);
});

test('updateRocket puts payload to /api/rockets/:id', async () => {
  vi.mocked(httpClient.put).mockResolvedValue(sample);

  await updateRocket('r-1', { name: 'Aurora', capacity: 4, range: 'Moon' });

  expect(httpClient.put).toHaveBeenCalledWith('/api/rockets/r-1', {
    name: 'Aurora',
    capacity: 4,
    range: 'Moon',
  });
});

test('decommissionRocket deletes /api/rockets/:id', async () => {
  vi.mocked(httpClient.del).mockResolvedValue(undefined);

  await decommissionRocket('r-1');

  expect(httpClient.del).toHaveBeenCalledWith('/api/rockets/r-1');
});
