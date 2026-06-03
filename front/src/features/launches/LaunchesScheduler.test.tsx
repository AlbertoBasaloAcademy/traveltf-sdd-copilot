import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as rocketsApi from '../rockets/rocketsApi';
import { LaunchesScheduler } from './LaunchesScheduler';
import * as launchesApi from './launchesApi';

vi.mock('./launchesApi', () => ({ getLaunches: vi.fn(), createLaunch: vi.fn(), transitionLaunchStatus: vi.fn() }));
vi.mock('../rockets/rocketsApi', () => ({ getRockets: vi.fn() }));

beforeEach(() => {
  vi.mocked(launchesApi.getLaunches).mockReset();
  vi.mocked(launchesApi.createLaunch).mockReset();
  vi.mocked(launchesApi.transitionLaunchStatus).mockReset();
  vi.mocked(rocketsApi.getRockets).mockReset();
});

test('renders and allows scheduling', async () => {
  vi.mocked(rocketsApi.getRockets).mockResolvedValue([{ id: 'r-1', name: 'Aurora', capacity: 4, range: 'Moon', decommissioned: false }]);
  vi.mocked(launchesApi.getLaunches).mockResolvedValue([]);
  vi.mocked(launchesApi.createLaunch).mockResolvedValue({ id: 'l-1', rocketId: 'r-1', launchTime: 't', ticketPrice: 1, minimumOccupancy: 1, status: 'created', createdAt: 'c', updatedAt: 'u' });

  render(<LaunchesScheduler />);

  const user = userEvent.setup();
  // wait for rockets to be loaded and option rendered
  expect(await screen.findByText('Aurora')).toBeInTheDocument();
  await user.selectOptions(screen.getByLabelText('Rocket'), 'r-1');
  await user.type(screen.getByLabelText('Launch time'), '2026-06-30T10:00');
  await user.click(screen.getByRole('button', { name: 'Schedule launch' }));

  expect(launchesApi.createLaunch).toHaveBeenCalled();
});
