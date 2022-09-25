import { IAppointment } from "../interfaces/IAppointment";
import { IUser, UserRole } from '../interfaces/IUser';
import { MockParticipantList } from "./mock-users";

const participants: IUser[] = MockParticipantList;
const barber = { email: 'danielr@chameleon.com', role: UserRole.TALENT};

export const MockAppointmentList: IAppointment[] = [
    {
      id: 1, 
      date: "09/01/2022",
      startTime: "10:00 AM", 
      endTime: "11:00 AM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut (Premium)",
      registeredUsers: [participants[0], barber],
    }, {
      id: 2, 
      date: "09/01/2022",
      startTime: "11:00 AM", 
      endTime: "12:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: [participants[5], barber],
    }, {
      id: 3, 
      date: "09/01/2022",
      startTime: "01:00 PM", 
      endTime: "02:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: [participants[4], barber],
    }, {
      id: 4, 
      date: "09/02/2022",
      startTime: "09:00 AM", 
      endTime: "10:00 AM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: [participants[1], barber],
    }, {
      id: 5, 
      date: "09/04/2022",
      startTime: "03:00 PM", 
      endTime: "04:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Hot Towel",
      registeredUsers: [participants[6], barber],
    }, {
      id: 6, 
      date: "09/05/2022",
      startTime: "05:00 PM", 
      endTime: "06:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Beard Trim",
      registeredUsers: [participants[3], barber],
    }
];

// Will default to the first one on the list
export const MockSelectedAppointment: IAppointment | undefined = {
    id: 1, 
    date: "09/01/2022",
    startTime: "10:00 AM", 
    endTime: "11:00 AM", 
    title: "Appointment with Daniel", 
    location: "123 Main Street, Sacramento, CA 95810",
    description: "Men's Haircut (Premium)",
    registeredUsers: ['Matt Crow'],
}