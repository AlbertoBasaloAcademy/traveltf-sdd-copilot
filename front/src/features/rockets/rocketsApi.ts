import { httpClient } from '../../shared/api/httpClient';
import type { Rocket, RocketPayload } from '../../shared/types/rocket';

export async function getRockets(): Promise<Rocket[]> {
  return httpClient.get<Rocket[]>('/api/rockets');
}

export async function createRocket(payload: RocketPayload): Promise<Rocket> {
  return httpClient.post<Rocket, RocketPayload>('/api/rockets', payload);
}

export async function updateRocket(id: string, payload: RocketPayload): Promise<Rocket> {
  return httpClient.put<Rocket, RocketPayload>(`/api/rockets/${id}`, payload);
}

export async function decommissionRocket(id: string): Promise<void> {
  await httpClient.del(`/api/rockets/${id}`);
}
