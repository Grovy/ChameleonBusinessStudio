import { IAppointment } from "../interfaces/IAppointment";
import { IUser, UserRole } from '../interfaces/IUser';
import { MockParticipantList } from "./mock-users";

const participants: IUser[] = MockParticipantList;
const barber = {
  displayName:'danielr@chameleon.com',
  email: 'danielr@chameleon.com',
  role: UserRole.TALENT
};

export const MockAppointmentList: IAppointment[] = [
    {
      id: 1,
      date: "",
      startTime: new Date("10/14/2022 10:00 AM"),
      endTime: new Date("10/14/2022 11:00 AM"),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut (Premium)",
      registeredUsers: [participants[0], barber],
    }, {
      id: 2,
      date: "",
      startTime: new Date("09/01/2022 11:00 AM"),
      endTime: new Date("09/01/2022 12:00 PM"),
      title: "Appointment with Matt",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: [participants[5], barber],
    }

];

// Will default to the first one on the list
export const MockSelectedAppointment: IAppointment | undefined = {
    id: 1,
    date: "09/01/2022",
    startTime:new Date("09/01/2022 10:00 AM"),
    endTime:new Date("09/01/2022 10:00 AM"),
    title: "Appointment with Daniel",
    location: "123 Main Street, Sacramento, CA 95810",
    description: "Men's Haircut (Premium)",
    registeredUsers: ['Matt Crow'],
}
