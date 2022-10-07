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
    appointments?: string[];
    
}