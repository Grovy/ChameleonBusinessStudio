import { IUser } from "./IUser";

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
    tags?: string[];
    registeredUsers: string[] | IUser[];
    canceled?: boolean;
}