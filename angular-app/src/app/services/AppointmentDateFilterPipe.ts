import { Pipe, PipeTransform } from "@angular/core";
import { DateManager } from 'src/app/services/DateManager';

@Pipe({
    name: 'ApptDateFilter',
    pure: false
})
export class AppointmentDateFilterPipe implements PipeTransform {

    constructor(private dateManager: DateManager) { }

    transform(items: any[], filter: Object): any {
        if(!items || !filter) {
            return items;
        }

        return items.filter(
            item => {
                this.dateManager.arrayToDate(item.startTime).toLocaleDateString() == filter;
            }
        );
    }
}