import { IUser, UserRole } from "../interfaces/IUser";

/*
    Migrated the mock data from the admin-panel-test.ts file to here
*/

const EMAIL: string[] = [
    'example@gmail.com',
  ];
  
const NAMES: string[] = [
    'Matt',
    'Daniel',
    'Daniel',
    'Ariel',
    'Nero',
    'Rozan',
    'Dave',
    'Theodore',
    'Isla',
    'Oliver',
    'Isabella',
    'Jasper',
    'Cora',
    'Levi',
    'Violet',
    'Arthur',
    'Mia',
    'Thomas',
    'Elizabeth',
];

function getRandomRole<T>(IUser: T): T[keyof T] {
    const roleValues = Object.keys(UserRole)
        .map(n => Number.parseInt(n))
        .filter(n => !Number.isNaN(n)) as unknown as T[keyof T][];
    const randomIndex = Math.floor(Math.random() * roleValues.length);
    const randomRoleValue = roleValues[randomIndex];
    return randomRoleValue;
}

export const createNewUser = (id: number): IUser => {
    const FirstName = 
        NAMES[Math.round(Math.random() * (NAMES.length -1))] + ' ';
    const LastName = 
        NAMES[Math.round(Math.random() * (NAMES.length - 1))].charAt(0) + ' ';

        return {
            displayName: FirstName + " " + LastName,
            email: EMAIL.toString(),
            role: getRandomRole(UserRole),
        };
};

export const AdminPanelTestUsers = Array.from({length: 100}, (_, k) => createNewUser(k + 1));