import { httpClient } from '../../shared/api/httpClient';
import type { Launch, LaunchPayload, LaunchStatus } from '../../shared/types/launch';

export async function getLaunches(): Promise<Launch[]> {
  return httpClient.get<Launch[]>('/api/launches');
}

export async function createLaunch(payload: LaunchPayload): Promise<Launch> {
  return httpClient.post<Launch, LaunchPayload>('/api/launches', payload);
}

export async function getLaunchById(id: string): Promise<Launch> {
  return httpClient.get<Launch>(`/api/launches/${id}`);
}

export async function transitionLaunchStatus(id: string, status: LaunchStatus): Promise<Launch> {
  return httpClient.post<Launch, { status: LaunchStatus }>(`/api/launches/${id}/status`, { status });
}
