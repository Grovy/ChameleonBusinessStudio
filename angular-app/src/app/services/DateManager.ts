import { Injectable } from "@angular/core";

// Providing a series of function we can use to convert between Javascript's Date class and Java's LocalDateTime
@Injectable()
export class DateManager {

    // Returns the index needed in nextDay() to find the next occurence of the selected day of the week
    public getDayIndex(dayOfWeek: string): number {
        switch(dayOfWeek) {
            case "SUNDAY":
                return 0;
            case "MONDAY":
                return 1;
            case "TUESDAY":
                return 2;
            case "WEDNESDAY":
                return 3;
            case "THURSDAY":
                return 4;
            case "FRIDAY":
                return 5;
            case "SATURDAY":
                return 6;
            default:
                return -1;
        }
    }

    // Pass in an index which corresponds to day of the week (Sunday is 0, Saturday is 6)
    // and returns the next occurance of that day of the week
    public nextDay(dayIndex: number): Date {
        let newDate = new Date;
        newDate.setDate(newDate.getDate() + (dayIndex - 1 - newDate.getDay() + 7) % 7 + 1);
        newDate.setMinutes(newDate.getMinutes() - newDate.getTimezoneOffset());
        return newDate;
    }

    // Takes in the string and parses it to a time value
    public toTime(time: string): string {
        if(time == "12:00 PM") {
            return "12:00:00";
        }
        let matches = time.toLowerCase().match(/(\d{1,2}):(\d{2}) ([ap]m)/);
        let output = (parseInt(matches[1]) + (matches[3] == 'pm' ? 12 : 0)) + ':' + matches[2] + ':00';
        let newTime = this.addTime(output, "00:00:00");
        return newTime;
    }

    // Takes in the duration of an event and returns it in a readable format to add using addTime()
    // We only suppored 15 min increments for duration
    public durationToTime(duration: number): string {
        switch(duration) {
            case 15:
                return "00:15:00";
            case 30:
                return "00:30:00";
            case 45:
                return "00:45:00";
            case 60:
                return "01:00:00";
            default:
                return "00:00:00";
        }
    }

    public timeToDuration(time: string): number {
        switch(time) {
            case "00:15:00":
                return 15;
            case "00:30:00":
                return 30;
            case "00:45:00":
                return 45;
            case "01:00:00":
                return 60;
            default:
                return 0;
        }
    }

    // Adds two times together
    public addTime(timeA: string, timeB: string): string {
        let start = timeA;
        let end = timeB;
        let a = start.split(":");
        let secondsA = (+a[0]) * 60 * 60 + (+a[1]) * 60 + (+a[2]);
        let b = end.split(":");
        let secondsB = (+b[0]) * 60 * 60 + (+b[1]) * 60 + (+b[2]);

        let date = new Date(1970, 0, 1);
        date.setSeconds(secondsA + secondsB);

        let c = date.toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
        return c;
    }

    // Subtracts timeA from timeB
    public subtractTime(timeA: string, timeB: string): string {
        let start = timeA;
        let end = timeB;
        let a = start.split(":");
        let secondsA = (+a[0]) * 60 * 60 + (+a[1]) * 60 - (+a[2]);
        let b = end.split(":");
        let secondsB = (+b[0]) * 60 * 60 + (+b[1]) * 60 - (+b[2]);

        let date = new Date(1970, 0, 1);
        date.setSeconds(secondsA - secondsB);

        let c = date.toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
        return c;
    }

    public arrayToISOString(date: number[]): string {
        let year = date[0];
        let month = date[1];
        let day = date[2];
        let hour = date[3];
        let minute = date[4];
        let newMinute = "";
        let newDate = new Date(year, month - 1, day).toISOString().split("T")[0];
        let time = "" + hour + ":" + minute + ":00";

        let timeAsISO = "" + year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00";
        let newTime = this.addTime(time, "00:00:00");


        return newDate + "T" + newTime; 
    }

    public arrayToDate(date: number[]): Date {
        let year = date[0];
        let month = date[1];
        let day = date[2];
        let hour = date[3];
        let minute = date[4];
        return new Date(year, month - 1, day, hour, minute);
    }
}