export enum UserRole {
    'ADMIN' = 'Admin',
    'ORGANIZER' = 'Organizer',
    'TALENT' = 'Talent',
    'PARTICIPANT' = 'Participant',
}

export interface IUser {
    displayName: string;
    email: string;
    role: UserRole | string;
    phoneNumber?: string;
    id?: string; 
    appointments?: string[];
    
}