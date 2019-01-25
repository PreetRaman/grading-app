/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GradingAppTestModule } from '../../../test.module';
import { ActiveUsersDeleteDialogComponent } from 'app/entities/active-users/active-users-delete-dialog.component';
import { ActiveUsersService } from 'app/entities/active-users/active-users.service';

describe('Component Tests', () => {
    describe('ActiveUsers Management Delete Component', () => {
        let comp: ActiveUsersDeleteDialogComponent;
        let fixture: ComponentFixture<ActiveUsersDeleteDialogComponent>;
        let service: ActiveUsersService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [ActiveUsersDeleteDialogComponent]
            })
                .overrideTemplate(ActiveUsersDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ActiveUsersDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActiveUsersService);
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
