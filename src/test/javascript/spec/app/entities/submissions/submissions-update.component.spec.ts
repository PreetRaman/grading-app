/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { SubmissionsUpdateComponent } from 'app/entities/submissions/submissions-update.component';
import { SubmissionsService } from 'app/entities/submissions/submissions.service';
import { Submissions } from 'app/shared/model/submissions.model';

describe('Component Tests', () => {
    describe('Submissions Management Update Component', () => {
        let comp: SubmissionsUpdateComponent;
        let fixture: ComponentFixture<SubmissionsUpdateComponent>;
        let service: SubmissionsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [SubmissionsUpdateComponent]
            })
                .overrideTemplate(SubmissionsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SubmissionsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubmissionsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Submissions(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.submissions = entity;
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
                    const entity = new Submissions();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.submissions = entity;
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
