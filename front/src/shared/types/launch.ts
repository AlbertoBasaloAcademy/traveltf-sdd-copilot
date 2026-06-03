export type LaunchStatus = 'created' | 'confirmed' | 'completed' | 'cancelled';

export interface Launch {
  id: string;
  rocketId: string;
  launchTime: string; // ISO string
  ticketPrice: number;
  minimumOccupancy: number;
  status: LaunchStatus;
  createdAt: string;
  updatedAt: string;
}

export interface LaunchPayload {
  rocketId: string;
  launchTime: string;
  ticketPrice: number;
  minimumOccupancy: number;
}

export interface LaunchStatusPayload {
  status: LaunchStatus;
}
