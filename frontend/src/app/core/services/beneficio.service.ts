import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Beneficio } from '../models/beneficio.model';


@Injectable({
    providedIn: 'root'
})
export class BeneficioService {
    private base = '/api/beneficios';
    constructor(private http: HttpClient) {}


    list(): Observable<Beneficio[]> { return this.http.get<Beneficio[]>(this.base); }
    create(b: Partial<Beneficio>) { return this.http.post<Beneficio>(this.base, b); }
    transfer(fromId: number, toId: number, amount: number) {
        return this.http.post(`${this.base}/transfer`, { fromId, toId, amount });
    }
}