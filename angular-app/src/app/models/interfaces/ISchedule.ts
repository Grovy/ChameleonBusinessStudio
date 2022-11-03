import { IRepeatingAppointment } from "./IRepeatingAppointment";

export interface ISchedule {
    id?: string,
    name: string,
    isEnabled: boolean,
    appointments: IRepeatingAppointment[],
}