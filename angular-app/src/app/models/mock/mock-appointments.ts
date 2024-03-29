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
      _id: 1,
      startTime: new Date(2022, 9, 1, 10, 0o0, 0o0),
      endTime: new Date(2022, 9, 1, 11, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut (Premium)",
      participants: [],
    }, {
      _id: 2,
      startTime: new Date(2022, 9, 1, 11, 0o0, 0o0),
      endTime: new Date(2022, 9, 1, 12, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      participants: [],
    }, {
      _id: 3,
      startTime: new Date(2022, 9, 1, 12, 0o0, 0o0),
      endTime: new Date(2022, 9, 1, 13, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      participants: [],
    }, {
      _id: 4,
      startTime: new Date(2022, 9, 2, 10, 0o0, 0o0),
      endTime: new Date(2022, 9, 2, 11, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      participants: [],
    }, {
      _id: 5,
      startTime: new Date(2022, 9, 4, 15, 0o0, 0o0),
      endTime: new Date(2022, 9, 4, 16, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Hot Towel",
      participants: [],
    }, {
      _id: 6,
      startTime: new Date(2022, 9, 5, 16, 0o0, 0o0),
      endTime: new Date(2022, 9, 5, 17, 0o0, 0o0),
      title: "Appointment with Daniel",
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Beard Trim",
      participants: [],
    }

];

// Will default to the first one on the list
export const MockSelectedAppointment: IAppointment | undefined = {
    _id: 1,
    date: "09/01/2022",
    startTime: new Date(2022, 9, 1, 10, 0o0, 0o0),
    endTime: new Date(2022, 9, 1, 11, 0o0, 0o0),
    title: "Appointment with Daniel",
    location: "123 Main Street, Sacramento, CA 95810",
    description: "Men's Haircut (Premium)",
    participants: ['Matt Crow'],
}
