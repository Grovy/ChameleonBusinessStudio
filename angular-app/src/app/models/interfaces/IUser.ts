/*
    This model is pending a final revamp once we decide what kind of schema to use.
    Email and role will be the only required fields, as we will default to email as display name.
*/

export enum UserRole {
    'ADMIN',
    'ORGANIZER',
    'TALENT',
    'PARTICIPANT',
}

export interface IUser {
    email: string;
    role: UserRole;
    firstName?: string;
    lastName?: string;
    id?: string;
    age?: number;
    gender?: string;
}