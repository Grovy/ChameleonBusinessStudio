import { IUser } from "./IUser";

export interface IAppointment {
    id?: number;
    title: string;
    startTime: Date | string;
    endTime: Date | string;
    date?: string;
    location: string;
    description: string;
    restrictions?: string;
    cancelled?: boolean;
    totalSlots?: number;
    tags?: string[];
    participants: string[] | IUser[];
}