/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LojaComumTestModule } from '../../../test.module';
import { ContinenteUpdateComponent } from 'app/entities/continente/continente-update.component';
import { ContinenteService } from 'app/entities/continente/continente.service';
import { Continente } from 'app/shared/model/continente.model';

describe('Component Tests', () => {
    describe('Continente Management Update Component', () => {
        let comp: ContinenteUpdateComponent;
        let fixture: ComponentFixture<ContinenteUpdateComponent>;
        let service: ContinenteService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LojaComumTestModule],
                declarations: [ContinenteUpdateComponent]
            })
                .overrideTemplate(ContinenteUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ContinenteUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContinenteService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Continente(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.continente = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Continente();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.continente = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
