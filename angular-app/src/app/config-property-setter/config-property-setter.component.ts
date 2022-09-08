import {Component, Input} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
// I (Matt) am pretty sure this doesn't work
@Component({
    selector: 'config-property-setter',
    templateUrl: './config-property-setter.component.html',
    styleUrls: []
})
export class ConfigPropertySetterComponent {
    @Input() label = "";
    @Input() endpoint = "";
    @Input() type="text";
    form = this.fb.group({
        value: ''
    });
    
    constructor(private http: HttpClient, private fb: FormBuilder){
        
    }
    
    onSubmit(): void {
        console.log(this.form.value);
        this.http.post(`/api/v1/config/${this.endpoint}`, this.form.value);
    }
}