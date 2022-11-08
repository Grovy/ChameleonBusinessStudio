import { IUser } from "./IUser";

export interface IAppointment {
    id?: number;
    title: string;
    startTime: Date | string | number[];
    endTime: Date | string | number[];
    date?: string;
    location: string;
    description: string;
    cancelled?: boolean;
    totalSlots?: number;
    tags?: string[];
    participants: string[] | IUser[];
}
