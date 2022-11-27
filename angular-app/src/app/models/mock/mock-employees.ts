import { IUser, UserRole } from "../interfaces/IUser";
import { v4 as uuidv4 } from 'uuid';

export const MockEmployeeList: IUser[] = [
    {
        _id: uuidv4(),
        displayName: 'danielr@chameleon.com',
        email: 'danielr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'danielv@chameleon.com',
        email: 'danielv@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'danielt@chameleon.com',
        email: 'danielt@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'jessk@chameleon.com',
        email: 'jessk@chameleon.com',
        role: UserRole.TALENT,
    } , {
        _id: uuidv4(),
        displayName: 'johns@chameleon.com',
        email: 'johns@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'mattw@chameleon.com',
        email: 'mattw@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'tomr@chameleon.com',
        email: 'tomr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'seans@chameleon.com',
        email: 'seans@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'adrianz@chameleon.com',
        email: 'adrianz@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'marias@chameleon.com',
        email: 'tomr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'marias@chameleon.com',
        email: 'tomr@chameleon.com',
        role: UserRole.TALENT,
    }, {
        _id: uuidv4(),
        displayName: 'mayar@chameleon.com',
        email: 'mayar@chameleon.com',
        role: UserRole.TALENT,
    }


];