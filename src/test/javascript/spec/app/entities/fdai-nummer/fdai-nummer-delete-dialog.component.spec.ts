/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GradingAppTestModule } from '../../../test.module';
import { FdaiNummerDeleteDialogComponent } from 'app/entities/fdai-nummer/fdai-nummer-delete-dialog.component';
import { FdaiNummerService } from 'app/entities/fdai-nummer/fdai-nummer.service';

describe('Component Tests', () => {
    describe('FdaiNummer Management Delete Component', () => {
        let comp: FdaiNummerDeleteDialogComponent;
        let fixture: ComponentFixture<FdaiNummerDeleteDialogComponent>;
        let service: FdaiNummerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [FdaiNummerDeleteDialogComponent]
            })
                .overrideTemplate(FdaiNummerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FdaiNummerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FdaiNummerService);
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
