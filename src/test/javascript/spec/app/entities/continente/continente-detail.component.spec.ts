/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LojaComumTestModule } from '../../../test.module';
import { ContinenteDetailComponent } from 'app/entities/continente/continente-detail.component';
import { Continente } from 'app/shared/model/continente.model';

describe('Component Tests', () => {
    describe('Continente Management Detail Component', () => {
        let comp: ContinenteDetailComponent;
        let fixture: ComponentFixture<ContinenteDetailComponent>;
        const route = ({ data: of({ continente: new Continente(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LojaComumTestModule],
                declarations: [ContinenteDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ContinenteDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ContinenteDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.continente).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
