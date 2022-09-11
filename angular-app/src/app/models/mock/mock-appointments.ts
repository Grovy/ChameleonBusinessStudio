import { IAppointment } from "../interfaces/IAppointment";

export const MockAppointmentList: IAppointment[] = [
    {
      id: 1, 
      date: "09/01/2022",
      startTime: "10:00 AM", 
      endTime: "11:00 AM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut (Premium)",
      registeredUsers: ['Matt Crow'],
    }, {
      id: 2, 
      date: "09/01/2022",
      startTime: "11:00 AM", 
      endTime: "12:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: ['Nero Tandel'],
    }, {
      id: 3, 
      date: "09/01/2022",
      startTime: "01:00 PM", 
      endTime: "02:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: ['Ariel Carmargo'],
    }, {
      id: 4, 
      date: "09/02/2022",
      startTime: "09:00 AM", 
      endTime: "10:00 AM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut",
      registeredUsers: ['Daniel Villavicencio'],
    }, {
      id: 5, 
      date: "09/04/2022",
      startTime: "03:00 PM", 
      endTime: "04:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Hot Towel",
      registeredUsers: ['Dave Kaercher'],
    }, {
      id: 6, 
      date: "09/05/2022",
      startTime: "05:00 PM", 
      endTime: "06:00 PM", 
      title: "Appointment with Daniel", 
      location: "123 Main Street, Sacramento, CA 95810",
      description: "Men's Haircut with Beard Trim",
      registeredUsers: ['Rojan Maharjan'],
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