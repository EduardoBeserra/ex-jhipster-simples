/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LojaComumTestModule } from '../../../test.module';
import { ContinenteComponent } from 'app/entities/continente/continente.component';
import { ContinenteService } from 'app/entities/continente/continente.service';
import { Continente } from 'app/shared/model/continente.model';

describe('Component Tests', () => {
    describe('Continente Management Component', () => {
        let comp: ContinenteComponent;
        let fixture: ComponentFixture<ContinenteComponent>;
        let service: ContinenteService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LojaComumTestModule],
                declarations: [ContinenteComponent],
                providers: []
            })
                .overrideTemplate(ContinenteComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ContinenteComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContinenteService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Continente(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.continentes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
