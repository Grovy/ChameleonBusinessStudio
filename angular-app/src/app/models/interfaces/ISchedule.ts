import { IRepeatingAppointment } from "./IRepeatingAppointment";

export interface ISchedule {
    title: string,
    isEnabled: boolean,
    appointments: IRepeatingAppointment[],
    id?: string,

}