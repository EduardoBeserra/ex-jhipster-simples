/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LojaComumTestModule } from '../../../test.module';
import { ContinenteDeleteDialogComponent } from 'app/entities/continente/continente-delete-dialog.component';
import { ContinenteService } from 'app/entities/continente/continente.service';

describe('Component Tests', () => {
    describe('Continente Management Delete Component', () => {
        let comp: ContinenteDeleteDialogComponent;
        let fixture: ComponentFixture<ContinenteDeleteDialogComponent>;
        let service: ContinenteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LojaComumTestModule],
                declarations: [ContinenteDeleteDialogComponent]
            })
                .overrideTemplate(ContinenteDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ContinenteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContinenteService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
