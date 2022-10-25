import { IUser } from './IUser';

export enum DaysOfWeek {
    'MONDAY' = 'Monday',
    'TUESDAY' = "Tuesday",
    'WEDNESDAY' = "Wednesday",
    'THURSDAY' = "Thursday",
    'FRIDAY' = "Friday", 
    'SATURDAY' = "Saturday",
    'SUNDAY' = "Sunday",
}

export interface IAvailability {
    title: string;
    hoursFrom: string;
    hoursTo: string;
    daysOfWeek: DaysOfWeek[];
    id?: string;
    user?: IUser
}