export type RocketRange = 'Earth' | 'Moon' | 'Mars';

export interface Rocket {
  id: string;
  name: string;
  capacity: number;
  range: RocketRange;
  decommissioned: boolean;
}

export interface RocketPayload {
  name: string;
  capacity: number;
  range: RocketRange;
}
