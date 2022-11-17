export enum UserRole {
    'ADMIN' = 'Admin',
    'ORGANIZER' = 'Organizer',
    'TALENT' = 'Talent',
    'PARTICIPANT' = 'Participant',
}

export interface IUser {
    _id: string;
    displayName: string;
    email: string;
    role: UserRole | string;
    phoneNumber?: string; 
    appointments?: string[];
    
}