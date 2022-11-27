import { IUser, UserRole } from "../interfaces/IUser";
import { v4 as uuidv4 } from 'uuid';
/*
    This mock data is for the purposes of developing UI that's centered around displaying user data.
    This file will be modified once our schemas and data modeling is done.
*/

export const MockAdminUserList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'Matt Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.ADMIN,
    }, {
        _id: uuidv4(),
        displayName: 'Rojan Maharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Ariel Camargo',
        email: 'ariel@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Nero Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Dave Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ADMIN
    }, 
];

export const MockParticipantList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'Matt Crow',
        email: 'matt@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.PARTICIPANT,
    }, {
        _id: uuidv4(),
        displayName: 'Rojan Maharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Ariel Camargo',
        email: 'ariel@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Nero Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Dave Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.PARTICIPANT
    }, 
];

export const MockTalentList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'Matt Crow',
        email: 'matt@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'Rojan Maharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Ariel Camargo',
        email: 'ariel@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Nero Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Dave Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.TALENT
    }, 
];

export const MockOrganizerUserList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'Matt Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.ORGANIZER,
    }, {
        _id: uuidv4(),
        displayName: 'Rojan Maharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Ariel Camargo',
        email: 'ariel@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Nero Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Dave Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ORGANIZER
    }, 
];

export const MockGenericUserList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'Matt Crow',
        email: 'matt@chameleon.com',
        role: UserRole.ADMIN
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Villavicencio',
        email: 'danielv@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Daniel Ramos',
        email: 'danielr@chameleon.com',
        role: UserRole.PARTICIPANT,
    }, {
        _id: uuidv4(),
        displayName: 'Rojan Maharjan',
        email: 'rojan@chameleon.com',
        role: UserRole.TALENT
    }, {
        _id: uuidv4(),
        displayName: 'Ariel Camargo',
        email: 'ariel@chameleon.com',
        role: UserRole.ORGANIZER
    }, {
        _id: uuidv4(),
        displayName: 'Nero Tandel',
        email: 'nero@chameleon.com',
        role: UserRole.PARTICIPANT
    }, {
        _id: uuidv4(),
        displayName: 'Dave Kaercher',
        email: 'dave@chameleon.com',
        role: UserRole.ADMIN
    }, 
];