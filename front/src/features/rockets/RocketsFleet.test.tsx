import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { RocketsFleet } from './RocketsFleet';
import { createRocket, decommissionRocket, getRockets, updateRocket } from './rocketsApi';

vi.mock('./rocketsApi', () => ({
  getRockets: vi.fn(),
  createRocket: vi.fn(),
  updateRocket: vi.fn(),
  decommissionRocket: vi.fn(),
}));

beforeEach(() => {
  vi.mocked(getRockets).mockReset();
  vi.mocked(createRocket).mockReset();
  vi.mocked(updateRocket).mockReset();
  vi.mocked(decommissionRocket).mockReset();
});

test('loads and renders rockets catalog', async () => {
  vi.mocked(getRockets).mockResolvedValue([
    { id: 'r-1', name: 'Aurora', capacity: 4, range: 'Moon', decommissioned: false },
  ]);

  render(<RocketsFleet />);

  expect(await screen.findByText('Aurora')).toBeInTheDocument();
  expect(screen.getByText(/4 seats/i)).toBeInTheDocument();
});

test('registers a new rocket', async () => {
  vi.mocked(getRockets).mockResolvedValue([]);
  vi.mocked(createRocket).mockResolvedValue({
    id: 'r-2',
    name: 'Nova',
    capacity: 5,
    range: 'Mars',
    decommissioned: false,
  });

  render(<RocketsFleet />);

  const user = userEvent.setup();
  await user.type(screen.getByLabelText('Name'), 'Nova');
  fireEvent.change(screen.getByLabelText('Capacity'), { target: { value: '5' } });
  await user.selectOptions(screen.getByLabelText('Range'), 'Mars');
  await user.click(screen.getByRole('button', { name: 'Register rocket' }));

  await waitFor(() => {
    expect(createRocket).toHaveBeenCalledWith({ name: 'Nova', capacity: 5, range: 'Mars' });
  });
  expect(await screen.findByText('Nova')).toBeInTheDocument();
});

test('decommissions a rocket', async () => {
  vi.mocked(getRockets).mockResolvedValue([
    { id: 'r-1', name: 'Aurora', capacity: 4, range: 'Moon', decommissioned: false },
  ]);
  vi.mocked(decommissionRocket).mockResolvedValue(undefined);

  render(<RocketsFleet />);

  const user = userEvent.setup();
  await user.click(await screen.findByRole('button', { name: 'Decommission' }));

  await waitFor(() => {
    expect(decommissionRocket).toHaveBeenCalledWith('r-1');
  });
  expect(await screen.findByRole('button', { name: 'Decommissioned' })).toBeDisabled();
});
