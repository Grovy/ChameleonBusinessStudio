/*
    This model is pending a final revamp once we decide what kind of schema to use.
    Email and role will be the only required fields, as we will default to email as display name.
*/

import { IAppointment } from "./IAppointment";

export enum UserRole {
    'ADMIN' = 'Admin',
    'ORGANIZER' = 'Organizer',
    'TALENT' = 'Talent',
    'PARTICIPANT' = 'Participant',
}

export interface IUser {
    displayName: string;
    email: string;
    role: UserRole;
    id?: string; 
    appointments?: IAppointment[];
    
}