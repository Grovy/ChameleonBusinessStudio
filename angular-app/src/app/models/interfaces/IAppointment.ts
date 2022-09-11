import { IAppointmentTag } from './IAppointmentTag';

export interface IAppointment {
    id?: number;
    date?: string;
    startTime: string;
    endTime: string;
    title: string;
    location: string;
    description: string;
    restrictions?: string;
    totalSlots?: number;
    tags?: IAppointmentTag[];
    registeredUsers: string[];
    canceled?: boolean;
}