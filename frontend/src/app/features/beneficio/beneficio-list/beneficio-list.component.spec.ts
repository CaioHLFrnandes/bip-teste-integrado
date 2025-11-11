import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BeneficioListComponent } from './beneficio-list.component';
import { BeneficioService } from '../../../core/services/beneficio.service';

describe('BeneficioListComponent', () => {
    let component: BeneficioListComponent;
    let fixture: ComponentFixture<BeneficioListComponent>;
    let beneficioServiceSpy: jasmine.SpyObj<BeneficioService>;

    beforeEach(async () => {
        beneficioServiceSpy = jasmine.createSpyObj('BeneficioService', ['listar']);

        await TestBed.configureTestingModule({
            declarations: [BeneficioListComponent],
            imports: [HttpClientTestingModule],
            providers: [
                { provide: BeneficioService, useValue: beneficioServiceSpy }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(BeneficioListComponent);
        component = fixture.componentInstance;
    });

    it('deve criar o componente', () => {
        expect(component).toBeTruthy();
    });

    it('deve carregar a lista ao iniciar', () => {
        const beneficiosMock = [
            { id: 1, nome: 'Benefício A', valor: 100 },
            { id: 2, nome: 'Benefício B', valor: 200 }
        ];

        // @ts-ignore
        beneficioServiceSpy.list.and.returnValue(of(beneficiosMock));

        component.ngOnInit();

        expect(beneficioServiceSpy.list).toHaveBeenCalled();
        it('should have 2 beneficios', () => {
            expect(component.beneficios.data.length).toBe(2);
        });
    });
});
