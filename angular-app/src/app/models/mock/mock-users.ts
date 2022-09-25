import { IUser, UserRole } from "../interfaces/IUser";
/*
    This mock data is for the purposes of developing UI that's centered around displaying user data.
    This file will be modified once our schemas and data modeling is done.
*/

export const MockAdminUserList: IUser[] = [
    {
        id: '1',
        firstName: 'Matt',
        lastName: 'Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '2',
        firstName: 'Daniel',
        lastName: 'Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '3',
        firstName: 'Daniel',
        lastName: 'Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.ADMIN,
    }, {
        id: '4',
        firstName: 'Rojan',
        lastName: 'Marharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '5',
        firstName: 'Ariel',
        lastName: 'Carmago',
        email: 'ariel@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '6',
        firstName: 'Nero',
        lastName: 'Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '7',
        firstName: 'Dave',
        lastName: 'Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ADMIN
    }, 
];

export const MockParticipantList: IUser[] = [
    {
        id: '1',
        firstName: 'Matt',
        lastName: 'Crow',
        email: 'matt@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '2',
        firstName: 'Daniel',
        lastName: 'Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '3',
        firstName: 'Daniel',
        lastName: 'Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.PARTICIPANT,
    }, {
        id: '4',
        firstName: 'Rojan',
        lastName: 'Marharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '5',
        firstName: 'Ariel',
        lastName: 'Carmago',
        email: 'ariel@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '6',
        firstName: 'Nero',
        lastName: 'Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '7',
        firstName: 'Dave',
        lastName: 'Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.PARTICIPANT
    }, 
];

export const MockTalentList: IUser[] = [
    {
        id: '1',
        firstName: 'Matt',
        lastName: 'Crow',
        email: 'matt@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '2',
        firstName: 'Daniel',
        lastName: 'Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '3',
        firstName: 'Daniel',
        lastName: 'Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        id: '4',
        firstName: 'Rojan',
        lastName: 'Marharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '5',
        firstName: 'Ariel',
        lastName: 'Carmago',
        email: 'ariel@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '6',
        firstName: 'Nero',
        lastName: 'Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '7',
        firstName: 'Dave',
        lastName: 'Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.TALENT
    }, 
];

export const MockOrganizerUserList: IUser[] = [
    {
        id: '1',
        firstName: 'Matt',
        lastName: 'Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '2',
        firstName: 'Daniel',
        lastName: 'Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '3',
        firstName: 'Daniel',
        lastName: 'Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.ORGANIZER,
    }, {
        id: '4',
        firstName: 'Rojan',
        lastName: 'Marharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '5',
        firstName: 'Ariel',
        lastName: 'Carmago',
        email: 'ariel@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '6',
        firstName: 'Nero',
        lastName: 'Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '7',
        firstName: 'Dave',
        lastName: 'Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ORGANIZER
    }, 
];

export const MockGenericUserList: IUser[] = [
    {
        id: '1',
        firstName: 'Matt',
        lastName: 'Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ADMIN
    }, {
        id: '2',
        firstName: 'Daniel',
        lastName: 'Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '3',
        firstName: 'Daniel',
        lastName: 'Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.PARTICIPANT,
    }, {
        id: '4',
        firstName: 'Rojan',
        lastName: 'Marharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.TALENT
    }, {
        id: '5',
        firstName: 'Ariel',
        lastName: 'Carmago',
        email: 'ariel@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        id: '6',
        firstName: 'Nero',
        lastName: 'Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        id: '7',
        firstName: 'Dave',
        lastName: 'Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ADMIN
    }, 
];