/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { ActiveUsersUpdateComponent } from 'app/entities/active-users/active-users-update.component';
import { ActiveUsersService } from 'app/entities/active-users/active-users.service';
import { ActiveUsers } from 'app/shared/model/active-users.model';

describe('Component Tests', () => {
    describe('ActiveUsers Management Update Component', () => {
        let comp: ActiveUsersUpdateComponent;
        let fixture: ComponentFixture<ActiveUsersUpdateComponent>;
        let service: ActiveUsersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [ActiveUsersUpdateComponent]
            })
                .overrideTemplate(ActiveUsersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ActiveUsersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActiveUsersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ActiveUsers(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.activeUsers = entity;
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
                    const entity = new ActiveUsers();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.activeUsers = entity;
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
