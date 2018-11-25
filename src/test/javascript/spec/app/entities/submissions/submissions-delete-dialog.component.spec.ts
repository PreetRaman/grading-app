/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GradingAppTestModule } from '../../../test.module';
import { SubmissionsDeleteDialogComponent } from 'app/entities/submissions/submissions-delete-dialog.component';
import { SubmissionsService } from 'app/entities/submissions/submissions.service';

describe('Component Tests', () => {
    describe('Submissions Management Delete Component', () => {
        let comp: SubmissionsDeleteDialogComponent;
        let fixture: ComponentFixture<SubmissionsDeleteDialogComponent>;
        let service: SubmissionsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [SubmissionsDeleteDialogComponent]
            })
                .overrideTemplate(SubmissionsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubmissionsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubmissionsService);
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
