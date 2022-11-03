import { IAppointment } from "./IAppointment";
import { DaysOfWeek } from "./IAvailability";

export interface IRepeatingAppointment {
    id?: string,
    isEnabled: boolean,
    repeatsOn: DaysOfWeek[],
    appointment: IAppointment
}